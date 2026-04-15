package com.walmartlabs.concord.github.appinstallation.exception;

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

class GitHubAppExceptionTest {

    @Test
    void testMessageConstructor() {
        var ex = new GitHubAppException("something failed");
        assertEquals("something failed", ex.getMessage());
        assertNull(ex.getCause());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void testMessageAndCauseConstructor() {
        var cause = new RuntimeException("root cause");
        var ex = new GitHubAppException("something failed", cause);
        assertEquals("something failed", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    void testNotFoundExceptionMessage() {
        var ex = new GitHubAppException.NotFoundException("not found");
        assertEquals("not found", ex.getMessage());
        assertNull(ex.getCause());
        assertInstanceOf(GitHubAppException.class, ex);
    }

    @Test
    void testNotFoundExceptionMessageAndCause() {
        var cause = new RuntimeException("root");
        var ex = new GitHubAppException.NotFoundException("not found", cause);
        assertEquals("not found", ex.getMessage());
        assertSame(cause, ex.getCause());
    }
}
