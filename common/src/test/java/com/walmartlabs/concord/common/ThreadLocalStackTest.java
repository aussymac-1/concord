package com.walmartlabs.concord.common;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2026 Walmart Inc.
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

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class ThreadLocalStackTest {

    @Test
    public void testPushAndPop() {
        var stack = new ThreadLocalStack<String>();
        stack.push("a");
        stack.push("b");
        assertEquals("b", stack.pop());
        assertEquals("a", stack.pop());
    }

    @Test
    public void testPeek() {
        var stack = new ThreadLocalStack<String>();
        assertNull(stack.peek());

        stack.push("a");
        assertEquals("a", stack.peek());
        assertEquals("a", stack.peek());
        assertEquals("a", stack.pop());
    }

    @Test
    public void testPopEmptyThrows() {
        var stack = new ThreadLocalStack<String>();
        assertThrows(IllegalStateException.class, stack::pop);
    }

    @Test
    public void testThreadIsolation() throws Exception {
        var stack = new ThreadLocalStack<String>();
        stack.push("main");

        var failure = new AtomicReference<Throwable>();
        var thread = new Thread(() -> {
            try {
                assertNull(stack.peek());
                stack.push("other");
                assertEquals("other", stack.peek());
            } catch (Throwable t) {
                failure.set(t);
            }
        });
        thread.start();
        thread.join();
        if (failure.get() != null) {
            throw failure.get();
        }

        assertEquals("main", stack.peek());
    }
}
