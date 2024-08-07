/*
*  Copyright (c) 2021 Daimler TSS GmbH
*
*  This program and the accompanying materials are made available under the
*  terms of the Apache License, Version 2.0 which is available at
*  https://www.apache.org/licenses/LICENSE-2.0
*
*  SPDX-License-Identifier: Apache-2.0
*
*  Contributors:
*       Daimler TSS GmbH - Initial API and Implementation
*       Microsoft Corporation - introduced Awaitility
*
*/
plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    api(project(":spi:common:policy-engine-spi"))
    api(project(":spi:control-plane:contract-spi"))

    implementation(project(":core:common:state-machine"))
    implementation(libs.opentelemetry.instrumentation.annotations)
    implementation("com.github.f4b6a3:uuid-creator:5.2.0")

    testImplementation(project(":core:control-plane:control-plane-core"))
    testImplementation(project(":core:control-plane:control-plane-aggregate-services"))
    testImplementation(project(":core:common:junit"))
    testImplementation(libs.awaitility)
    testImplementation(project(":core:common:policy-engine"))
}


