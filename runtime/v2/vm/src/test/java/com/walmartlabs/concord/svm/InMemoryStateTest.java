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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryStateTest {

    private final Command dummyCmd = (runtime, state, threadId) -> {};

    @Test
    public void testInitialState() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();

        assertNotNull(rootId);
        assertNotNull(state.peekFrame(rootId));
    }

    @Test
    public void testPushAndPopFrame() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();

        Frame frame = Frame.builder().nonRoot().build();
        state.pushFrame(rootId, frame);

        assertSame(frame, state.peekFrame(rootId));

        state.popFrame(rootId);
        assertNotNull(state.peekFrame(rootId)); // still has root frame
    }

    @Test
    public void testPopFrameThrowsOnEmpty() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId fakeId = state.nextThreadId();

        assertThrows(IllegalStateException.class, () -> state.popFrame(fakeId));
    }

    @Test
    public void testGetFrames() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();

        List<Frame> frames = state.getFrames(rootId);
        assertEquals(1, frames.size());
    }

    @Test
    public void testGetFramesUnmodifiable() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();

        List<Frame> frames = state.getFrames(rootId);
        assertThrows(UnsupportedOperationException.class, () -> frames.add(Frame.builder().root().build()));
    }

    @Test
    public void testGetFramesNonExistentThread() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId fakeId = state.nextThreadId();

        List<Frame> frames = state.getFrames(fakeId);
        assertTrue(frames.isEmpty());
    }

    @Test
    public void testDropAllFrames() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();

        state.dropAllFrames();
        assertNull(state.peekFrame(rootId));
    }

    @Test
    public void testSetAndGetStatus() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();

        state.setStatus(rootId, ThreadStatus.READY);
        assertEquals(ThreadStatus.READY, state.getStatus(rootId));

        state.setStatus(rootId, ThreadStatus.SUSPENDED);
        assertEquals(ThreadStatus.SUSPENDED, state.getStatus(rootId));
    }

    @Test
    public void testFork() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();
        ThreadId childId = state.nextThreadId();

        Command childCmd = (runtime, s, threadId) -> {};
        state.fork(rootId, childId, childCmd);

        assertEquals(ThreadStatus.READY, state.getStatus(childId));
        assertNotNull(state.peekFrame(childId));
    }

    @Test
    public void testThreadStatus() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();

        state.setStatus(rootId, ThreadStatus.READY);

        Map<ThreadId, ThreadStatus> statusMap = state.threadStatus();
        assertEquals(ThreadStatus.READY, statusMap.get(rootId));
    }

    @Test
    public void testNextThreadId() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId id1 = state.nextThreadId();
        ThreadId id2 = state.nextThreadId();

        assertNotEquals(id1, id2);
    }

    @Test
    public void testEventRefs() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();

        state.setEventRef(rootId, "event1");

        Map<ThreadId, String> refs = state.getEventRefs();
        assertEquals("event1", refs.get(rootId));

        ThreadId removed = state.removeEventRef("event1");
        assertSame(rootId, removed);
        assertTrue(state.getEventRefs().isEmpty());
    }

    @Test
    public void testRemoveNonExistentEventRef() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId result = state.removeEventRef("nonexistent");
        assertNull(result);
    }

    @Test
    public void testSetEventRefDuplicate() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();

        state.setEventRef(rootId, "event1");
        assertThrows(IllegalStateException.class, () -> state.setEventRef(rootId, "event2"));
    }

    @Test
    public void testThreadError() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();

        assertNull(state.getThreadError(rootId));

        RuntimeException ex = new RuntimeException("test error");
        state.setThreadError(rootId, ex);

        ThreadError error = state.getThreadError(rootId);
        assertNotNull(error);

        ThreadError cleared = state.clearThreadError(rootId);
        assertNotNull(cleared);
        assertNull(state.getThreadError(rootId));
    }

    @Test
    public void testThreadErrorWithCommand() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();

        RuntimeException ex = new RuntimeException("test");
        state.setThreadError(rootId, dummyCmd, ex);

        ThreadError error = state.getThreadError(rootId);
        assertNotNull(error);
    }

    @Test
    public void testThreadLocals() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();

        assertNull(state.getThreadLocal(rootId, "key"));

        state.setThreadLocal(rootId, "key", "value");
        assertEquals("value", state.getThreadLocal(rootId, "key"));

        state.removeThreadLocal(rootId, "key");
        assertNull(state.getThreadLocal(rootId, "key"));
    }

    @Test
    public void testStackTrace() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();

        assertTrue(state.getStackTrace(rootId).isEmpty());

        FrameId frameId = state.peekFrame(rootId).id();
        StackTraceItem item = new StackTraceItem(frameId, rootId, "file.concord.yml", "default", 1, 0);
        state.pushStackTraceItem(rootId, item);

        List<StackTraceItem> trace = state.getStackTrace(rootId);
        assertEquals(1, trace.size());

        state.clearStackTrace(rootId);
        assertTrue(state.getStackTrace(rootId).isEmpty());
    }

    @Test
    public void testGc() {
        InMemoryState state = new InMemoryState(dummyCmd);
        ThreadId rootId = state.getRootThreadId();
        ThreadId childId = state.nextThreadId();

        state.fork(rootId, childId, dummyCmd);
        state.setStatus(childId, ThreadStatus.DONE);

        state.gc();

        assertNull(state.getStatus(childId));
    }
}
