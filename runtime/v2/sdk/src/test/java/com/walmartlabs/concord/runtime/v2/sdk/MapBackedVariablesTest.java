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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapBackedVariablesTest {

    @Test
    public void testNullDelegateBecomesEmpty() {
        var v = new MapBackedVariables(null);

        assertNull(v.get("missing"));
        assertFalse(v.has("missing"));
        assertNotNull(v.toMap());
        assertTrue(v.toMap().isEmpty());
    }

    @Test
    public void testHasAndGet() {
        var data = new HashMap<String, Object>();
        data.put("k", "v");
        data.put("nullValue", null);

        var v = new MapBackedVariables(data);

        assertTrue(v.has("k"));
        assertEquals("v", v.get("k"));
        // null value still counts as "has"
        assertTrue(v.has("nullValue"));
        assertNull(v.get("nullValue"));
        assertFalse(v.has("absent"));
    }

    @Test
    public void testToMapIsUnmodifiable() {
        var v = new MapBackedVariables(new HashMap<>(Map.of("k", "v")));

        assertThrows(UnsupportedOperationException.class, () -> v.toMap().put("x", 1));
    }

    @Test
    public void testSetAlwaysThrows() {
        var v = new MapBackedVariables(Map.of());

        assertThrows(IllegalStateException.class, () -> v.set("k", "v"));
    }

    @Test
    public void testGetStringDefaults() {
        var v = new MapBackedVariables(Map.of("present", "hello"));

        assertEquals("hello", v.getString("present"));
        assertNull(v.getString("missing"));
        assertEquals("def", v.getString("missing", "def"));
    }

    @Test
    public void testGetStringWrongTypeThrows() {
        var v = new MapBackedVariables(Map.of("k", 123));

        assertThrows(IllegalArgumentException.class, () -> v.getString("k"));
    }

    @Test
    public void testAssertString() {
        var v = new MapBackedVariables(Map.of("k", "v"));

        assertEquals("v", v.assertString("k"));
        assertEquals("v", v.assertString("custom", "k"));

        // missing
        assertThrows(IllegalArgumentException.class, () -> v.assertString("missing"));
        var ex = assertThrows(IllegalArgumentException.class, () -> v.assertString("custom message", "missing"));
        assertEquals("custom message", ex.getMessage());
    }

    @Test
    public void testGetNumberAndAssertNumber() {
        var v = new MapBackedVariables(Map.of("n", 5));

        assertEquals(5, v.getNumber("n", 0).intValue());
        assertEquals(99, v.getNumber("missing", 99).intValue());
        assertEquals(5, v.assertNumber("n").intValue());
        assertThrows(IllegalArgumentException.class, () -> v.assertNumber("missing"));
    }

    @Test
    public void testGetBoolean() {
        var data = new HashMap<String, Object>();
        data.put("present", true);
        data.put("nullValue", null);

        var v = new MapBackedVariables(data);

        assertTrue(v.getBoolean("present", false));
        assertFalse(v.getBoolean("missing", false));
        // null value falls back to default
        assertTrue(v.getBoolean("nullValue", true));
        assertTrue(v.assertBoolean("present"));
        assertThrows(IllegalArgumentException.class, () -> v.assertBoolean("missing"));
    }

    @Test
    public void testIntAndLong() {
        var v = new MapBackedVariables(Map.of("n", 7L));

        assertEquals(7, v.getInt("n", 0));
        assertEquals(0, v.getInt("missing", 0));
        assertEquals(7L, v.getLong("n", 0));
        assertEquals(7, v.assertInt("n"));
        assertEquals(7L, v.assertLong("n"));
    }

    @Test
    public void testGetUUID() {
        var id = UUID.randomUUID();
        var data = new HashMap<String, Object>();
        data.put("uuid", id);
        data.put("uuidStr", id.toString());

        var v = new MapBackedVariables(data);

        assertEquals(id, v.getUUID("uuid"));
        assertEquals(id, v.getUUID("uuidStr"));
        assertNull(v.getUUID("missing"));
    }

    @Test
    public void testGetUUIDInvalidType() {
        var v = new MapBackedVariables(Map.of("uuid", 123));

        assertThrows(IllegalArgumentException.class, () -> v.getUUID("uuid"));
    }

    @Test
    public void testAssertUUID() {
        var id = UUID.randomUUID();
        var v = new MapBackedVariables(Map.of("uuid", id));

        assertEquals(id, v.assertUUID("uuid"));
        assertThrows(IllegalArgumentException.class, () -> v.assertUUID("missing"));
    }

    @Test
    public void testCollectionsAndMaps() {
        var list = List.of("a", "b");
        var map = new LinkedHashMap<String, Object>();
        map.put("k", "v");

        var v = new MapBackedVariables(Map.of("list", list, "map", map));

        assertEquals(list, v.getList("list", null));
        assertEquals(list, v.assertList("list"));
        assertEquals(list, v.getCollection("list", null));
        assertEquals(list, v.assertCollection("list"));
        assertEquals(map, v.getMap("map", null));
        assertEquals(map, v.assertMap("map"));

        assertSame(list, v.getList("list", List.of()));
        assertNull(v.getList("missing", null));
        assertThrows(IllegalArgumentException.class, () -> v.assertList("missing"));
        assertThrows(IllegalArgumentException.class, () -> v.assertMap("missing"));
        assertThrows(IllegalArgumentException.class, () -> v.assertCollection("missing"));
    }

    @Test
    public void testGetWithTypeMismatch() {
        var v = new MapBackedVariables(Map.of("k", 1));

        assertThrows(IllegalArgumentException.class, () -> v.get("k", null, String.class));
    }

    @Test
    public void testGetReturnsDefaultForMissing() {
        var v = new MapBackedVariables(Map.of());

        assertEquals("def", v.get("missing", "def", String.class));
    }
}
