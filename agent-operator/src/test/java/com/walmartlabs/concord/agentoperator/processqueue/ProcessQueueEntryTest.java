package com.walmartlabs.concord.agentoperator.processqueue;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2019 Walmart Inc.
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProcessQueueEntryTest {

    @Test
    void testConstructorAndGetter() {
        var requirements = Map.<String, Object>of("agent", Map.of("flavor", "default"));
        var entry = new ProcessQueueEntry(requirements);
        assertEquals(requirements, entry.getRequirements());
    }

    @Test
    void testNullRequirements() {
        var entry = new ProcessQueueEntry(null);
        assertNull(entry.getRequirements());
    }

    @Test
    void testToString() {
        var entry = new ProcessQueueEntry(Map.of("key", "value"));
        var str = entry.toString();
        assertTrue(str.contains("ProcessQueueEntry"));
        assertTrue(str.contains("key"));
    }
}
