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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FrameTest {

    @Test
    public void testDefaults() {
        var frame = Frame.builder().build();

        assertNotNull(frame.id());
        assertEquals(FrameType.ROOT, frame.getType());
        assertNull(frame.peek());
        assertNull(frame.getExceptionHandler());
        assertNull(frame.getFinallyHandler());
        assertTrue(frame.getLocals().isEmpty());
    }

    @Test
    public void testRootAndNonRoot() {
        var root = Frame.builder().root().build();
        var nonRoot = Frame.builder().nonRoot().build();

        assertEquals(FrameType.ROOT, root.getType());
        assertEquals(FrameType.NON_ROOT, nonRoot.getType());
    }

    @Test
    public void testCommandsArePushedAndPeekReturnsLast() {
        var c1 = new TestCommand("a");
        var c2 = new TestCommand("b");

        var frame = Frame.builder()
                .commands(c1, c2)
                .build();

        // The Builder iterates over commands and pushes each to the head of the
        // stack. So `c2` is pushed last, leaving `c2` at the head.
        assertSame(c2, frame.peek());
        frame.pop();
        assertSame(c1, frame.peek());
        frame.pop();
        assertNull(frame.peek());
    }

    @Test
    public void testCommandsBuilderHandlesNullAndEmpty() {
        var f1 = Frame.builder().commands().build();
        var f2 = Frame.builder().commands((Command[]) null).build();

        assertNull(f1.peek());
        assertNull(f2.peek());
    }

    @Test
    public void testManualPushAndPop() {
        var frame = Frame.builder().build();
        var c1 = new TestCommand("a");
        var c2 = new TestCommand("b");

        frame.push(c1);
        frame.push(c2);

        assertSame(c2, frame.peek());
        frame.pop();
        assertSame(c1, frame.peek());
    }

    @Test
    public void testExceptionHandler() {
        var handler = new TestCommand("h");

        var frame = Frame.builder().exceptionHandler(handler).build();

        assertSame(handler, frame.getExceptionHandler());

        frame.clearExceptionHandler();
        assertNull(frame.getExceptionHandler());

        frame.setExceptionHandler(handler);
        assertSame(handler, frame.getExceptionHandler());
    }

    @Test
    public void testFinallyHandler() {
        var handler = new TestCommand("h");

        var frame = Frame.builder().finallyHandler(handler).build();

        assertSame(handler, frame.getFinallyHandler());
    }

    @Test
    public void testLocalsBuilderAndAccess() {
        Map<String, Object> locals = new HashMap<>();
        locals.put("a", "1");
        locals.put("b", null);

        var frame = Frame.builder().locals(locals).build();

        assertTrue(frame.hasLocal("a"));
        assertEquals("1", frame.getLocal("a"));
        assertTrue(frame.hasLocal("b"));
        assertNull(frame.getLocal("b"));
        assertFalse(frame.hasLocal("missing"));
        assertEquals(2, frame.getLocals().size());
    }

    @Test
    public void testLocalsBuilderHandlesNullAndEmpty() {
        var f1 = Frame.builder().locals(null).build();
        var f2 = Frame.builder().locals(Map.of()).build();

        assertTrue(f1.getLocals().isEmpty());
        assertTrue(f2.getLocals().isEmpty());
    }

    @Test
    public void testLocalsBuilderRejectsNonSerializable() {
        Map<String, Object> locals = new HashMap<>();
        locals.put("a", new Object());

        var ex = assertThrows(IllegalStateException.class,
                () -> Frame.builder().locals(locals).build());

        assertTrue(ex.getMessage().contains("Can't set a non-serializable local variable: a"));
    }

    @Test
    public void testSetLocalThenRead() {
        var frame = Frame.builder().build();

        frame.setLocal("k", "v");

        assertTrue(frame.hasLocal("k"));
        assertEquals("v", frame.getLocal("k"));
    }

    @Test
    public void testGetLocalsIsUnmodifiable() {
        var frame = Frame.builder().build();
        frame.setLocal("k", "v");

        var locals = frame.getLocals();

        assertThrows(UnsupportedOperationException.class, () -> locals.put("x", "y"));
    }

    @Test
    public void testIdsAreUnique() {
        var f1 = Frame.builder().build();
        var f2 = Frame.builder().build();

        assertNotNull(f1.id());
        assertNotNull(f2.id());
        // there's an extremely small probability of collision but two random
        // UUIDs should never be equal
        assertFalse(f1.id().equals(f2.id()));
    }

    private static final class TestCommand implements Command {
        private static final long serialVersionUID = 1L;
        private final String name;

        TestCommand(String name) {
            this.name = name;
        }

        @Override
        public void eval(Runtime runtime, State state, ThreadId threadId) {
        }

        @Override
        public String toString() {
            return "TestCommand{" + name + "}";
        }
    }
}
