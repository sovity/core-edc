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

package org.eclipse.edc.iam.oauth2.spi.client;

import java.util.Map;

public record OauthTokenRequestCacheKey(
        String url,
        String scope,
        String grantType,
        String resource,
        Map<String, String> params
) {
    public static OauthTokenRequestCacheKey from(Oauth2CredentialsRequest request) {
        return new OauthTokenRequestCacheKey(
                request.getUrl(),
                request.getScope(),
                request.getGrantType(),
                request.getResource(),
                request.getParams()
        );
    }
}
