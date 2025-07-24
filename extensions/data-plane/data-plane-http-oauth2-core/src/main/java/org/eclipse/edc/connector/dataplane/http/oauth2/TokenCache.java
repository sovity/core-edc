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

import com.nimbusds.jwt.SignedJWT;
import org.eclipse.edc.iam.oauth2.spi.client.Oauth2Client;
import org.eclipse.edc.iam.oauth2.spi.client.Oauth2CredentialsRequest;
import org.eclipse.edc.spi.iam.TokenRepresentation;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.result.Result;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TokenCache {
    private final Oauth2Client client;
    private final Monitor monitor;

    private final Duration minimumTimeToLive;
    private final Map<OauthTokenRequestCacheKey, TokenRepresentation> cache =
            Collections.synchronizedMap(new HashMap<>());

    public TokenCache(Oauth2Client client, Monitor monitor, Duration minimumTimeToLive) {
        this.client = client;
        this.monitor = monitor;
        this.minimumTimeToLive = minimumTimeToLive;
    }

    public Result<TokenRepresentation> getCachedToken(Oauth2CredentialsRequest request) {
        var key = OauthTokenRequestCacheKey.from(request);
        var tokenRepresentation = cache.get(key);
        var isMissing = tokenRepresentation == null;

        boolean isOutdated = !isMissing && isOutdated(tokenRepresentation);

        if (isMissing || isOutdated) {
            var response = client.requestToken(request);
            if (response.failed()) {
                // Propagate the error
                return response;
            }

            try {
                tryCacheResponse(response, key);
            } catch (ParseException e) {
                monitor.debug(() -> "Failed to parse token as JWT for request key " + key + ": " + e.getMessage() + ". No caching will happen.");
                return response;
            }
        }

        return Result.success(cache.get(key));
    }

    private void tryCacheResponse(Result<TokenRepresentation> response, OauthTokenRequestCacheKey key) throws ParseException {
        // test that it's a JWT
        SignedJWT.parse(response.getContent().getToken());
        // it is now a JWT that we can parse later
        cache.put(key, response.getContent());
    }

    private boolean isOutdated(TokenRepresentation tokenRepresentation) {
        boolean isOutdated = true;

        try {
            final var jwt = SignedJWT.parse(tokenRepresentation.getToken());
            final var expirationTime = jwt.getJWTClaimsSet().getExpirationTime().toInstant();
            final var requireValidUntil = Instant.now().plus(minimumTimeToLive);
            isOutdated = requireValidUntil.isAfter(expirationTime);
        } catch (ParseException e) {
            // should not happen, because the token is parsed when added to the cache
            // if we reach this branch, then we assume that we must fetch the token again
        }
        return isOutdated;
    }

}
