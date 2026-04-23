package com.walmartlabs.concord.plugins.variables;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VariablesTaskCommonTest {

    @Test
    public void concatConcatenatesTwoLists() {
        var result = VariablesTaskCommon.concat(List.of("a", "b"), List.of("c"));
        assertEquals(List.of("a", "b", "c"), result);
    }

    @Test
    public void concatHandlesNullInputs() {
        assertEquals(List.of("x"), VariablesTaskCommon.concat(null, List.of("x")));
        assertEquals(List.of("y"), VariablesTaskCommon.concat(List.of("y"), null));
        assertEquals(Collections.emptyList(), VariablesTaskCommon.concat(null, null));
    }

    @Test
    public void concatAllowsDuplicates() {
        var result = VariablesTaskCommon.concat(List.of("a"), List.of("a"));
        assertEquals(List.of("a", "a"), result);
    }

    @Test
    public void concatResultIsIndependentOfInputs() {
        List<Object> a = new ArrayList<>(List.<Object>of(1));
        List<Object> b = new ArrayList<>(List.<Object>of(2));
        var result = VariablesTaskCommon.concat(a, b);
        a.add(99);
        b.add(100);
        assertEquals(List.<Object>of(1, 2), result);
    }

    @Test
    public void getReturnsVariableValue() {
        var vars = new MapVars(Map.of("foo", "bar"));
        var c = new VariablesTaskCommon(vars, Collections.emptyList());
        assertEquals("bar", c.get("foo", "default"));
    }

    @Test
    public void getReturnsDefaultWhenAbsent() {
        var vars = new MapVars(new HashMap<>());
        var c = new VariablesTaskCommon(vars, Collections.emptyList());
        assertEquals("def", c.get("missing", "def"));
    }

    @Test
    public void getAllowsNullDefault() {
        var vars = new MapVars(new HashMap<>());
        var c = new VariablesTaskCommon(vars, Collections.emptyList());
        assertNull(c.get("missing", null));
    }

    @Test
    public void getResolvesNestedPathsViaEL() {
        var nested = new LinkedHashMap<String, Object>();
        nested.put("inner", "value");
        var vars = new MapVars(Map.of("outer", nested));
        var c = new VariablesTaskCommon(vars, Collections.emptyList());

        assertEquals("value", c.get("outer.inner", "default"));
    }

    @Test
    public void getReturnsDefaultForMissingNestedPath() {
        var vars = new MapVars(new HashMap<>());
        var c = new VariablesTaskCommon(vars, Collections.emptyList());
        assertEquals("fallback", c.get("outer.inner", "fallback"));
    }

    @Test
    public void setTargetKeyFromSource() {
        var backing = new HashMap<String, Object>();
        backing.put("src", "hello");
        var vars = new MapVars(backing);
        var c = new VariablesTaskCommon(vars, Collections.emptyList());

        c.set("tgt", "src", "fallback");
        assertEquals("hello", backing.get("tgt"));
    }

    @Test
    public void setTargetKeyFallsBackToDefaultKey() {
        var backing = new HashMap<String, Object>();
        backing.put("fallbackKey", "fallbackValue");
        var vars = new MapVars(backing);
        var c = new VariablesTaskCommon(vars, Collections.emptyList());

        c.set("tgt", "missing", "fallbackKey");
        assertEquals("fallbackValue", backing.get("tgt"));
    }

    @Test
    public void setMapWritesEachEntry() {
        var backing = new HashMap<String, Object>();
        var vars = new MapVars(backing);
        var c = new VariablesTaskCommon(vars, Collections.emptyList());

        var in = new LinkedHashMap<String, Object>();
        in.put("a", 1);
        in.put("b", 2);
        c.set(in);

        assertEquals(1, backing.get("a"));
        assertEquals(2, backing.get("b"));
    }

    @Test
    public void evalVariableHoldsFields() {
        var v = new VariablesTaskCommon.EvalVariable("n", 42, Integer.class);
        assertEquals("n", v.name());
        assertEquals(42, v.value());
        assertEquals(Integer.class, v.clazz());
    }

    @Test
    public void evalVariableAllowsNullValue() {
        var v = new VariablesTaskCommon.EvalVariable("x", null, String.class);
        assertEquals("x", v.name());
        assertNull(v.value());
        assertNotNull(v.clazz());
    }

    /**
     * Minimal, identity-interpolating {@link VariablesTaskCommon.Variables} backed by a map
     * so we can exercise the pure logic without pulling in concord-runner.
     */
    private static final class MapVars implements VariablesTaskCommon.Variables {

        private final Map<String, Object> backing;

        private MapVars(Map<String, Object> backing) {
            this.backing = backing;
        }

        @Override
        public Object get(String name) {
            return backing.get(name);
        }

        @Override
        public void set(String name, Object value) {
            backing.put(name, value);
        }

        @Override
        public Object interpolate(Object v) {
            return v;
        }
    }

    @Test
    public void concatReturnsAMutableList() {
        var result = VariablesTaskCommon.concat(List.of("a"), List.of("b"));
        result.add("c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
        assertTrue(result instanceof ArrayList);
    }
}
