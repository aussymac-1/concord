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

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ContentTypeTest {

    @Test
    void createWithMimeTypeOnly() {
        ContentType ct = ContentType.create("text/html");
        assertThat(ct.getMimeType()).isEqualTo("text/html");
        assertThat(ct.getCharset()).isNull();
    }

    @Test
    void createNormalizesMimeTypeToLowerCase() {
        ContentType ct = ContentType.create("Text/HTML");
        assertThat(ct.getMimeType()).isEqualTo("text/html");
    }

    @Test
    void createWithCharset() {
        ContentType ct = ContentType.create("text/plain", StandardCharsets.UTF_8);
        assertThat(ct.getMimeType()).isEqualTo("text/plain");
        assertThat(ct.getCharset()).isEqualTo(StandardCharsets.UTF_8);
    }

    @Test
    void applicationJsonConstant() {
        assertThat(ContentType.APPLICATION_JSON.getMimeType()).isEqualTo("application/json");
        assertThat(ContentType.APPLICATION_JSON.getCharset()).isEqualTo(StandardCharsets.UTF_8);
    }

    @Test
    void applicationOctetStreamConstant() {
        assertThat(ContentType.APPLICATION_OCTET_STREAM.getMimeType()).isEqualTo("application/octet-stream");
        assertThat(ContentType.APPLICATION_OCTET_STREAM.getCharset()).isNull();
    }

    @Test
    void textPlainConstant() {
        assertThat(ContentType.TEXT_PLAIN.getMimeType()).isEqualTo("text/plain");
    }

    @Test
    void multipartFormConstant() {
        assertThat(ContentType.MULTIPART_FORM.getMimeType()).isEqualTo("multipart/form-data");
    }

    @Test
    void withCharsetReturnsNewInstance() {
        ContentType original = ContentType.create("text/plain");
        ContentType withCharset = original.withCharset(StandardCharsets.ISO_8859_1);
        assertThat(withCharset.getCharset()).isEqualTo(StandardCharsets.ISO_8859_1);
        assertThat(original.getCharset()).isNull();
    }

    @Test
    void withParametersReturnsNewInstance() {
        ContentType original = ContentType.create("multipart/form-data");
        List<NameValuePair> params = List.of(new NameValuePair("boundary", "abc123"));
        ContentType withParams = original.withParameters(params);
        assertThat(withParams.toString()).contains("boundary=abc123");
    }

    @Test
    void toStringWithCharset() {
        ContentType ct = ContentType.create("text/plain", StandardCharsets.UTF_8);
        assertThat(ct.toString()).isEqualTo("text/plain; charset=utf-8");
    }

    @Test
    void toStringWithoutCharset() {
        ContentType ct = ContentType.create("text/plain");
        assertThat(ct.toString()).isEqualTo("text/plain");
    }

    @Test
    void toStringWithParameters() {
        ContentType ct = new ContentType("multipart/form-data", null,
                List.of(new NameValuePair("boundary", "xyz")));
        assertThat(ct.toString()).isEqualTo("multipart/form-data; boundary=xyz");
    }

    @Test
    void toStringWithMultipleParameters() {
        ContentType ct = new ContentType("multipart/form-data", null,
                List.of(new NameValuePair("boundary", "xyz"),
                        new NameValuePair("charset", "utf-8")));
        assertThat(ct.toString()).isEqualTo("multipart/form-data; boundary=xyz; charset=utf-8");
    }
}
