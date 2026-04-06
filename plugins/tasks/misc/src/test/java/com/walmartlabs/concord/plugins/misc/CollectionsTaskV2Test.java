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

class CollectionsTaskV2Test {

    @Test
    void testConcatTwoLists() {
        List<String> a = Arrays.asList("a", "b");
        List<String> b = Arrays.asList("c", "d");
        List<String> result = CollectionsTaskV2.concat(a, b);
        assertEquals(Arrays.asList("a", "b", "c", "d"), result);
    }

    @Test
    void testConcatWithNullList() {
        List<String> a = Arrays.asList("a", "b");
        List<String> result = CollectionsTaskV2.concat(a, null);
        assertEquals(Arrays.asList("a", "b"), result);
    }

    @Test
    void testConcatEmptyLists() {
        List<String> result = CollectionsTaskV2.concat(Collections.emptyList(), Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    void testConcatAsSet() {
        List<String> a = Arrays.asList("a", "b", "a");
        List<String> b = Arrays.asList("b", "c");
        Set<String> result = CollectionsTaskV2.concatAsSet(a, b);
        assertEquals(new HashSet<>(Arrays.asList("a", "b", "c")), result);
    }

    @Test
    void testConcatAsSetWithNull() {
        List<String> a = Arrays.asList("x", "y");
        Set<String> result = CollectionsTaskV2.concatAsSet(a, null);
        assertEquals(new HashSet<>(Arrays.asList("x", "y")), result);
    }

    @Test
    void testReverse() {
        List<String> input = Arrays.asList("a", "b", "c");
        List<String> result = CollectionsTaskV2.reverse(input);
        assertEquals(Arrays.asList("c", "b", "a"), result);
    }

    @Test
    void testReverseEmpty() {
        List<String> result = CollectionsTaskV2.reverse(Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    void testRange() {
        List<Integer> result = CollectionsTaskV2.range(5);
        assertEquals(Arrays.asList(0, 1, 2, 3, 4), result);
    }

    @Test
    void testRangeZero() {
        List<Integer> result = CollectionsTaskV2.range(0);
        assertTrue(result.isEmpty());
    }

    @Test
    void testNewMap() {
        Map<String, Object> result = CollectionsTaskV2.newMap();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertTrue(result instanceof LinkedHashMap);
    }
}
