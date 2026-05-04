package com.walmartlabs.concord.sdk;

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

import static org.junit.jupiter.api.Assertions.*;

public class ClientExceptionTest {

    @Test
    public void testMessageOnly() {
        var e = new ClientException("oops");
        assertEquals("oops", e.getMessage());
        assertNull(e.getCause());
        assertNull(e.getInstanceId());
    }

    @Test
    public void testMessageAndCause() {
        var cause = new RuntimeException("inner");
        var e = new ClientException("oops", cause);
        assertSame(cause, e.getCause());
        assertNull(e.getInstanceId());
    }

    @Test
    public void testFullConstructor() {
        var cause = new RuntimeException("inner");
        var e = new ClientException("instance-1", "oops", cause);
        assertEquals("instance-1", e.getInstanceId());
        assertEquals("oops", e.getMessage());
        assertSame(cause, e.getCause());
    }
}
