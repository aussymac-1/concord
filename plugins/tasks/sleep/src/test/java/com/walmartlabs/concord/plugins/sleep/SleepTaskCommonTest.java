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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SleepTaskCommonTest {

    @Test
    public void validateInputParamsRequiresEitherValue() {
        assertThrows(IllegalArgumentException.class,
                () -> SleepTaskCommon.validateInputParams(null, null));
    }

    @Test
    public void validateInputParamsRejectsBothValues() {
        assertThrows(IllegalArgumentException.class,
                () -> SleepTaskCommon.validateInputParams(1, Instant.now()));
    }

    @Test
    public void validateInputParamsAllowsDurationOnly() {
        SleepTaskCommon.validateInputParams(1, null); // no throw
    }

    @Test
    public void validateInputParamsAllowsUntilOnly() {
        SleepTaskCommon.validateInputParams(null, Instant.now()); // no throw
    }

    @Test
    public void sleepInterruptsAreSwallowedAndFlagRestored() {
        Thread.currentThread().interrupt();
        try {
            SleepTaskCommon.sleep(100);
            assertTrue(Thread.currentThread().isInterrupted(), "interrupted flag should be preserved");
        } finally {
            Thread.interrupted();
        }
    }

    @Test
    public void sleepForZeroReturnsQuickly() {
        var start = System.nanoTime();
        SleepTaskCommon.sleep(0);
        var elapsedMs = (System.nanoTime() - start) / 1_000_000;
        assertTrue(elapsedMs < 500);
    }

    @Test
    public void executeWithNoDurationOrUntilThrows() {
        var task = new SleepTaskCommon(() -> { throw new AssertionError("supplier should not be invoked"); });
        var params = new TaskParams(Map.of());
        assertThrows(IllegalArgumentException.class, () -> task.execute(params));
    }

    @Test
    public void executeWithPastUntilReturnsSuccessWithoutSleeping() throws Exception {
        var task = new SleepTaskCommon(() -> { throw new AssertionError("supplier not expected"); });
        var pastIso = java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
                java.time.OffsetDateTime.now().minusHours(1));
        var params = new TaskParams(Map.of(Constants.UNTIL_KEY, pastIso));
        var result = task.execute(params);
        assertNotNull(result);
    }

    @Test
    public void executeWithZeroDurationDoesNotSleep() throws Exception {
        var task = new SleepTaskCommon(() -> { throw new AssertionError("supplier not expected"); });
        var params = new TaskParams(Map.of(Constants.DURATION_KEY, 0));
        var start = System.nanoTime();
        task.execute(params);
        var elapsedMs = (System.nanoTime() - start) / 1_000_000;
        assertTrue(elapsedMs < 500);
    }

    @Test
    public void executeWithPastUntilAndSuspendReturnsSuccessWithoutInvokingSuspender() throws Exception {
        var task = new SleepTaskCommon(() -> { throw new AssertionError("supplier not expected for past datetime"); });
        var pastIso = java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
                java.time.OffsetDateTime.now().minusHours(1));
        var params = new TaskParams(Map.of(
                Constants.UNTIL_KEY, pastIso,
                Constants.SUSPEND_KEY, true));
        var result = task.execute(params);
        assertNotNull(result);
    }
}
