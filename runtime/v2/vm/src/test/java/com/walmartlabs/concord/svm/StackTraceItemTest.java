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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StackTraceItemTest {

    @Test
    public void testGetters() {
        var frameId = new FrameId(UUID.randomUUID());
        var threadId = new InMemoryState(new TestCommand()).nextThreadId();

        var item = new StackTraceItem(frameId, threadId, "flow.yml", "default", 5, 3);

        assertEquals(frameId, item.getFrameId());
        assertEquals(threadId, item.getThreadId());
        assertEquals("flow.yml", item.getFileName());
        assertEquals("default", item.getFlowName());
        assertEquals(5, item.getLineNumber());
        assertEquals(3, item.getColumnNumber());
    }

    @Test
    public void testToStringWithFileName() {
        var threadId = new InMemoryState(new TestCommand()).nextThreadId();
        var item = new StackTraceItem(null, threadId, "flow.yml", "default", 5, 3);

        var s = item.toString();
        assertTrue(s.contains("(flow.yml)"));
        assertTrue(s.contains("line: 5"));
        assertTrue(s.contains("col: 3"));
        assertTrue(s.contains("flow: default"));
    }

    @Test
    public void testToStringWithoutFileName() {
        var threadId = new InMemoryState(new TestCommand()).nextThreadId();
        var item = new StackTraceItem(null, threadId, null, "default", 5, 3);

        var s = item.toString();
        assertTrue(s.contains("(n/a)"));
    }

    @Test
    public void testNullFlowName() {
        var threadId = new InMemoryState(new TestCommand()).nextThreadId();
        var item = new StackTraceItem(null, threadId, "f.yml", null, 1, 1);

        assertNull(item.getFlowName());
    }

    private static final class TestCommand implements Command {
        private static final long serialVersionUID = 1L;

        @Override
        public void eval(Runtime runtime, State state, ThreadId threadId) {
        }
    }
}
