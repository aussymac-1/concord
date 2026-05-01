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
    public void testGrepBytes() throws IOException {
        byte[] data = "hello world\nfoo bar\nhello again\n".getBytes(StandardCharsets.UTF_8);
        List<String> result = GrepUtils.grep(".*hello.*", data);
        assertEquals(2, result.size());
        assertEquals("hello world", result.get(0));
        assertEquals("hello again", result.get(1));
    }

    @Test
    public void testGrepNoMatch() throws IOException {
        byte[] data = "abc\ndef\n".getBytes(StandardCharsets.UTF_8);
        List<String> result = GrepUtils.grep("xyz", data);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGrepInputStream() throws IOException {
        String input = "line1\nline2\nline3\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        List<String> result = GrepUtils.grep(".*2.*", in);
        assertEquals(1, result.size());
        assertEquals("line2", result.get(0));
    }

    @Test
    public void testGrepEmptyInput() throws IOException {
        byte[] data = new byte[0];
        List<String> result = GrepUtils.grep(".*", data);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGrepRegexPattern() throws IOException {
        byte[] data = "abc123\ndef456\nabc789\n".getBytes(StandardCharsets.UTF_8);
        List<String> result = GrepUtils.grep("abc\\d+", data);
        assertEquals(2, result.size());
    }
}
