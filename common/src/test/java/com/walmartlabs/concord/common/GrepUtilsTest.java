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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GrepUtilsTest {

    @Test
    public void testGrepMatchingLines() throws IOException {
        String input = "hello world\nfoo bar\nhello again\n";
        List<String> result = GrepUtils.grep(".*hello.*", input.getBytes(StandardCharsets.UTF_8));
        assertEquals(2, result.size());
        assertEquals("hello world", result.get(0));
        assertEquals("hello again", result.get(1));
    }

    @Test
    public void testGrepNoMatch() throws IOException {
        String input = "foo\nbar\nbaz\n";
        List<String> result = GrepUtils.grep(".*hello.*", input.getBytes(StandardCharsets.UTF_8));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGrepWithInputStream() throws IOException {
        String input = "line1\nline2\nline3\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        List<String> result = GrepUtils.grep(".*2.*", is);
        assertEquals(1, result.size());
        assertEquals("line2", result.get(0));
    }

    @Test
    public void testGrepEmptyInput() throws IOException {
        List<String> result = GrepUtils.grep(".*", "".getBytes(StandardCharsets.UTF_8));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGrepRegexPattern() throws IOException {
        String input = "abc123\ndef456\nabc789\n";
        List<String> result = GrepUtils.grep("abc\\d+", input.getBytes(StandardCharsets.UTF_8));
        assertEquals(2, result.size());
    }
}
