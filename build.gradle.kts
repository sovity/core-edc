/*
 *  Copyright (c) 2022 Microsoft Corporation
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


plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.edc.build)
}

val edcScmUrl: String by project
val edcScmConnection: String by project

buildscript {
    dependencies {
        classpath("org.eclipse.edc.autodoc:org.eclipse.edc.autodoc.gradle.plugin:0.17.0")
    }
}

val edcBuildId = libs.plugins.edc.build.get().pluginId

allprojects {
    apply(plugin = edcBuildId)
    apply(plugin = "org.eclipse.edc.autodoc")

    configure<org.eclipse.edc.plugins.edcbuild.extensions.BuildExtension> {
        pom {
            scmUrl.set(edcScmUrl)
            scmConnection.set(edcScmConnection)
        }
    }

    configure<CheckstyleExtension> {
        configFile = rootProject.file("resources/edc-checkstyle-config.xml")
        configDirectory.set(rootProject.file("resources"))
    }

    java {
        withSourcesJar()
    }

    apply(plugin = "maven-publish")

    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.eclipse.edc" && requested.name == "autodoc-processor") {
                useVersion("0.17.0")
            }
        }
    }

    tasks.withType(Sign::class.java).configureEach {
        onlyIf { false }
    }
}

subprojects {

    apply(plugin = "maven-publish")

    publishing {
        repositories {
            if (System.getenv("IS_RELEASE") == "true") {
                maven {
                    name = "AzureProd"
                    url =
                        uri(
                            "https://pkgs.dev.azure.com/sovity/41799556-91c8-4df6-8ddb-4471d6f15953/_packaging/core-edc/maven/v1"
                        )
                    credentials {
                        username = "sovity"
                        password = project.findProperty("azure.token") as String? ?: System.getenv("AZURE_TOKEN")
                    }
                }
            } else {
                maven {
                    name = "AzureTest"
                    url = uri("https://pkgs.dev.azure.com/sovity/Test/_packaging/test/maven/v1")
                    credentials {
                        username = "sovity"
                        password = project.findProperty("azure.token") as String? ?: System.getenv("AZURE_TOKEN")
                    }
                }
            }
        }
    }
}
