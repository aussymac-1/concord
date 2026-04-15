package com.walmartlabs.concord.agent.logging;

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

import com.walmartlabs.concord.runtime.common.logger.LogSegmentStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogSegmentStatsTest {

    @Test
    void testAllFields() {
        LogSegmentStats stats = new LogSegmentStats(LogSegmentStatus.OK, 5, 3);
        assertEquals(LogSegmentStatus.OK, stats.status());
        assertEquals(5, stats.errors());
        assertEquals(3, stats.warnings());
    }

    @Test
    void testNullFields() {
        LogSegmentStats stats = new LogSegmentStats(null, null, null);
        assertNull(stats.status());
        assertNull(stats.errors());
        assertNull(stats.warnings());
    }

    @Test
    void testRunningStatus() {
        LogSegmentStats stats = new LogSegmentStats(LogSegmentStatus.RUNNING, 0, 0);
        assertEquals(LogSegmentStatus.RUNNING, stats.status());
        assertEquals(0, stats.errors());
        assertEquals(0, stats.warnings());
    }

    @Test
    void testErrorStatus() {
        LogSegmentStats stats = new LogSegmentStats(LogSegmentStatus.ERROR, 10, 2);
        assertEquals(LogSegmentStatus.ERROR, stats.status());
        assertEquals(10, stats.errors());
        assertEquals(2, stats.warnings());
    }
}
