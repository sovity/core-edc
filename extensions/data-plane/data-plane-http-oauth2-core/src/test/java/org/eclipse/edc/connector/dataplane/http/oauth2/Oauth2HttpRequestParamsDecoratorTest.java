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
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
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
import org.eclipse.edc.connector.dataplane.http.spi.HttpRequestParams;
import org.eclipse.edc.iam.oauth2.spi.Oauth2DataAddressSchema;
import org.eclipse.edc.iam.oauth2.spi.client.Oauth2Client;
import org.eclipse.edc.iam.oauth2.spi.client.SharedSecretOauth2CredentialsRequest;
import org.eclipse.edc.spi.EdcException;
import org.eclipse.edc.spi.iam.TokenRepresentation;
import org.eclipse.edc.spi.result.Result;
import org.eclipse.edc.spi.types.domain.transfer.DataFlowStartMessage;
import org.eclipse.edc.spi.uuid.UuidGenerator;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.map;
import static org.eclipse.edc.iam.oauth2.spi.Oauth2DataAddressSchema.CLIENT_ID;
import static org.eclipse.edc.iam.oauth2.spi.Oauth2DataAddressSchema.CLIENT_SECRET_KEY;
import static org.eclipse.edc.iam.oauth2.spi.Oauth2DataAddressSchema.TOKEN_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class Oauth2HttpRequestParamsDecoratorTest {

    private final Oauth2CredentialsRequestFactory requestFactory = mock(Oauth2CredentialsRequestFactory.class);
    private final Oauth2Client client = mock(Oauth2Client.class);

    private final Oauth2HttpRequestParamsDecorator decorator = new Oauth2HttpRequestParamsDecorator(requestFactory, client, Duration.ofSeconds(1));

    @Test
    void requestOauth2TokenAndSetItOnRequest() {
        var dataFlowRequest = dummyDataFlowRequest();
        var httpAddress = httpDataAddressWithOauth2Properties();
        when(requestFactory.create(any())).thenReturn(Result.success(createRequest()));
        when(client.requestToken(any())).thenReturn(Result.success(TokenRepresentation.Builder.newInstance().token("token-test").build()));
        var paramsBuilder = HttpRequestParams.Builder.newInstance().baseUrl("http://any").method("GET");

        var result = decorator.decorate(dataFlowRequest, httpAddress, paramsBuilder).build();

        assertThat(result.getHeaders()).asInstanceOf(map(String.class, String.class))
                .containsEntry("Authorization", "Bearer token-test");
    }

    @Test
    void shouldThrowExceptionIfCannotBuildRequest() {
        var dataFlowRequest = dummyDataFlowRequest();
        var httpAddress = httpDataAddressWithOauth2Properties();
        when(requestFactory.create(any())).thenReturn(Result.failure("cannot build request"));
        var paramsBuilder = HttpRequestParams.Builder.newInstance().baseUrl("http://any").method("GET");

        assertThatThrownBy(() -> decorator.decorate(dataFlowRequest, httpAddress, paramsBuilder))
                .isInstanceOf(EdcException.class);
    }

    @Test
    void shouldThrowExceptionIfCannotGetToken() {
        var dataFlowRequest = dummyDataFlowRequest();
        var httpAddress = httpDataAddressWithOauth2Properties();
        when(requestFactory.create(any())).thenReturn(Result.success(createRequest()));
        when(client.requestToken(any())).thenReturn(Result.failure("Cannot get token"));
        var paramsBuilder = HttpRequestParams.Builder.newInstance().baseUrl("http://any").method("GET");

        assertThatThrownBy(() -> decorator.decorate(dataFlowRequest, httpAddress, paramsBuilder))
                .isInstanceOf(EdcException.class);
    }

    @Test
    void shouldDoNothingIfNoOauthPropertiesContained() {
        var dataFlowRequest = dummyDataFlowRequest();
        var httpAddress = HttpDataAddress.Builder.newInstance().build();
        var paramsBuilder = HttpRequestParams.Builder.newInstance().baseUrl("http://any").method("GET");

        var result = decorator.decorate(dataFlowRequest, httpAddress, paramsBuilder).build();

        assertThat(result.getHeaders()).asInstanceOf(map(String.class, String.class)).isEmpty();
        verifyNoInteractions(requestFactory, client);
    }

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

        var minimumTimeToLive = Duration.ofSeconds(3);
        var decorator = new Oauth2HttpRequestParamsDecorator(requestFactory, oauthClient, minimumTimeToLive);

        var request1 = mock(DataFlowStartMessage.class);
        var params1 = HttpRequestParams.Builder.newInstance()
                .baseUrl("http://example.com")
                .method("GET");

        // get a new token using oauth
        var decorated1 = decorator.decorate(request1, dataAddress, params1);

        // assert fetch a new token
        verify(oauthClient, times(1)).requestToken(request);
        assertThat(decorated1.build().getHeaders()).isEqualTo(Map.of("Authorization", "Bearer " + jwt));

        // get a new token, this must use the cache
        var decorated2 = decorator.decorate(request1, dataAddress, params1);

        // no oauth client call
        verify(oauthClient, times(1)).requestToken(request);
        assertThat(decorated2.build().getHeaders()).isEqualTo(Map.of("Authorization", "Bearer " + jwt));

        // let the token expire and fetch a new one
        Thread.sleep(5000);

        // get a new token, this must fetch a new token
        var decorated3 = decorator.decorate(request1, dataAddress, params1);

        // call oauth client again and change the token
        verify(oauthClient, times(2)).requestToken(request);
        assertThat(decorated3.build().getHeaders())
                // different JWT
                .isNotEqualTo(Map.of("Authorization", "Bearer " + jwt));
    }

    private static String generateJwt() throws JOSEException {
        JWSObject jwsObject = new JWSObject(
                new JWSHeader(JWSAlgorithm.HS256),
                new Payload(Map.of(
                        "exp", Instant.now().plus(5, ChronoUnit.SECONDS).getEpochSecond(),
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

    private HttpDataAddress httpDataAddressWithOauth2Properties() {
        return HttpDataAddress.Builder.newInstance()
                .property(TOKEN_URL, "any")
                .property(CLIENT_ID, "any")
                .property(CLIENT_SECRET_KEY, "any")
                .build();
    }

    private DataFlowStartMessage dummyDataFlowRequest() {
        return DataFlowStartMessage.Builder.newInstance()
                .processId(UuidGenerator.INSTANCE.generate().toString())
                .sourceDataAddress(dummyAddress())
                .destinationDataAddress(dummyAddress())
                .properties(emptyMap())
                .build();
    }

    private SharedSecretOauth2CredentialsRequest createRequest() {
        return SharedSecretOauth2CredentialsRequest.Builder.newInstance()
                .url("http://any")
                .grantType("any")
                .clientId("any").clientSecret("any").build();
    }

    private HttpDataAddress dummyAddress() {
        return HttpDataAddress.Builder.newInstance().baseUrl("http://dummy").build();
    }
}
