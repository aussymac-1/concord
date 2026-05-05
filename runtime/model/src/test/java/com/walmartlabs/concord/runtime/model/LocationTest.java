package com.walmartlabs.concord.runtime.model;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LocationTest {

    @Test
    public void testDefaults() {
        var loc = Location.builder().build();

        assertEquals(-1, loc.lineNum());
        assertEquals(-1, loc.column());
        assertNull(loc.fileName());
    }

    @Test
    public void testBuilderRoundTrip() {
        var loc = Location.builder()
                .lineNum(10)
                .column(4)
                .fileName("flow.yml")
                .build();

        assertEquals(10, loc.lineNum());
        assertEquals(4, loc.column());
        assertEquals("flow.yml", loc.fileName());
    }

    @Test
    public void testToShortStringNull() {
        assertEquals("n/a", Location.toShortString(null));
    }

    @Test
    public void testToShortStringNullFileName() {
        var loc = Location.builder()
                .lineNum(1)
                .column(2)
                .build();

        assertEquals("n/a", Location.toShortString(loc));
    }

    @Test
    public void testToShortStringWithFileName() {
        var loc = Location.builder()
                .lineNum(7)
                .column(3)
                .fileName("flow.yml")
                .build();

        assertEquals("line: 7, col: 3", Location.toShortString(loc));
    }

    @Test
    public void testToErrorPrefixNull() {
        assertEquals("(n/a): Error.", Location.toErrorPrefix(null));
    }

    @Test
    public void testToErrorPrefixNullFileName() {
        var loc = Location.builder()
                .lineNum(1)
                .column(2)
                .build();

        assertEquals("(n/a): Error.", Location.toErrorPrefix(loc));
    }

    @Test
    public void testToErrorPrefixWithFileName() {
        var loc = Location.builder()
                .lineNum(12)
                .column(8)
                .fileName("flow.yml")
                .build();

        assertEquals("(flow.yml): Error @ line: 12, col: 8", Location.toErrorPrefix(loc));
    }

    @Test
    public void testEqualsAndHashCode() {
        var a = Location.builder().lineNum(1).column(2).fileName("a").build();
        var b = Location.builder().lineNum(1).column(2).fileName("a").build();
        var c = Location.builder().lineNum(1).column(2).fileName("b").build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
