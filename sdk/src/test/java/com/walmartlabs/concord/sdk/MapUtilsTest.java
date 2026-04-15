package com.walmartlabs.concord.sdk;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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

    // --- getString ---

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
    public void testGetStringWithDefault() {
        Map<String, Object> m = new HashMap<>();
        assertEquals("def", MapUtils.getString(m, "key", "def"));
    }

    @Test
    public void testGetStringWrongType() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", 123);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getString(m, "key"));
    }

    // --- getInt ---

    @Test
    public void testGetIntPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", 42);
        assertEquals(42, MapUtils.getInt(m, "key", -1));
    }

    @Test
    public void testGetIntMissing() {
        Map<String, Object> m = new HashMap<>();
        assertEquals(-1, MapUtils.getInt(m, "key", -1));
    }

    // --- getBoolean ---

    @Test
    public void testGetBooleanPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("flag", true);
        assertTrue(MapUtils.getBoolean(m, "flag", false));
    }

    @Test
    public void testGetBooleanMissing() {
        Map<String, Object> m = new HashMap<>();
        assertFalse(MapUtils.getBoolean(m, "flag", false));
    }

    // --- getNumber ---

    @Test
    public void testGetNumberPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("num", 3.14);
        assertEquals(3.14, MapUtils.getNumber(m, "num", 0));
    }

    @Test
    public void testGetNumberMissing() {
        Map<String, Object> m = new HashMap<>();
        assertEquals(0, MapUtils.getNumber(m, "num", 0));
    }

    // --- getUUID ---

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
    public void testGetUUIDMissing() {
        Map<String, Object> m = new HashMap<>();
        assertNull(MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDWrongType() {
        Map<String, Object> m = new HashMap<>();
        m.put("id", 123);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getUUID(m, "id"));
    }

    // --- getMap ---

    @Test
    public void testGetMapPresent() {
        Map<String, Object> inner = Collections.singletonMap("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("nested", inner);
        assertEquals(inner, MapUtils.getMap(m, "nested", null));
    }

    @Test
    public void testGetMapMissing() {
        Map<String, Object> m = new HashMap<>();
        Map<String, Object> def = Collections.singletonMap("x", "y");
        assertEquals(def, MapUtils.getMap(m, "nested", def));
    }

    // --- getList ---

    @Test
    public void testGetListPresent() {
        List<String> list = Arrays.asList("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("items", list);
        assertEquals(list, MapUtils.getList(m, "items", null));
    }

    @Test
    public void testGetListMissing() {
        Map<String, Object> m = new HashMap<>();
        List<String> def = Collections.singletonList("default");
        assertEquals(def, MapUtils.getList(m, "items", def));
    }

    // --- getEnum ---

    enum TestEnum {
        FOO, BAR
    }

    @Test
    public void testGetEnumPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("e", "FOO");
        assertEquals(TestEnum.FOO, MapUtils.getEnum(m, "e", TestEnum.class, null));
    }

    @Test
    public void testGetEnumMissing() {
        Map<String, Object> m = new HashMap<>();
        assertEquals(TestEnum.BAR, MapUtils.getEnum(m, "e", TestEnum.class, TestEnum.BAR));
    }

    @Test
    public void testGetEnumInvalid() {
        Map<String, Object> m = new HashMap<>();
        m.put("e", "INVALID");
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getEnum(m, "e", TestEnum.class, null));
    }

    // --- assert methods ---

    @Test
    public void testAssertStringPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");
        assertEquals("value", MapUtils.assertString(m, "key"));
    }

    @Test
    public void testAssertStringMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertString(m, "key"));
    }

    @Test
    public void testAssertUUIDPresent() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> m = new HashMap<>();
        m.put("id", uuid.toString());
        assertEquals(uuid, MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testAssertUUIDMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testAssertIntPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("num", 42);
        assertEquals(42, MapUtils.assertInt(m, "num"));
    }

    @Test
    public void testAssertIntMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertInt(m, "num"));
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

    @Test
    public void testAssertMapPresent() {
        Map<String, Object> inner = Collections.singletonMap("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("nested", inner);
        assertEquals(inner, MapUtils.assertMap(m, "nested"));
    }

    @Test
    public void testAssertMapMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertMap(m, "nested"));
    }

    @Test
    public void testAssertListPresent() {
        List<String> list = Arrays.asList("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("items", list);
        assertEquals(list, MapUtils.assertList(m, "items"));
    }

    @Test
    public void testAssertListMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertList(m, "items"));
    }

    // --- get with null map ---

    @Test
    public void testGetWithNullMap() {
        assertNull(MapUtils.get(null, "key", null));
    }

    @Test
    public void testGetWithNullMapAndDefault() {
        assertEquals("def", MapUtils.get(null, "key", "def"));
    }
}
