package com.walmartlabs.concord.plugins.misc;

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

public class CollectionsTaskV2Test {

    @Test
    public void testConcatPreservesOrderAndDuplicates() {
        List<Integer> a = Arrays.asList(1, 2, 3);
        List<Integer> b = Arrays.asList(3, 4);

        List<Integer> result = CollectionsTaskV2.concat(a, b);

        assertEquals(Arrays.asList(1, 2, 3, 3, 4), result);
    }

    @Test
    public void testConcatWithNoArgs() {
        List<Integer> result = CollectionsTaskV2.concat();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testConcatIgnoresNullLists() {
        List<Integer> a = Arrays.asList(1, 2);
        List<Integer> result = CollectionsTaskV2.concat(a, null);

        assertEquals(Arrays.asList(1, 2), result);
    }

    @Test
    public void testConcatAllNullLists() {
        List<Integer> result = CollectionsTaskV2.concat((List<Integer>) null, (List<Integer>) null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testConcatAsSetRemovesDuplicates() {
        List<Integer> a = Arrays.asList(1, 2, 3);
        List<Integer> b = Arrays.asList(3, 4);

        Set<Integer> result = CollectionsTaskV2.concatAsSet(a, b);

        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4)), result);
    }

    @Test
    public void testConcatAsSetIgnoresNulls() {
        List<Integer> a = Arrays.asList(1, 2);
        Set<Integer> result = CollectionsTaskV2.concatAsSet(a, null);

        assertEquals(new HashSet<>(Arrays.asList(1, 2)), result);
    }

    @Test
    public void testReverse() {
        List<Integer> input = Arrays.asList(1, 2, 3);

        List<Integer> reversed = CollectionsTaskV2.reverse(input);

        assertEquals(Arrays.asList(3, 2, 1), reversed);
    }

    @Test
    public void testReverseEmpty() {
        assertTrue(CollectionsTaskV2.reverse(Collections.emptyList()).isEmpty());
    }

    @Test
    public void testRangeOfZero() {
        assertTrue(CollectionsTaskV2.range(0).isEmpty());
    }

    @Test
    public void testRangeProducesSequentialIntegers() {
        assertEquals(Arrays.asList(0, 1, 2, 3, 4), CollectionsTaskV2.range(5));
    }

    @Test
    public void testRangeOfOne() {
        assertEquals(Collections.singletonList(0), CollectionsTaskV2.range(1));
    }

    @Test
    public void testNewMapIsEmptyAndOrdered() {
        Map<String, Object> map = CollectionsTaskV2.newMap();

        assertTrue(map.isEmpty());
        assertInstanceOf(LinkedHashMap.class, map);

        map.put("b", 1);
        map.put("a", 2);
        assertEquals(Arrays.asList("b", "a"), new ArrayList<>(map.keySet()));
    }
}
