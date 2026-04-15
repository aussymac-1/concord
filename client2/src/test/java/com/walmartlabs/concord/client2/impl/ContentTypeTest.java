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

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContentTypeTest {

    @Test
    void testApplicationJson() {
        ContentType ct = ContentType.APPLICATION_JSON;
        assertEquals("application/json", ct.getMimeType());
        assertEquals(StandardCharsets.UTF_8, ct.getCharset());
    }

    @Test
    void testApplicationOctetStream() {
        ContentType ct = ContentType.APPLICATION_OCTET_STREAM;
        assertEquals("application/octet-stream", ct.getMimeType());
        assertNull(ct.getCharset());
    }

    @Test
    void testTextPlain() {
        ContentType ct = ContentType.TEXT_PLAIN;
        assertEquals("text/plain", ct.getMimeType());
        assertNull(ct.getCharset());
    }

    @Test
    void testMultipartForm() {
        ContentType ct = ContentType.MULTIPART_FORM;
        assertEquals("multipart/form-data", ct.getMimeType());
    }

    @Test
    void testCreateWithMimeType() {
        ContentType ct = ContentType.create("text/html");
        assertEquals("text/html", ct.getMimeType());
        assertNull(ct.getCharset());
    }

    @Test
    void testCreateWithMimeTypeAndCharset() {
        ContentType ct = ContentType.create("text/xml", StandardCharsets.ISO_8859_1);
        assertEquals("text/xml", ct.getMimeType());
        assertEquals(StandardCharsets.ISO_8859_1, ct.getCharset());
    }

    @Test
    void testMimeTypeNormalized() {
        ContentType ct = ContentType.create("TEXT/HTML");
        assertEquals("text/html", ct.getMimeType());
    }

    @Test
    void testWithCharset() {
        ContentType ct = ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_16);
        assertEquals("text/plain", ct.getMimeType());
        assertEquals(StandardCharsets.UTF_16, ct.getCharset());
    }

    @Test
    void testToStringWithCharset() {
        ContentType ct = ContentType.create("text/plain", StandardCharsets.UTF_8);
        assertEquals("text/plain; charset=utf-8", ct.toString());
    }

    @Test
    void testToStringWithoutCharset() {
        ContentType ct = ContentType.create("application/octet-stream");
        assertEquals("application/octet-stream", ct.toString());
    }

    @Test
    void testWithParameters() {
        ContentType ct = ContentType.create("multipart/form-data")
                .withParameters(List.of(new NameValuePair("boundary", "abc123")));
        String s = ct.toString();
        assertTrue(s.contains("boundary=abc123"));
    }
}
