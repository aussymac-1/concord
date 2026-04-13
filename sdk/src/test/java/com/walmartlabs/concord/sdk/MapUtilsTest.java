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

public class MapUtilsTest {

    @Test
    public void testGetStringPresent() {
        var m = Map.of("key", (Object) "value");
        assertEquals("value", MapUtils.getString(m, "key"));
    }

    @Test
    public void testGetStringMissing() {
        Map<String, Object> m = Collections.emptyMap();
        assertNull(MapUtils.getString(m, "key"));
    }

    @Test
    public void testGetStringDefault() {
        Map<String, Object> m = Collections.emptyMap();
        assertEquals("def", MapUtils.getString(m, "key", "def"));
    }

    @Test
    public void testGetStringWithHasKey() {
        HasKey k = () -> "myKey";
        var m = Map.of("myKey", (Object) "myVal");
        assertEquals("myVal", MapUtils.getString(m, k));
    }

    @Test
    public void testGetStringWithHasKeyDefault() {
        HasKey k = () -> "myKey";
        Map<String, Object> m = Collections.emptyMap();
        assertEquals("fallback", MapUtils.getString(m, k, "fallback"));
    }

    @Test
    public void testGetBooleanPresent() {
        var m = Map.of("flag", (Object) true);
        assertTrue(MapUtils.getBoolean(m, "flag", false));
    }

    @Test
    public void testGetBooleanDefault() {
        Map<String, Object> m = Collections.emptyMap();
        assertTrue(MapUtils.getBoolean(m, "flag", true));
    }

    @Test
    public void testGetBooleanWithHasKey() {
        HasKey k = () -> "flag";
        var m = Map.of("flag", (Object) false);
        assertFalse(MapUtils.getBoolean(m, k, true));
    }

    @Test
    public void testGetIntPresent() {
        var m = Map.of("num", (Object) 42);
        assertEquals(42, MapUtils.getInt(m, "num", 0));
    }

    @Test
    public void testGetIntDefault() {
        Map<String, Object> m = Collections.emptyMap();
        assertEquals(99, MapUtils.getInt(m, "num", 99));
    }

    @Test
    public void testGetNumber() {
        var m = Map.of("n", (Object) 3.14);
        assertEquals(3.14, MapUtils.getNumber(m, "n", 0).doubleValue());
    }

    @Test
    public void testGetMapPresent() {
        var inner = Map.of("a", "b");
        var m = Map.of("nested", (Object) inner);
        assertEquals(inner, MapUtils.getMap(m, "nested", null));
    }

    @Test
    public void testGetMapWithHasKey() {
        HasKey k = () -> "nested";
        var inner = Map.of("a", "b");
        var m = Map.of("nested", (Object) inner);
        assertEquals(inner, MapUtils.getMap(m, k, null));
    }

    @Test
    public void testGetListPresent() {
        var list = List.of("a", "b");
        var m = Map.of("items", (Object) list);
        assertEquals(list, MapUtils.getList(m, "items", null));
    }

    @Test
    public void testGetListWithHasKey() {
        HasKey k = () -> "items";
        var list = List.of("x");
        var m = Map.of("items", (Object) list);
        assertEquals(list, MapUtils.getList(m, k, null));
    }

    @Test
    public void testGetUUIDFromString() {
        var uuid = UUID.randomUUID();
        var m = Map.of("id", (Object) uuid.toString());
        assertEquals(uuid, MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDFromUUID() {
        var uuid = UUID.randomUUID();
        var m = Map.of("id", (Object) uuid);
        assertEquals(uuid, MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDNull() {
        Map<String, Object> m = Collections.emptyMap();
        assertNull(MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDInvalidType() {
        var m = Map.of("id", (Object) 123);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testAssertStringPresent() {
        var m = Map.of("key", (Object) "val");
        assertEquals("val", MapUtils.assertString(m, "key"));
    }

    @Test
    public void testAssertStringMissing() {
        Map<String, Object> m = Collections.emptyMap();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertString(m, "key"));
    }

    @Test
    public void testAssertIntPresent() {
        var m = Map.of("n", (Object) 5);
        assertEquals(5, MapUtils.assertInt(m, "n"));
    }

    @Test
    public void testAssertUUIDPresent() {
        var uuid = UUID.randomUUID();
        var m = Map.of("id", (Object) uuid.toString());
        assertEquals(uuid, MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testAssertUUIDMissing() {
        Map<String, Object> m = Collections.emptyMap();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testAssertMapPresent() {
        var inner = Map.of("a", "b");
        var m = Map.of("nested", (Object) inner);
        assertEquals(inner, MapUtils.assertMap(m, "nested"));
    }

    @Test
    public void testAssertListPresent() {
        var list = List.of("a");
        var m = Map.of("items", (Object) list);
        assertEquals(list, MapUtils.assertList(m, "items"));
    }

    @Test
    public void testGetTypeMismatch() {
        var m = Map.of("key", (Object) 123);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getString(m, "key"));
    }

    @Test
    public void testGetFromNullMap() {
        assertEquals("def", MapUtils.get(null, "key", "def"));
    }

    enum TestEnum { A, B, C }

    @Test
    public void testGetEnumPresent() {
        var m = Map.of("e", (Object) "B");
        assertEquals(TestEnum.B, MapUtils.getEnum(m, "e", TestEnum.class, TestEnum.A));
    }

    @Test
    public void testGetEnumDefault() {
        Map<String, Object> m = Collections.emptyMap();
        assertEquals(TestEnum.A, MapUtils.getEnum(m, "e", TestEnum.class, TestEnum.A));
    }

    @Test
    public void testGetEnumInvalid() {
        var m = Map.of("e", (Object) "INVALID");
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getEnum(m, "e", TestEnum.class, TestEnum.A));
    }
}
