package com.walmartlabs.concord.runtime.common.logger;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LogSegmentStatusTest {

    @Test
    void runningStatusHasId0() {
        assertThat(LogSegmentStatus.RUNNING.id()).isZero();
    }

    @Test
    void okStatusHasId1() {
        assertThat(LogSegmentStatus.OK.id()).isEqualTo(1);
    }

    @Test
    void suspendedStatusHasId2() {
        assertThat(LogSegmentStatus.SUSPENDED.id()).isEqualTo(2);
    }

    @Test
    void errorStatusHasId3() {
        assertThat(LogSegmentStatus.ERROR.id()).isEqualTo(3);
    }

    @Test
    void fromIdReturnsCorrectStatus() {
        assertThat(LogSegmentStatus.fromId(0)).isEqualTo(LogSegmentStatus.RUNNING);
        assertThat(LogSegmentStatus.fromId(1)).isEqualTo(LogSegmentStatus.OK);
        assertThat(LogSegmentStatus.fromId(2)).isEqualTo(LogSegmentStatus.SUSPENDED);
        assertThat(LogSegmentStatus.fromId(3)).isEqualTo(LogSegmentStatus.ERROR);
    }

    @Test
    void fromIdThrowsOnUnknownId() {
        assertThatThrownBy(() -> LogSegmentStatus.fromId(99))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown log segment status");
    }

    @Test
    void allValuesHaveUniqueIds() {
        LogSegmentStatus[] values = LogSegmentStatus.values();
        long distinctIds = java.util.Arrays.stream(values)
                .mapToInt(LogSegmentStatus::id)
                .distinct()
                .count();
        assertThat(distinctIds).isEqualTo(values.length);
    }
}
