package com.walmartlabs.concord.runtime.common;

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

public class SensitiveDataMaskerExtendedTest {

    @Test
    public void testEmptySensitiveStrings() {
        String input = "some secret data";
        String result = SensitiveDataMasker.mask(input, Collections.emptySet());
        assertEquals("some secret data", result);
    }

    @Test
    public void testMaskString() {
        String result = SensitiveDataMasker.mask("password is secret123", Set.of("secret123"));
        assertEquals("password is ******", result);
    }

    @Test
    public void testMaskMultipleSensitiveStringsInString() {
        String result = SensitiveDataMasker.mask("user=admin pass=s3cret", Set.of("admin", "s3cret"));
        assertEquals("user=****** pass=******", result);
    }

    @Test
    public void testMaskList() {
        List<String> input = Arrays.asList("hello", "secret", "world");
        List<String> result = SensitiveDataMasker.mask(input, Set.of("secret"));
        assertEquals(Arrays.asList("hello", "******", "world"), result);
    }

    @Test
    public void testMaskSet() {
        Set<String> input = new LinkedHashSet<>(Arrays.asList("abc", "secret", "xyz"));
        Set<String> result = SensitiveDataMasker.mask(input, Set.of("secret"));
        assertTrue(result.contains("abc"));
        assertTrue(result.contains("******"));
        assertTrue(result.contains("xyz"));
        assertFalse(result.contains("secret"));
    }

    @Test
    public void testMaskMap() {
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("key1", "value1");
        input.put("key2", "secret_value");
        input.put("key3", "other");

        Map<String, Object> result = SensitiveDataMasker.mask(input, Set.of("secret_value"));
        assertEquals("value1", result.get("key1"));
        assertEquals("******", result.get("key2"));
        assertEquals("other", result.get("key3"));
    }

    @Test
    public void testMaskNestedMap() {
        Map<String, Object> inner = new LinkedHashMap<>();
        inner.put("password", "mysecret");

        Map<String, Object> outer = new LinkedHashMap<>();
        outer.put("config", inner);
        outer.put("name", "mysecret-service");

        Map<String, Object> result = SensitiveDataMasker.mask(outer, Set.of("mysecret"));
        assertEquals("******-service", result.get("name"));

        @SuppressWarnings("unchecked")
        Map<String, Object> resultInner = (Map<String, Object>) result.get("config");
        assertEquals("******", resultInner.get("password"));
    }

    @Test
    public void testMaskNonStringTypes() {
        Integer intVal = 42;
        Integer result = SensitiveDataMasker.mask(intVal, Set.of("42"));
        assertEquals(42, result);
    }

    @Test
    public void testMaskNestedList() {
        List<Object> input = Arrays.asList("public", Arrays.asList("nested_secret", "ok"));
        List<Object> result = SensitiveDataMasker.mask(input, Set.of("nested_secret"));

        assertEquals("public", result.get(0));
        @SuppressWarnings("unchecked")
        List<Object> nested = (List<Object>) result.get(1);
        assertEquals("******", nested.get(0));
        assertEquals("ok", nested.get(1));
    }
}
