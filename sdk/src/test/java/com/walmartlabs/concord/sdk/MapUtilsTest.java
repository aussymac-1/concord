package com.walmartlabs.concord.sdk;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2026 Walmart Inc.
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

class MapUtilsTest {

    @Test
    void testGetStringPresent() {
        var m = Map.<String, Object>of("key", "value");
        assertEquals("value", MapUtils.getString(m, "key"));
    }

    @Test
    void testGetStringMissing() {
        var m = Map.<String, Object>of();
        assertNull(MapUtils.getString(m, "key"));
    }

    @Test
    void testGetStringDefault() {
        var m = Map.<String, Object>of();
        assertEquals("default", MapUtils.getString(m, "key", "default"));
    }

    @Test
    void testGetStringWrongType() {
        var m = Map.<String, Object>of("key", 123);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getString(m, "key"));
    }

    @Test
    void testGetBooleanPresent() {
        var m = Map.<String, Object>of("flag", true);
        assertTrue(MapUtils.getBoolean(m, "flag", false));
    }

    @Test
    void testGetBooleanDefault() {
        var m = Map.<String, Object>of();
        assertFalse(MapUtils.getBoolean(m, "flag", false));
    }

    @Test
    void testGetIntPresent() {
        var m = Map.<String, Object>of("count", 42);
        assertEquals(42, MapUtils.getInt(m, "count", 0));
    }

    @Test
    void testGetIntDefault() {
        var m = Map.<String, Object>of();
        assertEquals(99, MapUtils.getInt(m, "count", 99));
    }

    @Test
    void testGetNumberPresent() {
        var m = Map.<String, Object>of("num", 3.14);
        assertEquals(3.14, MapUtils.getNumber(m, "num", 0));
    }

    @Test
    void testGetMapPresent() {
        var inner = Map.<String, Object>of("a", "b");
        var m = Map.<String, Object>of("nested", inner);
        assertEquals(inner, MapUtils.getMap(m, "nested", null));
    }

    @Test
    void testGetMapDefault() {
        var m = Map.<String, Object>of();
        var def = Map.of("x", "y");
        assertEquals(def, MapUtils.getMap(m, "nested", def));
    }

    @Test
    void testGetListPresent() {
        var list = List.of("a", "b");
        var m = Map.<String, Object>of("items", list);
        assertEquals(list, MapUtils.getList(m, "items", null));
    }

    @Test
    void testGetListDefault() {
        var m = Map.<String, Object>of();
        var def = List.of("x");
        assertEquals(def, MapUtils.getList(m, "items", def));
    }

    @Test
    void testGetUUIDFromString() {
        var expected = UUID.randomUUID();
        var m = Map.<String, Object>of("id", expected.toString());
        assertEquals(expected, MapUtils.getUUID(m, "id"));
    }

    @Test
    void testGetUUIDFromUUID() {
        var expected = UUID.randomUUID();
        var m = Map.<String, Object>of("id", expected);
        assertEquals(expected, MapUtils.getUUID(m, "id"));
    }

    @Test
    void testGetUUIDNull() {
        var m = new HashMap<String, Object>();
        m.put("id", null);
        assertNull(MapUtils.getUUID(m, "id"));
    }

    @Test
    void testGetUUIDWrongType() {
        var m = Map.<String, Object>of("id", 123);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getUUID(m, "id"));
    }

    @Test
    void testAssertStringPresent() {
        var m = Map.<String, Object>of("name", "test");
        assertEquals("test", MapUtils.assertString(m, "name"));
    }

    @Test
    void testAssertStringMissing() {
        var m = Map.<String, Object>of();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertString(m, "name"));
    }

    @Test
    void testAssertIntPresent() {
        var m = Map.<String, Object>of("count", 5);
        assertEquals(5, MapUtils.assertInt(m, "count"));
    }

    @Test
    void testAssertIntMissing() {
        var m = Map.<String, Object>of();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertInt(m, "count"));
    }

    @Test
    void testAssertUUIDPresent() {
        var id = UUID.randomUUID();
        var m = Map.<String, Object>of("id", id);
        assertEquals(id, MapUtils.assertUUID(m, "id"));
    }

    @Test
    void testAssertUUIDMissing() {
        var m = new HashMap<String, Object>();
        m.put("id", null);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertUUID(m, "id"));
    }

    @Test
    void testAssertMapPresent() {
        var inner = Map.<String, Object>of("k", "v");
        var m = Map.<String, Object>of("data", inner);
        assertEquals(inner, MapUtils.assertMap(m, "data"));
    }

    @Test
    void testAssertListPresent() {
        var list = List.of("a", "b");
        var m = Map.<String, Object>of("items", list);
        assertEquals(list, MapUtils.assertList(m, "items"));
    }

    @Test
    void testGetFromNullMap() {
        assertNull(MapUtils.get(null, "key", null));
    }

    @Test
    void testGetFromNullMapWithDefault() {
        assertEquals("default", MapUtils.get(null, "key", "default"));
    }

    enum TestEnum { A, B, C }

    @Test
    void testGetEnumPresent() {
        var m = Map.<String, Object>of("action", "B");
        assertEquals(TestEnum.B, MapUtils.getEnum(m, "action", TestEnum.class, null));
    }

    @Test
    void testGetEnumDefault() {
        var m = Map.<String, Object>of();
        assertEquals(TestEnum.A, MapUtils.getEnum(m, "action", TestEnum.class, TestEnum.A));
    }

    @Test
    void testGetEnumInvalid() {
        var m = Map.<String, Object>of("action", "INVALID");
        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getEnum(m, "action", TestEnum.class, null));
    }
}
