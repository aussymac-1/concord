package com.walmartlabs.concord.policyengine;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    @Test
    public void testMatchAny() {
        boolean result = Utils.matchAny(Collections.singletonList("\\.concord"), ".concord");
        assertTrue(result);

        result = Utils.matchAny(Collections.emptyList(), ".concord");
        assertFalse(result);
    }

    @Test
    public void testSimple() {
        String s = "100KB";

        long result = Utils.parseFileSize(s);

        assertEquals(100 * 1024, result);
    }

    @Test
    public void testTrim() {
        String s = "   100  kb ";

        long result = Utils.parseFileSize(s);

        assertEquals(100 * 1024, result);
    }

    @Test
    public void testParseFileSizeNull() {
        assertNull(Utils.parseFileSize(null));
    }

    @Test
    public void testParseFileSizeMB() {
        assertEquals(10L * 1024 * 1024, Utils.parseFileSize("10MB"));
    }

    @Test
    public void testParseFileSizeGB() {
        assertEquals(2L * 1024 * 1024 * 1024, Utils.parseFileSize("2GB"));
    }

    @Test
    public void testParseFileSizeTB() {
        assertEquals(1L * 1024 * 1024 * 1024 * 1024, Utils.parseFileSize("1TB"));
    }

    @Test
    public void testMatchAnyMultiplePatterns() {
        boolean result = Utils.matchAny(Arrays.asList("foo", "bar", "\\.concord"), ".concord");
        assertTrue(result);
    }

    @Test
    public void testMatchAnyNoMatch() {
        boolean result = Utils.matchAny(Arrays.asList("foo", "bar"), "baz");
        assertFalse(result);
    }

    @Test
    public void testMatchesStringPattern() {
        assertTrue(Utils.matches("hello.*", "hello world"));
        assertFalse(Utils.matches("hello.*", "goodbye world"));
    }
}
