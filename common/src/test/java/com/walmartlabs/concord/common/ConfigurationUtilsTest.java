package com.walmartlabs.concord.common;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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

public class ConfigurationUtilsTest {

    @Test
    public void deepMergeTest() {
        Map<String, Object> m1 = new HashMap<>();
        m1.put("a", "a-value1");
        m1.put("b", "b-value1");

        Map<String, Object> m2 = new HashMap<>();
        m2.put("a", "a-value2");
        m2.put("c", "b-value2");

        Map<String, Object> result = ConfigurationUtils.deepMerge(m1, m2);
        assertEquals("a-value2", result.get("a"));
        assertEquals("b-value1", result.get("b"));
        assertEquals("b-value2", result.get("c"));
    }

    @Test
    public void deepEqualsTest() {
        Object a = Collections.singletonMap("x", Collections.singletonList("test1"));
        Object b = Collections.singletonMap("x", Collections.singletonList("test2"));
        assertFalse(ConfigurationUtils.deepEquals(a, b));

        a = Collections.singletonMap("x", Collections.singletonList("test"));
        b = Collections.singletonMap("x", Collections.singletonList("test"));
        assertTrue(ConfigurationUtils.deepEquals(a, b));
    }

    // --- has() ---

    @Test
    public void testHasNullMap() {
        assertFalse(ConfigurationUtils.has(null, new String[]{"a"}));
    }

    @Test
    public void testHasEmptyPath() {
        Map<String, Object> m = new HashMap<>();
        m.put("a", "value");
        assertFalse(ConfigurationUtils.has(m, new String[]{}));
    }

    @Test
    public void testHasDirectKey() {
        Map<String, Object> m = new HashMap<>();
        m.put("a", "value");
        assertTrue(ConfigurationUtils.has(m, new String[]{"a"}));
    }

    @Test
    public void testHasMissingKey() {
        Map<String, Object> m = new HashMap<>();
        m.put("a", "value");
        assertFalse(ConfigurationUtils.has(m, new String[]{"b"}));
    }

    @Test
    public void testHasNestedKey() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("b", "value");
        Map<String, Object> m = new HashMap<>();
        m.put("a", inner);
        assertTrue(ConfigurationUtils.has(m, new String[]{"a", "b"}));
    }

    @Test
    public void testHasNestedKeyMissing() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("b", "value");
        Map<String, Object> m = new HashMap<>();
        m.put("a", inner);
        assertFalse(ConfigurationUtils.has(m, new String[]{"a", "c"}));
    }

    @Test
    public void testHasNonMapIntermediate() {
        Map<String, Object> m = new HashMap<>();
        m.put("a", "string-not-map");
        assertFalse(ConfigurationUtils.has(m, new String[]{"a", "b"}));
    }

    // --- get() ---

    @Test
    public void testGetNullMap() {
        assertNull(ConfigurationUtils.get(null, "a"));
    }

    @Test
    public void testGetDirectValue() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");
        assertEquals("value", ConfigurationUtils.get(m, "key"));
    }

    @Test
    public void testGetNestedValue() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("b", "nested-value");
        Map<String, Object> m = new HashMap<>();
        m.put("a", inner);
        assertEquals("nested-value", ConfigurationUtils.get(m, "a", "b"));
    }

    @Test
    public void testGetMissingKey() {
        Map<String, Object> m = new HashMap<>();
        assertNull(ConfigurationUtils.get(m, "missing"));
    }

    @Test
    public void testGetZeroDepth() {
        Map<String, Object> m = new HashMap<>();
        m.put("a", "value");
        assertSame(m, ConfigurationUtils.get(m, 0));
    }

    @Test
    public void testGetNonMapIntermediate() {
        Map<String, Object> m = new HashMap<>();
        m.put("a", "string");
        assertThrows(IllegalArgumentException.class, () -> ConfigurationUtils.get(m, "a", "b"));
    }

    // --- set() ---

    @Test
    public void testSetDirectValue() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "old");
        ConfigurationUtils.set(m, "new", "key");
        assertEquals("new", m.get("key"));
    }

    @Test
    public void testSetNestedValue() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("b", "old");
        Map<String, Object> m = new HashMap<>();
        m.put("a", inner);
        ConfigurationUtils.set(m, "new", "a", "b");
        assertEquals("new", inner.get("b"));
    }

    // --- delete() ---

    @Test
    public void testDeleteDirectKey() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");
        ConfigurationUtils.delete(m, "key");
        assertFalse(m.containsKey("key"));
    }

    @Test
    public void testDeleteNestedKey() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("b", "value");
        Map<String, Object> m = new HashMap<>();
        m.put("a", inner);
        ConfigurationUtils.delete(m, "a", "b");
        assertFalse(inner.containsKey("b"));
    }

    @Test
    public void testDeleteMissingKey() {
        Map<String, Object> m = new HashMap<>();
        ConfigurationUtils.delete(m, "missing");
        // no exception
    }

    // --- merge() ---

    @Test
    public void testMergeIntoExisting() {
        Map<String, Object> m = new HashMap<>();
        Map<String, Object> inner = new HashMap<>();
        inner.put("existing", "old");
        m.put("target", inner);

        Map<String, Object> toMerge = new HashMap<>();
        toMerge.put("new-key", "new-value");

        ConfigurationUtils.merge(m, toMerge, "target");
        assertEquals("old", inner.get("existing"));
        assertEquals("new-value", inner.get("new-key"));
    }

    // --- deepMerge varargs ---

    @Test
    public void testDeepMergeVarargsEmpty() {
        Map<String, Object> result = ConfigurationUtils.deepMerge();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeepMergeVarargsNull() {
        Map<String, Object> result = ConfigurationUtils.deepMerge((Map<String, Object>[]) null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeepMergeNestedMaps() {
        Map<String, Object> inner1 = new HashMap<>();
        inner1.put("x", "1");
        Map<String, Object> m1 = new HashMap<>();
        m1.put("nested", inner1);

        Map<String, Object> inner2 = new HashMap<>();
        inner2.put("y", "2");
        Map<String, Object> m2 = new HashMap<>();
        m2.put("nested", inner2);

        Map<String, Object> result = ConfigurationUtils.deepMerge(m1, m2);
        @SuppressWarnings("unchecked")
        Map<String, Object> nested = (Map<String, Object>) result.get("nested");
        assertEquals("1", nested.get("x"));
        assertEquals("2", nested.get("y"));
    }

    @Test
    public void testDeepMergeWithNullFirst() {
        Map<String, Object> m2 = new HashMap<>();
        m2.put("a", "value");
        Map<String, Object> result = ConfigurationUtils.deepMerge(null, m2);
        assertEquals("value", result.get("a"));
    }

    // --- toNested() ---

    @Test
    public void testToNestedSimpleKey() {
        Map<String, Object> result = ConfigurationUtils.toNested("key", "value");
        assertEquals("value", result.get("key"));
    }

    @Test
    public void testToNestedDottedKey() {
        Map<String, Object> result = ConfigurationUtils.toNested("a.b.c", "value");
        @SuppressWarnings("unchecked")
        Map<String, Object> a = (Map<String, Object>) result.get("a");
        assertNotNull(a);
        @SuppressWarnings("unchecked")
        Map<String, Object> b = (Map<String, Object>) a.get("b");
        assertNotNull(b);
        assertEquals("value", b.get("c"));
    }

    // --- distinct() ---

    @Test
    public void testDistinctMergesCollections() {
        Set<String> result = ConfigurationUtils.distinct(
                Arrays.asList("a", "b"),
                Arrays.asList("b", "c"));
        assertEquals(3, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
    }

    @Test
    public void testDistinctNullCollections() {
        Set<String> result = ConfigurationUtils.distinct((Collection<String>[]) null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDistinctWithNullElement() {
        Set<String> result = ConfigurationUtils.distinct(
                Arrays.asList("a"),
                null,
                Arrays.asList("b"));
        assertEquals(2, result.size());
    }

    // --- isNestedKey() ---

    @Test
    public void testIsNestedKeyTrue() {
        assertTrue(ConfigurationUtils.isNestedKey("a.b"));
    }

    @Test
    public void testIsNestedKeyFalse() {
        assertFalse(ConfigurationUtils.isNestedKey("simple"));
    }

    // --- deepEquals additional ---

    @Test
    public void testDeepEqualsBothNull() {
        assertTrue(ConfigurationUtils.deepEquals(null, null));
    }

    @Test
    public void testDeepEqualsOneNull() {
        assertFalse(ConfigurationUtils.deepEquals("a", null));
    }

    @Test
    public void testDeepEqualsDifferentSizeMaps() {
        Map<String, Object> a = new HashMap<>();
        a.put("x", "1");
        Map<String, Object> b = new HashMap<>();
        b.put("x", "1");
        b.put("y", "2");
        assertFalse(ConfigurationUtils.deepEquals(a, b));
    }

    @Test
    public void testDeepEqualsDifferentSizeCollections() {
        assertFalse(ConfigurationUtils.deepEquals(
                Arrays.asList("a"),
                Arrays.asList("a", "b")));
    }
}
