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

class LogSegmentDeserializerTest {

    @Test
    void deserializeRunningStatus() {
        LogSegmentStatus result = LogSegmentDeserializer.deserializeStatus("0");
        assertThat(result).isEqualTo(LogSegmentStatus.RUNNING);
    }

    @Test
    void deserializeOkStatus() {
        LogSegmentStatus result = LogSegmentDeserializer.deserializeStatus("1");
        assertThat(result).isEqualTo(LogSegmentStatus.OK);
    }

    @Test
    void deserializeErrorStatus() {
        LogSegmentStatus result = LogSegmentDeserializer.deserializeStatus("3");
        assertThat(result).isEqualTo(LogSegmentStatus.ERROR);
    }

    @Test
    void deserializeInvalidStatusThrows() {
        assertThatThrownBy(() -> LogSegmentDeserializer.deserializeStatus("99"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deserializeNonNumericThrows() {
        assertThatThrownBy(() -> LogSegmentDeserializer.deserializeStatus("abc"))
                .isInstanceOf(NumberFormatException.class);
    }
}
