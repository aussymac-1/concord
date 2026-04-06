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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MapBackedVariablesTest {

    @Test
    void testGet() {
        var vars = new MapBackedVariables(Map.of("key", "value"));
        assertEquals("value", vars.get("key"));
    }

    @Test
    void testGetMissing() {
        var vars = new MapBackedVariables(Map.of("key", "value"));
        assertNull(vars.get("missing"));
    }

    @Test
    void testHas() {
        var vars = new MapBackedVariables(Map.of("key", "value"));
        assertTrue(vars.has("key"));
        assertFalse(vars.has("missing"));
    }

    @Test
    void testToMap() {
        var delegate = Map.<String, Object>of("k1", "v1", "k2", "v2");
        var vars = new MapBackedVariables(delegate);
        assertEquals(delegate, vars.toMap());
    }

    @Test
    void testSetThrows() {
        var vars = new MapBackedVariables(Map.of("key", "value"));
        assertThrows(IllegalStateException.class, () -> vars.set("key", "newValue"));
    }

    @Test
    void testNullDelegate() {
        var vars = new MapBackedVariables(null);
        assertNull(vars.get("key"));
        assertFalse(vars.has("key"));
        assertTrue(vars.toMap().isEmpty());
    }

    @Test
    void testGetString() {
        var vars = new MapBackedVariables(Map.of("name", "alice"));
        assertEquals("alice", vars.getString("name"));
        assertNull(vars.getString("missing"));
    }

    @Test
    void testGetStringWithDefault() {
        var vars = new MapBackedVariables(Map.of("name", "alice"));
        assertEquals("alice", vars.getString("name", "default"));
        assertEquals("default", vars.getString("missing", "default"));
    }

    @Test
    void testAssertString() {
        var vars = new MapBackedVariables(Map.of("name", "alice"));
        assertEquals("alice", vars.assertString("name"));
    }

    @Test
    void testAssertStringMissing() {
        var vars = new MapBackedVariables(Collections.emptyMap());
        assertThrows(IllegalArgumentException.class, () -> vars.assertString("name"));
    }

    @Test
    void testGetBoolean() {
        var vars = new MapBackedVariables(Map.of("flag", true));
        assertTrue(vars.getBoolean("flag", false));
        assertFalse(vars.getBoolean("missing", false));
    }

    @Test
    void testGetInt() {
        var vars = new MapBackedVariables(Map.of("count", 42));
        assertEquals(42, vars.getInt("count", 0));
        assertEquals(0, vars.getInt("missing", 0));
    }

    @Test
    void testGetLong() {
        var vars = new MapBackedVariables(Map.of("bigCount", 100L));
        assertEquals(100L, vars.getLong("bigCount", 0L));
    }

    @Test
    void testGetUUID() {
        var id = UUID.randomUUID();
        var vars = new MapBackedVariables(Map.of("id", id.toString()));
        assertEquals(id, vars.getUUID("id"));
    }

    @Test
    void testGetUUIDAsUUIDObject() {
        var id = UUID.randomUUID();
        var vars = new MapBackedVariables(Map.of("id", id));
        assertEquals(id, vars.getUUID("id"));
    }

    @Test
    void testGetUUIDNull() {
        var vars = new MapBackedVariables(Collections.emptyMap());
        assertNull(vars.getUUID("id"));
    }

    @Test
    void testGetUUIDInvalidType() {
        var vars = new MapBackedVariables(Map.of("id", 123));
        assertThrows(IllegalArgumentException.class, () -> vars.getUUID("id"));
    }

    @Test
    void testAssertUUID() {
        var id = UUID.randomUUID();
        var vars = new MapBackedVariables(Map.of("id", id));
        assertEquals(id, vars.assertUUID("id"));
    }

    @Test
    void testAssertUUIDMissing() {
        var vars = new MapBackedVariables(Collections.emptyMap());
        assertThrows(IllegalArgumentException.class, () -> vars.assertUUID("id"));
    }

    @Test
    void testGetCollection() {
        var vars = new MapBackedVariables(Map.of("items", List.of("a", "b")));
        var items = vars.getCollection("items", null);
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    void testGetMap() {
        var vars = new MapBackedVariables(Map.of("nested", Map.of("k", "v")));
        var nested = vars.getMap("nested", null);
        assertNotNull(nested);
        assertEquals("v", nested.get("k"));
    }

    @Test
    void testGetList() {
        var vars = new MapBackedVariables(Map.of("items", List.of(1, 2, 3)));
        var items = vars.getList("items", null);
        assertNotNull(items);
        assertEquals(3, items.size());
    }

    @Test
    void testGetTypeMismatch() {
        var vars = new MapBackedVariables(Map.of("key", 123));
        assertThrows(IllegalArgumentException.class, () -> vars.getString("key"));
    }

    @Test
    void testAssertVariableWithMessage() {
        var vars = new MapBackedVariables(Collections.emptyMap());
        var ex = assertThrows(IllegalArgumentException.class,
                () -> vars.assertString("Custom message", "missing"));
        assertEquals("Custom message", ex.getMessage());
    }
}
