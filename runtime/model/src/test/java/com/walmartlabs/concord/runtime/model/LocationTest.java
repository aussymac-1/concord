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
    public void testBuilderDefaults() {
        Location loc = Location.builder().build();
        assertEquals(-1, loc.lineNum());
        assertEquals(-1, loc.column());
        assertNull(loc.fileName());
    }

    @Test
    public void testBuilderWithValues() {
        Location loc = Location.builder()
                .fileName("test.yml")
                .lineNum(10)
                .column(5)
                .build();

        assertEquals("test.yml", loc.fileName());
        assertEquals(10, loc.lineNum());
        assertEquals(5, loc.column());
    }

    @Test
    public void testToShortStringNull() {
        assertEquals("n/a", Location.toShortString(null));
    }

    @Test
    public void testToShortStringNullFileName() {
        Location loc = Location.builder().lineNum(1).column(2).build();
        assertEquals("n/a", Location.toShortString(loc));
    }

    @Test
    public void testToShortString() {
        Location loc = Location.builder()
                .fileName("test.yml")
                .lineNum(10)
                .column(5)
                .build();
        assertEquals("line: 10, col: 5", Location.toShortString(loc));
    }

    @Test
    public void testToErrorPrefixNull() {
        assertEquals("(n/a): Error.", Location.toErrorPrefix(null));
    }

    @Test
    public void testToErrorPrefixNullFileName() {
        Location loc = Location.builder().build();
        assertEquals("(n/a): Error.", Location.toErrorPrefix(loc));
    }

    @Test
    public void testToErrorPrefix() {
        Location loc = Location.builder()
                .fileName("test.yml")
                .lineNum(10)
                .column(5)
                .build();
        String result = Location.toErrorPrefix(loc);
        assertTrue(result.contains("test.yml"));
        assertTrue(result.contains("Error"));
    }
}
