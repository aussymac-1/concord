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

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserDefinedExceptionTest {

    @Test
    public void testMessageOnly() {
        UserDefinedException e = new UserDefinedException("boom");
        assertEquals("boom", e.getMessage());
        assertNull(e.getPayload());
        assertEquals("boom", e.toString());
    }

    @Test
    public void testMessageWithPayload() {
        Map<String, Object> payload = Collections.singletonMap("code", 42);
        UserDefinedException e = new UserDefinedException("boom", payload);
        assertEquals("boom", e.getMessage());
        assertEquals(42, e.getPayload().get("code"));
        assertTrue(e.toString().contains("boom"));
        assertTrue(e.toString().contains("42"));
    }

    @Test
    public void testEmptyStackTrace() {
        UserDefinedException e = new UserDefinedException("test");
        assertEquals(0, e.getStackTrace().length);
    }

    @Test
    public void testPrintStackTraceNoOp() {
        UserDefinedException e = new UserDefinedException("test");
        // should not throw
        e.printStackTrace(System.out);
        e.printStackTrace(new java.io.PrintWriter(System.out));
    }
}
