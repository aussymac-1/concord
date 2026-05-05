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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryStateTest {

    @Test
    public void testCommandConstructorBuildsRootFrame() {
        var cmd = new TestCommand("a");

        var state = new InMemoryState(cmd);

        var rootThread = state.getRootThreadId();
        assertNotNull(rootThread);

        var frame = state.peekFrame(rootThread);
        assertNotNull(frame);
        assertEquals(FrameType.ROOT, frame.getType());
        assertSame(cmd, frame.peek());
    }

    @Test
    public void testFrameConstructor() {
        var rootFrame = Frame.builder().build();

        var state = new InMemoryState(rootFrame);

        assertSame(rootFrame, state.peekFrame(state.getRootThreadId()));
        assertEquals(0, state.getRootThreadId().id());
    }

    @Test
    public void testPushAndPopFrame() {
        var state = new InMemoryState(new TestCommand("a"));
        var thread = state.getRootThreadId();
        var frame2 = Frame.builder().build();

        state.pushFrame(thread, frame2);
        assertSame(frame2, state.peekFrame(thread));

        state.popFrame(thread);

        assertNotNull(state.peekFrame(thread));
        assertNotSame(frame2, state.peekFrame(thread));
    }

    private static void assertNotSame(Object a, Object b) {
        if (a == b) {
            throw new AssertionError("Expected references to differ");
        }
    }

    @Test
    public void testPopUnknownThreadThrows() {
        var state = new InMemoryState(new TestCommand("a"));
        var unknown = state.nextThreadId();

        assertThrows(IllegalStateException.class, () -> state.popFrame(unknown));
    }

    @Test
    public void testPeekFrameReturnsNullForUnknownThread() {
        var state = new InMemoryState(new TestCommand("a"));
        var unknown = state.nextThreadId();

        assertNull(state.peekFrame(unknown));
    }

    @Test
    public void testGetFramesEmptyForUnknownThread() {
        var state = new InMemoryState(new TestCommand("a"));
        var unknown = state.nextThreadId();

        var frames = state.getFrames(unknown);

        assertTrue(frames.isEmpty());
    }

    @Test
    public void testGetFramesIsImmutableCopy() {
        var state = new InMemoryState(new TestCommand("a"));

        var frames = state.getFrames(state.getRootThreadId());

        assertEquals(1, frames.size());
        assertThrows(UnsupportedOperationException.class, () -> frames.add(Frame.builder().build()));
    }

    @Test
    public void testDropAllFramesClearsAll() {
        var state = new InMemoryState(new TestCommand("a"));

        state.dropAllFrames();

        assertTrue(state.getFrames(state.getRootThreadId()).isEmpty());
    }

    @Test
    public void testStatusGetSet() {
        var state = new InMemoryState(new TestCommand("a"));
        var t = state.nextThreadId();

        assertNull(state.getStatus(t));
        state.setStatus(t, ThreadStatus.READY);
        assertEquals(ThreadStatus.READY, state.getStatus(t));
        assertEquals(ThreadStatus.READY, state.threadStatus().get(t));
    }

    @Test
    public void testThreadStatusReturnsCopy() {
        var state = new InMemoryState(new TestCommand("a"));
        var t = state.nextThreadId();

        state.setStatus(t, ThreadStatus.READY);
        var snapshot = state.threadStatus();

        snapshot.clear();
        // original state is unaffected
        assertEquals(ThreadStatus.READY, state.getStatus(t));
    }

    @Test
    public void testForkAttachesChildAndStatus() {
        var state = new InMemoryState(new TestCommand("a"));
        var parent = state.getRootThreadId();
        var child = state.nextThreadId();

        state.fork(parent, child, new TestCommand("c"));

        assertEquals(ThreadStatus.READY, state.getStatus(child));
        assertSame(state.peekFrame(child).peek().getClass(), TestCommand.class);
    }

    @Test
    public void testNextThreadIdIsUnique() {
        var state = new InMemoryState(new TestCommand("a"));

        var a = state.nextThreadId();
        var b = state.nextThreadId();

        assertFalse(a.equals(b));
    }

    @Test
    public void testEventRefRoundTrip() {
        var state = new InMemoryState(new TestCommand("a"));
        var t = state.nextThreadId();

        state.setEventRef(t, "ev1");
        assertEquals("ev1", state.getEventRefs().get(t));

        var found = state.removeEventRef("ev1");
        assertEquals(t, found);

        assertNull(state.getEventRefs().get(t));
    }

    @Test
    public void testEventRefRemoveUnknownReturnsNull() {
        var state = new InMemoryState(new TestCommand("a"));

        assertNull(state.removeEventRef("ev1"));
    }

    @Test
    public void testEventRefDuplicateThrows() {
        var state = new InMemoryState(new TestCommand("a"));
        var t = state.nextThreadId();

        state.setEventRef(t, "ev1");

        assertThrows(IllegalStateException.class, () -> state.setEventRef(t, "ev2"));
    }

    @Test
    public void testThreadErrorRoundTrip() {
        var state = new InMemoryState(new TestCommand("a"));
        var t = state.nextThreadId();
        var ex = new RuntimeException("boom");

        state.setThreadError(t, ex);

        var err = state.getThreadError(t);
        assertNotNull(err);
        assertSame(ex, err.exception());
        assertNull(err.cmd());

        var cleared = state.clearThreadError(t);
        assertNotNull(cleared);
        assertNull(state.getThreadError(t));
    }

    @Test
    public void testThreadErrorWithCommand() {
        var state = new InMemoryState(new TestCommand("a"));
        var t = state.nextThreadId();
        var cmd = new TestCommand("c");
        var ex = new RuntimeException("boom");

        state.setThreadError(t, cmd, ex);

        var err = state.getThreadError(t);
        assertSame(cmd, err.cmd());
    }

    @Test
    public void testStackTracePushAndUnwindOnPopFrame() {
        var state = new InMemoryState(new TestCommand("a"));
        var t = state.getRootThreadId();
        var frame = state.peekFrame(t);

        var item = new StackTraceItem(frame.id(), t, "f.yml", "default", 1, 1);
        state.pushStackTraceItem(t, item);

        var trace = state.getStackTrace(t);
        assertEquals(1, trace.size());
        assertSame(item, trace.get(0));

        // pop the frame — its stack trace should be unwound
        state.popFrame(t);
        assertTrue(state.getStackTrace(t).isEmpty());
    }

    @Test
    public void testStackTraceUnwindKeepsOlderItems() {
        var state = new InMemoryState(new TestCommand("a"));
        var t = state.getRootThreadId();
        var rootFrame = state.peekFrame(t);

        var rootItem = new StackTraceItem(rootFrame.id(), t, "root.yml", "root", 1, 1);
        state.pushStackTraceItem(t, rootItem);

        var inner = Frame.builder().build();
        state.pushFrame(t, inner);
        var innerItem = new StackTraceItem(inner.id(), t, "inner.yml", "inner", 1, 1);
        state.pushStackTraceItem(t, innerItem);

        // Pre-condition: items present in head-first order
        assertEquals(2, state.getStackTrace(t).size());

        // Pop the inner frame — only the inner item should be removed
        state.popFrame(t);

        var trace = state.getStackTrace(t);
        assertEquals(1, trace.size());
        assertSame(rootItem, trace.get(0));
    }

    @Test
    public void testClearStackTrace() {
        var state = new InMemoryState(new TestCommand("a"));
        var t = state.getRootThreadId();

        state.pushStackTraceItem(t, new StackTraceItem(new FrameId(UUID.randomUUID()), t, "f", "fl", 1, 1));
        state.clearStackTrace(t);

        assertTrue(state.getStackTrace(t).isEmpty());
    }

    @Test
    public void testThreadLocalSetGetRemove() {
        var state = new InMemoryState(new TestCommand("a"));
        var t = state.nextThreadId();

        assertNull(state.getThreadLocal(t, "k"));

        state.setThreadLocal(t, "k", "v");
        assertEquals("v", state.getThreadLocal(t, "k"));

        state.removeThreadLocal(t, "k");
        assertNull(state.getThreadLocal(t, "k"));
    }

    @Test
    public void testGcRemovesDoneAndHandledFailedThreads() {
        var state = new InMemoryState(new TestCommand("a"));

        var doneThread = state.nextThreadId();
        state.setStatus(doneThread, ThreadStatus.DONE);
        state.pushFrame(doneThread, Frame.builder().build());

        var failedHandledThread = state.nextThreadId();
        state.setStatus(failedHandledThread, ThreadStatus.FAILED);
        // no thread error set => "handled"
        state.pushFrame(failedHandledThread, Frame.builder().build());

        var failedUnhandledThread = state.nextThreadId();
        state.setStatus(failedUnhandledThread, ThreadStatus.FAILED);
        state.setThreadError(failedUnhandledThread, new RuntimeException("boom"));
        state.pushFrame(failedUnhandledThread, Frame.builder().build());

        state.gc();

        assertNull(state.getStatus(doneThread));
        assertNull(state.getStatus(failedHandledThread));
        // unhandled failure remains
        assertEquals(ThreadStatus.FAILED, state.getStatus(failedUnhandledThread));
        assertNotNull(state.getThreadError(failedUnhandledThread));
    }

    @Test
    public void testGetStackTraceForChildThreadIncludesParent() {
        var state = new InMemoryState(new TestCommand("a"));
        var parent = state.getRootThreadId();
        var child = state.nextThreadId();

        state.fork(parent, child, new TestCommand("c"));

        var parentFrame = state.peekFrame(parent);
        var parentItem = new StackTraceItem(parentFrame.id(), parent, "p.yml", "p", 1, 1);
        state.pushStackTraceItem(parent, parentItem);

        var childFrame = state.peekFrame(child);
        var childItem = new StackTraceItem(childFrame.id(), child, "c.yml", "c", 2, 2);
        state.pushStackTraceItem(child, childItem);

        var trace = state.getStackTrace(child);

        assertEquals(2, trace.size());
        assertSame(childItem, trace.get(0));
        assertSame(parentItem, trace.get(1));
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
