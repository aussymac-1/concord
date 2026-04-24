package com.walmartlabs.concord.dependencymanager;

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

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RetryUtilsTest {

    @Test
    public void testSuccessOnFirstTry() throws Exception {
        AtomicInteger calls = new AtomicInteger(0);
        AtomicInteger retries = new AtomicInteger(0);

        String result = RetryUtils.withRetry(3, 1,
                () -> {
                    calls.incrementAndGet();
                    return "ok";
                },
                e -> true,
                (tryCount, retryCount, interval, e) -> retries.incrementAndGet());

        assertEquals("ok", result);
        assertEquals(1, calls.get());
        assertEquals(0, retries.get());
    }

    @Test
    public void testRetriesOnFailureThenSucceeds() throws Exception {
        AtomicInteger calls = new AtomicInteger(0);

        String result = RetryUtils.withRetry(3, 1,
                () -> {
                    int c = calls.incrementAndGet();
                    if (c < 3) {
                        throw new RuntimeException("fail #" + c);
                    }
                    return "success";
                },
                e -> true,
                (tryCount, retryCount, interval, e) -> {});

        assertEquals("success", result);
        assertEquals(3, calls.get());
    }

    @Test
    public void testThrowsAfterMaxRetries() {
        AtomicInteger calls = new AtomicInteger(0);

        Exception thrown = assertThrows(RuntimeException.class,
                () -> RetryUtils.withRetry(2, 1,
                        () -> {
                            calls.incrementAndGet();
                            throw new RuntimeException("always fails");
                        },
                        e -> true,
                        (tryCount, retryCount, interval, e) -> {}));

        assertEquals("always fails", thrown.getMessage());
        assertEquals(2, calls.get());
    }

    @Test
    public void testNonRetryableException() {
        AtomicInteger calls = new AtomicInteger(0);

        Exception thrown = assertThrows(IllegalArgumentException.class,
                () -> RetryUtils.withRetry(5, 1,
                        () -> {
                            calls.incrementAndGet();
                            throw new IllegalArgumentException("bad input");
                        },
                        e -> false,
                        (tryCount, retryCount, interval, e) -> {}));

        assertEquals("bad input", thrown.getMessage());
        assertEquals(1, calls.get());
    }

    @Test
    public void testRetryListenerCalled() throws Exception {
        AtomicInteger listenerCalls = new AtomicInteger(0);

        RetryUtils.withRetry(3, 1,
                () -> {
                    if (listenerCalls.get() < 2) {
                        throw new RuntimeException("retry me");
                    }
                    return "done";
                },
                e -> true,
                (tryCount, retryCount, interval, e) -> listenerCalls.incrementAndGet());

        assertEquals(2, listenerCalls.get());
    }

    @Test
    public void testSelectiveRetryStrategy() {
        AtomicInteger calls = new AtomicInteger(0);

        Exception thrown = assertThrows(IllegalStateException.class,
                () -> RetryUtils.withRetry(5, 1,
                        () -> {
                            int c = calls.incrementAndGet();
                            if (c == 1) {
                                throw new RuntimeException("retryable");
                            }
                            throw new IllegalStateException("not retryable");
                        },
                        e -> e instanceof RuntimeException && !(e instanceof IllegalStateException),
                        (tryCount, retryCount, interval, e) -> {}));

        assertEquals("not retryable", thrown.getMessage());
        assertEquals(2, calls.get());
    }
}
