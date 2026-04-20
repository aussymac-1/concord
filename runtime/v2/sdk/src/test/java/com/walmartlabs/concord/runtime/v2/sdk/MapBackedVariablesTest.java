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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapBackedVariablesTest {

    @Test
    public void testNullMapIsTreatedAsEmpty() {
        MapBackedVariables v = new MapBackedVariables(null);

        assertFalse(v.has("missing"));
        assertNull(v.get("missing"));
        assertTrue(v.toMap().isEmpty());
    }

    @Test
    public void testGetAndHasForExistingKey() {
        Map<String, Object> src = new HashMap<>();
        src.put("a", "value");
        src.put("b", 42);

        MapBackedVariables v = new MapBackedVariables(src);

        assertTrue(v.has("a"));
        assertEquals("value", v.get("a"));
        assertTrue(v.has("b"));
        assertEquals(42, v.get("b"));
        assertFalse(v.has("missing"));
    }

    @Test
    public void testSetIsUnsupported() {
        MapBackedVariables v = new MapBackedVariables(new HashMap<>());

        assertThrows(IllegalStateException.class, () -> v.set("k", "v"));
    }

    @Test
    public void testUnderlyingMapIsUnmodifiable() {
        Map<String, Object> src = new HashMap<>();
        src.put("a", "value");

        MapBackedVariables v = new MapBackedVariables(src);

        assertThrows(UnsupportedOperationException.class, () -> v.toMap().put("b", "other"));
    }

    @Test
    public void testGetStringFallsBackToDefault() {
        MapBackedVariables v = new MapBackedVariables(Collections.singletonMap("k", "hi"));

        assertEquals("hi", v.getString("k"));
        assertEquals("default", v.getString("missing", "default"));
        assertNull(v.getString("missing"));
    }

    @Test
    public void testAssertStringThrowsWhenMissing() {
        MapBackedVariables v = new MapBackedVariables(Collections.emptyMap());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> v.assertString("name"));
        assertTrue(ex.getMessage().contains("name"));
    }

    @Test
    public void testAssertStringReturnsValue() {
        MapBackedVariables v = new MapBackedVariables(Collections.singletonMap("name", "concord"));

        assertEquals("concord", v.assertString("name"));
        assertEquals("concord", v.assertString("custom message", "name"));
    }

    @Test
    public void testAssertStringCustomMessage() {
        MapBackedVariables v = new MapBackedVariables(Collections.emptyMap());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> v.assertString("custom", "name"));
        assertEquals("custom", ex.getMessage());
    }

    @Test
    public void testGetAndAssertNumber() {
        Map<String, Object> m = new HashMap<>();
        m.put("n", 123);
        MapBackedVariables v = new MapBackedVariables(m);

        assertEquals(123, v.getNumber("n", 0).intValue());
        assertEquals(7, v.getNumber("missing", 7).intValue());
        assertEquals(123, v.assertNumber("n").intValue());
        assertThrows(IllegalArgumentException.class, () -> v.assertNumber("missing"));
    }

    @Test
    public void testGetAndAssertInt() {
        MapBackedVariables v = new MapBackedVariables(Collections.singletonMap("n", 5L));

        assertEquals(5, v.getInt("n", 0));
        assertEquals(10, v.getInt("missing", 10));
        assertEquals(5, v.assertInt("n"));
    }

    @Test
    public void testGetAndAssertLong() {
        MapBackedVariables v = new MapBackedVariables(Collections.singletonMap("n", 5));

        assertEquals(5L, v.getLong("n", 0));
        assertEquals(10L, v.getLong("missing", 10));
        assertEquals(5L, v.assertLong("n"));
    }

    @Test
    public void testGetAndAssertBoolean() {
        Map<String, Object> m = new HashMap<>();
        m.put("t", Boolean.TRUE);
        m.put("f", Boolean.FALSE);
        MapBackedVariables v = new MapBackedVariables(m);

        assertTrue(v.getBoolean("t", false));
        assertFalse(v.getBoolean("f", true));
        assertTrue(v.getBoolean("missing", true));
        assertTrue(v.assertBoolean("t"));
        assertThrows(IllegalArgumentException.class, () -> v.assertBoolean("missing"));
    }

    @Test
    public void testGetUUIDFromString() {
        UUID expected = UUID.randomUUID();
        MapBackedVariables v = new MapBackedVariables(Collections.singletonMap("id", expected.toString()));

        assertEquals(expected, v.getUUID("id"));
        assertEquals(expected, v.assertUUID("id"));
    }

    @Test
    public void testGetUUIDFromUUID() {
        UUID expected = UUID.randomUUID();
        MapBackedVariables v = new MapBackedVariables(Collections.singletonMap("id", expected));

        assertEquals(expected, v.getUUID("id"));
    }

    @Test
    public void testGetUUIDReturnsNullWhenMissing() {
        MapBackedVariables v = new MapBackedVariables(Collections.emptyMap());

        assertNull(v.getUUID("id"));
    }

    @Test
    public void testAssertUUIDThrowsWhenMissing() {
        MapBackedVariables v = new MapBackedVariables(Collections.emptyMap());

        assertThrows(IllegalArgumentException.class, () -> v.assertUUID("id"));
    }

    @Test
    public void testGetUUIDRejectsInvalidType() {
        MapBackedVariables v = new MapBackedVariables(Collections.singletonMap("id", 123));

        assertThrows(IllegalArgumentException.class, () -> v.getUUID("id"));
    }

    @Test
    public void testGetAndAssertCollection() {
        List<String> list = Arrays.asList("a", "b");
        MapBackedVariables v = new MapBackedVariables(Collections.singletonMap("xs", list));

        Collection<String> coll = v.getCollection("xs", Collections.emptyList());
        assertEquals(2, coll.size());
        assertEquals(2, v.assertCollection("xs").size());
        assertThrows(IllegalArgumentException.class, () -> v.assertCollection("missing"));
    }

    @Test
    public void testGetAndAssertMap() {
        Map<String, String> inner = Collections.singletonMap("k", "v");
        MapBackedVariables v = new MapBackedVariables(Collections.singletonMap("m", inner));

        Map<String, String> read = v.getMap("m", Collections.emptyMap());
        assertEquals("v", read.get("k"));
        assertEquals(inner, v.assertMap("m"));
        assertThrows(IllegalArgumentException.class, () -> v.assertMap("missing"));
    }

    @Test
    public void testGetAndAssertList() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        MapBackedVariables v = new MapBackedVariables(Collections.singletonMap("nums", list));

        List<Integer> read = v.getList("nums", Collections.emptyList());
        assertEquals(3, read.size());
        assertEquals(list, v.assertList("nums"));
        assertThrows(IllegalArgumentException.class, () -> v.assertList("missing"));
    }

    @Test
    public void testGetWithTypeMismatchThrows() {
        MapBackedVariables v = new MapBackedVariables(Collections.singletonMap("s", "hi"));

        assertThrows(IllegalArgumentException.class, () -> v.getNumber("s", 0));
    }

    @Test
    public void testToMapPreservesOrder() {
        Map<String, Object> ordered = new LinkedHashMap<>();
        ordered.put("a", 1);
        ordered.put("b", 2);
        ordered.put("c", 3);

        MapBackedVariables v = new MapBackedVariables(ordered);

        assertEquals(Arrays.asList("a", "b", "c"), v.toMap().keySet().stream().toList());
    }
}
