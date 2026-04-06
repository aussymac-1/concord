package com.walmartlabs.concord.server.sdk;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PartialProcessKeyTest {

    @Test
    void testFrom() {
        var id = UUID.randomUUID();
        var key = PartialProcessKey.from(id);
        assertEquals(id, key.getInstanceId());
    }

    @Test
    void testCreate() {
        var key = PartialProcessKey.create();
        assertNotNull(key.getInstanceId());
    }

    @Test
    void testEquals() {
        var id = UUID.randomUUID();
        var key1 = PartialProcessKey.from(id);
        var key2 = PartialProcessKey.from(id);
        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    void testNotEquals() {
        var key1 = PartialProcessKey.create();
        var key2 = PartialProcessKey.create();
        assertNotEquals(key1, key2);
    }

    @Test
    void testPartOf() {
        var id = UUID.randomUUID();
        var key1 = PartialProcessKey.from(id);
        var key2 = PartialProcessKey.from(id);
        assertTrue(key1.partOf(key2));
    }

    @Test
    void testPartOfDifferent() {
        var key1 = PartialProcessKey.create();
        var key2 = PartialProcessKey.create();
        assertFalse(key1.partOf(key2));
    }

    @Test
    void testToString() {
        var id = UUID.randomUUID();
        var key = PartialProcessKey.from(id);
        assertEquals(id.toString(), key.toString());
    }
}
