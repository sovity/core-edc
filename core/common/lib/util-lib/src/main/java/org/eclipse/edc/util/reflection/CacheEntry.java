/*
 *  Copyright (c) 2025 sovity GmbH
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

package org.eclipse.edc.util.reflection;


import java.util.List;

public class CacheEntry {
    private final String propertyName;
    private final List<PathItem> getPathItems;
    private int count;

    public CacheEntry(String propertyName, List<PathItem> getPathItems) {
        this.propertyName = propertyName;
        this.getPathItems = getPathItems;
        this.count = 0;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public List<PathItem> getGetPathItems() {
        return getPathItems;
    }

    public int getCount() {
        return count;
    }

    public void increaseCount() {
        count++;
    }
}
