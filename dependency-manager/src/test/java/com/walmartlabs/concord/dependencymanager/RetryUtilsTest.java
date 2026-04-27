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
    public void testSucceedsOnFirstTry() throws Exception {
        var result = RetryUtils.withRetry(3, 0, () -> "ok",
                e -> true, (tryCount, retryCount, interval, e) -> {});
        assertEquals("ok", result);
    }

    @Test
    public void testRetriesAndSucceeds() throws Exception {
        var counter = new AtomicInteger(0);
        var result = RetryUtils.withRetry(3, 0,
                () -> {
                    if (counter.incrementAndGet() < 3) {
                        throw new RuntimeException("fail");
                    }
                    return "success";
                },
                e -> true,
                (tryCount, retryCount, interval, e) -> {});
        assertEquals("success", result);
        assertEquals(3, counter.get());
    }

    @Test
    public void testExhaustsRetriesAndThrows() {
        var counter = new AtomicInteger(0);
        assertThrows(RuntimeException.class, () ->
                RetryUtils.withRetry(2, 0,
                        () -> {
                            counter.incrementAndGet();
                            throw new RuntimeException("always fail");
                        },
                        e -> true,
                        (tryCount, retryCount, interval, e) -> {}));
        assertEquals(2, counter.get());
    }

    @Test
    public void testNonRetryableExceptionThrowsImmediately() {
        var counter = new AtomicInteger(0);
        assertThrows(IllegalArgumentException.class, () ->
                RetryUtils.withRetry(5, 0,
                        () -> {
                            counter.incrementAndGet();
                            throw new IllegalArgumentException("non-retryable");
                        },
                        e -> false,
                        (tryCount, retryCount, interval, e) -> {}));
        assertEquals(1, counter.get());
    }

    @Test
    public void testListenerReceivesCorrectArgs() throws Exception {
        var lastTryCount = new AtomicInteger(0);
        var callCount = new AtomicInteger(0);

        RetryUtils.withRetry(3, 10,
                () -> {
                    if (callCount.get() < 2) {
                        throw new RuntimeException("fail");
                    }
                    return "ok";
                },
                e -> true,
                (tryCount, retryCount, interval, e) -> {
                    lastTryCount.set(tryCount);
                    callCount.incrementAndGet();
                    assertEquals(3, retryCount);
                    assertEquals(10, interval);
                    assertNotNull(e);
                });
        assertEquals(2, lastTryCount.get());
    }
}
