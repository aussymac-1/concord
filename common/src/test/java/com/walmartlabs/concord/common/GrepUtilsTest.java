package com.walmartlabs.concord.common;

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

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GrepUtilsTest {

    @Test
    void testGrepMatchesLines() throws IOException {
        byte[] data = "hello world\nfoo bar\nhello again\n".getBytes();
        List<String> result = GrepUtils.grep(".*hello.*", data);
        assertEquals(2, result.size());
        assertEquals("hello world", result.get(0));
        assertEquals("hello again", result.get(1));
    }

    @Test
    void testGrepNoMatches() throws IOException {
        byte[] data = "alpha\nbeta\ngamma\n".getBytes();
        List<String> result = GrepUtils.grep(".*xyz.*", data);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGrepEmptyInput() throws IOException {
        byte[] data = new byte[0];
        List<String> result = GrepUtils.grep(".*", data);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGrepRegexPattern() throws IOException {
        byte[] data = "error: something failed\nwarn: low memory\nerror: timeout\n".getBytes();
        List<String> result = GrepUtils.grep("^error:.*", data);
        assertEquals(2, result.size());
    }
}
