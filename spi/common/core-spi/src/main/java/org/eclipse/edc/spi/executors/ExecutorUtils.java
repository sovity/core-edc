/*
 *  Copyright (c) 2021 sovity GmbH
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       sovity GmbH - initial API and implementation
 *
 */

package org.eclipse.edc.spi.executors;

import org.eclipse.edc.spi.monitor.Monitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorUtils {
    private ExecutorUtils() {
    }

    public static void orderlyShutdown(ExecutorService executor, String description, Monitor monitor) {
        if (executor != null) {
            try {
                monitor.debug(description + ": awaiting termination");
                executor.shutdown();
                var stopped = executor.awaitTermination(10, TimeUnit.SECONDS);
                monitor.debug(description + ": stopped: " + stopped);
                if (!stopped) {
                    monitor.warning(description + ": shutting down now...");
                    executor.shutdownNow();
                    monitor.warning(description + ": shutdown forced");
                }
            } catch (InterruptedException e) {
                monitor.severe(description + " await termination failed", e);
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
