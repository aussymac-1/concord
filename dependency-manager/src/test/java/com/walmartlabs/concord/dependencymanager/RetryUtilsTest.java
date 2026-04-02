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
    public void testSuccessOnFirstTry() throws Exception {
        String result = RetryUtils.withRetry(3, 1,
                () -> "ok",
                e -> true,
                (tryCount, retryCount, retryInterval, e) -> {});
        assertEquals("ok", result);
    }

    @Test
    public void testSuccessAfterRetry() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        String result = RetryUtils.withRetry(3, 1,
                () -> {
                    if (counter.incrementAndGet() < 3) {
                        throw new RuntimeException("fail");
                    }
                    return "ok";
                },
                e -> true,
                (tryCount, retryCount, retryInterval, e) -> {});
        assertEquals("ok", result);
    }

    @Test
    public void testExhaustedRetries() {
        assertThrows(RuntimeException.class, () ->
                RetryUtils.withRetry(2, 1,
                        () -> { throw new RuntimeException("always fail"); },
                        e -> true,
                        (tryCount, retryCount, retryInterval, e) -> {}));
    }

    @Test
    public void testNonRetryableException() {
        assertThrows(IllegalStateException.class, () ->
                RetryUtils.withRetry(3, 1,
                        () -> { throw new IllegalStateException("not retryable"); },
                        e -> false,
                        (tryCount, retryCount, retryInterval, e) -> {}));
    }

    @Test
    public void testListenerCalled() throws Exception {
        AtomicInteger listenerCount = new AtomicInteger(0);
        AtomicInteger callCount = new AtomicInteger(0);

        try {
            RetryUtils.withRetry(2, 1,
                    () -> {
                        callCount.incrementAndGet();
                        throw new RuntimeException("fail");
                    },
                    e -> true,
                    (tryCount, retryCount, retryInterval, e) -> listenerCount.incrementAndGet());
        } catch (RuntimeException e) {
            // expected
        }

        assertEquals(2, callCount.get());
        assertEquals(2, listenerCount.get());
    }
}
