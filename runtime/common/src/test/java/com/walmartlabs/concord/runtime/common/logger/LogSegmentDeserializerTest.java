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

public class LogSegmentDeserializerTest {

    @Test
    void testDeserializeRunning() {
        assertEquals(LogSegmentStatus.RUNNING, LogSegmentDeserializer.deserializeStatus("0"));
    }

    @Test
    void testDeserializeOk() {
        assertEquals(LogSegmentStatus.OK, LogSegmentDeserializer.deserializeStatus("1"));
    }

    @Test
    void testDeserializeSuspended() {
        assertEquals(LogSegmentStatus.SUSPENDED, LogSegmentDeserializer.deserializeStatus("2"));
    }

    @Test
    void testDeserializeError() {
        assertEquals(LogSegmentStatus.ERROR, LogSegmentDeserializer.deserializeStatus("3"));
    }

    @Test
    void testDeserializeInvalidThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> LogSegmentDeserializer.deserializeStatus("99"));
    }

    @Test
    void testDeserializeNonNumericThrows() {
        assertThrows(NumberFormatException.class,
                () -> LogSegmentDeserializer.deserializeStatus("abc"));
    }
}
