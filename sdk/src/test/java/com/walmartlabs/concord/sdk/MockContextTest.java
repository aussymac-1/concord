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

public class MockContextTest {

    @Test
    public void testGetSetVariable() {
        var ctx = new MockContext(new HashMap<>());
        ctx.setVariable("key", "value");
        assertEquals("value", ctx.getVariable("key"));
    }

    @Test
    public void testRemoveVariable() {
        var delegate = new HashMap<String, Object>();
        delegate.put("key", "value");
        var ctx = new MockContext(delegate);

        ctx.removeVariable("key");
        assertNull(ctx.getVariable("key"));
    }

    @Test
    public void testGetVariableNames() {
        var delegate = new HashMap<String, Object>();
        delegate.put("a", 1);
        delegate.put("b", 2);
        var ctx = new MockContext(delegate);

        var names = ctx.getVariableNames();
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
        assertEquals(2, names.size());
    }

    @Test
    public void testToMapReturnsCopy() {
        var delegate = new HashMap<String, Object>();
        delegate.put("k", "v");
        var ctx = new MockContext(delegate);

        var copy = ctx.toMap();
        assertEquals(delegate, copy);

        copy.put("new", "value");
        assertNull(ctx.getVariable("new"));
    }

    @Test
    public void testGetCurrentFlowName() {
        var ctx = new MockContext(new HashMap<>());
        assertEquals("n/a", ctx.getCurrentFlowName());
    }

    @Test
    public void testSetProtectedVariableThrows() {
        var ctx = new MockContext(new HashMap<>());
        assertThrows(IllegalArgumentException.class, () -> ctx.setProtectedVariable("k", "v"));
    }

    @Test
    public void testGetProtectedVariableThrows() {
        var ctx = new MockContext(new HashMap<>());
        assertThrows(IllegalArgumentException.class, () -> ctx.getProtectedVariable("k"));
    }

    @Test
    public void testEvalThrows() {
        var ctx = new MockContext(new HashMap<>());
        assertThrows(IllegalStateException.class, () -> ctx.eval("expr", String.class));
    }

    @Test
    public void testInterpolateThrows() {
        var ctx = new MockContext(new HashMap<>());
        assertThrows(IllegalStateException.class, () -> ctx.interpolate("v"));
    }

    @Test
    public void testSuspendThrows() {
        var ctx = new MockContext(new HashMap<>());
        assertThrows(IllegalStateException.class, () -> ctx.suspend("event"));
    }
}
