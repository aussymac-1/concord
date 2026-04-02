package com.walmartlabs.concord.sdk;

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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MapUtilsTest {

    private enum TestEnum { A, B, C }

    @Test
    public void testGetEnumReturnsValue() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "B");
        assertEquals(TestEnum.B, MapUtils.getEnum(m, "key", TestEnum.class, TestEnum.A));
    }

    @Test
    public void testGetEnumReturnsDefaultWhenMissing() {
        Map<String, Object> m = new HashMap<>();
        assertEquals(TestEnum.A, MapUtils.getEnum(m, "key", TestEnum.class, TestEnum.A));
    }

    @Test
    public void testGetEnumThrowsOnInvalidValue() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "INVALID");
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getEnum(m, "key", TestEnum.class, TestEnum.A));
    }

    @Test
    public void testGetUUIDFromString() {
        UUID expected = UUID.randomUUID();
        Map<String, Object> m = Map.of("id", expected.toString());
        assertEquals(expected, MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDFromUUID() {
        UUID expected = UUID.randomUUID();
        Map<String, Object> m = Map.of("id", expected);
        assertEquals(expected, MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDReturnsNullWhenMissing() {
        assertNull(MapUtils.getUUID(new HashMap<>(), "id"));
    }

    @Test
    public void testGetUUIDThrowsOnInvalidType() {
        Map<String, Object> m = Map.of("id", 123);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetString() {
        Map<String, Object> m = Map.of("name", "hello");
        assertEquals("hello", MapUtils.getString(m, "name"));
    }

    @Test
    public void testGetStringDefault() {
        assertEquals("default", MapUtils.getString(new HashMap<>(), "name", "default"));
    }

    @Test
    public void testGetStringNull() {
        assertNull(MapUtils.getString(new HashMap<>(), "name"));
    }

    @Test
    public void testGetMap() {
        Map<String, Object> inner = Map.of("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("nested", inner);
        assertEquals(inner, MapUtils.getMap(m, "nested", null));
    }

    @Test
    public void testGetMapDefault() {
        Map<String, Object> def = Map.of("x", "y");
        assertEquals(def, MapUtils.getMap(new HashMap<>(), "nested", def));
    }

    @Test
    public void testGetList() {
        List<String> list = List.of("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("items", list);
        assertEquals(list, MapUtils.getList(m, "items", null));
    }

    @Test
    public void testGetBoolean() {
        Map<String, Object> m = Map.of("flag", true);
        assertTrue(MapUtils.getBoolean(m, "flag", false));
    }

    @Test
    public void testGetBooleanDefault() {
        assertFalse(MapUtils.getBoolean(new HashMap<>(), "flag", false));
    }

    @Test
    public void testGetInt() {
        Map<String, Object> m = Map.of("count", 42);
        assertEquals(42, MapUtils.getInt(m, "count", 0));
    }

    @Test
    public void testGetIntDefault() {
        assertEquals(10, MapUtils.getInt(new HashMap<>(), "count", 10));
    }

    @Test
    public void testGetNumber() {
        Map<String, Object> m = Map.of("val", 3.14);
        assertEquals(3.14, MapUtils.getNumber(m, "val", 0.0));
    }

    @Test
    public void testAssertUUID() {
        UUID expected = UUID.randomUUID();
        Map<String, Object> m = Map.of("id", expected.toString());
        assertEquals(expected, MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testAssertUUIDThrowsWhenMissing() {
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertUUID(new HashMap<>(), "id"));
    }

    @Test
    public void testAssertString() {
        Map<String, Object> m = Map.of("name", "test");
        assertEquals("test", MapUtils.assertString(m, "name"));
    }

    @Test
    public void testAssertStringThrowsWhenMissing() {
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertString(new HashMap<>(), "name"));
    }

    @Test
    public void testAssertInt() {
        Map<String, Object> m = Map.of("num", 5);
        assertEquals(5, MapUtils.assertInt(m, "num"));
    }

    @Test
    public void testAssertNumber() {
        Map<String, Object> m = Map.of("num", 5.5);
        assertEquals(5.5, MapUtils.assertNumber(m, "num"));
    }

    @Test
    public void testAssertMap() {
        Map<String, Object> inner = Map.of("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("nested", inner);
        assertEquals(inner, MapUtils.assertMap(m, "nested"));
    }

    @Test
    public void testAssertList() {
        List<String> list = List.of("x");
        Map<String, Object> m = new HashMap<>();
        m.put("items", list);
        assertEquals(list, MapUtils.assertList(m, "items"));
    }

    @Test
    public void testGetTyped() {
        Map<String, Object> m = Map.of("val", "hello");
        assertEquals("hello", MapUtils.get(m, "val", null, String.class));
    }

    @Test
    public void testGetTypedThrowsOnWrongType() {
        Map<String, Object> m = Map.of("val", 123);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.get(m, "val", null, String.class));
    }

    @Test
    public void testGetFromNullMap() {
        assertNull(MapUtils.get(null, "key", null));
    }

    @Test
    public void testGetWithHasKey() {
        HasKey key = () -> "name";
        Map<String, Object> m = Map.of("name", "value");
        assertEquals("value", MapUtils.getString(m, key));
    }

    @Test
    public void testGetStringWithHasKeyAndDefault() {
        HasKey key = () -> "missing";
        assertEquals("def", MapUtils.getString(new HashMap<>(), key, "def"));
    }

    @Test
    public void testGetMapWithHasKey() {
        HasKey key = () -> "nested";
        Map<String, Object> inner = Map.of("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("nested", inner);
        assertEquals(inner, MapUtils.getMap(m, key, null));
    }

    @Test
    public void testGetListWithHasKey() {
        HasKey key = () -> "items";
        List<String> list = List.of("x");
        Map<String, Object> m = new HashMap<>();
        m.put("items", list);
        assertEquals(list, MapUtils.getList(m, key, list));
    }

    @Test
    public void testGetBooleanWithHasKey() {
        HasKey key = () -> "flag";
        Map<String, Object> m = Map.of("flag", true);
        assertTrue(MapUtils.getBoolean(m, key, false));
    }

    @Test
    public void testAssertVariableThrowsWhenMissing() {
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertVariable(new HashMap<>(), "x", String.class));
    }
}
