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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PopFrameCommandTest {

    @Test
    public void testPopFrameWithoutFinallyHandler() {
        var pop = new PopFrameCommand();

        var inner = Frame.builder().commands(pop).build();
        var outer = Frame.builder().commands(new TestCommand("outer")).build();

        var state = new InMemoryState(outer);
        var t = state.getRootThreadId();
        state.pushFrame(t, inner);

        // The pop command itself is the head of the inner frame
        pop.eval(null, state, t);

        // After eval the inner frame has been removed
        assertSame(outer, state.peekFrame(t));
    }

    @Test
    public void testPopFrameWithFinallyHandler() {
        var pop = new PopFrameCommand();
        var finallyCmd = new TestCommand("finally");

        var inner = Frame.builder()
                .commands(pop)
                .finallyHandler(finallyCmd)
                .build();
        var outer = Frame.builder().build();

        var state = new InMemoryState(outer);
        var t = state.getRootThreadId();
        state.pushFrame(t, inner);

        pop.eval(null, state, t);

        // pop itself is removed, then finallyCmd is pushed, then a synthetic
        // skip-finally PopFrameCommand is pushed underneath
        var head = state.peekFrame(t);
        assertSame(inner, head);
        assertSame(finallyCmd, head.peek());
        head.pop();
        assertNotNull(head.peek());
        // the next command must be a PopFrameCommand instance
        assertEquals(PopFrameCommand.class, head.peek().getClass());
    }

    @Test
    public void testPopFrameDuringUnwindingPushesAnotherPop() {
        var pop = new PopFrameCommand();

        var inner = Frame.builder().commands(pop).build();
        var outer = Frame.builder().build();

        var state = new InMemoryState(outer);
        var t = state.getRootThreadId();
        state.pushFrame(t, inner);
        state.setStatus(t, ThreadStatus.UNWINDING);

        pop.eval(null, state, t);

        var head = state.peekFrame(t);
        assertSame(outer, head);
        // unwinding => head should now contain another PopFrameCommand
        assertNotNull(head.peek());
        assertEquals(PopFrameCommand.class, head.peek().getClass());
    }

    @Test
    public void testPopFrameAtRootDuringUnwindingDoesNotPushOnNullFrame() {
        var pop = new PopFrameCommand();

        var only = Frame.builder().commands(pop).build();
        var state = new InMemoryState(only);
        var t = state.getRootThreadId();
        state.setStatus(t, ThreadStatus.UNWINDING);

        // Should not throw — popping the only frame leaves the thread without
        // a frame, but the eval method tolerates that case.
        pop.eval(null, state, t);

        assertNull(state.peekFrame(t));
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
