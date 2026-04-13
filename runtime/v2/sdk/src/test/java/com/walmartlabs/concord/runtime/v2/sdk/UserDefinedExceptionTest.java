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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserDefinedExceptionTest {

    @Test
    public void testMessageOnly() {
        var ex = new UserDefinedException("test error");
        assertEquals("test error", ex.getMessage());
        assertNull(ex.getPayload());
    }

    @Test
    public void testMessageWithPayload() {
        var payload = Map.of("key", (Object) "value");
        var ex = new UserDefinedException("test error", payload);
        assertEquals("test error", ex.getMessage());
        assertEquals(payload, ex.getPayload());
    }

    @Test
    public void testToStringWithoutPayload() {
        var ex = new UserDefinedException("hello");
        assertEquals("hello", ex.toString());
    }

    @Test
    public void testToStringWithPayload() {
        var ex = new UserDefinedException("hello", Map.of("k", (Object) "v"));
        var str = ex.toString();
        assertTrue(str.startsWith("hello: "));
        assertTrue(str.contains("k=v") || str.contains("k" + "=" + "v"));
    }

    @Test
    public void testEmptyStackTrace() {
        var ex = new UserDefinedException("test");
        assertEquals(0, ex.getStackTrace().length);
    }

    @Test
    public void testPrintStackTraceWriter() {
        var ex = new UserDefinedException("test");
        var sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        assertEquals("", sw.toString());
    }

    @Test
    public void testIsRuntimeException() {
        var ex = new UserDefinedException("test");
        assertInstanceOf(RuntimeException.class, ex);
    }
}
