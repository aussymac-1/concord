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

import static org.assertj.core.api.Assertions.assertThat;

class GrepUtilsTest {

    @Test
    void grepWithByteArrayMatchesPattern() throws IOException {
        String content = "hello world\nfoo bar\nhello again";
        List<String> result = GrepUtils.grep("hello.*", content.getBytes(StandardCharsets.UTF_8));
        assertThat(result).containsExactly("hello world", "hello again");
    }

    @Test
    void grepWithInputStreamMatchesPattern() throws IOException {
        String content = "line1\nline2\nline3";
        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        List<String> result = GrepUtils.grep("line[13]", in);
        assertThat(result).containsExactly("line1", "line3");
    }

    @Test
    void grepReturnsEmptyWhenNoMatch() throws IOException {
        String content = "abc\ndef";
        List<String> result = GrepUtils.grep("xyz.*", content.getBytes(StandardCharsets.UTF_8));
        assertThat(result).isEmpty();
    }

    @Test
    void grepWithEmptyInput() throws IOException {
        List<String> result = GrepUtils.grep(".*", new byte[0]);
        assertThat(result).isEmpty();
    }

    @Test
    void grepMatchesFullLineRegex() throws IOException {
        String content = "abc123\nabc\n123abc";
        List<String> result = GrepUtils.grep("^abc$", content.getBytes(StandardCharsets.UTF_8));
        assertThat(result).containsExactly("abc");
    }
}
