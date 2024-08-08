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
