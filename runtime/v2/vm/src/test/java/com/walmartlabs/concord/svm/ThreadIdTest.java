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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadIdTest {

    @Test
    public void testEqualsAndHashCode() {
        var state = new InMemoryState(new TestCommand());

        var a = state.nextThreadId();
        var b = state.nextThreadId();
        var aCopy = new InMemoryState(new TestCommand()).nextThreadId();

        assertEquals(a, a);
        assertNotEquals(a, b);
        // independent state instances start at 0 — same id values are equal
        assertEquals(a.id(), aCopy.id());
        assertEquals(a, aCopy);
        assertEquals(a.hashCode(), aCopy.hashCode());
    }

    @Test
    public void testEqualsHandlesNullAndOtherType() {
        var state = new InMemoryState(new TestCommand());
        var id = state.nextThreadId();

        assertNotEquals(id, null);
        assertNotEquals(id, "not-a-thread-id");
    }

    @Test
    public void testIdReturnsUnderlyingValue() {
        var state = new InMemoryState(new TestCommand());

        // root constructor consumed thread id 0
        var t1 = state.nextThreadId();
        var t2 = state.nextThreadId();

        assertEquals(1, t1.id());
        assertEquals(2, t2.id());
    }

    @Test
    public void testCompareTo() {
        var state = new InMemoryState(new TestCommand());

        var t1 = state.nextThreadId();
        var t2 = state.nextThreadId();

        assertTrue(t1.compareTo(t2) < 0);
        assertTrue(t2.compareTo(t1) > 0);
        assertEquals(0, t1.compareTo(t1));
    }

    @Test
    public void testToStringIncludesId() {
        var state = new InMemoryState(new TestCommand());

        var id = state.nextThreadId();

        assertTrue(id.toString().contains("id="));
    }

    private static final class TestCommand implements Command {
        private static final long serialVersionUID = 1L;

        @Override
        public void eval(Runtime runtime, State state, ThreadId threadId) {
        }
    }
}
