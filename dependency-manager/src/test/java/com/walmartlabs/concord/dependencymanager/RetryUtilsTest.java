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
    public void testReturnsValueOnFirstSuccess() throws Exception {
        var calls = new AtomicInteger();
        var notifications = new AtomicInteger();

        var result = RetryUtils.withRetry(3, 0, () -> {
            calls.incrementAndGet();
            return "ok";
        }, e -> true, (t, r, i, e) -> notifications.incrementAndGet());

        assertEquals("ok", result);
        assertEquals(1, calls.get());
        assertEquals(0, notifications.get());
    }

    @Test
    public void testRetriesUntilSuccess() throws Exception {
        var calls = new AtomicInteger();
        var notifications = new AtomicInteger();

        var result = RetryUtils.withRetry(3, 0, () -> {
            int n = calls.incrementAndGet();
            if (n < 3) {
                throw new RuntimeException("fail");
            }
            return n;
        }, e -> true, (t, r, i, e) -> notifications.incrementAndGet());

        assertEquals(3, result);
        assertEquals(3, calls.get());
        assertEquals(2, notifications.get());
    }

    @Test
    public void testNonRetryableImmediatelyThrows() {
        var calls = new AtomicInteger();
        var ex = assertThrows(IllegalStateException.class, () -> RetryUtils.withRetry(3, 0, () -> {
            calls.incrementAndGet();
            throw new IllegalStateException("hard fail");
        }, e -> false, (t, r, i, e) -> {}));
        assertEquals("hard fail", ex.getMessage());
        assertEquals(1, calls.get());
    }

    @Test
    public void testThrowsLastExceptionWhenExceeded() {
        var calls = new AtomicInteger();
        var ex = assertThrows(RuntimeException.class, () -> RetryUtils.withRetry(2, 0, () -> {
            calls.incrementAndGet();
            throw new RuntimeException("kaboom");
        }, e -> true, (t, r, i, e) -> {}));

        assertEquals("kaboom", ex.getMessage());
        assertEquals(2, calls.get());
    }

    @Test
    public void testInterruptedThreadStopsRetrying() throws Exception {
        // Pre-interrupt the thread; loop should exit via the isInterrupted check
        // before the first call is attempted.
        Thread.currentThread().interrupt();
        try {
            var result = RetryUtils.withRetry(3, 0, () -> "should-not-run",
                    e -> true, (t, r, i, e) -> {});
            assertNull(result);
        } finally {
            // reset interrupt flag so other tests aren't affected
            Thread.interrupted();
        }
    }
}
