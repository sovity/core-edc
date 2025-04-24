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

plugins {
    `java-library`
    `maven-publish`
    `java-test-fixtures`
}

repositories {
    maven {
        name = "AzureTest"
        url = uri("https://pkgs.dev.azure.com/sovity/Test/_packaging/test/maven/v1")
        credentials {
            username = "sovity"
            password = project.findProperty("azure.token") as String? ?: System.getenv("AZURE_TOKEN")
        }
    }

    maven {
        name = "Azure"
        url = uri("https://pkgs.dev.azure.com/sovity/41799556-91c8-4df6-8ddb-4471d6f15953/_packaging/core-edc/maven/v1")
        credentials {
            username = "sovity"
            password = project.findProperty("azure.token") as String? ?: System.getenv("AZURE_TOKEN")
        }
    }
}

dependencies {
    api(libs.bundles.jackson)
    api(libs.edc.runtime.metamodel)
    api(libs.failsafe.core)

    implementation(libs.opentelemetry.api)

    testImplementation(project(":tests:junit-base"));
}

autodocextension {
    processorVersion = "0.11.1"
}
