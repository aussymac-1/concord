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
    public void testGetString() {
        var m = Map.<String, Object>of("key", "value");
        assertEquals("value", MapUtils.getString(m, "key"));
        assertNull(MapUtils.getString(m, "missing"));
    }

    @Test
    public void testGetStringWithDefault() {
        var m = Map.<String, Object>of("key", "value");
        assertEquals("value", MapUtils.getString(m, "key", "def"));
        assertEquals("def", MapUtils.getString(m, "missing", "def"));
    }

    @Test
    public void testGetStringWithHasKey() {
        HasKey k = () -> "key";
        var m = Map.<String, Object>of("key", "value");
        assertEquals("value", MapUtils.getString(m, k));
        assertEquals("value", MapUtils.getString(m, k, "def"));
    }

    @Test
    public void testGetBoolean() {
        var m = Map.<String, Object>of("flag", true);
        assertTrue(MapUtils.getBoolean(m, "flag", false));
        assertFalse(MapUtils.getBoolean(m, "missing", false));
    }

    @Test
    public void testGetBooleanWithHasKey() {
        HasKey k = () -> "flag";
        var m = Map.<String, Object>of("flag", true);
        assertTrue(MapUtils.getBoolean(m, k, false));
    }

    @Test
    public void testGetInt() {
        var m = Map.<String, Object>of("count", 42);
        assertEquals(42, MapUtils.getInt(m, "count", 0));
        assertEquals(0, MapUtils.getInt(m, "missing", 0));
    }

    @Test
    public void testGetNumber() {
        var m = Map.<String, Object>of("val", 3.14);
        assertEquals(3.14, MapUtils.getNumber(m, "val", 0).doubleValue());
    }

    @Test
    public void testGetMap() {
        Map<String, Object> inner = Map.of("a", "b");
        var m = Map.<String, Object>of("nested", inner);
        assertEquals(inner, MapUtils.getMap(m, "nested", null));
        assertNull(MapUtils.getMap(m, "missing", null));
    }

    @Test
    public void testGetMapWithHasKey() {
        HasKey k = () -> "nested";
        Map<String, Object> inner = Map.of("a", "b");
        var m = Map.<String, Object>of("nested", inner);
        assertEquals(inner, MapUtils.getMap(m, k, null));
    }

    @Test
    public void testGetList() {
        var list = List.of("a", "b");
        var m = Map.<String, Object>of("items", list);
        assertEquals(list, MapUtils.getList(m, "items", null));
        assertNull(MapUtils.getList(m, "missing", null));
    }

    @Test
    public void testGetListWithHasKey() {
        HasKey k = () -> "items";
        var list = List.of("a", "b");
        var m = Map.<String, Object>of("items", list);
        assertEquals(list, MapUtils.getList(m, k, null));
    }

    @Test
    public void testGetUUID() {
        var uuid = UUID.randomUUID();
        var m = new HashMap<String, Object>();
        m.put("id", uuid);
        assertEquals(uuid, MapUtils.getUUID(m, "id"));

        m.put("idStr", uuid.toString());
        assertEquals(uuid, MapUtils.getUUID(m, "idStr"));

        m.put("empty", null);
        assertNull(MapUtils.getUUID(m, "missing"));
    }

    @Test
    public void testGetUUIDInvalidType() {
        var m = Map.<String, Object>of("id", 123);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetEnum() {
        var m = Map.<String, Object>of("dir", "NORTH");
        assertEquals(Direction.NORTH, MapUtils.getEnum(m, "dir", Direction.class, null));
    }

    @Test
    public void testGetEnumDefault() {
        var m = Collections.<String, Object>emptyMap();
        assertEquals(Direction.SOUTH, MapUtils.getEnum(m, "dir", Direction.class, Direction.SOUTH));
    }

    @Test
    public void testGetEnumInvalid() {
        var m = Map.<String, Object>of("dir", "INVALID");
        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getEnum(m, "dir", Direction.class, null));
    }

    @Test
    public void testAssertString() {
        var m = Map.<String, Object>of("name", "concord");
        assertEquals("concord", MapUtils.assertString(m, "name"));
    }

    @Test
    public void testAssertStringMissing() {
        var m = Collections.<String, Object>emptyMap();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertString(m, "name"));
    }

    @Test
    public void testAssertInt() {
        var m = Map.<String, Object>of("count", 5);
        assertEquals(5, MapUtils.assertInt(m, "count"));
    }

    @Test
    public void testAssertNumber() {
        var m = Map.<String, Object>of("val", 2.5);
        assertEquals(2.5, MapUtils.assertNumber(m, "val").doubleValue());
    }

    @Test
    public void testAssertUUID() {
        var uuid = UUID.randomUUID();
        var m = Map.<String, Object>of("id", uuid.toString());
        assertEquals(uuid, MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testAssertUUIDMissing() {
        var m = Collections.<String, Object>emptyMap();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testAssertMap() {
        Map<String, Object> inner = Map.of("x", "y");
        var m = Map.<String, Object>of("data", inner);
        assertEquals(inner, MapUtils.assertMap(m, "data"));
    }

    @Test
    public void testAssertList() {
        var list = List.of("a", "b");
        var m = Map.<String, Object>of("items", list);
        assertEquals(list, MapUtils.assertList(m, "items"));
    }

    @Test
    public void testGetWithNullMap() {
        assertNull(MapUtils.get(null, "key", null));
        assertEquals("default", MapUtils.get(null, "key", "default"));
    }

    @Test
    public void testGetTypeMismatchThrows() {
        var m = Map.<String, Object>of("key", 123);
        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getString(m, "key"));
    }

    enum Direction {
        NORTH, SOUTH, EAST, WEST
    }
}
