package com.walmartlabs.concord.common;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2024 Walmart Inc.
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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class MemoSupplierTest {

    @Test
    public void testMemoizedValueReturnedOnFirstCall() {
        Supplier<String> memo = MemoSupplier.memo(() -> "hello");
        assertEquals("hello", memo.get());
    }

    @Test
    public void testDelegateCalledOnlyOnce() {
        AtomicInteger counter = new AtomicInteger(0);
        Supplier<Integer> memo = MemoSupplier.memo(() -> counter.incrementAndGet());

        assertEquals(1, memo.get());
        assertEquals(1, memo.get());
        assertEquals(1, memo.get());
        assertEquals(1, counter.get());
    }

    @Test
    public void testMemoizedNullValue() {
        AtomicInteger counter = new AtomicInteger(0);
        Supplier<String> memo = MemoSupplier.memo(() -> {
            counter.incrementAndGet();
            return null;
        });

        assertNull(memo.get());
        assertNull(memo.get());
        assertEquals(1, counter.get());
    }

    @Test
    public void testConstructorDirectly() {
        AtomicInteger counter = new AtomicInteger(0);
        MemoSupplier<String> memo = new MemoSupplier<>(() -> {
            counter.incrementAndGet();
            return "value";
        });

        assertEquals("value", memo.get());
        assertEquals("value", memo.get());
        assertEquals(1, counter.get());
    }
}
