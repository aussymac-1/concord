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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

class MemoSupplierTest {

    @Test
    void memoizedSupplierCallsDelegateOnlyOnce() {
        AtomicInteger counter = new AtomicInteger(0);
        Supplier<String> memo = MemoSupplier.memo(() -> {
            counter.incrementAndGet();
            return "result";
        });

        assertThat(memo.get()).isEqualTo("result");
        assertThat(memo.get()).isEqualTo("result");
        assertThat(memo.get()).isEqualTo("result");
        assertThat(counter.get()).isEqualTo(1);
    }

    @Test
    void memoizedSupplierCachesNullValue() {
        AtomicInteger counter = new AtomicInteger(0);
        Supplier<String> memo = MemoSupplier.memo(() -> {
            counter.incrementAndGet();
            return null;
        });

        assertThat(memo.get()).isNull();
        assertThat(memo.get()).isNull();
        assertThat(counter.get()).isEqualTo(1);
    }

    @Test
    void constructorCreatesWorkingInstance() {
        MemoSupplier<Integer> supplier = new MemoSupplier<>(() -> 42);
        assertThat(supplier.get()).isEqualTo(42);
        assertThat(supplier.get()).isEqualTo(42);
    }
}
