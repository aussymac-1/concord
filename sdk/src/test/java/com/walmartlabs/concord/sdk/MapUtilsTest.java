package com.walmartlabs.concord.sdk;

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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MapUtilsTest {

    @Test
    public void testGetStringPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");
        assertEquals("value", MapUtils.getString(m, "key"));
    }

    @Test
    public void testGetStringMissing() {
        Map<String, Object> m = new HashMap<>();
        assertNull(MapUtils.getString(m, "key"));
    }

    @Test
    public void testGetStringDefault() {
        Map<String, Object> m = new HashMap<>();
        assertEquals("def", MapUtils.getString(m, "key", "def"));
    }

    @Test
    public void testGetStringWrongType() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", 123);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getString(m, "key"));
    }

    @Test
    public void testGetBooleanPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("flag", true);
        assertTrue(MapUtils.getBoolean(m, "flag", false));
    }

    @Test
    public void testGetBooleanDefault() {
        Map<String, Object> m = new HashMap<>();
        assertFalse(MapUtils.getBoolean(m, "flag", false));
    }

    @Test
    public void testGetIntPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("count", 42);
        assertEquals(42, MapUtils.getInt(m, "count", 0));
    }

    @Test
    public void testGetIntDefault() {
        Map<String, Object> m = new HashMap<>();
        assertEquals(10, MapUtils.getInt(m, "count", 10));
    }

    @Test
    public void testGetNumberPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("num", 3.14);
        assertEquals(3.14, MapUtils.getNumber(m, "num", 0.0));
    }

    @Test
    public void testGetMapPresent() {
        Map<String, Object> inner = Collections.singletonMap("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("nested", inner);
        assertEquals(inner, MapUtils.getMap(m, "nested", null));
    }

    @Test
    public void testGetMapDefault() {
        Map<String, Object> m = new HashMap<>();
        Map<String, Object> def = Collections.singletonMap("x", "y");
        assertEquals(def, MapUtils.getMap(m, "nested", def));
    }

    @Test
    public void testGetListPresent() {
        List<String> list = Arrays.asList("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("items", list);
        assertEquals(list, MapUtils.getList(m, "items", null));
    }

    @Test
    public void testGetListDefault() {
        Map<String, Object> m = new HashMap<>();
        List<String> def = Collections.singletonList("default");
        assertEquals(def, MapUtils.getList(m, "items", def));
    }

    @Test
    public void testGetUUIDFromString() {
        UUID expected = UUID.randomUUID();
        Map<String, Object> m = new HashMap<>();
        m.put("id", expected.toString());
        assertEquals(expected, MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDFromUUID() {
        UUID expected = UUID.randomUUID();
        Map<String, Object> m = new HashMap<>();
        m.put("id", expected);
        assertEquals(expected, MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDNull() {
        Map<String, Object> m = new HashMap<>();
        assertNull(MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDWrongType() {
        Map<String, Object> m = new HashMap<>();
        m.put("id", 123);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testAssertStringPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("name", "test");
        assertEquals("test", MapUtils.assertString(m, "name"));
    }

    @Test
    public void testAssertStringMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertString(m, "name"));
    }

    @Test
    public void testAssertIntPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("val", 5);
        assertEquals(5, MapUtils.assertInt(m, "val"));
    }

    @Test
    public void testAssertIntMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertInt(m, "val"));
    }

    @Test
    public void testAssertUUIDPresent() {
        UUID expected = UUID.randomUUID();
        Map<String, Object> m = new HashMap<>();
        m.put("id", expected);
        assertEquals(expected, MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testAssertUUIDMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testAssertMapPresent() {
        Map<String, Object> inner = Collections.singletonMap("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("data", inner);
        assertEquals(inner, MapUtils.assertMap(m, "data"));
    }

    @Test
    public void testAssertListPresent() {
        List<String> list = Arrays.asList("x", "y");
        Map<String, Object> m = new HashMap<>();
        m.put("items", list);
        assertEquals(list, MapUtils.assertList(m, "items"));
    }

    @Test
    public void testAssertNumberPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("num", 3.14);
        assertEquals(3.14, MapUtils.assertNumber(m, "num"));
    }

    @Test
    public void testAssertNumberMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertNumber(m, "num"));
    }

    enum TestEnum { A, B, C }

    @Test
    public void testGetEnumPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("e", "B");
        assertEquals(TestEnum.B, MapUtils.getEnum(m, "e", TestEnum.class, null));
    }

    @Test
    public void testGetEnumDefault() {
        Map<String, Object> m = new HashMap<>();
        assertEquals(TestEnum.A, MapUtils.getEnum(m, "e", TestEnum.class, TestEnum.A));
    }

    @Test
    public void testGetEnumInvalid() {
        Map<String, Object> m = new HashMap<>();
        m.put("e", "INVALID");
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getEnum(m, "e", TestEnum.class, null));
    }

    @Test
    public void testGetFromNullMap() {
        assertNull(MapUtils.get(null, "key", null));
        assertEquals("def", MapUtils.get(null, "key", "def"));
    }

    @Test
    public void testGetWithType() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");
        assertEquals("value", MapUtils.get(m, "key", null, String.class));
    }

    @Test
    public void testGetWithTypeMismatch() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");
        assertThrows(IllegalArgumentException.class, () -> MapUtils.get(m, "key", null, Integer.class));
    }

    @Test
    public void testGetStringWithHasKey() {
        HasKey k = () -> "myKey";
        Map<String, Object> m = new HashMap<>();
        m.put("myKey", "myValue");
        assertEquals("myValue", MapUtils.getString(m, k));
    }

    @Test
    public void testGetStringWithHasKeyDefault() {
        HasKey k = () -> "myKey";
        Map<String, Object> m = new HashMap<>();
        assertEquals("def", MapUtils.getString(m, k, "def"));
    }

    @Test
    public void testGetBooleanWithHasKey() {
        HasKey k = () -> "flag";
        Map<String, Object> m = new HashMap<>();
        m.put("flag", true);
        assertTrue(MapUtils.getBoolean(m, k, false));
    }

    @Test
    public void testGetMapWithHasKey() {
        HasKey k = () -> "data";
        Map<String, Object> inner = Collections.singletonMap("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("data", inner);
        assertEquals(inner, MapUtils.getMap(m, k, null));
    }

    @Test
    public void testGetListWithHasKey() {
        HasKey k = () -> "items";
        List<String> list = Arrays.asList("x", "y");
        Map<String, Object> m = new HashMap<>();
        m.put("items", list);
        assertEquals(list, MapUtils.getList(m, k, null));
    }
}
