package com.walmartlabs.concord.runtime.v2.sdk;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NoopVariablesTest {

    @Test
    public void testGetReturnsNull() {
        assertNull(new NoopVariables().get("anything"));
    }

    @Test
    public void testHasReturnsFalse() {
        assertFalse(new NoopVariables().has("anything"));
    }

    @Test
    public void testToMapReturnsEmpty() {
        var map = new NoopVariables().toMap();

        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testSetAlwaysThrows() {
        var v = new NoopVariables();

        assertThrows(IllegalStateException.class, () -> v.set("k", "v"));
    }

    @Test
    public void testDefaultMethodsBehaveAsForMissing() {
        var v = new NoopVariables();

        assertNull(v.getString("k"));
        assertEquals("def", v.getString("k", "def"));
        assertEquals(0, v.getInt("k", 0));
        assertEquals(0L, v.getLong("k", 0L));
        assertFalse(v.getBoolean("k", false));
        assertNull(v.getUUID("k"));
        assertNull(v.getList("k", null));
        assertNull(v.getMap("k", null));
        assertNull(v.getCollection("k", null));
    }
}
