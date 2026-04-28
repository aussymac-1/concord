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

public class ObjectTruncaterTest {

    @Test
    public void testTruncArray() {
        List<Integer> value = Arrays.asList(1, 2, 3, 4, 5);
        int maxStringLength = 3;
        int maxArrayLength = 2;
        int maxDepth = 2;

        // ---
        Object result = ObjectTruncater.truncate(value, maxStringLength, maxArrayLength, maxDepth);
        assertEquals(Arrays.asList(1, "skipped 3 lines", 5), result);
    }

    @Test
    public void testTruncArray1() {
        int[] value = new int[]{1, 2, 3, 4, 5};
        int maxStringLength = 3;
        int maxArrayLength = 2;
        int maxDepth = 2;

        // ---
        Object result = ObjectTruncater.truncate(value, maxStringLength, maxArrayLength, maxDepth);

        assertTrue(result.getClass().getComponentType().isPrimitive());
        assertArrayEquals(new int[]{1, 2}, (int[]) result);
    }

    @Test
    public void testTruncArray2() {
        List<Integer> value = Arrays.asList(1, 2, 3, 4, 5);
        int maxStringLength = 3;
        int maxArrayLength = 3;
        int maxDepth = 2;

        // ---
        Object result = ObjectTruncater.truncate(value, maxStringLength, maxArrayLength, maxDepth);
        assertEquals(Arrays.asList(1, 2, "skipped 2 lines", 5), result);
    }

    @Test
    public void testTruncArray3() {
        List<Integer> value = Arrays.asList(1, 2, 3, 4, 5);
        int maxStringLength = 3;
        int maxArrayLength = 4;
        int maxDepth = 2;

        // ---
        Object result = ObjectTruncater.truncate(value, maxStringLength, maxArrayLength, maxDepth);
        assertEquals(Arrays.asList(1, 2, "skipped 1 lines", 4, 5), result);
    }

    @Test
    public void testTruncArray4() {
        List<Integer> value = Arrays.asList(1, 2, 3);
        int maxStringLength = 3;
        int maxArrayLength = 4;
        int maxDepth = 2;

        // ---
        Object result = ObjectTruncater.truncate(value, maxStringLength, maxArrayLength, maxDepth);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    public void testTruncArray5() {
        List<Object> value = Arrays.asList(1, 2, Arrays.asList(11, 22, 33, 44, 55));
        int maxStringLength = 3;
        int maxArrayLength = 4;
        int maxDepth = 2;

        // ---
        Object result = ObjectTruncater.truncate(value, maxStringLength, maxArrayLength, maxDepth);
        assertEquals(Arrays.asList(1, 2, Arrays.asList(11, 22, "skipped 1 lines", 44, 55)), result);
    }

    @Test
    public void testTruncArray6() {
        List<Object> value = Arrays.asList(1, 2, Arrays.asList(11, Arrays.asList(111, 222)));
        int maxStringLength = 3;
        int maxArrayLength = 4;
        int maxDepth = 1;

        // ---
        Object result = ObjectTruncater.truncate(value, maxStringLength, maxArrayLength, maxDepth);
        assertEquals(Arrays.asList(1, 2, Arrays.asList(11, Collections.singletonList("skipped: max depth reached"))), result);
    }

    @Test
    public void testTruncString() {
        Map<String, Object> value = new HashMap<>();
        value.put("k", "123456");
        int maxStringLength = 3;
        int maxArrayLength = 4;
        int maxDepth = 2;

        // ---
        Object result = ObjectTruncater.truncate(value, maxStringLength, maxArrayLength, maxDepth);
        assertEquals(Collections.<String, Object>singletonMap("k", "12...[skipped 3 chars]...6"), result);
    }

    @Test
    public void testTruncString2() {
        Map<String, Object> value = new HashMap<>();
        value.put("k", "123");
        int maxStringLength = 3;
        int maxArrayLength = 4;
        int maxDepth = 2;

        // ---
        Object result = ObjectTruncater.truncate(value, maxStringLength, maxArrayLength, maxDepth);
        assertEquals(Collections.<String, Object>singletonMap("k", "123"), result);
    }

    @Test
    public void testTruncString3() {
        Map<String, Object> value = new HashMap<>();
        value.put("k", "1234567890");
        int maxStringLength = 2;
        int maxArrayLength = 4;
        int maxDepth = 2;

        // ---
        Object result = ObjectTruncater.truncate(value, maxStringLength, maxArrayLength, maxDepth);
        assertEquals(Collections.<String, Object>singletonMap("k", "1...[skipped 8 chars]...0"), result);
    }

    @Test
    public void testTruncateNull() {
        assertNull(ObjectTruncater.truncate(null, 10, 10, 10));
    }

    @Test
    public void testTruncateMapNull() {
        Map<String, Object> result = ObjectTruncater.truncateMap(null, 10, 10, 10);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testTruncateMapEmpty() {
        Map<String, Object> result = ObjectTruncater.truncateMap(Collections.emptyMap(), 10, 10, 10);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testTruncateEmptyCollection() {
        Object result = ObjectTruncater.truncate(Collections.emptyList(), 10, 10, 10);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testTruncateEmptyArray() {
        Object[] empty = new Object[0];
        Object result = ObjectTruncater.truncate(empty, 10, 10, 10);
        assertArrayEquals(empty, (Object[]) result);
    }

    @Test
    public void testTruncateEmptyPrimitiveArray() {
        int[] empty = new int[0];
        Object result = ObjectTruncater.truncate(empty, 10, 10, 10);
        assertSame(empty, result);
    }

    @Test
    public void testTruncateObjectArray() {
        Object[] value = {"a", "b", "c", "d", "e"};

        Object result = ObjectTruncater.truncate(value, 100, 2, 2);
        assertTrue(result instanceof Collection);
    }

    @Test
    public void testTruncateIntegerValue() {
        Object result = ObjectTruncater.truncate(42, 10, 10, 10);
        assertEquals(42, result);
    }

    @Test
    public void testTruncateMapMaxDepthReached() {
        Map<String, Object> nested = new HashMap<>();
        nested.put("deep", "value");
        Map<String, Object> value = new HashMap<>();
        value.put("child", nested);

        Object result = ObjectTruncater.truncate(value, 100, 100, 0);
        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = (Map<String, Object>) result;
        @SuppressWarnings("unchecked")
        Map<String, Object> child = (Map<String, Object>) resultMap.get("child");
        assertEquals("skipped: max depth reached", child.get("_"));
    }

    @Test
    public void testTruncateCollectionMaxDepthReached() {
        List<Object> value = Arrays.asList(1, 2, 3);

        Object result = ObjectTruncater.truncate(value, 100, 100, -1);
        assertEquals(Collections.singletonList("skipped: max depth reached"), result);
    }

    @Test
    public void testTruncatePrimitiveArrayMaxDepthReached() {
        int[] value = {1, 2, 3};

        Object result = ObjectTruncater.truncate(value, 100, 100, -1);
        assertArrayEquals(new String[]{"skipped: max depth reached"}, (String[]) result);
    }

    @Test
    public void testTruncateObjectArrayMaxDepthReached() {
        Object[] value = {"a", "b", "c"};

        Object result = ObjectTruncater.truncate(value, 100, 100, -1);
        assertArrayEquals(new String[]{"skipped: max depth reached"}, (String[]) result);
    }
}
