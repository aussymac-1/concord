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

public class SourceMapTest {

    @Test
    public void testFromLocationWithFileName() {
        var location = Location.builder()
                .fileName("flow.yml")
                .lineNum(5)
                .column(2)
                .build();

        var sourceMap = SourceMap.from(location);

        assertEquals("flow.yml", sourceMap.source());
        assertEquals(5, sourceMap.line());
        assertEquals(2, sourceMap.column());
        assertNull(sourceMap.description());
    }

    @Test
    public void testFromLocationWithoutFileName() {
        var location = Location.builder()
                .lineNum(1)
                .column(1)
                .build();

        var sourceMap = SourceMap.from(location);

        assertEquals("n/a", sourceMap.source());
        assertEquals(1, sourceMap.line());
        assertEquals(1, sourceMap.column());
    }

    @Test
    public void testBuilderWithDescription() {
        var sourceMap = SourceMap.builder()
                .source("a.yml")
                .line(7)
                .column(3)
                .description("trigger handler")
                .build();

        assertEquals("a.yml", sourceMap.source());
        assertEquals(7, sourceMap.line());
        assertEquals(3, sourceMap.column());
        assertEquals("trigger handler", sourceMap.description());
    }

    @Test
    public void testEqualsAndHashCode() {
        var a = SourceMap.builder().source("a.yml").line(1).column(1).build();
        var b = SourceMap.builder().source("a.yml").line(1).column(1).build();
        var c = SourceMap.builder().source("a.yml").line(2).column(1).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
