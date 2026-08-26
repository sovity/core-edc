/*
 *  Copyright (c) 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       sovity GmbH - initial API and implementation
 *
 */

package org.eclipse.edc.connector.dataplane.http.oauth2;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import org.eclipse.edc.connector.dataplane.http.spi.HttpDataAddress;
import org.eclipse.edc.iam.oauth2.spi.Oauth2DataAddressSchema;
import org.eclipse.edc.iam.oauth2.spi.client.Oauth2Client;
import org.eclipse.edc.iam.oauth2.spi.client.Oauth2CredentialsRequest;
import org.eclipse.edc.iam.oauth2.spi.client.SharedSecretOauth2CredentialsRequest;
import org.eclipse.edc.spi.iam.TokenRepresentation;
import org.eclipse.edc.spi.monitor.ConsoleMonitor;
import org.eclipse.edc.spi.result.Result;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TokenCacheTest {

    @Test
    void cacheOauthTokensRequest() throws JOSEException, InterruptedException {
        // arrange
        var dataAddress = HttpDataAddress.Builder.newInstance()
                .property(Oauth2DataAddressSchema.CLIENT_ID, "someClientId")
                .property(Oauth2DataAddressSchema.TOKEN_URL, "http://example.com/token")
                .property(Oauth2DataAddressSchema.CLIENT_SECRET_KEY, "someSecret")
                .build();
        var request = createRequest();

        var requestFactory = mock(Oauth2CredentialsRequestFactory.class);
        when(requestFactory.create(dataAddress)).thenReturn(Result.success(request));

        var jwt = generateJwt();

        var oauthClient = mock(Oauth2Client.class);
        when(oauthClient.requestToken(request))
                .thenReturn(Result.success(TokenRepresentation.Builder.newInstance().token(jwt).build()))
                .thenAnswer(it -> Result.success(TokenRepresentation.Builder.newInstance().token(generateJwt()).build()));

        var minimumTimeToLive = Duration.ofSeconds(2);
        var cache = new TokenCache(oauthClient, new ConsoleMonitor(), minimumTimeToLive);

        // query once and use the cache
        var token1 = cache.getCachedToken(request);
        var token2 = cache.getCachedToken(request);

        assertThat(token1.getContent()).isEqualTo(token2.getContent());

        // Wait until the token expires
        Thread.sleep(4000);

        // query again
        var token3 = cache.getCachedToken(request);
        assertThat(token3).isNotEqualTo(token1);
    }

    @Test
    void propagateTheFailureWhenTheOauthClientFails() throws JOSEException {
        // arrange
        var dataAddress = HttpDataAddress.Builder.newInstance()
                .property(Oauth2DataAddressSchema.CLIENT_ID, "someClientId")
                .property(Oauth2DataAddressSchema.TOKEN_URL, "http://example.com/token")
                .property(Oauth2DataAddressSchema.CLIENT_SECRET_KEY, "someSecret")
                .build();
        var request = createRequest();

        var requestFactory = mock(Oauth2CredentialsRequestFactory.class);
        when(requestFactory.create(dataAddress)).thenReturn(Result.success(request));

        var oauthClient = mock(Oauth2Client.class);
        when(oauthClient.requestToken(request)).thenReturn(Result.failure("OAuth failure"));

        var minimumTimeToLive = Duration.ofSeconds(2);
        var cache = new TokenCache(oauthClient, new ConsoleMonitor(), minimumTimeToLive);

        var response = cache.getCachedToken(request);
        assertThat(response.getFailureDetail()).isEqualTo("OAuth failure");
    }

    @Test
    void dontCacheTheTokenWhenItsNotJwt() {
        // arrange
        var dataAddress = HttpDataAddress.Builder.newInstance()
                .property(Oauth2DataAddressSchema.CLIENT_ID, "someClientId")
                .property(Oauth2DataAddressSchema.TOKEN_URL, "http://example.com/token")
                .property(Oauth2DataAddressSchema.CLIENT_SECRET_KEY, "someSecret")
                .build();
        var request = createRequest();

        var requestFactory = mock(Oauth2CredentialsRequestFactory.class);
        when(requestFactory.create(dataAddress)).thenReturn(Result.success(request));

        var oauthClient = mock(Oauth2Client.class);
        when(oauthClient.requestToken(request))
                .thenReturn(
                        Result.success(TokenRepresentation.Builder.newInstance().token("not JWT").build()),
                        Result.success(TokenRepresentation.Builder.newInstance().token("still not JWT").build())
                );

        var minimumTimeToLive = Duration.ofSeconds(2);
        var cache = new TokenCache(oauthClient, new ConsoleMonitor(), minimumTimeToLive);

        // act
        var response1 = cache.getCachedToken(request);

        // get the first token
        assertThat(response1.getContent().getToken()).isEqualTo("not JWT");

        // try again
        var response2 = cache.getCachedToken(request);

        // get the second token
        assertThat(response2.getContent().getToken()).isEqualTo("still not JWT");

        verify(oauthClient, times(2)).requestToken(request);
    }

    private Oauth2CredentialsRequest createRequest() {
        return SharedSecretOauth2CredentialsRequest.Builder.newInstance()
                .url("http://example.com/token")
                .grantType("client_credentials")
                .clientId("someClientId")
                .clientSecret("someSecret")
                .build();
    }

    private static String generateJwt() throws JOSEException {
        JWSObject jwsObject = new JWSObject(
                new JWSHeader(JWSAlgorithm.HS256),
                new Payload(Map.of(
                        "exp", Instant.now().plus(3, ChronoUnit.SECONDS).getEpochSecond(),
                        "sub", "dummy-subject",
                        "scope", "dummy-scope"
                ))
        );

        // We need a 256-bit key for HS256 which must be pre-shared
        byte[] sharedKey = new byte[32];
        new SecureRandom().nextBytes(sharedKey);

        // Apply the HMAC to the JWS object
        jwsObject.sign(new MACSigner(sharedKey));

        return jwsObject.serialize();
    }
}