package com.walmartlabs.concord.plugins.throwex;

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

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ConcordExceptionTest {

    @Test
    public void testMessageOnly() {
        var ex = new ConcordException("test error");
        assertEquals("test error", ex.getMessage());
        assertNull(ex.getPayload());
    }

    @Test
    public void testMessageWithPayload() {
        var payload = new HashMap<String, Object>();
        payload.put("key", "value");
        var ex = new ConcordException("test error", payload);
        assertEquals("test error", ex.getMessage());
        assertEquals(payload, ex.getPayload());
    }

    @Test
    public void testNullPayload() {
        var ex = new ConcordException("msg", null);
        assertEquals("msg", ex.getMessage());
        assertNull(ex.getPayload());
    }

    @Test
    public void testIsException() {
        var ex = new ConcordException("test");
        assertInstanceOf(Exception.class, ex);
    }
}
