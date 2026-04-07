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
        m.put("name", "alice");
        assertEquals("alice", MapUtils.getString(m, "name"));
    }

    @Test
    public void testGetStringMissing() {
        Map<String, Object> m = new HashMap<>();
        assertNull(MapUtils.getString(m, "name"));
    }

    @Test
    public void testGetStringDefault() {
        Map<String, Object> m = new HashMap<>();
        assertEquals("default", MapUtils.getString(m, "name", "default"));
    }

    @Test
    public void testGetStringInvalidType() {
        Map<String, Object> m = new HashMap<>();
        m.put("name", 123);
        assertThrows(IllegalArgumentException.class, () -> MapUtils.getString(m, "name"));
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
    public void testGetMapPresent() {
        Map<String, Object> m = new HashMap<>();
        Map<String, String> nested = new HashMap<>();
        nested.put("k", "v");
        m.put("data", nested);

        Map<String, String> result = MapUtils.getMap(m, "data", null);
        assertEquals("v", result.get("k"));
    }

    @Test
    public void testGetMapDefault() {
        Map<String, Object> m = new HashMap<>();
        Map<String, String> defaultVal = Collections.singletonMap("k", "default");
        Map<String, String> result = MapUtils.getMap(m, "data", defaultVal);
        assertEquals("default", result.get("k"));
    }

    @Test
    public void testGetListPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("items", Arrays.asList("a", "b", "c"));
        List<String> result = MapUtils.getList(m, "items", null);
        assertEquals(3, result.size());
    }

    @Test
    public void testGetListDefault() {
        Map<String, Object> m = new HashMap<>();
        List<String> result = MapUtils.getList(m, "items", Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetNumberPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("num", 3.14);
        assertEquals(3.14, MapUtils.getNumber(m, "num", 0));
    }

    @Test
    public void testGetUUIDString() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> m = new HashMap<>();
        m.put("id", uuid.toString());
        assertEquals(uuid, MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDObject() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> m = new HashMap<>();
        m.put("id", uuid);
        assertEquals(uuid, MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDNull() {
        Map<String, Object> m = new HashMap<>();
        assertNull(MapUtils.getUUID(m, "id"));
    }

    @Test
    public void testGetUUIDInvalidType() {
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
        m.put("count", 5);
        assertEquals(5, MapUtils.assertInt(m, "count"));
    }

    @Test
    public void testAssertIntMissing() {
        Map<String, Object> m = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertInt(m, "count"));
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
    public void testAssertMapPresent() {
        Map<String, Object> m = new HashMap<>();
        Map<String, String> nested = Collections.singletonMap("k", "v");
        m.put("data", nested);
        assertEquals(nested, MapUtils.assertMap(m, "data"));
    }

    @Test
    public void testAssertListPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("items", Arrays.asList(1, 2));
        List<Integer> result = MapUtils.assertList(m, "items");
        assertEquals(2, result.size());
    }

    @Test
    public void testGetFromNullMap() {
        Object result = MapUtils.get(null, "key", "default");
        assertEquals("default", result);
    }

    private enum TestEnum {
        VALUE_A, VALUE_B
    }

    @Test
    public void testGetEnumPresent() {
        Map<String, Object> m = new HashMap<>();
        m.put("type", "VALUE_A");
        assertEquals(TestEnum.VALUE_A, MapUtils.getEnum(m, "type", TestEnum.class, TestEnum.VALUE_B));
    }

    @Test
    public void testGetEnumDefault() {
        Map<String, Object> m = new HashMap<>();
        assertEquals(TestEnum.VALUE_B, MapUtils.getEnum(m, "type", TestEnum.class, TestEnum.VALUE_B));
    }

    @Test
    public void testGetEnumInvalid() {
        Map<String, Object> m = new HashMap<>();
        m.put("type", "INVALID");
        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getEnum(m, "type", TestEnum.class, TestEnum.VALUE_A));
    }
}
