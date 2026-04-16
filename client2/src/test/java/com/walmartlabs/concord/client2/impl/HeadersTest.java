package com.walmartlabs.concord.client2.impl;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HeadersTest {

    @Test
    void ofCreatesSingleHeader() {
        Headers headers = Headers.of("Content-Type", "application/json");
        assertThat(headers.size()).isEqualTo(1);
        assertThat(headers.name(0)).isEqualTo("Content-Type");
        assertThat(headers.value(0)).isEqualTo("application/json");
    }

    @Test
    void getCaseInsensitive() {
        Headers headers = Headers.of("Content-Type", "text/plain");
        assertThat(headers.get("content-type")).isEqualTo("text/plain");
        assertThat(headers.get("CONTENT-TYPE")).isEqualTo("text/plain");
    }

    @Test
    void getReturnsNullWhenNotFound() {
        Headers headers = Headers.of("Accept", "text/html");
        assertThat(headers.get("Content-Type")).isNull();
    }

    @Test
    void constructorWithMultipleHeaders() {
        List<NameValuePair> items = List.of(
                new NameValuePair("X-Header1", "val1"),
                new NameValuePair("X-Header2", "val2"));
        Headers headers = new Headers(items);

        assertThat(headers.size()).isEqualTo(2);
        assertThat(headers.get("X-Header1")).isEqualTo("val1");
        assertThat(headers.get("X-Header2")).isEqualTo("val2");
    }

    @Test
    void nameAndValueByIndex() {
        List<NameValuePair> items = List.of(
                new NameValuePair("A", "1"),
                new NameValuePair("B", "2"));
        Headers headers = new Headers(items);

        assertThat(headers.name(0)).isEqualTo("A");
        assertThat(headers.value(0)).isEqualTo("1");
        assertThat(headers.name(1)).isEqualTo("B");
        assertThat(headers.value(1)).isEqualTo("2");
    }
}
