package com.walmartlabs.concord.server.sdk;

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

class ProcessStatusTest {

    @Test
    void containsAllExpectedStatuses() {
        assertThat(ProcessStatus.values())
                .containsExactly(
                        ProcessStatus.NEW,
                        ProcessStatus.PREPARING,
                        ProcessStatus.ENQUEUED,
                        ProcessStatus.WAITING,
                        ProcessStatus.STARTING,
                        ProcessStatus.RUNNING,
                        ProcessStatus.SUSPENDED,
                        ProcessStatus.RESUMING,
                        ProcessStatus.FINISHED,
                        ProcessStatus.FAILED,
                        ProcessStatus.CANCELLED,
                        ProcessStatus.TIMED_OUT);
    }

    @Test
    void valueOfReturnsMatchingEnum() {
        assertThat(ProcessStatus.valueOf("RUNNING")).isEqualTo(ProcessStatus.RUNNING);
        assertThat(ProcessStatus.valueOf("FINISHED")).isEqualTo(ProcessStatus.FINISHED);
        assertThat(ProcessStatus.valueOf("TIMED_OUT")).isEqualTo(ProcessStatus.TIMED_OUT);
    }

    @Test
    void valueOfThrowsForUnknownName() {
        assertThatThrownBy(() -> ProcessStatus.valueOf("UNKNOWN"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void statusNameMatchesEnumName() {
        assertThat(ProcessStatus.NEW.name()).isEqualTo("NEW");
        assertThat(ProcessStatus.CANCELLED.name()).isEqualTo("CANCELLED");
    }
}
