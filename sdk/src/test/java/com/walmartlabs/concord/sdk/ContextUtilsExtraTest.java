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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ContextUtilsExtraTest {

    private static MockContext ctx() {
        return new MockContext(new HashMap<>());
    }

    @Test
    public void testGetUUID() {
        var ctx = ctx();
        var u = UUID.randomUUID();
        ctx.setVariable("u", u);
        ctx.setVariable("s", u.toString());
        assertEquals(u, ContextUtils.getUUID(ctx, "u"));
        assertEquals(u, ContextUtils.getUUID(ctx, "s"));
        assertNull(ContextUtils.getUUID(ctx, "missing"));
        ctx.setVariable("bad", 1);
        assertThrows(IllegalArgumentException.class, () -> ContextUtils.getUUID(ctx, "bad"));
    }

    @Test
    public void testGetStringWithDefault() {
        var ctx = ctx();
        ctx.setVariable("k", "hi");
        assertEquals("hi", ContextUtils.getString(ctx, "k"));
        assertEquals("def", ContextUtils.getString(ctx, "missing", "def"));
        assertNull(ContextUtils.getString(ctx, "missing"));
    }

    @Test
    public void testGetBoolean() {
        var ctx = ctx();
        ctx.setVariable("k", true);
        assertTrue(ContextUtils.getBoolean(ctx, "k", false));
        assertFalse(ContextUtils.getBoolean(ctx, "missing", false));
    }

    @Test
    public void testGetMapAndList() {
        var ctx = ctx();
        ctx.setVariable("m", Map.of("a", 1));
        ctx.setVariable("l", List.of(1, 2, 3));
        assertEquals(Map.of("a", 1), ContextUtils.getMap(ctx, "m"));
        assertEquals(List.of(1, 2, 3), ContextUtils.getList(ctx, "l", List.of()));

        HasKey hk = () -> "m";
        assertEquals(Map.of("a", 1), ContextUtils.getMap(ctx, hk, Map.of()));
    }

    @Test
    public void testAssertString() {
        var ctx = ctx();
        ctx.setVariable("k", "hi");
        assertEquals("hi", ContextUtils.assertString(ctx, "k"));
        assertEquals("hi", ContextUtils.assertString("custom message", ctx, "k"));
        assertThrows(IllegalArgumentException.class, () -> ContextUtils.assertString(ctx, "missing"));
    }

    @Test
    public void testAssertInt() {
        var ctx = ctx();
        ctx.setVariable("k", 123);
        assertEquals(123, ContextUtils.assertInt(ctx, "k"));
        assertThrows(IllegalArgumentException.class, () -> ContextUtils.assertInt(ctx, "missing"));
    }

    @Test
    public void testAssertMapAndList() {
        var ctx = ctx();
        ctx.setVariable("m", Map.of("a", 1));
        ctx.setVariable("l", List.of(1));
        assertEquals(Map.of("a", 1), ContextUtils.assertMap(ctx, "m"));
        assertEquals(List.of(1), ContextUtils.assertList(ctx, "l"));
        assertThrows(IllegalArgumentException.class, () -> ContextUtils.assertMap(ctx, "missing"));
        assertThrows(IllegalArgumentException.class, () -> ContextUtils.assertList(ctx, "missing"));
    }

    @Test
    public void testGetVariableNullCtxReturnsDefault() {
        assertEquals("def", ContextUtils.getVariable((Context) null, "k", "def"));
        assertEquals("def", ContextUtils.getVariable((Context) null, "k", "def", String.class));
    }
}
