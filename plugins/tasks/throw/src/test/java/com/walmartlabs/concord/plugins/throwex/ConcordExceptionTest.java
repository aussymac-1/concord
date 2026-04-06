package com.walmartlabs.concord.plugins.throwex;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2019 Walmart Inc.
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConcordExceptionTest {

    @Test
    void testMessageOnly() {
        var ex = new ConcordException("something went wrong");
        assertEquals("something went wrong", ex.getMessage());
        assertNull(ex.getPayload());
    }

    @Test
    void testMessageWithPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("code", 42);
        payload.put("detail", "timeout");

        var ex = new ConcordException("error occurred", (java.io.Serializable) payload);
        assertEquals("error occurred", ex.getMessage());
        assertNotNull(ex.getPayload());
    }

    @Test
    void testNullPayload() {
        var ex = new ConcordException("msg", null);
        assertEquals("msg", ex.getMessage());
        assertNull(ex.getPayload());
    }

    @Test
    void testIsException() {
        var ex = new ConcordException("test");
        assertTrue(ex instanceof Exception);
    }

    @Test
    void testPayloadIsSerializable() {
        var ex = new ConcordException("test", "string-payload");
        assertEquals("string-payload", ex.getPayload());
    }
}
