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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollectionsTaskV2Test {

    @Test
    public void concatJoinsMultipleLists() {
        var result = CollectionsTaskV2.concat(List.of(1, 2), List.of(3), List.of(4, 5));
        assertEquals(List.of(1, 2, 3, 4, 5), result);
    }

    @Test
    public void concatSkipsNullLists() {
        var result = CollectionsTaskV2.concat(List.of("a"), null, List.of("b"));
        assertEquals(List.of("a", "b"), result);
    }

    @Test
    public void concatEmptyArgsReturnsEmpty() {
        assertEquals(Collections.emptyList(), CollectionsTaskV2.concat());
    }

    @Test
    public void concatAsSetEliminatesDuplicates() {
        var result = CollectionsTaskV2.concatAsSet(List.of(1, 2), List.of(2, 3));
        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
    }

    @Test
    public void concatAsSetSkipsNullLists() {
        var result = CollectionsTaskV2.concatAsSet(null, List.of("a"));
        assertEquals(1, result.size());
        assertTrue(result.contains("a"));
    }

    @Test
    public void reverseReturnsReversedView() {
        var in = new ArrayList<>(List.of(1, 2, 3));
        var reversed = CollectionsTaskV2.reverse(in);
        assertEquals(List.of(3, 2, 1), reversed);
    }

    @Test
    public void rangeReturnsExpectedIntegers() {
        assertEquals(List.of(0, 1, 2, 3), CollectionsTaskV2.range(4));
    }

    @Test
    public void rangeZeroReturnsEmpty() {
        assertEquals(Collections.emptyList(), CollectionsTaskV2.range(0));
    }

    @Test
    public void newMapIsLinkedHashMapAndMutable() {
        var m = CollectionsTaskV2.newMap();
        assertNotNull(m);
        assertTrue(m instanceof LinkedHashMap);
        m.put("a", 1);
        m.put("b", 2);
        assertEquals(2, m.size());
        assertEquals(List.of("a", "b"), new ArrayList<>(m.keySet())); // insertion-ordered
    }

    @Test
    public void newMapReturnsDistinctInstances() {
        var a = CollectionsTaskV2.newMap();
        var b = CollectionsTaskV2.newMap();
        assertNotSame(a, b);
    }
}
