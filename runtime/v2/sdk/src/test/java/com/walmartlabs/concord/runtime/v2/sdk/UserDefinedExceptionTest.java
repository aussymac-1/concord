package com.walmartlabs.concord.runtime.v2.sdk;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2020 Walmart Inc.
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

import static org.junit.jupiter.api.Assertions.*;

class UserDefinedExceptionTest {

    @Test
    void testMessageOnly() {
        var ex = new UserDefinedException("test error");
        assertEquals("test error", ex.getMessage());
        assertNull(ex.getPayload());
    }

    @Test
    void testMessageWithPayload() {
        var payload = Map.<String, Object>of("key", "value");
        var ex = new UserDefinedException("test error", payload);
        assertEquals("test error", ex.getMessage());
        assertEquals(payload, ex.getPayload());
    }

    @Test
    void testToStringWithoutPayload() {
        var ex = new UserDefinedException("test error");
        assertEquals("test error", ex.toString());
    }

    @Test
    void testToStringWithPayload() {
        var payload = Map.<String, Object>of("key", "value");
        var ex = new UserDefinedException("test error", payload);
        var str = ex.toString();
        assertTrue(str.startsWith("test error"));
        assertTrue(str.contains("key"));
        assertTrue(str.contains("value"));
    }

    @Test
    void testEmptyStackTrace() {
        var ex = new UserDefinedException("test error");
        assertEquals(0, ex.getStackTrace().length);
    }

    @Test
    void testPrintStackTracePrintWriter() {
        var ex = new UserDefinedException("test error");
        var sw = new StringWriter();
        var pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        pw.flush();
        assertEquals("", sw.toString());
    }

    @Test
    void testPrintStackTracePrintStream() {
        var ex = new UserDefinedException("test error");
        var baos = new ByteArrayOutputStream();
        var ps = new PrintStream(baos);
        ex.printStackTrace(ps);
        ps.flush();
        assertEquals(0, baos.size());
    }

    @Test
    void testIsRuntimeException() {
        var ex = new UserDefinedException("test");
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void testToStringWithEmptyPayload() {
        var ex = new UserDefinedException("msg", Map.of());
        assertEquals("msg", ex.toString());
    }
}
