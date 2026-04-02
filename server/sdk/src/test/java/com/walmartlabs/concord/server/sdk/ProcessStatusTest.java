package com.walmartlabs.concord.server.sdk;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2024 Walmart Inc.
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

public class ProcessStatusTest {

    @Test
    public void testAllStatuses() {
        assertEquals(12, ProcessStatus.values().length);
    }

    @Test
    public void testValueOf() {
        assertEquals(ProcessStatus.NEW, ProcessStatus.valueOf("NEW"));
        assertEquals(ProcessStatus.PREPARING, ProcessStatus.valueOf("PREPARING"));
        assertEquals(ProcessStatus.ENQUEUED, ProcessStatus.valueOf("ENQUEUED"));
        assertEquals(ProcessStatus.WAITING, ProcessStatus.valueOf("WAITING"));
        assertEquals(ProcessStatus.STARTING, ProcessStatus.valueOf("STARTING"));
        assertEquals(ProcessStatus.RUNNING, ProcessStatus.valueOf("RUNNING"));
        assertEquals(ProcessStatus.SUSPENDED, ProcessStatus.valueOf("SUSPENDED"));
        assertEquals(ProcessStatus.RESUMING, ProcessStatus.valueOf("RESUMING"));
        assertEquals(ProcessStatus.FINISHED, ProcessStatus.valueOf("FINISHED"));
        assertEquals(ProcessStatus.FAILED, ProcessStatus.valueOf("FAILED"));
        assertEquals(ProcessStatus.CANCELLED, ProcessStatus.valueOf("CANCELLED"));
        assertEquals(ProcessStatus.TIMED_OUT, ProcessStatus.valueOf("TIMED_OUT"));
    }
}
