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
    public void testGetStringReturnsValue() {
        Map<String, Object> m = Map.of("key", "value");
        assertEquals("value", MapUtils.getString(m, "key"));
    }

    @Test
    public void testGetStringReturnsNullForMissing() {
        Map<String, Object> m = Map.of("key", "value");
        assertNull(MapUtils.getString(m, "missing"));
    }

    @Test
    public void testGetStringWithDefault() {
        Map<String, Object> m = Map.of("key", "value");
        assertEquals("value", MapUtils.getString(m, "key", "default"));
        assertEquals("default", MapUtils.getString(m, "missing", "default"));
    }

    @Test
    public void testGetBooleanReturnsValue() {
        Map<String, Object> m = Map.of("flag", true);
        assertTrue(MapUtils.getBoolean(m, "flag", false));
    }

    @Test
    public void testGetBooleanReturnsDefault() {
        Map<String, Object> m = Map.of("other", "x");
        assertFalse(MapUtils.getBoolean(m, "flag", false));
    }

    @Test
    public void testGetIntReturnsValue() {
        Map<String, Object> m = Map.of("count", 42);
        assertEquals(42, MapUtils.getInt(m, "count", 0));
    }

    @Test
    public void testGetIntReturnsDefault() {
        Map<String, Object> m = Map.of("other", "x");
        assertEquals(99, MapUtils.getInt(m, "count", 99));
    }

    @Test
    public void testGetMapReturnsValue() {
        Map<String, Object> inner = Map.of("a", "b");
        Map<String, Object> m = Map.of("nested", inner);
        assertEquals(inner, MapUtils.getMap(m, "nested", Collections.emptyMap()));
    }

    @Test
    public void testGetListReturnsValue() {
        List<String> list = List.of("a", "b");
        Map<String, Object> m = Map.of("items", list);
        assertEquals(list, MapUtils.getList(m, "items", Collections.emptyList()));
    }

    @Test
    public void testGetNumberReturnsValue() {
        Map<String, Object> m = Map.of("num", 3.14);
        assertEquals(3.14, MapUtils.getNumber(m, "num", 0));
    }

    @Test
    public void testGetUUIDFromString() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> m = Map.of("id", uuid.toString());
        assertEquals(uuid, MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDFromUUID() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> m = Map.of("id", uuid);
        assertEquals(uuid, MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDReturnsNullForMissing() {
        Map<String, Object> m = Map.of("other", "x");
        assertNull(MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDThrowsForInvalidType() {
        Map<String, Object> m = Map.of("id", 123);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testAssertStringReturnsValue() {
        Map<String, Object> m = Map.of("key", "value");
        assertEquals("value", MapUtils.assertString(m, "key"));
    }

    @Test
    public void testAssertStringThrowsForMissing() {
        Map<String, Object> m = Map.of("other", "x");
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertString(m, "key"));
    }

    @Test
    public void testAssertIntReturnsValue() {
        Map<String, Object> m = Map.of("count", 42);
        assertEquals(42, MapUtils.assertInt(m, "count"));
    }

    @Test
    public void testAssertUUIDReturnsValue() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> m = Map.of("id", uuid);
        assertEquals(uuid, MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testAssertUUIDThrowsForMissing() {
        Map<String, Object> m = Map.of("other", "x");
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testGetWithNullMap() {
        assertNull(MapUtils.get(null, "key", null));
        assertEquals("default", MapUtils.get(null, "key", "default"));
    }

    @Test
    public void testGetThrowsForTypeMismatch() {
        Map<String, Object> m = Map.of("key", 123);
        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.get(m, "key", null, String.class));
    }

    @Test
    public void testGetEnumReturnsValue() {
        Map<String, Object> m = Map.of("level", "INFO");
        assertEquals(TestEnum.INFO, MapUtils.getEnum(m, "level", TestEnum.class, TestEnum.DEBUG));
    }

    @Test
    public void testGetEnumReturnsDefault() {
        Map<String, Object> m = Map.of("other", "x");
        assertEquals(TestEnum.DEBUG, MapUtils.getEnum(m, "level", TestEnum.class, TestEnum.DEBUG));
    }

    @Test
    public void testGetEnumThrowsForInvalid() {
        Map<String, Object> m = Map.of("level", "INVALID");
        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getEnum(m, "level", TestEnum.class, TestEnum.DEBUG));
    }

    @Test
    public void testAssertMapReturnsValue() {
        Map<String, Object> inner = Map.of("a", "b");
        Map<String, Object> m = Map.of("nested", inner);
        assertEquals(inner, MapUtils.assertMap(m, "nested"));
    }

    @Test
    public void testAssertListReturnsValue() {
        List<String> list = List.of("a", "b");
        Map<String, Object> m = Map.of("items", list);
        assertEquals(list, MapUtils.assertList(m, "items"));
    }

    @Test
    public void testAssertNumberReturnsValue() {
        Map<String, Object> m = Map.of("num", 42L);
        assertEquals(42L, MapUtils.assertNumber(m, "num"));
    }

    private enum TestEnum {
        DEBUG, INFO, WARN, ERROR
    }
}
