/*
 *  Copyright (c) 2021 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *       Siemens AG - changes to make it compatible with AWS S3, Azure blob and ALI Object Storage presigned URL for upload
 *
 */

package org.eclipse.edc.connector.dataplane.http.oauth2;

import org.eclipse.edc.connector.dataplane.http.spi.HttpRequestParamsProvider;
import org.eclipse.edc.iam.oauth2.spi.client.Oauth2Client;
import org.eclipse.edc.jwt.signer.spi.JwsSignerProvider;
import org.eclipse.edc.runtime.metamodel.annotation.Extension;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.runtime.metamodel.annotation.Setting;
import org.eclipse.edc.spi.security.Vault;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;

import java.time.Clock;
import java.time.Duration;

/**
 * Provides support for adding OAuth2 authentication to http data transfer
 */
@Extension(value = DataPlaneHttpOauth2Extension.NAME)
public class DataPlaneHttpOauth2Extension implements ServiceExtension {
    public static final String NAME = "Data Plane HTTP OAuth2";

    public static final String SOVITY_EDC_DATAPLANE_OAUTH2_CACHE_MINIMUM_TIME_TO_LIVE_KEY = "sovity.edc.dataplane.oauth2.cache.minimum.time.to.live";

    @Setting(
            description = "Format: ISO 8601 duration. The minimum duration between the token's age and the time at which it expires. If the token has a shorter remaining lifespan than this, it is renewed.",
            required = false,
            defaultValue = "PT5s",
            key = SOVITY_EDC_DATAPLANE_OAUTH2_CACHE_MINIMUM_TIME_TO_LIVE_KEY
    )
    private String minimumTimeToLive;

    @Inject
    private Clock clock;

    @Inject
    private HttpRequestParamsProvider paramsProvider;

    @Inject
    private Vault vault;

    @Inject
    private JwsSignerProvider jwsSignerProvider;

    @Inject
    private Oauth2Client oauth2Client;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        final var requestFactory = new Oauth2CredentialsRequestFactory(jwsSignerProvider, clock, vault);
        final var minimumTimeToLiveDuration = Duration.parse(minimumTimeToLive);
        final var oauth2ParamsDecorator = new Oauth2HttpRequestParamsDecorator(requestFactory, oauth2Client, minimumTimeToLiveDuration);

        paramsProvider.registerSourceDecorator(oauth2ParamsDecorator);
    }

}
