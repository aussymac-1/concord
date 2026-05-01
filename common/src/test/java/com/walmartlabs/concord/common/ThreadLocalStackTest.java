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

import static org.junit.jupiter.api.Assertions.*;

public class ThreadLocalStackTest {

    @Test
    public void testPushAndPop() {
        ThreadLocalStack<String> stack = new ThreadLocalStack<>();
        stack.push("a");
        stack.push("b");

        assertEquals("b", stack.pop());
        assertEquals("a", stack.pop());
    }

    @Test
    public void testPeek() {
        ThreadLocalStack<String> stack = new ThreadLocalStack<>();
        assertNull(stack.peek());

        stack.push("a");
        assertEquals("a", stack.peek());
        assertEquals("a", stack.peek());

        stack.push("b");
        assertEquals("b", stack.peek());
    }

    @Test
    public void testPopEmptyThrows() {
        ThreadLocalStack<String> stack = new ThreadLocalStack<>();
        assertThrows(IllegalStateException.class, stack::pop);
    }

    @Test
    public void testPushPopCleanup() {
        ThreadLocalStack<String> stack = new ThreadLocalStack<>();
        stack.push("a");
        assertEquals("a", stack.pop());
        assertNull(stack.peek());
    }

    @Test
    public void testMultiplePushPop() {
        ThreadLocalStack<Integer> stack = new ThreadLocalStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);

        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertNull(stack.peek());
    }
}
