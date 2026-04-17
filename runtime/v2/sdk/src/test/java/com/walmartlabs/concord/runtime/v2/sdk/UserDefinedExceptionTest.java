package com.walmartlabs.concord.runtime.v2.sdk;

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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UserDefinedExceptionTest {

    @Test
    void singleArgConstructorHasNullPayload() {
        UserDefinedException ex = new UserDefinedException("boom");

        assertThat(ex.getMessage()).isEqualTo("boom");
        assertThat(ex.getPayload()).isNull();
    }

    @Test
    void payloadConstructorRetainsPayload() {
        Map<String, Object> payload = Map.of("k", "v");
        UserDefinedException ex = new UserDefinedException("msg", payload);

        assertThat(ex.getMessage()).isEqualTo("msg");
        assertThat(ex.getPayload()).isEqualTo(payload);
    }

    @Test
    void toStringWithoutPayloadReturnsMessage() {
        UserDefinedException ex = new UserDefinedException("bad");

        assertThat(ex).hasToString("bad");
    }

    @Test
    void toStringAppendsNonEmptyPayload() {
        UserDefinedException ex = new UserDefinedException("bad", Map.of("k", "v"));

        assertThat(ex.toString()).startsWith("bad: ").contains("k", "v");
    }

    @Test
    void toStringIgnoresEmptyPayload() {
        UserDefinedException ex = new UserDefinedException("bad", Map.of());

        assertThat(ex).hasToString("bad");
    }

    @Test
    void getStackTraceIsAlwaysEmpty() {
        UserDefinedException ex = new UserDefinedException("bad");

        assertThat(ex.getStackTrace()).isEmpty();
    }

    @Test
    void printStackTraceOnWriterProducesNothing() {
        UserDefinedException ex = new UserDefinedException("bad");
        StringWriter sw = new StringWriter();

        ex.printStackTrace(new PrintWriter(sw));

        assertThat(sw.toString()).isEmpty();
    }

    @Test
    void printStackTraceOnStreamProducesNothing() {
        UserDefinedException ex = new UserDefinedException("bad");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ex.printStackTrace(new PrintStream(baos));

        assertThat(baos.toByteArray()).isEmpty();
    }
}
