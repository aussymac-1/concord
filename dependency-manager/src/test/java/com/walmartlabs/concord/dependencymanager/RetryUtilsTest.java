package com.walmartlabs.concord.dependencymanager;

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

import static org.junit.jupiter.api.Assertions.*;

public class RetryUtilsTest {

    @Test
    public void testSucceedsFirstTry() throws Exception {
        AtomicInteger tryCount = new AtomicInteger(0);

        String result = RetryUtils.withRetry(3, 1,
                () -> {
                    tryCount.incrementAndGet();
                    return "success";
                },
                e -> true,
                (t, r, i, e) -> {});

        assertEquals("success", result);
        assertEquals(1, tryCount.get());
    }

    @Test
    public void testRetriesAndSucceeds() throws Exception {
        AtomicInteger tryCount = new AtomicInteger(0);

        String result = RetryUtils.withRetry(3, 1,
                () -> {
                    if (tryCount.incrementAndGet() < 3) {
                        throw new RuntimeException("fail");
                    }
                    return "success";
                },
                e -> true,
                (t, r, i, e) -> {});

        assertEquals("success", result);
        assertEquals(3, tryCount.get());
    }

    @Test
    public void testRetriesAndFails() {
        AtomicInteger tryCount = new AtomicInteger(0);

        assertThrows(RuntimeException.class, () ->
                RetryUtils.withRetry(2, 1,
                        () -> {
                            tryCount.incrementAndGet();
                            throw new RuntimeException("always fails");
                        },
                        e -> true,
                        (t, r, i, e) -> {}));

        assertEquals(2, tryCount.get());
    }

    @Test
    public void testNonRetryableException() {
        AtomicInteger tryCount = new AtomicInteger(0);

        assertThrows(RuntimeException.class, () ->
                RetryUtils.withRetry(3, 1,
                        () -> {
                            tryCount.incrementAndGet();
                            throw new RuntimeException("non-retryable");
                        },
                        e -> false,
                        (t, r, i, e) -> {}));

        assertEquals(1, tryCount.get());
    }

    @Test
    public void testListenerCalled() throws Exception {
        AtomicInteger listenerCallCount = new AtomicInteger(0);
        AtomicInteger tryCount = new AtomicInteger(0);

        RetryUtils.withRetry(3, 1,
                () -> {
                    if (tryCount.incrementAndGet() < 3) {
                        throw new RuntimeException("fail");
                    }
                    return "done";
                },
                e -> true,
                (t, r, i, e) -> listenerCallCount.incrementAndGet());

        assertEquals(2, listenerCallCount.get());
    }

    @Test
    public void testSingleRetryCount() throws Exception {
        AtomicInteger tryCount = new AtomicInteger(0);

        String result = RetryUtils.withRetry(1, 1,
                () -> {
                    tryCount.incrementAndGet();
                    return "ok";
                },
                e -> true,
                (t, r, i, e) -> {});

        assertEquals("ok", result);
        assertEquals(1, tryCount.get());
    }
}
