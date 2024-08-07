/*
 *  Copyright (c) 2020 - 2022 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *
 */

package org.eclipse.edc.connector.api.management.contractnegotiation;

import org.eclipse.edc.connector.api.management.contractnegotiation.model.ContractOfferDescription;
import org.eclipse.edc.policy.model.Policy;

import com.github.f4b6a3.uuid.UuidCreator;

public class TestFunctions {
    public static ContractOfferDescription createOffer(String offerId, String assetId) {
        return ContractOfferDescription.Builder.newInstance()
                .offerId(offerId)
                .assetId(assetId)
                .policy(Policy.Builder.newInstance().build())
                .build();
    }

    public static ContractOfferDescription createOffer(Policy policy) {
        return ContractOfferDescription.Builder.newInstance()
                .offerId(UuidCreator.getTimeOrderedEpoch().toString())
                .assetId(UuidCreator.getTimeOrderedEpoch().toString())
                .policy(policy)
                .build();
    }

    public static ContractOfferDescription createOffer(String offerId) {
        return createOffer(offerId, UuidCreator.getTimeOrderedEpoch().toString());
    }

    public static ContractOfferDescription createOffer() {
        return createOffer(UuidCreator.getTimeOrderedEpoch().toString(), UuidCreator.getTimeOrderedEpoch().toString());
    }
}
