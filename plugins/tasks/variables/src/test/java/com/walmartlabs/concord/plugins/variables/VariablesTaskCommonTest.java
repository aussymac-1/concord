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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class VariablesTaskCommonTest {

    @Test
    public void testConcatTwoLists() {
        List<Object> result = VariablesTaskCommon.concat(Arrays.asList(1, 2), Arrays.asList(3, 4));

        assertEquals(Arrays.asList(1, 2, 3, 4), result);
    }

    @Test
    public void testConcatFirstNull() {
        List<Object> result = VariablesTaskCommon.concat(null, Arrays.asList("a", "b"));

        assertEquals(Arrays.asList("a", "b"), result);
    }

    @Test
    public void testConcatSecondNull() {
        List<Object> result = VariablesTaskCommon.concat(Arrays.asList("a", "b"), null);

        assertEquals(Arrays.asList("a", "b"), result);
    }

    @Test
    public void testConcatBothNull() {
        assertTrue(VariablesTaskCommon.concat(null, null).isEmpty());
    }

    @Test
    public void testConcatPreservesDuplicates() {
        List<Object> result = VariablesTaskCommon.concat(Arrays.asList(1, 1), Arrays.asList(1));

        assertEquals(Arrays.asList(1, 1, 1), result);
    }

    @Test
    public void testGetTopLevelExistingKey() {
        MapVariables vars = new MapVariables();
        vars.set("foo", "bar");

        VariablesTaskCommon common = new VariablesTaskCommon(vars, Collections.emptyList());

        assertEquals("bar", common.get("foo", "default"));
    }

    @Test
    public void testGetTopLevelMissingKeyUsesDefault() {
        VariablesTaskCommon common = new VariablesTaskCommon(new MapVariables(), Collections.emptyList());

        assertEquals("fallback", common.get("missing", "fallback"));
    }

    @Test
    public void testGetTopLevelNullDefault() {
        VariablesTaskCommon common = new VariablesTaskCommon(new MapVariables(), Collections.emptyList());

        assertNull(common.get("missing", null));
    }

    @Test
    public void testGetNestedPath() {
        MapVariables vars = new MapVariables();
        vars.set("parent", Collections.singletonMap("child", "leaf"));

        VariablesTaskCommon common = new VariablesTaskCommon(vars, Collections.emptyList());

        // EL resolver splits on the dot and traverses the map.
        assertEquals("leaf", common.get("parent.child", "default"));
    }

    @Test
    public void testGetNestedMissingUsesDefault() {
        VariablesTaskCommon common = new VariablesTaskCommon(new MapVariables(), Collections.emptyList());

        assertEquals("default", common.get("missing.path", "default"));
    }

    @Test
    public void testSetFromSource() {
        MapVariables vars = new MapVariables();
        vars.set("source", "value");

        VariablesTaskCommon common = new VariablesTaskCommon(vars, Collections.emptyList());
        common.set("target", "source", "defaultKey");

        assertEquals("value", vars.get("target"));
    }

    @Test
    public void testSetFromDefaultWhenSourceMissing() {
        MapVariables vars = new MapVariables();
        vars.set("defaultKey", "fallback");

        VariablesTaskCommon common = new VariablesTaskCommon(vars, Collections.emptyList());
        common.set("target", "missingSource", "defaultKey");

        assertEquals("fallback", vars.get("target"));
    }

    @Test
    public void testSetWritesNullWhenNeitherFound() {
        MapVariables vars = new MapVariables();

        VariablesTaskCommon common = new VariablesTaskCommon(vars, Collections.emptyList());
        common.set("target", "missingSource", "missingDefault");

        assertTrue(vars.getBackingMap().containsKey("target"));
        assertNull(vars.get("target"));
    }

    @Test
    public void testSetMapTopLevel() {
        MapVariables vars = new MapVariables();

        VariablesTaskCommon common = new VariablesTaskCommon(vars, Collections.emptyList());
        common.set(Collections.singletonMap("a", 1));

        assertEquals(1, vars.get("a"));
    }

    /**
     * A simple {@link VariablesTaskCommon.Variables} implementation backed by a map
     * that performs no expression interpolation. Sufficient for unit-testing the
     * top-level branches of {@link VariablesTaskCommon}.
     */
    private static class MapVariables implements VariablesTaskCommon.Variables {

        private final Map<String, Object> data = new LinkedHashMap<>();

        @Override
        public Object get(String name) {
            return data.get(name);
        }

        @Override
        public void set(String name, Object value) {
            data.put(name, value);
        }

        @Override
        public Object interpolate(Object v) {
            return v;
        }

        public Map<String, Object> getBackingMap() {
            return data;
        }
    }
}
