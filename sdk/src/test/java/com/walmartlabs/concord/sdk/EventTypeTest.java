package com.walmartlabs.concord.sdk;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTypeTest {

    @Test
    public void testEnumValues() {
        assertEquals(5, EventType.values().length);
        assertEquals(EventType.ANSIBLE, EventType.valueOf("ANSIBLE"));
        assertEquals(EventType.PROCESS_ELEMENT, EventType.valueOf("PROCESS_ELEMENT"));
        assertEquals(EventType.PROCESS_STATUS, EventType.valueOf("PROCESS_STATUS"));
        assertEquals(EventType.PROCESS_WAIT, EventType.valueOf("PROCESS_WAIT"));
        assertEquals(EventType.CHECKPOINT_RESTORE, EventType.valueOf("CHECKPOINT_RESTORE"));
    }
}
