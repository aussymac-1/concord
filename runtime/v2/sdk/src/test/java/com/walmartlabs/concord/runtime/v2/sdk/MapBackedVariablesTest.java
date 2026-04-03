package com.walmartlabs.concord.runtime.v2.sdk;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2026 Walmart Inc.
 * -----
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =====
 */

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MapBackedVariablesTest {

    @Test
    public void testGetReturnsValue() {
        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");
        MapBackedVariables vars = new MapBackedVariables(data);
        assertEquals("value", vars.get("key"));
    }

    @Test
    public void testGetReturnsNullForMissing() {
        MapBackedVariables vars = new MapBackedVariables(Map.of("key", "value"));
        assertNull(vars.get("missing"));
    }

    @Test
    public void testHasReturnsTrueForExisting() {
        MapBackedVariables vars = new MapBackedVariables(Map.of("key", "value"));
        assertTrue(vars.has("key"));
    }

    @Test
    public void testHasReturnsFalseForMissing() {
        MapBackedVariables vars = new MapBackedVariables(Map.of("key", "value"));
        assertFalse(vars.has("missing"));
    }

    @Test
    public void testSetThrowsException() {
        MapBackedVariables vars = new MapBackedVariables(Map.of("key", "value"));
        assertThrows(IllegalStateException.class, () -> vars.set("key", "newValue"));
    }

    @Test
    public void testToMapReturnsUnmodifiableMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");
        MapBackedVariables vars = new MapBackedVariables(data);
        Map<String, Object> result = vars.toMap();
        assertEquals(1, result.size());
        assertEquals("value", result.get("key"));
        assertThrows(UnsupportedOperationException.class, () -> result.put("new", "val"));
    }

    @Test
    public void testNullDelegateCreatesEmptyMap() {
        MapBackedVariables vars = new MapBackedVariables(null);
        assertNull(vars.get("key"));
        assertFalse(vars.has("key"));
        assertTrue(vars.toMap().isEmpty());
    }
}
