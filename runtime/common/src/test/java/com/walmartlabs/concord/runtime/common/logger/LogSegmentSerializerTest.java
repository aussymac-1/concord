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

class LogSegmentSerializerTest {

    @Test
    void serializeWithMessage() {
        LogSegmentHeader header = ImmutableLogSegmentHeader.builder()
                .length(0)
                .segmentId(42L)
                .status(LogSegmentStatus.RUNNING)
                .warnCount(1)
                .errorCount(2)
                .build();

        byte[] result = LogSegmentSerializer.serialize(header, "hello");
        String serialized = new String(result);
        assertThat(serialized).contains("|42|");
        assertThat(serialized).endsWith("hello");
    }

    @Test
    void serializeWithNullMessage() {
        LogSegmentHeader header = ImmutableLogSegmentHeader.builder()
                .length(0)
                .segmentId(1L)
                .status(LogSegmentStatus.OK)
                .warnCount(0)
                .errorCount(0)
                .build();

        byte[] result = LogSegmentSerializer.serialize(header, null);
        String serialized = new String(result);
        assertThat(serialized).startsWith("|0|1|1|0|0|");
    }

    @Test
    void serializeHeaderOnly() {
        LogSegmentHeader header = ImmutableLogSegmentHeader.builder()
                .length(100)
                .segmentId(5L)
                .status(LogSegmentStatus.ERROR)
                .warnCount(3)
                .errorCount(7)
                .build();

        byte[] result = LogSegmentSerializer.serializeHeader(header);
        String serialized = new String(result);
        assertThat(serialized).isEqualTo("|100|5|3|3|7|");
    }

    @Test
    void serializeHeaderWithCustomMessageLength() {
        LogSegmentHeader header = ImmutableLogSegmentHeader.builder()
                .length(0)
                .segmentId(10L)
                .status(LogSegmentStatus.RUNNING)
                .warnCount(0)
                .errorCount(0)
                .build();

        byte[] result = LogSegmentSerializer.serializeHeader(header, 50);
        String serialized = new String(result);
        assertThat(serialized).isEqualTo("|50|10|0|0|0|");
    }
}
