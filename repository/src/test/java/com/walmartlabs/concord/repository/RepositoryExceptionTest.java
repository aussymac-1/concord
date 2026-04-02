package com.walmartlabs.concord.repository;

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

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryExceptionTest {

    @Test
    public void testMessageOnlyConstructor() {
        RepositoryException e = new RepositoryException("test error");
        assertEquals("test error", e.getMessage());
        assertNull(e.getCause());
    }

    @Test
    public void testMessageAndCauseConstructor() {
        RuntimeException cause = new RuntimeException("root cause");
        RepositoryException e = new RepositoryException("test error", cause);
        assertEquals("test error", e.getMessage());
        assertSame(cause, e.getCause());
    }

    @Test
    public void testIsRuntimeException() {
        RepositoryException e = new RepositoryException("test");
        assertInstanceOf(RuntimeException.class, e);
    }
}
