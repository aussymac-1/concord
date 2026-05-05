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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class StateBackwardCompatibilityTest {

    @Test
    public void testNullReturnsNull() {
        var threadId = new InMemoryState(new TestCommand()).nextThreadId();

        assertNull(StateBackwardCompatibility.processThreadError(null, threadId));
    }

    @Test
    public void testExceptionWrappedIntoThreadError() {
        var threadId = new InMemoryState(new TestCommand()).nextThreadId();
        var ex = new RuntimeException("oops");

        var err = StateBackwardCompatibility.processThreadError(ex, threadId);

        assertEquals(threadId, err.threadId());
        assertNull(err.cmd());
        assertSame(ex, err.exception());
    }

    @Test
    public void testThreadErrorIsReturnedAsIs() {
        var threadId = new InMemoryState(new TestCommand()).nextThreadId();
        var input = new ThreadError(threadId, new TestCommand(), new RuntimeException("oops"));

        var result = StateBackwardCompatibility.processThreadError(input, threadId);

        assertSame(input, result);
    }

    private static final class TestCommand implements Command {
        private static final long serialVersionUID = 1L;

        @Override
        public void eval(Runtime runtime, State state, ThreadId threadId) {
        }
    }
}
