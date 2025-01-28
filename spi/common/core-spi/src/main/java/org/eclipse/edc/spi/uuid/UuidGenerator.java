/*
 *  Copyright (c) 2024-2025 sovity GmbH
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       sovity GmbH - Initial implementation
 *
 */

package org.eclipse.edc.spi.uuid;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;

import java.util.UUID;

public enum UuidGenerator {
    INSTANCE(Generators.timeBasedEpochGenerator());

    private final TimeBasedEpochGenerator generator;

    UuidGenerator(TimeBasedEpochGenerator generator) {
        this.generator = generator;
    }

    public UUID generate() {
        return generator.generate();
    }
}

