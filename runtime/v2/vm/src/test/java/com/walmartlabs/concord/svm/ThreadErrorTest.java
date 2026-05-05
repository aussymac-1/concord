package com.walmartlabs.concord.svm;

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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ThreadErrorTest {

    @Test
    public void testRecordAccessors() {
        var threadId = new InMemoryState(new TestCommand()).nextThreadId();
        var cmd = new TestCommand();
        var ex = new RuntimeException("oops");

        var err = new ThreadError(threadId, cmd, ex);

        assertEquals(threadId, err.threadId());
        assertSame(cmd, err.cmd());
        assertSame(ex, err.exception());
    }

    @Test
    public void testGetStackTraceDelegatesToException() {
        var threadId = new InMemoryState(new TestCommand()).nextThreadId();
        var ex = new RuntimeException("oops");

        var err = new ThreadError(threadId, null, ex);

        assertArrayEquals(ex.getStackTrace(), err.getStackTrace());
    }

    @Test
    public void testNullCmdAllowed() {
        var threadId = new InMemoryState(new TestCommand()).nextThreadId();

        var err = new ThreadError(threadId, null, new RuntimeException("oops"));

        assertNull(err.cmd());
    }

    private static final class TestCommand implements Command {
        private static final long serialVersionUID = 1L;

        @Override
        public void eval(Runtime runtime, State state, ThreadId threadId) {
        }
    }
}
