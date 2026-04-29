package com.walmartlabs.concord.common;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2024 Walmart Inc.
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

public class ConfigurationUtilsExtendedTest {

    @Test
    public void testHasWithNullMap() {
        assertFalse(ConfigurationUtils.has(null, new String[]{"a"}));
    }

    @Test
    public void testHasWithEmptyPath() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");
        assertFalse(ConfigurationUtils.has(m, new String[]{}));
    }

    @Test
    public void testHasTopLevel() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");
        assertTrue(ConfigurationUtils.has(m, new String[]{"key"}));
        assertFalse(ConfigurationUtils.has(m, new String[]{"missing"}));
    }

    @Test
    public void testHasNested() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("b", "value");

        Map<String, Object> m = new HashMap<>();
        m.put("a", inner);

        assertTrue(ConfigurationUtils.has(m, new String[]{"a", "b"}));
        assertFalse(ConfigurationUtils.has(m, new String[]{"a", "c"}));
    }

    @Test
    public void testHasNestedNonMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("a", "notAMap");

        assertFalse(ConfigurationUtils.has(m, new String[]{"a", "b"}));
    }

    @Test
    public void testGetFromNullMap() {
        assertNull(ConfigurationUtils.get(null, "a", "b"));
    }

    @Test
    public void testGetZeroDepth() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");

        assertSame(m, ConfigurationUtils.get(m, 0, "ignored"));
    }

    @Test
    public void testGetNested() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("b", 42);

        Map<String, Object> m = new HashMap<>();
        m.put("a", inner);

        assertEquals(42, ConfigurationUtils.get(m, "a", "b"));
    }

    @Test
    public void testGetNestedMissingKey() {
        Map<String, Object> m = new HashMap<>();
        m.put("a", new HashMap<>());

        assertNull(ConfigurationUtils.get(m, "a", "b"));
    }

    @Test
    public void testGetNestedNullIntermediate() {
        Map<String, Object> m = new HashMap<>();
        m.put("a", null);

        assertNull(ConfigurationUtils.get(m, "a", "b"));
    }

    @Test
    public void testGetNestedNonMapIntermediate() {
        Map<String, Object> m = new HashMap<>();
        m.put("a", "string");

        assertThrows(IllegalArgumentException.class, () -> ConfigurationUtils.get(m, "a", "b"));
    }

    @Test
    public void testGetWithNullPath() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");

        Object result = ConfigurationUtils.get(m, (String[]) null);
        assertSame(m, result);
    }

    @Test
    public void testSet() {
        Map<String, Object> inner = new HashMap<>();
        Map<String, Object> m = new HashMap<>();
        m.put("a", inner);

        ConfigurationUtils.set(m, "newValue", "a", "b");
        assertEquals("newValue", inner.get("b"));
    }

    @Test
    public void testSetNonMapHolder() {
        Map<String, Object> m = new HashMap<>();
        m.put("a", "string");

        assertThrows(IllegalArgumentException.class,
                () -> ConfigurationUtils.set(m, "value", "a", "b"));
    }

    @Test
    public void testDelete() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("b", "value");

        Map<String, Object> m = new HashMap<>();
        m.put("a", inner);

        ConfigurationUtils.delete(m, "a", "b");
        assertFalse(inner.containsKey("b"));
    }

    @Test
    public void testDeleteNullHolder() {
        Map<String, Object> m = new HashMap<>();
        ConfigurationUtils.delete(m, "missing", "key");
    }

    @Test
    public void testDeleteNonMapHolder() {
        Map<String, Object> m = new HashMap<>();
        m.put("a", "string");

        assertThrows(IllegalArgumentException.class,
                () -> ConfigurationUtils.delete(m, "a", "b"));
    }

    @Test
    public void testMerge() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("existing", "old");

        Map<String, Object> m = new HashMap<>();
        m.put("config", inner);

        Map<String, Object> toMerge = new HashMap<>();
        toMerge.put("new", "value");

        ConfigurationUtils.merge(m, toMerge, "config");
        assertEquals("old", inner.get("existing"));
        assertEquals("value", inner.get("new"));
    }

    @Test
    public void testMergeNonMapHolder() {
        Map<String, Object> m = new HashMap<>();
        m.put("config", "string");

        assertThrows(IllegalArgumentException.class,
                () -> ConfigurationUtils.merge(m, Collections.emptyMap(), "config"));
    }

    @Test
    public void testDeepMergeNested() {
        Map<String, Object> inner1 = new HashMap<>();
        inner1.put("x", 1);

        Map<String, Object> inner2 = new HashMap<>();
        inner2.put("y", 2);

        Map<String, Object> m1 = new HashMap<>();
        m1.put("nested", inner1);

        Map<String, Object> m2 = new HashMap<>();
        m2.put("nested", inner2);

        Map<String, Object> result = ConfigurationUtils.deepMerge(m1, m2);

        @SuppressWarnings("unchecked")
        Map<String, Object> merged = (Map<String, Object>) result.get("nested");
        assertEquals(1, merged.get("x"));
        assertEquals(2, merged.get("y"));
    }

    @Test
    public void testDeepMergeNullFirst() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");

        Map<String, Object> result = ConfigurationUtils.deepMerge(null, m);
        assertEquals("value", result.get("key"));
    }

    @Test
    public void testDeepMergeVarargs() {
        Map<String, Object> m1 = new HashMap<>();
        m1.put("a", "1");

        Map<String, Object> m2 = new HashMap<>();
        m2.put("b", "2");

        Map<String, Object> m3 = new HashMap<>();
        m3.put("c", "3");

        Map<String, Object> result = ConfigurationUtils.deepMerge(m1, m2, m3);
        assertEquals("1", result.get("a"));
        assertEquals("2", result.get("b"));
        assertEquals("3", result.get("c"));
    }

    @Test
    public void testDeepMergeVarargsEmpty() {
        Map<String, Object> result = ConfigurationUtils.deepMerge();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToNestedSimpleKey() {
        Map<String, Object> result = ConfigurationUtils.toNested("key", "value");
        assertEquals(Collections.singletonMap("key", "value"), result);
    }

    @Test
    public void testToNestedDottedKey() {
        Map<String, Object> result = ConfigurationUtils.toNested("a.b.c", "value");
        assertNotNull(result.get("a"));

        @SuppressWarnings("unchecked")
        Map<String, Object> level1 = (Map<String, Object>) result.get("a");

        @SuppressWarnings("unchecked")
        Map<String, Object> level2 = (Map<String, Object>) level1.get("b");

        assertEquals("value", level2.get("c"));
    }

    @Test
    public void testDistinct() {
        Set<String> result = ConfigurationUtils.distinct(
                Arrays.asList("a", "b"),
                Arrays.asList("b", "c"),
                null
        );
        assertEquals(new HashSet<>(Arrays.asList("a", "b", "c")), result);
    }

    @Test
    public void testDistinctNullInput() {
        Set<Object> result = ConfigurationUtils.distinct((Collection<Object>[]) null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIsNestedKey() {
        assertTrue(ConfigurationUtils.isNestedKey("a.b"));
        assertFalse(ConfigurationUtils.isNestedKey("simple"));
    }

    @Test
    public void testDeepEqualsCollectionsDifferentSize() {
        Object a = Collections.singletonMap("x", Arrays.asList("a", "b"));
        Object b = Collections.singletonMap("x", Collections.singletonList("a"));
        assertFalse(ConfigurationUtils.deepEquals(a, b));
    }

    @Test
    public void testDeepEqualsMapsDifferentSize() {
        Map<String, Object> a = new HashMap<>();
        a.put("x", 1);
        a.put("y", 2);

        Map<String, Object> b = new HashMap<>();
        b.put("x", 1);

        assertFalse(ConfigurationUtils.deepEquals(a, b));
    }
}
