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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDefinedExceptionTest {

    @Test
    public void testMessageOnlyConstructor() {
        UserDefinedException ex = new UserDefinedException("something went wrong");

        assertEquals("something went wrong", ex.getMessage());
        assertNull(ex.getPayload());
    }

    @Test
    public void testMessageAndPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("code", 42);
        payload.put("reason", "bad");

        UserDefinedException ex = new UserDefinedException("failure", payload);

        assertEquals("failure", ex.getMessage());
        assertEquals(payload, ex.getPayload());
    }

    @Test
    public void testStackTraceIsSuppressed() {
        UserDefinedException ex = new UserDefinedException("silent");

        assertEquals(0, ex.getStackTrace().length);
    }

    @Test
    public void testPrintStackTraceToPrintWriterWritesNothing() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        new UserDefinedException("silent").printStackTrace(pw);
        pw.flush();

        assertTrue(sw.toString().isEmpty(), "PrintWriter output should be empty");
    }

    @Test
    public void testPrintStackTraceToPrintStreamWritesNothing() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        new UserDefinedException("silent").printStackTrace(ps);
        ps.flush();

        assertEquals(0, baos.size());
    }

    @Test
    public void testToStringWithEmptyPayloadOmitsPayload() {
        UserDefinedException ex = new UserDefinedException("oops", Collections.emptyMap());

        assertEquals("oops", ex.toString());
    }

    @Test
    public void testToStringWithNullPayloadOmitsPayload() {
        UserDefinedException ex = new UserDefinedException("oops", null);

        assertEquals("oops", ex.toString());
    }

    @Test
    public void testToStringWithPayloadAppendsPayload() {
        Map<String, Object> payload = Collections.singletonMap("k", "v");
        UserDefinedException ex = new UserDefinedException("oops", payload);

        assertTrue(ex.toString().startsWith("oops: "));
        assertTrue(ex.toString().contains("k=v"));
    }
}
