package com.walmartlabs.concord.sdk;

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

class MockContextTest {

    @Test
    void testGetAndSetVariable() {
        var ctx = new MockContext(new HashMap<>());
        ctx.setVariable("key", "value");
        assertEquals("value", ctx.getVariable("key"));
    }

    @Test
    void testGetVariableMissing() {
        var ctx = new MockContext(new HashMap<>());
        assertNull(ctx.getVariable("missing"));
    }

    @Test
    void testRemoveVariable() {
        var vars = new HashMap<String, Object>();
        vars.put("key", "value");
        var ctx = new MockContext(vars);

        ctx.removeVariable("key");
        assertNull(ctx.getVariable("key"));
    }

    @Test
    void testGetVariableNames() {
        var vars = new HashMap<String, Object>();
        vars.put("a", 1);
        vars.put("b", 2);
        var ctx = new MockContext(vars);

        assertEquals(2, ctx.getVariableNames().size());
        assertTrue(ctx.getVariableNames().contains("a"));
        assertTrue(ctx.getVariableNames().contains("b"));
    }

    @Test
    void testToMapReturnsCopy() {
        var vars = new HashMap<String, Object>();
        vars.put("key", "value");
        var ctx = new MockContext(vars);

        var copy = ctx.toMap();
        assertEquals(vars, copy);

        copy.put("extra", "data");
        assertNull(ctx.getVariable("extra"));
    }

    @Test
    void testSetProtectedVariableThrows() {
        var ctx = new MockContext(new HashMap<>());
        assertThrows(IllegalArgumentException.class,
                () -> ctx.setProtectedVariable("key", "value"));
    }

    @Test
    void testGetProtectedVariableThrows() {
        var ctx = new MockContext(new HashMap<>());
        assertThrows(IllegalArgumentException.class,
                () -> ctx.getProtectedVariable("key"));
    }

    @Test
    void testEvalThrows() {
        var ctx = new MockContext(new HashMap<>());
        assertThrows(IllegalStateException.class,
                () -> ctx.eval("${expr}", String.class));
    }

    @Test
    void testInterpolateThrows() {
        var ctx = new MockContext(new HashMap<>());
        assertThrows(IllegalStateException.class,
                () -> ctx.interpolate("value"));
    }

    @Test
    void testGetCurrentFlowName() {
        var ctx = new MockContext(new HashMap<>());
        assertEquals("n/a", ctx.getCurrentFlowName());
    }
}
