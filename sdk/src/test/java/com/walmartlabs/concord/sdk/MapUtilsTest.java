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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MapUtilsTest {

    enum Color {RED, BLUE}

    @Test
    public void testGetEnumReturnsDefaultWhenAbsent() {
        assertEquals(Color.RED, MapUtils.getEnum(Map.of(), "c", Color.class, Color.RED));
    }

    @Test
    public void testGetEnumParsesString() {
        assertEquals(Color.BLUE, MapUtils.getEnum(Map.of("c", "BLUE"), "c", Color.class, Color.RED));
    }

    @Test
    public void testGetEnumThrowsOnInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getEnum(Map.of("c", "GREEN"), "c", Color.class, Color.RED));
    }

    @Test
    public void testGetUUIDFromString() {
        var u = UUID.randomUUID();
        assertEquals(u, MapUtils.getUUID(Map.of("u", u.toString()), "u"));
    }

    @Test
    public void testGetUUIDFromUUID() {
        var u = UUID.randomUUID();
        assertEquals(u, MapUtils.getUUID(Map.of("u", u), "u"));
    }

    @Test
    public void testGetUUIDNullForMissing() {
        assertNull(MapUtils.getUUID(Map.of(), "u"));
    }

    @Test
    public void testGetUUIDThrowsOnInvalidType() {
        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getUUID(Map.of("u", 1), "u"));
    }

    @Test
    public void testGetStringDefaults() {
        assertNull(MapUtils.getString(Map.of(), "k"));
        assertEquals("def", MapUtils.getString(Map.of(), "k", "def"));
        assertEquals("hi", MapUtils.getString(Map.of("k", "hi"), "k"));
    }

    @Test
    public void testGetStringWithHasKey() {
        HasKey hk = () -> "k";
        assertEquals("hi", MapUtils.getString(Map.of("k", "hi"), hk));
        assertEquals("def", MapUtils.getString(Map.of(), hk, "def"));
    }

    @Test
    public void testGetMap() {
        Map<String, Object> sub = Map.of("a", 1);
        assertEquals(sub, MapUtils.getMap(Map.of("k", sub), "k", Map.of()));
        assertEquals(Map.of(), MapUtils.getMap(Map.of(), "k", Map.of()));
    }

    @Test
    public void testGetMapWithHasKey() {
        HasKey hk = () -> "k";
        assertEquals(Map.of(), MapUtils.getMap(Map.of(), hk, Map.of()));
    }

    @Test
    public void testGetList() {
        List<Integer> list = List.of(1, 2);
        assertEquals(list, MapUtils.getList(Map.of("k", list), "k", List.of()));
        assertEquals(List.of(), MapUtils.getList(Map.of(), "k", List.of()));
    }

    @Test
    public void testGetListWithHasKey() {
        HasKey hk = () -> "k";
        assertEquals(List.of(), MapUtils.getList(Map.of(), hk, List.of()));
    }

    @Test
    public void testGetBoolean() {
        assertTrue(MapUtils.getBoolean(Map.of("k", true), "k", false));
        assertFalse(MapUtils.getBoolean(Map.of(), "k", false));
    }

    @Test
    public void testGetBooleanHasKey() {
        HasKey hk = () -> "k";
        assertTrue(MapUtils.getBoolean(Map.of("k", true), hk, false));
    }

    @Test
    public void testGetInt() {
        assertEquals(5, MapUtils.getInt(Map.of("k", 5), "k", 0));
        assertEquals(7, MapUtils.getInt(Map.of(), "k", 7));
    }

    @Test
    public void testGetNumber() {
        assertEquals(2.5, MapUtils.getNumber(Map.of("k", 2.5), "k", 0));
        assertEquals(0, MapUtils.getNumber(Map.of(), "k", 0));
    }

    @Test
    public void testAssertUUID() {
        var u = UUID.randomUUID();
        assertEquals(u, MapUtils.assertUUID(Map.of("u", u), "u"));
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertUUID(Map.of(), "u"));
    }

    @Test
    public void testAssertInt() {
        assertEquals(5, MapUtils.assertInt(Map.of("k", 5), "k"));
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertInt(Map.of(), "k"));
    }

    @Test
    public void testAssertNumber() {
        assertEquals(5, MapUtils.assertNumber(Map.of("k", 5), "k"));
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertNumber(Map.of(), "k"));
    }

    @Test
    public void testAssertString() {
        assertEquals("hi", MapUtils.assertString(Map.of("k", "hi"), "k"));
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertString(Map.of(), "k"));
    }

    @Test
    public void testAssertMap() {
        Map<String, Object> sub = Map.of("a", 1);
        assertEquals(sub, MapUtils.assertMap(Map.of("k", sub), "k"));
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertMap(Map.of(), "k"));
    }

    @Test
    public void testAssertList() {
        var list = List.of(1);
        assertEquals(list, MapUtils.assertList(Map.of("k", list), "k"));
        assertThrows(IllegalArgumentException.class, () -> MapUtils.assertList(Map.of(), "k"));
    }

    @Test
    public void testGetTypeMismatch() {
        assertThrows(IllegalArgumentException.class,
                () -> MapUtils.getString(Map.of("k", 1), "k"));
    }

    @Test
    public void testGetWithNullMapReturnsDefault() {
        assertEquals("def", MapUtils.getString(null, "k", "def"));
        assertEquals(7, MapUtils.getInt(null, "k", 7));
    }

    @Test
    public void testGetWithExplicitNullValue() {
        var m = new HashMap<String, Object>();
        m.put("k", null);
        assertNull(MapUtils.getString(m, "k"));
        assertEquals("def", MapUtils.getString(m, "k", "def"));
    }
}
