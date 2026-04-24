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

    enum Color { RED, GREEN, BLUE }

    @Test
    public void testGetString() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");

        assertEquals("value", MapUtils.getString(m, "key"));
        assertNull(MapUtils.getString(m, "missing"));
    }

    @Test
    public void testGetStringDefault() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");

        assertEquals("value", MapUtils.getString(m, "key", "default"));
        assertEquals("default", MapUtils.getString(m, "missing", "default"));
    }

    @Test
    public void testGetStringInvalidType() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", 123);

        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getString(m, "key"));
    }

    @Test
    public void testGetBoolean() {
        Map<String, Object> m = new HashMap<>();
        m.put("flag", true);

        assertTrue(MapUtils.getBoolean(m, "flag", false));
        assertFalse(MapUtils.getBoolean(m, "missing", false));
    }

    @Test
    public void testGetInt() {
        Map<String, Object> m = new HashMap<>();
        m.put("count", 42);

        assertEquals(42, MapUtils.getInt(m, "count", 0));
        assertEquals(0, MapUtils.getInt(m, "missing", 0));
    }

    @Test
    public void testGetNumber() {
        Map<String, Object> m = new HashMap<>();
        m.put("num", 3.14);

        Number result = MapUtils.getNumber(m, "num", 0);
        assertEquals(3.14, result);
        assertEquals(0, MapUtils.getNumber(m, "missing", 0));
    }

    @Test
    public void testGetMap() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("a", 1);

        Map<String, Object> m = new HashMap<>();
        m.put("nested", inner);

        Map<String, Object> result = MapUtils.getMap(m, "nested", null);
        assertNotNull(result);
        assertEquals(1, result.get("a"));

        assertNull(MapUtils.getMap(m, "missing", null));
    }

    @Test
    public void testGetList() {
        Map<String, Object> m = new HashMap<>();
        m.put("items", Arrays.asList("a", "b", "c"));

        List<String> result = MapUtils.getList(m, "items", null);
        assertNotNull(result);
        assertEquals(3, result.size());

        assertNull(MapUtils.getList(m, "missing", null));
    }

    @Test
    public void testGetUUID() {
        UUID expected = UUID.randomUUID();
        Map<String, Object> m = new HashMap<>();
        m.put("id", expected);
        m.put("idStr", expected.toString());

        assertEquals(expected, MapUtils.getUUID(m, "id"));
        assertEquals(expected, MapUtils.getUUID(m, "idStr"));
        assertNull(MapUtils.getUUID(m, "missing"));
    }

    @Test
    public void testGetUUIDInvalidType() {
        Map<String, Object> m = new HashMap<>();
        m.put("id", 123);

        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetEnum() {
        Map<String, Object> m = new HashMap<>();
        m.put("color", "RED");

        assertEquals(Color.RED, MapUtils.getEnum(m, "color", Color.class, Color.BLUE));
        assertEquals(Color.BLUE, MapUtils.getEnum(m, "missing", Color.class, Color.BLUE));
    }

    @Test
    public void testGetEnumInvalidValue() {
        Map<String, Object> m = new HashMap<>();
        m.put("color", "YELLOW");

        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getEnum(m, "color", Color.class, Color.BLUE));
    }

    @Test
    public void testAssertString() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");

        assertEquals("value", MapUtils.assertString(m, "key"));
    }

    @Test
    public void testAssertStringMissing() {
        Map<String, Object> m = new HashMap<>();

        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.assertString(m, "key"));
    }

    @Test
    public void testAssertInt() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", 42);

        assertEquals(42, MapUtils.assertInt(m, "key"));
    }

    @Test
    public void testAssertIntMissing() {
        Map<String, Object> m = new HashMap<>();

        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.assertInt(m, "key"));
    }

    @Test
    public void testAssertNumber() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", 3.14);

        assertEquals(3.14, MapUtils.assertNumber(m, "key"));
    }

    @Test
    public void testAssertMap() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("a", 1);

        Map<String, Object> m = new HashMap<>();
        m.put("nested", inner);

        Map<String, Object> result = MapUtils.assertMap(m, "nested");
        assertEquals(1, result.get("a"));
    }

    @Test
    public void testAssertMapMissing() {
        Map<String, Object> m = new HashMap<>();

        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.assertMap(m, "key"));
    }

    @Test
    public void testAssertList() {
        Map<String, Object> m = new HashMap<>();
        m.put("items", Arrays.asList("a", "b"));

        List<String> result = MapUtils.assertList(m, "items");
        assertEquals(2, result.size());
    }

    @Test
    public void testAssertUUID() {
        UUID expected = UUID.randomUUID();
        Map<String, Object> m = new HashMap<>();
        m.put("id", expected);

        assertEquals(expected, MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testAssertUUIDMissing() {
        Map<String, Object> m = new HashMap<>();

        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testGetWithNullMap() {
        assertNull(MapUtils.getString(null, "key", null));
        assertFalse(MapUtils.getBoolean(null, "key", false));
    }

    @Test
    public void testGetStringWithHasKey() {
        Map<String, Object> m = new HashMap<>();
        m.put("myKey", "myValue");

        HasKey hk = () -> "myKey";
        assertEquals("myValue", MapUtils.getString(m, hk));
        assertEquals("myValue", MapUtils.getString(m, hk, "default"));
    }

    @Test
    public void testGetMapWithHasKey() {
        Map<String, Object> inner = Collections.singletonMap("a", 1);
        Map<String, Object> m = new HashMap<>();
        m.put("myKey", inner);

        HasKey hk = () -> "myKey";
        Map<String, Object> result = MapUtils.getMap(m, hk, null);
        assertNotNull(result);
    }

    @Test
    public void testGetListWithHasKey() {
        Map<String, Object> m = new HashMap<>();
        m.put("myKey", Arrays.asList("a"));

        HasKey hk = () -> "myKey";
        List<String> result = MapUtils.getList(m, hk, null);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetBooleanWithHasKey() {
        Map<String, Object> m = new HashMap<>();
        m.put("myKey", true);

        HasKey hk = () -> "myKey";
        assertTrue(MapUtils.getBoolean(m, hk, false));
    }
}
