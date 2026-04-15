package com.walmartlabs.concord.common;

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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class ThreadLocalStackTest {

    @Test
    void testPushAndPeek() {
        ThreadLocalStack<String> stack = new ThreadLocalStack<>();
        stack.push("first");
        assertEquals("first", stack.peek());
    }

    @Test
    void testPushAndPop() {
        ThreadLocalStack<String> stack = new ThreadLocalStack<>();
        stack.push("first");
        stack.push("second");
        assertEquals("second", stack.pop());
        assertEquals("first", stack.pop());
    }

    @Test
    void testPeekOnEmptyStackReturnsNull() {
        ThreadLocalStack<String> stack = new ThreadLocalStack<>();
        assertNull(stack.peek());
    }

    @Test
    void testPopOnEmptyStackThrows() {
        ThreadLocalStack<String> stack = new ThreadLocalStack<>();
        assertThrows(IllegalStateException.class, stack::pop);
    }

    @Test
    void testThreadIsolation() throws Exception {
        ThreadLocalStack<String> stack = new ThreadLocalStack<>();
        stack.push("mainThread");

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> otherThreadPeek = new AtomicReference<>();

        Thread t = new Thread(() -> {
            otherThreadPeek.set(stack.peek());
            latch.countDown();
        });
        t.start();
        latch.await();

        assertEquals("mainThread", stack.peek());
        assertNull(otherThreadPeek.get());
    }

    @Test
    void testMultiplePushPop() {
        ThreadLocalStack<Integer> stack = new ThreadLocalStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertThrows(IllegalStateException.class, stack::pop);
    }
}
