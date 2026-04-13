package com.walmartlabs.concord.common;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2026 Walmart Inc.
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
    public void testGrepByteArray() throws IOException {
        var data = "hello world\nfoo bar\nhello again".getBytes(StandardCharsets.UTF_8);
        var result = GrepUtils.grep(".*hello.*", data);
        assertEquals(2, result.size());
        assertEquals("hello world", result.get(0));
        assertEquals("hello again", result.get(1));
    }

    @Test
    public void testGrepInputStream() throws IOException {
        var data = "line1\nline2\nline3\n";
        var is = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        var result = GrepUtils.grep(".*2.*", is);
        assertEquals(1, result.size());
        assertEquals("line2", result.get(0));
    }

    @Test
    public void testGrepNoMatch() throws IOException {
        var data = "abc\ndef\n".getBytes(StandardCharsets.UTF_8);
        var result = GrepUtils.grep("xyz", data);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGrepEmptyInput() throws IOException {
        var data = "".getBytes(StandardCharsets.UTF_8);
        var result = GrepUtils.grep(".*", data);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGrepRegexPattern() throws IOException {
        var data = "abc123\ndef456\nabc789".getBytes(StandardCharsets.UTF_8);
        var result = GrepUtils.grep("abc\\d+", data);
        assertEquals(2, result.size());
    }
}
