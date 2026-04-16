package com.walmartlabs.concord.agent.logging;

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

import com.walmartlabs.concord.runtime.common.logger.LogSegmentStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LogSegmentStatsTest {

    @Test
    void recordWithAllFields() {
        LogSegmentStats stats = new LogSegmentStats(LogSegmentStatus.OK, 3, 1);
        assertThat(stats.status()).isEqualTo(LogSegmentStatus.OK);
        assertThat(stats.errors()).isEqualTo(3);
        assertThat(stats.warnings()).isEqualTo(1);
    }

    @Test
    void recordWithNullFields() {
        LogSegmentStats stats = new LogSegmentStats(null, null, null);
        assertThat(stats.status()).isNull();
        assertThat(stats.errors()).isNull();
        assertThat(stats.warnings()).isNull();
    }

    @Test
    void recordWithRunningStatus() {
        LogSegmentStats stats = new LogSegmentStats(LogSegmentStatus.RUNNING, 0, 0);
        assertThat(stats.status()).isEqualTo(LogSegmentStatus.RUNNING);
        assertThat(stats.errors()).isZero();
        assertThat(stats.warnings()).isZero();
    }

    @Test
    void recordWithErrorStatus() {
        LogSegmentStats stats = new LogSegmentStats(LogSegmentStatus.ERROR, 5, 2);
        assertThat(stats.status()).isEqualTo(LogSegmentStatus.ERROR);
        assertThat(stats.errors()).isEqualTo(5);
        assertThat(stats.warnings()).isEqualTo(2);
    }
}
