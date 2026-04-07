package com.walmartlabs.concord.runtime.v2.sdk;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2025 Walmart Inc.
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
    public void testGetExistingKey() {
        Map<String, Object> data = new HashMap<>();
        data.put("key1", "value1");
        MapBackedVariables vars = new MapBackedVariables(data);

        assertEquals("value1", vars.get("key1"));
    }

    @Test
    public void testGetMissingKey() {
        Map<String, Object> data = new HashMap<>();
        MapBackedVariables vars = new MapBackedVariables(data);

        assertNull(vars.get("nonexistent"));
    }

    @Test
    public void testHasExistingKey() {
        Map<String, Object> data = new HashMap<>();
        data.put("key1", "value1");
        MapBackedVariables vars = new MapBackedVariables(data);

        assertTrue(vars.has("key1"));
    }

    @Test
    public void testHasMissingKey() {
        Map<String, Object> data = new HashMap<>();
        MapBackedVariables vars = new MapBackedVariables(data);

        assertFalse(vars.has("nonexistent"));
    }

    @Test
    public void testSetThrows() {
        MapBackedVariables vars = new MapBackedVariables(new HashMap<>());
        assertThrows(IllegalStateException.class, () -> vars.set("key", "value"));
    }

    @Test
    public void testToMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("a", 1);
        data.put("b", 2);
        MapBackedVariables vars = new MapBackedVariables(data);

        Map<String, Object> result = vars.toMap();
        assertEquals(2, result.size());
        assertEquals(1, result.get("a"));
        assertEquals(2, result.get("b"));
    }

    @Test
    public void testToMapIsUnmodifiable() {
        Map<String, Object> data = new HashMap<>();
        data.put("a", 1);
        MapBackedVariables vars = new MapBackedVariables(data);

        assertThrows(UnsupportedOperationException.class, () -> vars.toMap().put("b", 2));
    }

    @Test
    public void testNullDelegate() {
        MapBackedVariables vars = new MapBackedVariables(null);
        assertNull(vars.get("key"));
        assertFalse(vars.has("key"));
        assertTrue(vars.toMap().isEmpty());
    }
}
