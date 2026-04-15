package com.walmartlabs.concord.runtime.common.logger;

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

import static org.junit.jupiter.api.Assertions.*;

public class LogSegmentSerializerTest {

    @Test
    void testSerializeWithMessage() {
        LogSegmentHeader header = LogSegmentHeader.builder()
                .length(0)
                .segmentId(42L)
                .status(LogSegmentStatus.RUNNING)
                .warnCount(1)
                .errorCount(2)
                .build();

        byte[] result = LogSegmentSerializer.serialize(header, "hello");
        String s = new String(result);
        // Format: |msgLength|segmentId|status|warnings|errors|message
        assertTrue(s.startsWith("|5|42|0|1|2|"));
        assertTrue(s.endsWith("hello"));
    }

    @Test
    void testSerializeWithNullMessage() {
        LogSegmentHeader header = LogSegmentHeader.builder()
                .length(0)
                .segmentId(1L)
                .status(LogSegmentStatus.OK)
                .warnCount(0)
                .errorCount(0)
                .build();

        byte[] result = LogSegmentSerializer.serialize(header, null);
        String s = new String(result);
        assertEquals("|0|1|1|0|0|", s);
    }

    @Test
    void testSerializeWithEmptyMessage() {
        LogSegmentHeader header = LogSegmentHeader.builder()
                .length(0)
                .segmentId(5L)
                .status(LogSegmentStatus.ERROR)
                .warnCount(3)
                .errorCount(7)
                .build();

        byte[] result = LogSegmentSerializer.serialize(header, "");
        String s = new String(result);
        assertEquals("|0|5|3|3|7|", s);
    }

    @Test
    void testSerializeHeader() {
        LogSegmentHeader header = LogSegmentHeader.builder()
                .length(100)
                .segmentId(10L)
                .status(LogSegmentStatus.SUSPENDED)
                .warnCount(0)
                .errorCount(0)
                .build();

        byte[] result = LogSegmentSerializer.serializeHeader(header);
        String s = new String(result);
        assertEquals("|100|10|2|0|0|", s);
    }

    @Test
    void testSerializeHeaderWithCustomLength() {
        LogSegmentHeader header = LogSegmentHeader.builder()
                .length(0)
                .segmentId(99L)
                .status(LogSegmentStatus.OK)
                .warnCount(5)
                .errorCount(10)
                .build();

        byte[] result = LogSegmentSerializer.serializeHeader(header, 256);
        String s = new String(result);
        assertEquals("|256|99|1|5|10|", s);
    }
}
