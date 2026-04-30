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
        var map = new HashMap<String, Object>();
        map.put("key", "value");
        var vars = new MapBackedVariables(map);
        assertEquals("value", vars.get("key"));
    }

    @Test
    public void testGetMissing() {
        var vars = new MapBackedVariables(new HashMap<>());
        assertNull(vars.get("missing"));
    }

    @Test
    public void testHas() {
        var map = new HashMap<String, Object>();
        map.put("key", "value");
        var vars = new MapBackedVariables(map);
        assertTrue(vars.has("key"));
        assertFalse(vars.has("missing"));
    }

    @Test
    public void testSetThrows() {
        var vars = new MapBackedVariables(new HashMap<>());
        assertThrows(IllegalStateException.class, () -> vars.set("key", "value"));
    }

    @Test
    public void testToMap() {
        var map = new HashMap<String, Object>();
        map.put("a", 1);
        map.put("b", 2);
        var vars = new MapBackedVariables(map);
        var result = vars.toMap();
        assertEquals(2, result.size());
        assertEquals(1, result.get("a"));
    }

    @Test
    public void testToMapUnmodifiable() {
        var map = new HashMap<String, Object>();
        map.put("a", 1);
        var vars = new MapBackedVariables(map);
        assertThrows(UnsupportedOperationException.class, () -> vars.toMap().put("b", 2));
    }

    @Test
    public void testNullMap() {
        var vars = new MapBackedVariables(null);
        assertFalse(vars.has("any"));
        assertNull(vars.get("any"));
        assertTrue(vars.toMap().isEmpty());
    }
}
