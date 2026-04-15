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

public class LogSegmentStatusTest {

    @Test
    void testRunningId() {
        assertEquals(0, LogSegmentStatus.RUNNING.id());
    }

    @Test
    void testOkId() {
        assertEquals(1, LogSegmentStatus.OK.id());
    }

    @Test
    void testSuspendedId() {
        assertEquals(2, LogSegmentStatus.SUSPENDED.id());
    }

    @Test
    void testErrorId() {
        assertEquals(3, LogSegmentStatus.ERROR.id());
    }

    @Test
    void testFromIdRunning() {
        assertEquals(LogSegmentStatus.RUNNING, LogSegmentStatus.fromId(0));
    }

    @Test
    void testFromIdOk() {
        assertEquals(LogSegmentStatus.OK, LogSegmentStatus.fromId(1));
    }

    @Test
    void testFromIdSuspended() {
        assertEquals(LogSegmentStatus.SUSPENDED, LogSegmentStatus.fromId(2));
    }

    @Test
    void testFromIdError() {
        assertEquals(LogSegmentStatus.ERROR, LogSegmentStatus.fromId(3));
    }

    @Test
    void testFromIdUnknownThrows() {
        assertThrows(IllegalArgumentException.class, () -> LogSegmentStatus.fromId(99));
    }

    @Test
    void testFromIdNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> LogSegmentStatus.fromId(-1));
    }

    @Test
    void testAllValuesExist() {
        assertEquals(4, LogSegmentStatus.values().length);
    }
}
