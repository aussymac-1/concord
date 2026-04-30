package com.walmartlabs.concord.runtime.v2.sdk;

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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class UserDefinedExceptionTest {

    @Test
    public void testMessageOnly() {
        var ex = new UserDefinedException("something went wrong");
        assertEquals("something went wrong", ex.getMessage());
        assertNull(ex.getPayload());
    }

    @Test
    public void testMessageWithPayload() {
        var payload = new HashMap<String, Object>();
        payload.put("code", 42);
        var ex = new UserDefinedException("error", payload);
        assertEquals("error", ex.getMessage());
        assertEquals(42, ex.getPayload().get("code"));
    }

    @Test
    public void testToStringWithoutPayload() {
        var ex = new UserDefinedException("simple error");
        assertEquals("simple error", ex.toString());
    }

    @Test
    public void testToStringWithPayload() {
        var payload = new HashMap<String, Object>();
        payload.put("key", "val");
        var ex = new UserDefinedException("error", payload);
        var s = ex.toString();
        assertTrue(s.contains("error"));
        assertTrue(s.contains("key"));
    }

    @Test
    public void testEmptyStackTrace() {
        var ex = new UserDefinedException("error");
        assertEquals(0, ex.getStackTrace().length);
    }

    @Test
    public void testPrintStackTracePrintWriter() {
        var ex = new UserDefinedException("error");
        var sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        assertEquals("", sw.toString());
    }

    @Test
    public void testPrintStackTracePrintStream() {
        var ex = new UserDefinedException("error");
        var baos = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(baos));
        assertEquals(0, baos.size());
    }

    @Test
    public void testToStringWithEmptyPayload() {
        var ex = new UserDefinedException("error", new HashMap<>());
        assertEquals("error", ex.toString());
    }
}
