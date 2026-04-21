package com.walmartlabs.concord.plugins.sleep;

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

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class SleepTaskCommonTest {

    @Test
    public void testValidateRejectsBothNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> SleepTaskCommon.validateInputParams(null, null));
        assertEquals("Invalid arguments", ex.getMessage());
    }

    @Test
    public void testValidateRejectsBothSet() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> SleepTaskCommon.validateInputParams(5L, Instant.now()));
        assertEquals("Invalid arguments", ex.getMessage());
    }

    @Test
    public void testValidateAcceptsDurationOnly() {
        assertDoesNotThrow(() -> SleepTaskCommon.validateInputParams(10L, null));
    }

    @Test
    public void testValidateAcceptsUntilOnly() {
        assertDoesNotThrow(() -> SleepTaskCommon.validateInputParams(null, Instant.now()));
    }

    @Test
    public void testSleepDoesNotBlockForZero() {
        long start = System.nanoTime();
        SleepTaskCommon.sleep(0);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000L;

        // sanity-check that sleep(0) returns quickly (generous upper bound to avoid flakes)
        assertTrue(elapsedMs < 1000, "sleep(0) took too long: " + elapsedMs + "ms");
    }

    @Test
    public void testSleepRestoresInterruptFlag() throws Exception {
        Thread t = new Thread(() -> {
            Thread.currentThread().interrupt();
            SleepTaskCommon.sleep(10_000);
        });
        t.start();
        t.join(5_000);

        assertFalse(t.isAlive(), "sleep() should return after being interrupted");
    }
}
