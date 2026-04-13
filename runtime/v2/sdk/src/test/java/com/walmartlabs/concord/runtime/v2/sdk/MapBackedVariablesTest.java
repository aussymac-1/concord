package com.walmartlabs.concord.runtime.v2.sdk;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2026 Walmart Inc.
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MapBackedVariablesTest {

    @Test
    public void testGet() {
        var delegate = Map.of("key", (Object) "value");
        var vars = new MapBackedVariables(delegate);
        assertEquals("value", vars.get("key"));
    }

    @Test
    public void testGetMissing() {
        var vars = new MapBackedVariables(Map.of());
        assertNull(vars.get("missing"));
    }

    @Test
    public void testHas() {
        var delegate = Map.of("key", (Object) "value");
        var vars = new MapBackedVariables(delegate);
        assertTrue(vars.has("key"));
        assertFalse(vars.has("missing"));
    }

    @Test
    public void testToMap() {
        var delegate = Map.of("a", (Object) 1, "b", (Object) 2);
        var vars = new MapBackedVariables(delegate);
        var result = vars.toMap();
        assertEquals(2, result.size());
        assertEquals(1, result.get("a"));
        assertEquals(2, result.get("b"));
    }

    @Test
    public void testSetThrows() {
        var vars = new MapBackedVariables(Map.of());
        assertThrows(IllegalStateException.class, () -> vars.set("key", "value"));
    }

    @Test
    public void testNullDelegate() {
        var vars = new MapBackedVariables(null);
        assertNull(vars.get("key"));
        assertFalse(vars.has("key"));
        assertTrue(vars.toMap().isEmpty());
    }

    @Test
    public void testUnmodifiable() {
        var delegate = new HashMap<String, Object>();
        delegate.put("key", "value");
        var vars = new MapBackedVariables(delegate);

        assertThrows(UnsupportedOperationException.class, () -> vars.toMap().put("new", "val"));
    }
}
