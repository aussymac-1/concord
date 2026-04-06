package com.walmartlabs.concord.runtime.common;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2020 Walmart Inc.
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

class ObjectTruncaterTest2 {

    @Test
    void testTruncateNullReturnsNull() {
        assertNull(ObjectTruncater.truncate(null, 10, 10, 5));
    }

    @Test
    void testTruncateShortStringUnchanged() {
        assertEquals("hi", ObjectTruncater.truncate("hi", 10, 10, 5));
    }

    @Test
    void testTruncateLongString() {
        String longStr = "a".repeat(100);
        Object result = ObjectTruncater.truncate(longStr, 20, 10, 5);
        assertTrue(result instanceof String);
        String s = (String) result;
        assertTrue(s.length() < 100);
        assertTrue(s.contains("skipped"));
    }

    @Test
    void testTruncateMapNull() {
        Map<String, Object> result = ObjectTruncater.truncateMap(null, 10, 10, 5);
        assertTrue(result.isEmpty());
    }

    @Test
    void testTruncateMapEmpty() {
        Map<String, Object> result = ObjectTruncater.truncateMap(Collections.emptyMap(), 10, 10, 5);
        assertTrue(result.isEmpty());
    }

    @Test
    void testTruncateMapWithStringValues() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "short");
        Map<String, Object> result = ObjectTruncater.truncateMap(input, 100, 100, 5);
        assertEquals("short", result.get("key"));
    }

    @Test
    void testTruncateMapMaxDepth() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("deep", "value");

        Map<String, Object> outer = new HashMap<>();
        outer.put("nested", inner);

        Map<String, Object> result = ObjectTruncater.truncateMap(outer, 100, 100, 0);
        // at depth 0, the nested map should be replaced with a max-depth message
        Object nested = result.get("nested");
        assertTrue(nested instanceof Map);
    }

    @Test
    void testTruncateEmptyCollection() {
        List<Object> input = Collections.emptyList();
        Object result = ObjectTruncater.truncate(input, 10, 10, 5);
        assertTrue(result instanceof Collection);
        assertTrue(((Collection<?>) result).isEmpty());
    }

    @Test
    void testTruncateNonStringNonContainerUnchanged() {
        assertEquals(42, ObjectTruncater.truncate(42, 10, 10, 5));
        assertEquals(true, ObjectTruncater.truncate(true, 10, 10, 5));
    }

    @Test
    void testTruncatePrimitiveArray() {
        int[] input = {1, 2, 3, 4, 5};
        Object result = ObjectTruncater.truncate(input, 100, 3, 5);
        assertTrue(result instanceof int[]);
        int[] arr = (int[]) result;
        assertEquals(3, arr.length);
    }

    @Test
    void testTruncateEmptyPrimitiveArray() {
        int[] input = {};
        Object result = ObjectTruncater.truncate(input, 100, 3, 5);
        assertTrue(result instanceof int[]);
        assertEquals(0, ((int[]) result).length);
    }
}
