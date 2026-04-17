package com.walmartlabs.concord.dependencymanager;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RetryUtilsTest {

    @Test
    void successOnFirstAttempt() throws Exception {
        String result = RetryUtils.withRetry(3, 1, () -> "ok",
                e -> true, (t, r, i, e) -> {});
        assertThat(result).isEqualTo("ok");
    }

    @Test
    void retriesOnFailureThenSucceeds() throws Exception {
        AtomicInteger attempts = new AtomicInteger(0);
        String result = RetryUtils.withRetry(3, 1, () -> {
            if (attempts.incrementAndGet() < 3) {
                throw new RuntimeException("fail");
            }
            return "ok";
        }, e -> true, (t, r, i, e) -> {});
        assertThat(result).isEqualTo("ok");
        assertThat(attempts.get()).isEqualTo(3);
    }

    @Test
    void throwsAfterExhaustingRetries() {
        assertThatThrownBy(() -> RetryUtils.withRetry(2, 1, () -> {
            throw new RuntimeException("always fail");
        }, e -> true, (t, r, i, e) -> {}))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("always fail");
    }

    @Test
    void throwsImmediatelyWhenStrategyDeniesRetry() {
        assertThatThrownBy(() -> RetryUtils.withRetry(3, 1, () -> {
            throw new RuntimeException("no retry");
        }, e -> false, (t, r, i, e) -> {}))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("no retry");
    }

    @Test
    void listenerIsCalledOnEachRetry() throws Exception {
        AtomicInteger listenerCalls = new AtomicInteger(0);
        AtomicInteger attempts = new AtomicInteger(0);
        RetryUtils.withRetry(3, 1, () -> {
            if (attempts.incrementAndGet() < 3) {
                throw new RuntimeException("fail");
            }
            return "ok";
        }, e -> true, (t, r, i, e) -> listenerCalls.incrementAndGet());
        assertThat(listenerCalls.get()).isEqualTo(2);
    }
}
