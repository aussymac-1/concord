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

public class SourceMapTest {

    @Test
    public void testBuilder() {
        SourceMap sm = SourceMap.builder()
                .source("test.yml")
                .line(10)
                .column(5)
                .description("step description")
                .build();

        assertEquals("test.yml", sm.source());
        assertEquals(10, sm.line());
        assertEquals(5, sm.column());
        assertEquals("step description", sm.description());
    }

    @Test
    public void testBuilderWithoutDescription() {
        SourceMap sm = SourceMap.builder()
                .source("test.yml")
                .line(1)
                .column(1)
                .build();

        assertEquals("test.yml", sm.source());
        assertNull(sm.description());
    }

    @Test
    public void testFromLocationWithFileName() {
        Location loc = Location.builder()
                .fileName("concord.yml")
                .lineNum(20)
                .column(3)
                .build();

        SourceMap sm = SourceMap.from(loc);
        assertEquals("concord.yml", sm.source());
        assertEquals(20, sm.line());
        assertEquals(3, sm.column());
    }

    @Test
    public void testFromLocationWithoutFileName() {
        Location loc = Location.builder()
                .lineNum(5)
                .column(2)
                .build();

        SourceMap sm = SourceMap.from(loc);
        assertEquals("n/a", sm.source());
        assertEquals(5, sm.line());
        assertEquals(2, sm.column());
    }
}
