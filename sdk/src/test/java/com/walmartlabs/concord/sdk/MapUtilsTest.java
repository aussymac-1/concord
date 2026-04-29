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

    @Test
    public void testGetString() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");

        assertEquals("value", MapUtils.getString(m, "key"));
        assertNull(MapUtils.getString(m, "missing"));
        assertEquals("default", MapUtils.getString(m, "missing", "default"));
    }

    @Test
    public void testGetStringWithHasKey() {
        Map<String, Object> m = new HashMap<>();
        m.put("myKey", "myValue");

        HasKey k = () -> "myKey";
        assertEquals("myValue", MapUtils.getString(m, k));
        assertEquals("fallback", MapUtils.getString(m, () -> "noKey", "fallback"));
    }

    @Test
    public void testGetBoolean() {
        Map<String, Object> m = new HashMap<>();
        m.put("flag", true);

        assertTrue(MapUtils.getBoolean(m, "flag", false));
        assertFalse(MapUtils.getBoolean(m, "missing", false));
    }

    @Test
    public void testGetBooleanWithHasKey() {
        Map<String, Object> m = new HashMap<>();
        m.put("enabled", true);

        HasKey k = () -> "enabled";
        assertTrue(MapUtils.getBoolean(m, k, false));
    }

    @Test
    public void testGetInt() {
        Map<String, Object> m = new HashMap<>();
        m.put("count", 42);

        assertEquals(42, MapUtils.getInt(m, "count", 0));
        assertEquals(99, MapUtils.getInt(m, "missing", 99));
    }

    @Test
    public void testGetNumber() {
        Map<String, Object> m = new HashMap<>();
        m.put("value", 3.14);

        assertEquals(3.14, MapUtils.getNumber(m, "value", 0).doubleValue());
        assertEquals(0, MapUtils.getNumber(m, "missing", 0).intValue());
    }

    @Test
    public void testGetMap() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("nested", "data");

        Map<String, Object> m = new HashMap<>();
        m.put("config", inner);

        assertEquals(inner, MapUtils.getMap(m, "config", null));
        assertNull(MapUtils.getMap(m, "missing", null));
    }

    @Test
    public void testGetMapWithHasKey() {
        Map<String, Object> inner = Collections.singletonMap("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("data", inner);

        HasKey k = () -> "data";
        assertEquals(inner, MapUtils.getMap(m, k, null));
    }

    @Test
    public void testGetList() {
        List<String> list = Arrays.asList("a", "b", "c");
        Map<String, Object> m = new HashMap<>();
        m.put("items", list);

        assertEquals(list, MapUtils.getList(m, "items", null));
        assertNull(MapUtils.getList(m, "missing", null));
    }

    @Test
    public void testGetListWithHasKey() {
        List<String> list = Arrays.asList("x", "y");
        Map<String, Object> m = new HashMap<>();
        m.put("vals", list);

        HasKey k = () -> "vals";
        assertEquals(list, MapUtils.getList(m, k, null));
    }

    @Test
    public void testGetUUID() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> m = new HashMap<>();
        m.put("id", uuid);
        m.put("idStr", uuid.toString());

        assertEquals(uuid, MapUtils.getUUID(m, "id"));
        assertEquals(uuid, MapUtils.getUUID(m, "idStr"));
        assertNull(MapUtils.getUUID(m, "missing"));
    }

    @Test
    public void testGetUUIDInvalidType() {
        Map<String, Object> m = new HashMap<>();
        m.put("id", 123);

        assertThrows(IllegalArgumentException.class, () -> MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetEnum() {
        Map<String, Object> m = new HashMap<>();
        m.put("direction", "NORTH");

        assertEquals(Direction.NORTH, MapUtils.getEnum(m, "direction", Direction.class, null));
        assertEquals(Direction.SOUTH, MapUtils.getEnum(m, "missing", Direction.class, Direction.SOUTH));
    }

    @Test
    public void testGetEnumInvalidValue() {
        Map<String, Object> m = new HashMap<>();
        m.put("direction", "INVALID");

        assertThrows(IllegalArgumentException.class, () -> MapUtils.getEnum(m, "direction", Direction.class, null));
    }

    @Test
    public void testAssertString() {
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
    public void testAssertInt() {
        Map<String, Object> m = new HashMap<>();
        m.put("count", 5);

        assertEquals(5, MapUtils.assertInt(m, "count"));
    }

    @Test
    public void testAssertIntMissing() {
        Map<String, Object> m = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertInt(m, "count"));
    }

    @Test
    public void testAssertNumber() {
        Map<String, Object> m = new HashMap<>();
        m.put("val", 7.5);

        assertEquals(7.5, MapUtils.assertNumber(m, "val").doubleValue());
    }

    @Test
    public void testAssertMap() {
        Map<String, Object> inner = Collections.singletonMap("k", "v");
        Map<String, Object> m = new HashMap<>();
        m.put("data", inner);

        assertEquals(inner, MapUtils.assertMap(m, "data"));
    }

    @Test
    public void testAssertMapMissing() {
        Map<String, Object> m = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertMap(m, "data"));
    }

    @Test
    public void testAssertList() {
        List<String> list = Arrays.asList("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("items", list);

        assertEquals(list, MapUtils.assertList(m, "items"));
    }

    @Test
    public void testAssertUUID() {
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
    public void testGetWithInvalidType() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", 123);

        assertThrows(IllegalArgumentException.class, () -> MapUtils.getString(m, "key"));
    }

    @Test
    public void testGetFromNullMap() {
        assertNull(MapUtils.get(null, "key", null));
    }

    private enum Direction {
        NORTH, SOUTH, EAST, WEST
    }
}
