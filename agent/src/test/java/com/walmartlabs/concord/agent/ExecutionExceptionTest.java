package com.walmartlabs.concord.agent;

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

public class ExecutionExceptionTest {

    @Test
    void testMessageOnly() {
        ExecutionException ex = new ExecutionException("something went wrong");
        assertEquals("something went wrong", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testMessageWithCause() {
        RuntimeException cause = new RuntimeException("root cause");
        ExecutionException ex = new ExecutionException("wrapper", cause);
        assertEquals("wrapper", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    void testIsException() {
        ExecutionException ex = new ExecutionException("test");
        assertInstanceOf(Exception.class, ex);
    }

    @Test
    void testNullMessage() {
        ExecutionException ex = new ExecutionException(null);
        assertNull(ex.getMessage());
    }
}
