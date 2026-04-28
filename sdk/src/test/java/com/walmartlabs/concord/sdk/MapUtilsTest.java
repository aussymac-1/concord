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

    private enum TestEnum {
        FOO, BAR, BAZ
    }

    @Test
    public void testGetEnumFound() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "FOO");

        TestEnum result = MapUtils.getEnum(m, "key", TestEnum.class, TestEnum.BAR);
        assertEquals(TestEnum.FOO, result);
    }

    @Test
    public void testGetEnumDefault() {
        Map<String, Object> m = new HashMap<>();

        TestEnum result = MapUtils.getEnum(m, "key", TestEnum.class, TestEnum.BAR);
        assertEquals(TestEnum.BAR, result);
    }

    @Test
    public void testGetEnumInvalid() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "INVALID");

        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getEnum(m, "key", TestEnum.class, TestEnum.BAR));
    }

    @Test
    public void testGetUUIDFromString() {
        UUID expected = UUID.randomUUID();
        Map<String, Object> m = new HashMap<>();
        m.put("id", expected.toString());

        UUID result = MapUtils.getUUID(m, "id");
        assertEquals(expected, result);
    }

    @Test
    public void testGetUUIDFromUUID() {
        UUID expected = UUID.randomUUID();
        Map<String, Object> m = new HashMap<>();
        m.put("id", expected);

        UUID result = MapUtils.getUUID(m, "id");
        assertEquals(expected, result);
    }

    @Test
    public void testGetUUIDNull() {
        Map<String, Object> m = new HashMap<>();

        UUID result = MapUtils.getUUID(m, "id");
        assertNull(result);
    }

    @Test
    public void testGetUUIDInvalidType() {
        Map<String, Object> m = new HashMap<>();
        m.put("id", 12345);

        assertThrows(IllegalArgumentException.class, () -> MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetString() {
        Map<String, Object> m = new HashMap<>();
        m.put("name", "hello");

        assertEquals("hello", MapUtils.getString(m, "name"));
    }

    @Test
    public void testGetStringDefault() {
        Map<String, Object> m = new HashMap<>();

        assertEquals("default", MapUtils.getString(m, "name", "default"));
    }

    @Test
    public void testGetStringNull() {
        Map<String, Object> m = new HashMap<>();

        assertNull(MapUtils.getString(m, "name"));
    }

    @Test
    public void testGetStringWithHasKey() {
        Map<String, Object> m = new HashMap<>();
        m.put("myKey", "myValue");

        HasKey k = () -> "myKey";
        assertEquals("myValue", MapUtils.getString(m, k));
    }

    @Test
    public void testGetStringWithHasKeyAndDefault() {
        Map<String, Object> m = new HashMap<>();

        HasKey k = () -> "missing";
        assertEquals("def", MapUtils.getString(m, k, "def"));
    }

    @Test
    public void testGetMap() {
        Map<String, Object> inner = Collections.singletonMap("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("nested", inner);

        Map<String, Object> result = MapUtils.getMap(m, "nested", null);
        assertEquals(inner, result);
    }

    @Test
    public void testGetMapDefault() {
        Map<String, Object> m = new HashMap<>();
        Map<String, Object> def = Collections.singletonMap("x", "y");

        Map<String, Object> result = MapUtils.getMap(m, "missing", def);
        assertEquals(def, result);
    }

    @Test
    public void testGetMapWithHasKey() {
        Map<String, Object> inner = Collections.singletonMap("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("nested", inner);

        HasKey k = () -> "nested";
        Map<String, Object> result = MapUtils.getMap(m, k, null);
        assertEquals(inner, result);
    }

    @Test
    public void testGetList() {
        List<String> expected = Arrays.asList("a", "b", "c");
        Map<String, Object> m = new HashMap<>();
        m.put("items", expected);

        List<String> result = MapUtils.getList(m, "items", null);
        assertEquals(expected, result);
    }

    @Test
    public void testGetListDefault() {
        Map<String, Object> m = new HashMap<>();
        List<String> def = Collections.singletonList("default");

        List<String> result = MapUtils.getList(m, "missing", def);
        assertEquals(def, result);
    }

    @Test
    public void testGetListWithHasKey() {
        List<String> expected = Arrays.asList("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("items", expected);

        HasKey k = () -> "items";
        List<String> result = MapUtils.getList(m, k, null);
        assertEquals(expected, result);
    }

    @Test
    public void testGetBoolean() {
        Map<String, Object> m = new HashMap<>();
        m.put("flag", true);

        assertTrue(MapUtils.getBoolean(m, "flag", false));
    }

    @Test
    public void testGetBooleanDefault() {
        Map<String, Object> m = new HashMap<>();

        assertTrue(MapUtils.getBoolean(m, "flag", true));
    }

    @Test
    public void testGetBooleanWithHasKey() {
        Map<String, Object> m = new HashMap<>();
        m.put("flag", false);

        HasKey k = () -> "flag";
        assertFalse(MapUtils.getBoolean(m, k, true));
    }

    @Test
    public void testGetInt() {
        Map<String, Object> m = new HashMap<>();
        m.put("count", 42);

        assertEquals(42, MapUtils.getInt(m, "count", 0));
    }

    @Test
    public void testGetIntDefault() {
        Map<String, Object> m = new HashMap<>();

        assertEquals(-1, MapUtils.getInt(m, "count", -1));
    }

    @Test
    public void testGetNumber() {
        Map<String, Object> m = new HashMap<>();
        m.put("val", 3.14);

        assertEquals(3.14, MapUtils.getNumber(m, "val", 0));
    }

    @Test
    public void testGetNumberDefault() {
        Map<String, Object> m = new HashMap<>();

        assertEquals(0, MapUtils.getNumber(m, "val", 0));
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

        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testAssertInt() {
        Map<String, Object> m = new HashMap<>();
        m.put("val", 99);

        assertEquals(99, MapUtils.assertInt(m, "val"));
    }

    @Test
    public void testAssertIntMissing() {
        Map<String, Object> m = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertInt(m, "val"));
    }

    @Test
    public void testAssertNumber() {
        Map<String, Object> m = new HashMap<>();
        m.put("val", 42L);

        assertEquals(42L, MapUtils.assertNumber(m, "val"));
    }

    @Test
    public void testAssertNumberMissing() {
        Map<String, Object> m = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertNumber(m, "val"));
    }

    @Test
    public void testAssertString() {
        Map<String, Object> m = new HashMap<>();
        m.put("name", "concord");

        assertEquals("concord", MapUtils.assertString(m, "name"));
    }

    @Test
    public void testAssertStringMissing() {
        Map<String, Object> m = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertString(m, "name"));
    }

    @Test
    public void testAssertMap() {
        Map<String, Object> inner = Collections.singletonMap("k", "v");
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
    public void testAssertList() {
        List<String> expected = Arrays.asList("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("items", expected);

        assertEquals(expected, MapUtils.assertList(m, "items"));
    }

    @Test
    public void testAssertListMissing() {
        Map<String, Object> m = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertList(m, "items"));
    }

    @Test
    public void testGetTypeMismatch() {
        Map<String, Object> m = new HashMap<>();
        m.put("name", 123);

        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getString(m, "name"));
    }

    @Test
    public void testGetNullMap() {
        Object result = MapUtils.get(null, "key", "default");
        assertEquals("default", result);
    }
}
