package com.walmartlabs.concord.runtime.v2.sdk;

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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class UserDefinedExceptionTest {

    @Test
    public void testMessageOnlyConstructor() {
        var ex = new UserDefinedException("oops");

        assertEquals("oops", ex.getMessage());
        assertNull(ex.getPayload());
    }

    @Test
    public void testMessageAndPayloadConstructor() {
        Map<String, Object> payload = Map.of("k", 1);

        var ex = new UserDefinedException("oops", payload);

        assertEquals("oops", ex.getMessage());
        assertSame(payload, ex.getPayload());
    }

    @Test
    public void testToStringWithoutPayload() {
        var ex = new UserDefinedException("oops");

        assertEquals("oops", ex.toString());
    }

    @Test
    public void testToStringWithEmptyPayloadOmitsPayload() {
        var ex = new UserDefinedException("oops", Map.of());

        assertEquals("oops", ex.toString());
    }

    @Test
    public void testToStringWithPayloadIncludesPayload() {
        var ex = new UserDefinedException("oops", Map.of("k", 1));

        assertEquals("oops: {k=1}", ex.toString());
    }

    @Test
    public void testEmptyStackTrace() {
        var ex = new UserDefinedException("oops");

        assertNotNull(ex.getStackTrace());
        assertEquals(0, ex.getStackTrace().length);
    }

    @Test
    public void testPrintStackTraceIsNoOp() {
        var ex = new UserDefinedException("oops");

        var writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        assertEquals("", writer.toString());

        var stream = new java.io.ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(stream));
        assertEquals("", stream.toString());
    }
}
