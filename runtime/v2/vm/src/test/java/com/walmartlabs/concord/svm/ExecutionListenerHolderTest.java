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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ExecutionListenerHolderTest {

    @Test
    public void testFireBeforeCommandReturnsContinueByDefault() {
        var holder = new ExecutionListenerHolder(null, List.of());

        assertEquals(ExecutionListener.Result.CONTINUE,
                holder.fireBeforeCommand(null, new InMemoryState(new TestCommand("c")), null, new TestCommand("a")));
    }

    @Test
    public void testFireBeforeCommandPropagatesBreak() {
        var listeners = List.<ExecutionListener>of(
                new RecordingListener(ExecutionListener.Result.CONTINUE),
                new RecordingListener(ExecutionListener.Result.BREAK)
        );
        var holder = new ExecutionListenerHolder(null, listeners);

        var result = holder.fireBeforeCommand(null, new InMemoryState(new TestCommand("c")), null, new TestCommand("a"));

        assertEquals(ExecutionListener.Result.BREAK, result);
    }

    @Test
    public void testFireAfterCommandPropagatesBreak() {
        var listeners = List.<ExecutionListener>of(
                new RecordingListener(ExecutionListener.Result.BREAK)
        );
        var holder = new ExecutionListenerHolder(null, listeners);

        var result = holder.fireAfterCommand(null, new InMemoryState(new TestCommand("c")), null, new TestCommand("a"));

        assertEquals(ExecutionListener.Result.BREAK, result);
    }

    @Test
    public void testFireAfterCommandWithErrorInvokesOnCommandError() {
        var l = new RecordingListener(ExecutionListener.Result.CONTINUE);
        var holder = new ExecutionListenerHolder(null, List.<ExecutionListener>of(l));
        var ex = new RuntimeException("boom");

        var result = holder.fireAfterCommandWithError(null, new InMemoryState(new TestCommand("c")), null, new TestCommand("a"), ex);

        assertEquals(ExecutionListener.Result.CONTINUE, result);
        assertEquals(1, l.errors.size());
        assertSame(ex, l.errors.get(0));
    }

    @Test
    public void testFireAfterEvalAndAfterWakeUp() {
        var l = new RecordingListener(ExecutionListener.Result.CONTINUE);
        var holder = new ExecutionListenerHolder(null, List.<ExecutionListener>of(l));
        var state = new InMemoryState(new TestCommand("c"));

        assertEquals(ExecutionListener.Result.CONTINUE, holder.fireAfterEval(null, state));
        assertEquals(ExecutionListener.Result.CONTINUE, holder.fireAfterWakeUp(null, state));
        assertEquals(1, l.afterEval);
        assertEquals(1, l.afterWakeUp);
    }

    @Test
    public void testProcessLifecycleHooks() {
        var l = new RecordingListener(ExecutionListener.Result.CONTINUE);
        var holder = new ExecutionListenerHolder(null, List.<ExecutionListener>of(l));
        var state = new InMemoryState(new TestCommand("c"));
        var lastFrame = Frame.builder().build();

        holder.fireBeforeProcessStart(null, state);
        holder.fireBeforeProcessResume(null, state);
        holder.fireAfterProcessEnds(null, state, lastFrame);
        holder.fireOnProcessError(null, state, new RuntimeException("oops"));

        assertEquals(1, l.beforeStart);
        assertEquals(1, l.beforeResume);
        assertEquals(1, l.afterEnds);
        assertSame(lastFrame, l.lastFrameSeen);
        assertEquals(1, l.processErrors.size());
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

    private static final class RecordingListener implements ExecutionListener {
        private final Result result;
        final List<Exception> errors = new ArrayList<>();
        final List<Exception> processErrors = new ArrayList<>();
        int afterEval;
        int afterWakeUp;
        int beforeStart;
        int beforeResume;
        int afterEnds;
        Frame lastFrameSeen;

        RecordingListener(Result result) {
            this.result = result;
        }

        @Override
        public Result beforeCommand(Runtime runtime, VM vm, State state, ThreadId threadId, Command cmd) {
            return result;
        }

        @Override
        public Result afterCommand(Runtime runtime, VM vm, State state, ThreadId threadId, Command cmd) {
            return result;
        }

        @Override
        public Result onCommandError(Runtime runtime, VM vm, State state, ThreadId threadId, Command cmd, Exception e) {
            errors.add(e);
            return result;
        }

        @Override
        public Result afterEval(Runtime runtime, VM vm, State state) {
            afterEval++;
            return result;
        }

        @Override
        public Result afterWakeUp(Runtime runtime, VM vm, State state) {
            afterWakeUp++;
            return result;
        }

        @Override
        public void beforeProcessStart(Runtime runtime, State state) {
            beforeStart++;
        }

        @Override
        public void beforeProcessResume(Runtime runtime, State state) {
            beforeResume++;
        }

        @Override
        public void afterProcessEnds(Runtime runtime, State state, Frame lastFrame) {
            afterEnds++;
            lastFrameSeen = lastFrame;
        }

        @Override
        public void onProcessError(Runtime runtime, State state, Exception e) {
            processErrors.add(e);
        }
    }
}
