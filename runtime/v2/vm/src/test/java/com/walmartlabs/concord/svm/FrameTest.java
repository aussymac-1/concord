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

import static org.junit.jupiter.api.Assertions.*;

public class FrameTest {

    private final Command dummyCmd = (runtime, state, threadId) -> {};

    @Test
    public void testBuilderRoot() {
        Frame frame = Frame.builder()
                .root()
                .build();

        assertNotNull(frame.id());
        assertEquals(FrameType.ROOT, frame.getType());
        assertNull(frame.peek());
    }

    @Test
    public void testBuilderNonRoot() {
        Frame frame = Frame.builder()
                .nonRoot()
                .build();

        assertEquals(FrameType.NON_ROOT, frame.getType());
    }

    @Test
    public void testBuilderWithCommands() {
        Command cmd1 = (runtime, state, threadId) -> {};
        Command cmd2 = (runtime, state, threadId) -> {};

        Frame frame = Frame.builder()
                .root()
                .commands(cmd1, cmd2)
                .build();

        // First command pushed is at the bottom, last pushed is at the top
        assertNotNull(frame.peek());
    }

    @Test
    public void testPushAndPeek() {
        Frame frame = Frame.builder().root().build();
        assertNull(frame.peek());

        frame.push(dummyCmd);
        assertSame(dummyCmd, frame.peek());
    }

    @Test
    public void testPushAndPop() {
        Command cmd1 = (runtime, state, threadId) -> {};
        Command cmd2 = (runtime, state, threadId) -> {};

        Frame frame = Frame.builder().root().build();
        frame.push(cmd1);
        frame.push(cmd2);

        assertSame(cmd2, frame.peek());
        frame.pop();
        assertSame(cmd1, frame.peek());
        frame.pop();
        assertNull(frame.peek());
    }

    @Test
    public void testLocals() {
        Frame frame = Frame.builder().root().build();

        assertFalse(frame.hasLocal("key"));
        assertNull(frame.getLocal("key"));

        frame.setLocal("key", "value");
        assertTrue(frame.hasLocal("key"));
        assertEquals("value", frame.getLocal("key"));
    }

    @Test
    public void testGetLocalsUnmodifiable() {
        Frame frame = Frame.builder().root().build();
        frame.setLocal("k", "v");

        Map<String, java.io.Serializable> locals = frame.getLocals();
        assertThrows(UnsupportedOperationException.class, () -> locals.put("k2", "v2"));
    }

    @Test
    public void testBuilderWithLocals() {
        Map<String, Object> locals = new HashMap<>();
        locals.put("key1", "value1");
        locals.put("key2", 42);

        Frame frame = Frame.builder()
                .root()
                .locals(locals)
                .build();

        assertTrue(frame.hasLocal("key1"));
        assertEquals("value1", frame.getLocal("key1"));
        assertEquals(42, frame.getLocal("key2"));
    }

    @Test
    public void testBuilderLocalsRejectsNonSerializable() {
        Map<String, Object> locals = new HashMap<>();
        locals.put("bad", new Object());

        assertThrows(IllegalStateException.class, () ->
                Frame.builder().root().locals(locals).build());
    }

    @Test
    public void testBuilderNullLocals() {
        Frame frame = Frame.builder()
                .root()
                .locals(null)
                .build();

        assertNotNull(frame);
        assertTrue(frame.getLocals().isEmpty());
    }

    @Test
    public void testExceptionHandler() {
        Command handler = (runtime, state, threadId) -> {};

        Frame frame = Frame.builder()
                .root()
                .exceptionHandler(handler)
                .build();

        assertSame(handler, frame.getExceptionHandler());

        frame.clearExceptionHandler();
        assertNull(frame.getExceptionHandler());
    }

    @Test
    public void testSetExceptionHandler() {
        Frame frame = Frame.builder().root().build();
        assertNull(frame.getExceptionHandler());

        Command handler = (runtime, state, threadId) -> {};
        frame.setExceptionHandler(handler);
        assertSame(handler, frame.getExceptionHandler());
    }

    @Test
    public void testFinallyHandler() {
        Command handler = (runtime, state, threadId) -> {};

        Frame frame = Frame.builder()
                .root()
                .finallyHandler(handler)
                .build();

        assertSame(handler, frame.getFinallyHandler());
    }

    @Test
    public void testFrameId() {
        Frame frame1 = Frame.builder().root().build();
        Frame frame2 = Frame.builder().root().build();

        assertNotNull(frame1.id());
        assertNotNull(frame2.id());
        assertNotEquals(frame1.id(), frame2.id());
    }
}
