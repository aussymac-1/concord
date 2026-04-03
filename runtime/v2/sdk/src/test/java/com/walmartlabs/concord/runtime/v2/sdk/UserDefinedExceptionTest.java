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

import static org.junit.jupiter.api.Assertions.*;

public class UserDefinedExceptionTest {

    @Test
    public void testMessageOnlyConstructor() {
        UserDefinedException e = new UserDefinedException("test error");
        assertEquals("test error", e.getMessage());
        assertNull(e.getPayload());
    }

    @Test
    public void testMessageAndPayloadConstructor() {
        Map<String, Object> payload = Map.of("code", 42);
        UserDefinedException e = new UserDefinedException("test error", payload);
        assertEquals("test error", e.getMessage());
        assertEquals(payload, e.getPayload());
    }

    @Test
    public void testToStringWithoutPayload() {
        UserDefinedException e = new UserDefinedException("test error");
        assertEquals("test error", e.toString());
    }

    @Test
    public void testToStringWithPayload() {
        Map<String, Object> payload = Map.of("code", 42);
        UserDefinedException e = new UserDefinedException("test error", payload);
        String str = e.toString();
        assertTrue(str.startsWith("test error"));
        assertTrue(str.contains("code"));
    }

    @Test
    public void testStackTraceIsEmpty() {
        UserDefinedException e = new UserDefinedException("test");
        assertEquals(0, e.getStackTrace().length);
    }

    @Test
    public void testPrintStackTraceDoesNothing() {
        UserDefinedException e = new UserDefinedException("test");

        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        assertEquals("", sw.toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        assertEquals("", baos.toString());
    }

    @Test
    public void testIsRuntimeException() {
        UserDefinedException e = new UserDefinedException("test");
        assertInstanceOf(RuntimeException.class, e);
    }
}
