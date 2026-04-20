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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NoopVariablesTest {

    @Test
    public void testGetAlwaysReturnsNull() {
        NoopVariables v = new NoopVariables();

        assertNull(v.get("anything"));
        assertNull(v.get(""));
    }

    @Test
    public void testHasAlwaysReturnsFalse() {
        NoopVariables v = new NoopVariables();

        assertFalse(v.has("anything"));
        assertFalse(v.has(""));
    }

    @Test
    public void testSetIsNotSupported() {
        NoopVariables v = new NoopVariables();

        assertThrows(IllegalStateException.class, () -> v.set("k", "v"));
    }

    @Test
    public void testToMapIsEmpty() {
        NoopVariables v = new NoopVariables();

        assertTrue(v.toMap().isEmpty());
    }

    @Test
    public void testDefaultHelpersReturnDefaults() {
        NoopVariables v = new NoopVariables();

        assertNull(v.getString("k"));
        assertTrue(v.getBoolean("k", true));
        assertFalse(v.getBoolean("k", false));
        assertThrows(IllegalArgumentException.class, () -> v.assertString("k"));
    }
}
