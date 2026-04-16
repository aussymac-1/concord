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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThreadLocalStackTest {

    @Test
    void pushAndPopInOrder() {
        ThreadLocalStack<String> stack = new ThreadLocalStack<>();
        stack.push("a");
        stack.push("b");
        stack.push("c");

        assertThat(stack.pop()).isEqualTo("c");
        assertThat(stack.pop()).isEqualTo("b");
        assertThat(stack.pop()).isEqualTo("a");
    }

    @Test
    void peekReturnsTopWithoutRemoving() {
        ThreadLocalStack<Integer> stack = new ThreadLocalStack<>();
        stack.push(1);
        stack.push(2);

        assertThat(stack.peek()).isEqualTo(2);
        assertThat(stack.peek()).isEqualTo(2);
        assertThat(stack.pop()).isEqualTo(2);
    }

    @Test
    void peekReturnsNullOnEmptyStack() {
        ThreadLocalStack<String> stack = new ThreadLocalStack<>();
        assertThat(stack.peek()).isNull();
    }

    @Test
    void popOnEmptyStackThrowsIllegalState() {
        ThreadLocalStack<String> stack = new ThreadLocalStack<>();
        assertThatThrownBy(stack::pop)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Stack is empty");
    }

    @Test
    void stackIsIsolatedPerThread() throws Exception {
        ThreadLocalStack<String> stack = new ThreadLocalStack<>();
        stack.push("main");

        Thread t = new Thread(() -> {
            stack.push("thread");
            assertThat(stack.peek()).isEqualTo("thread");
            stack.pop();
        });
        t.start();
        t.join();

        assertThat(stack.peek()).isEqualTo("main");
    }
}
