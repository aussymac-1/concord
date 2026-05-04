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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MockContextTest {

    @Test
    public void testVariableLifecycle() {
        var delegate = new HashMap<String, Object>();
        var ctx = new MockContext(delegate);

        ctx.setVariable("a", 1);
        assertEquals(1, ctx.getVariable("a"));
        assertTrue(ctx.getVariableNames().contains("a"));

        ctx.removeVariable("a");
        assertNull(ctx.getVariable("a"));
        assertFalse(ctx.getVariableNames().contains("a"));
    }

    @Test
    public void testToMapReturnsCopy() {
        var delegate = new HashMap<String, Object>();
        delegate.put("k", "v");
        var ctx = new MockContext(delegate);

        var copy = ctx.toMap();
        copy.put("x", "y");

        assertFalse(delegate.containsKey("x"));
    }

    @Test
    public void testCurrentFlowName() {
        assertEquals("n/a", new MockContext(new HashMap<>()).getCurrentFlowName());
    }

    @Test
    public void testProtectedAndOtherOpsThrow() {
        var ctx = new MockContext(new HashMap<>());
        assertThrows(IllegalArgumentException.class, () -> ctx.setProtectedVariable("k", "v"));
        assertThrows(IllegalArgumentException.class, () -> ctx.getProtectedVariable("k"));
        assertThrows(IllegalStateException.class, () -> ctx.eval("expr", String.class));
        assertThrows(IllegalStateException.class, () -> ctx.interpolate("v"));
        assertThrows(IllegalStateException.class, () -> ctx.interpolate("v", Map.of()));
        assertThrows(IllegalStateException.class, () -> ctx.suspend("e"));
        assertThrows(IllegalStateException.class, () -> ctx.suspend("e", null, false));
        assertThrows(IllegalStateException.class, ctx::getProcessDefinitionId);
        assertThrows(IllegalStateException.class, ctx::getElementId);
        assertThrows(IllegalStateException.class, () -> ctx.form("f", Map.of()));
    }
}
