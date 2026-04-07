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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserDefinedExceptionTest {

    @Test
    public void testMessageOnly() {
        UserDefinedException ex = new UserDefinedException("something failed");
        assertEquals("something failed", ex.getMessage());
        assertNull(ex.getPayload());
    }

    @Test
    public void testMessageWithPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("code", 42);
        UserDefinedException ex = new UserDefinedException("failed", payload);
        assertEquals("failed", ex.getMessage());
        assertEquals(42, ex.getPayload().get("code"));
    }

    @Test
    public void testToStringWithoutPayload() {
        UserDefinedException ex = new UserDefinedException("error msg");
        assertEquals("error msg", ex.toString());
    }

    @Test
    public void testToStringWithPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("key", "val");
        UserDefinedException ex = new UserDefinedException("error msg", payload);
        assertTrue(ex.toString().startsWith("error msg: "));
        assertTrue(ex.toString().contains("key=val"));
    }

    @Test
    public void testEmptyStackTrace() {
        UserDefinedException ex = new UserDefinedException("error");
        assertEquals(0, ex.getStackTrace().length);
    }

    @Test
    public void testPrintStackTraceDoesNothing() {
        UserDefinedException ex = new UserDefinedException("error");

        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        assertEquals("", sw.toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(baos));
        assertEquals("", baos.toString());
    }

    @Test
    public void testEmptyPayload() {
        Map<String, Object> payload = new HashMap<>();
        UserDefinedException ex = new UserDefinedException("error", payload);
        assertEquals("error", ex.toString());
    }
}
