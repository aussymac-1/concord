package com.walmartlabs.concord.agentoperator.scheduler;

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

public class EventTest {

    @Test
    void testModifiedType() {
        Event event = new Event(Event.Type.MODIFIED, null);
        assertEquals(Event.Type.MODIFIED, event.getType());
        assertNull(event.getResource());
    }

    @Test
    void testDeletedType() {
        Event event = new Event(Event.Type.DELETED, null);
        assertEquals(Event.Type.DELETED, event.getType());
    }

    @Test
    void testEventTypes() {
        assertEquals(2, Event.Type.values().length);
        assertNotNull(Event.Type.valueOf("MODIFIED"));
        assertNotNull(Event.Type.valueOf("DELETED"));
    }
}
