/*
 *  Copyright (c) 2024 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
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

package org.eclipse.edc.connector.controlplane.query.asset;

import org.eclipse.edc.connector.controlplane.asset.spi.domain.Asset;
import org.eclipse.edc.query.ReflectionPropertyLookup;
import org.eclipse.edc.spi.query.PropertyLookup;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Map.entry;

public class AssetPropertyLookup implements PropertyLookup {

    private final PropertyLookup fallbackPropertyLookup = new ReflectionPropertyLookup();

    @Override
    public Object getProperty(String key, Object object) {
        if (object instanceof Asset asset) {
            var singleQuotedKey = "'" + key + "'";
            Map<String, Object> properties = asset.getProperties();
            Map<String, Object> privateProperties = asset.getPrivateProperties();

            Stream<Map.Entry<String, Map<String, Object>>> mappings = Stream.of(
                    entry(key, properties),
                    entry(singleQuotedKey, properties),
                    entry(key, privateProperties),
                    entry(singleQuotedKey, privateProperties));

            return mappings
                    .map(entry -> fallbackPropertyLookup.getProperty(entry.getKey(), entry.getValue()))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseGet(() -> fallbackPropertyLookup.getProperty(key, asset));
        }

        return null;
    }
}
