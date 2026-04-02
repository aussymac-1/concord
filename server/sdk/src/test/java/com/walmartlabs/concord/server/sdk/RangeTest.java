package com.walmartlabs.concord.server.sdk;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2024 Walmart Inc.
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

public class RangeTest {

    @Test
    public void testBuilder() {
        Range range = Range.builder()
                .lower(0)
                .upper(100)
                .lowerMode(Range.Mode.INCLUSIVE)
                .upperMode(Range.Mode.EXCLUSIVE)
                .build();
        assertEquals(0, range.lower());
        assertEquals(100, range.upper());
        assertEquals(Range.Mode.INCLUSIVE, range.lowerMode());
        assertEquals(Range.Mode.EXCLUSIVE, range.upperMode());
    }

    @Test
    public void testModeValues() {
        assertEquals(2, Range.Mode.values().length);
        assertEquals(Range.Mode.INCLUSIVE, Range.Mode.valueOf("INCLUSIVE"));
        assertEquals(Range.Mode.EXCLUSIVE, Range.Mode.valueOf("EXCLUSIVE"));
    }
}
