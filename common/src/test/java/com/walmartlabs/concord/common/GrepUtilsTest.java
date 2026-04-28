package com.walmartlabs.concord.common;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GrepUtilsTest {

    @Test
    public void testGrepByteArrayMatchesLines() throws IOException {
        String content = "hello world\nfoo bar\nhello again\n";
        List<String> result = GrepUtils.grep(".*hello.*", content.getBytes(StandardCharsets.UTF_8));

        assertEquals(2, result.size());
        assertEquals("hello world", result.get(0));
        assertEquals("hello again", result.get(1));
    }

    @Test
    public void testGrepByteArrayNoMatches() throws IOException {
        String content = "alpha\nbeta\ngamma\n";
        List<String> result = GrepUtils.grep(".*xyz.*", content.getBytes(StandardCharsets.UTF_8));

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGrepInputStream() throws IOException {
        String content = "line1\nline2\nline3\n";
        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        List<String> result = GrepUtils.grep("line[12]", in);

        assertEquals(2, result.size());
        assertEquals("line1", result.get(0));
        assertEquals("line2", result.get(1));
    }

    @Test
    public void testGrepEmptyInput() throws IOException {
        List<String> result = GrepUtils.grep(".*", new byte[0]);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGrepExactMatch() throws IOException {
        String content = "exact\nnot-exact\nexact\n";
        List<String> result = GrepUtils.grep("exact", content.getBytes(StandardCharsets.UTF_8));

        assertEquals(2, result.size());
    }
}
