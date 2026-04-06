package com.walmartlabs.concord.runtime.v2.sdk;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2020 Walmart Inc.
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

class NoopVariablesTest {

    @Test
    void testGetReturnsNull() {
        var vars = new NoopVariables();
        assertNull(vars.get("anything"));
    }

    @Test
    void testHasReturnsFalse() {
        var vars = new NoopVariables();
        assertFalse(vars.has("anything"));
    }

    @Test
    void testSetThrows() {
        var vars = new NoopVariables();
        assertThrows(IllegalStateException.class, () -> vars.set("key", "value"));
    }

    @Test
    void testToMapReturnsEmpty() {
        var vars = new NoopVariables();
        assertTrue(vars.toMap().isEmpty());
    }

    @Test
    void testGetStringReturnsNull() {
        var vars = new NoopVariables();
        assertNull(vars.getString("key"));
    }

    @Test
    void testGetStringWithDefault() {
        var vars = new NoopVariables();
        assertEquals("default", vars.getString("key", "default"));
    }

    @Test
    void testGetBooleanReturnsDefault() {
        var vars = new NoopVariables();
        assertTrue(vars.getBoolean("key", true));
        assertFalse(vars.getBoolean("key", false));
    }

    @Test
    void testGetIntReturnsDefault() {
        var vars = new NoopVariables();
        assertEquals(42, vars.getInt("key", 42));
    }

    @Test
    void testAssertStringThrows() {
        var vars = new NoopVariables();
        assertThrows(IllegalArgumentException.class, () -> vars.assertString("key"));
    }
}
