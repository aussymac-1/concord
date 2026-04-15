package com.walmartlabs.concord.client2.impl;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HeadersTest {

    @Test
    void testOfCreation() {
        Headers headers = Headers.of("Content-Type", "application/json");
        assertEquals(1, headers.size());
        assertEquals("Content-Type", headers.name(0));
        assertEquals("application/json", headers.value(0));
    }

    @Test
    void testGetCaseInsensitive() {
        Headers headers = Headers.of("Content-Type", "application/json");
        assertEquals("application/json", headers.get("content-type"));
        assertEquals("application/json", headers.get("CONTENT-TYPE"));
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    void testGetMissing() {
        Headers headers = Headers.of("Content-Type", "application/json");
        assertNull(headers.get("Authorization"));
    }

    @Test
    void testMultipleHeaders() {
        List<NameValuePair> items = List.of(
                new NameValuePair("Content-Type", "application/json"),
                new NameValuePair("Authorization", "Bearer token"));
        Headers headers = new Headers(items);
        assertEquals(2, headers.size());
        assertEquals("application/json", headers.get("content-type"));
        assertEquals("Bearer token", headers.get("authorization"));
    }

    @Test
    void testNameAndValueByIndex() {
        List<NameValuePair> items = List.of(
                new NameValuePair("X-Custom", "value1"),
                new NameValuePair("X-Other", "value2"));
        Headers headers = new Headers(items);
        assertEquals("X-Custom", headers.name(0));
        assertEquals("value1", headers.value(0));
        assertEquals("X-Other", headers.name(1));
        assertEquals("value2", headers.value(1));
    }
}
