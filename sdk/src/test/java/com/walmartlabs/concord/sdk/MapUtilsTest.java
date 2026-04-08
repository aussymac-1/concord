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

    enum TestEnum { A, B, C }

    @Test
    public void testGetString() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");

        assertEquals("value", MapUtils.getString(m, "key"));
        assertNull(MapUtils.getString(m, "missing"));
    }

    @Test
    public void testGetStringWithDefault() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");

        assertEquals("value", MapUtils.getString(m, "key", "default"));
        assertEquals("default", MapUtils.getString(m, "missing", "default"));
    }

    @Test
    public void testGetStringWithHasKey() {
        Map<String, Object> m = new HashMap<>();
        m.put("myKey", "value");
        HasKey key = () -> "myKey";

        assertEquals("value", MapUtils.getString(m, key));
    }

    @Test
    public void testGetStringWithHasKeyAndDefault() {
        Map<String, Object> m = new HashMap<>();
        HasKey key = () -> "myKey";

        assertEquals("default", MapUtils.getString(m, key, "default"));
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
        m.put("flag", true);
        HasKey key = () -> "flag";

        assertTrue(MapUtils.getBoolean(m, key, false));
    }

    @Test
    public void testGetInt() {
        Map<String, Object> m = new HashMap<>();
        m.put("num", 42);

        assertEquals(42, MapUtils.getInt(m, "num", 0));
        assertEquals(0, MapUtils.getInt(m, "missing", 0));
    }

    @Test
    public void testGetNumber() {
        Map<String, Object> m = new HashMap<>();
        m.put("num", 3.14);

        assertEquals(3.14, MapUtils.getNumber(m, "num", 0));
        assertEquals(0, MapUtils.getNumber(m, "missing", 0));
    }

    @Test
    public void testGetMap() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("a", 1);
        Map<String, Object> m = new HashMap<>();
        m.put("sub", inner);

        assertEquals(inner, MapUtils.getMap(m, "sub", null));
        assertNull(MapUtils.getMap(m, "missing", null));
    }

    @Test
    public void testGetMapWithHasKey() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("a", 1);
        Map<String, Object> m = new HashMap<>();
        m.put("sub", inner);
        HasKey key = () -> "sub";

        assertEquals(inner, MapUtils.getMap(m, key, null));
    }

    @Test
    public void testGetList() {
        List<String> list = Arrays.asList("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("items", list);

        assertEquals(list, MapUtils.getList(m, "items", null));
        assertNull(MapUtils.getList(m, "missing", null));
    }

    @Test
    public void testGetListWithHasKey() {
        List<String> list = Arrays.asList("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("items", list);
        HasKey key = () -> "items";

        assertEquals(list, MapUtils.getList(m, key, null));
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
        m.put("val", "A");

        assertEquals(TestEnum.A, MapUtils.getEnum(m, "val", TestEnum.class, TestEnum.B));
        assertEquals(TestEnum.B, MapUtils.getEnum(m, "missing", TestEnum.class, TestEnum.B));
    }

    @Test
    public void testGetEnumInvalid() {
        Map<String, Object> m = new HashMap<>();
        m.put("val", "INVALID");

        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getEnum(m, "val", TestEnum.class, TestEnum.A));
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
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertString(m, "missing"));
    }

    @Test
    public void testAssertInt() {
        Map<String, Object> m = new HashMap<>();
        m.put("num", 42);

        assertEquals(42, MapUtils.assertInt(m, "num"));
    }

    @Test
    public void testAssertIntMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertInt(m, "missing"));
    }

    @Test
    public void testAssertNumber() {
        Map<String, Object> m = new HashMap<>();
        m.put("num", 3.14);

        assertEquals(3.14, MapUtils.assertNumber(m, "num"));
    }

    @Test
    public void testAssertUUID() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> m = new HashMap<>();
        m.put("id", uuid);

        assertEquals(uuid, MapUtils.assertUUID(m, "id"));
    }

    @Test
    public void testAssertUUIDMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertUUID(m, "missing"));
    }

    @Test
    public void testAssertMap() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("a", 1);
        Map<String, Object> m = new HashMap<>();
        m.put("sub", inner);

        assertEquals(inner, MapUtils.assertMap(m, "sub"));
    }

    @Test
    public void testAssertMapMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertMap(m, "missing"));
    }

    @Test
    public void testAssertList() {
        List<String> list = Arrays.asList("a", "b");
        Map<String, Object> m = new HashMap<>();
        m.put("items", list);

        assertEquals(list, MapUtils.assertList(m, "items"));
    }

    @Test
    public void testAssertListMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertList(m, "missing"));
    }

    @Test
    public void testGetWithInvalidType() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", 42);

        assertThrows(IllegalArgumentException.class, () -> MapUtils.getString(m, "key"));
    }

    @Test
    public void testGetWithNullMap() {
        assertNull(MapUtils.get(null, "key", null));
        assertEquals("default", MapUtils.get(null, "key", "default"));
    }

    @Test
    public void testAssertVariable() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");

        assertEquals("value", MapUtils.assertVariable(m, "key", String.class));
    }

    @Test
    public void testAssertVariableMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.assertVariable(m, "missing", String.class));
    }
}
