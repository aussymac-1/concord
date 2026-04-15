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

import static org.junit.jupiter.api.Assertions.*;

public class LocationTest {

    @Test
    void testToShortStringWithFileName() {
        Location loc = ImmutableLocation.builder()
                .fileName("test.yml")
                .lineNum(10)
                .column(5)
                .build();
        assertEquals("line: 10, col: 5", Location.toShortString(loc));
    }

    @Test
    void testToShortStringWithNullLocation() {
        assertEquals("n/a", Location.toShortString(null));
    }

    @Test
    void testToShortStringWithNullFileName() {
        Location loc = ImmutableLocation.builder()
                .lineNum(10)
                .column(5)
                .build();
        assertEquals("n/a", Location.toShortString(loc));
    }

    @Test
    void testToErrorPrefixWithFileName() {
        Location loc = ImmutableLocation.builder()
                .fileName("flow.yml")
                .lineNum(20)
                .column(3)
                .build();
        String result = Location.toErrorPrefix(loc);
        assertTrue(result.contains("flow.yml"));
        assertTrue(result.contains("Error @"));
    }

    @Test
    void testToErrorPrefixWithNullLocation() {
        assertEquals("(n/a): Error.", Location.toErrorPrefix(null));
    }

    @Test
    void testToErrorPrefixWithNullFileName() {
        Location loc = ImmutableLocation.builder()
                .lineNum(1)
                .column(1)
                .build();
        assertEquals("(n/a): Error.", Location.toErrorPrefix(loc));
    }

    @Test
    void testDefaultValues() {
        Location loc = ImmutableLocation.builder().build();
        assertEquals(-1, loc.lineNum());
        assertEquals(-1, loc.column());
        assertNull(loc.fileName());
    }

    @Test
    void testBuilder() {
        Location loc = Location.builder()
                .fileName("test.yml")
                .lineNum(42)
                .column(7)
                .build();
        assertEquals("test.yml", loc.fileName());
        assertEquals(42, loc.lineNum());
        assertEquals(7, loc.column());
    }
}
