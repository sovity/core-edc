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

import com.fasterxml.uuid.Generators;
import org.eclipse.edc.connector.api.management.contractnegotiation.model.ContractOfferDescription;
import org.eclipse.edc.policy.model.Policy;

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
                .offerId(Generators.timeBasedGenerator().generate().toString())
                .assetId(Generators.timeBasedGenerator().generate().toString())
                .policy(policy)
                .build();
    }

    public static ContractOfferDescription createOffer(String offerId) {
        return createOffer(offerId, Generators.timeBasedGenerator().generate().toString());
    }

    public static ContractOfferDescription createOffer() {
        return createOffer(Generators.timeBasedGenerator().generate().toString(), Generators.timeBasedGenerator().generate().toString());
    }
}
