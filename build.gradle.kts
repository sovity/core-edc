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
}

val javaVersion: String by project
val edcScmUrl: String by project
val edcScmConnection: String by project

buildscript {
    dependencies {
        classpath("org.eclipse.edc.edc-build:org.eclipse.edc.edc-build.gradle.plugin:0.11.1")
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
            url =
                uri("https://pkgs.dev.azure.com/sovity/41799556-91c8-4df6-8ddb-4471d6f15953/_packaging/core-edc/maven/v1")
            credentials {
                username = "sovity"
                password = project.findProperty("azure.token") as String? ?: System.getenv("AZURE_TOKEN")
            }
        }
    }
}

allprojects {
    apply(plugin = "${group}.edc-build")

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

    publishing {
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
                url =
                    uri("https://pkgs.dev.azure.com/sovity/41799556-91c8-4df6-8ddb-4471d6f15953/_packaging/core-edc/maven/v1")
                credentials {
                    username = "sovity"
                    password = project.findProperty("azure.token") as String? ?: System.getenv("AZURE_TOKEN")
                }
            }
        }
    }
}

subprojects {
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
            url =
                uri("https://pkgs.dev.azure.com/sovity/41799556-91c8-4df6-8ddb-4471d6f15953/_packaging/core-edc/maven/v1")
            credentials {
                username = "sovity"
                password = project.findProperty("azure.token") as String? ?: System.getenv("AZURE_TOKEN")
            }
        }
    }
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
        url =
            uri("https://pkgs.dev.azure.com/sovity/41799556-91c8-4df6-8ddb-4471d6f15953/_packaging/core-edc/maven/v1")
        credentials {
            username = "sovity"
            password = project.findProperty("azure.token") as String? ?: System.getenv("AZURE_TOKEN")
        }
    }
}