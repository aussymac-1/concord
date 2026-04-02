package com.walmartlabs.concord.runtime.v2.sdk;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2024 Walmart Inc.
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
    public void testGet() {
        Map<String, Object> data = new HashMap<>();
        data.put("key1", "value1");
        MapBackedVariables vars = new MapBackedVariables(data);
        assertEquals("value1", vars.get("key1"));
        assertNull(vars.get("nonexistent"));
    }

    @Test
    public void testHas() {
        Map<String, Object> data = new HashMap<>();
        data.put("key1", "value1");
        MapBackedVariables vars = new MapBackedVariables(data);
        assertTrue(vars.has("key1"));
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
        data.put("k", "v");
        MapBackedVariables vars = new MapBackedVariables(data);
        assertEquals(1, vars.toMap().size());
        assertEquals("v", vars.toMap().get("k"));
    }

    @Test
    public void testNullMap() {
        MapBackedVariables vars = new MapBackedVariables(null);
        assertTrue(vars.toMap().isEmpty());
        assertFalse(vars.has("anything"));
        assertNull(vars.get("anything"));
    }
}
