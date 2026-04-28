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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SensitiveDataMaskerTest {

    @Test
    public void testSensitiveDataMasking() throws JsonProcessingException {
        var sensitiveStrings = Set.of("foo", "bar");

        String in = "{" +
                "\"a\": \"foo\"," +
                "\"b\": \"bar\"," +
                "\"c\": \"baz\"," +
                "\"d\": { \"e\": \"foo\" }" +
                "}";

        Map<String, Object> result = SensitiveDataMasker.mask(vars(in), sensitiveStrings);
        String expected = "{" +
                "   \"a\": \"******\"," +
                "   \"b\": \"******\"," +
                "   \"c\": \"baz\"," +
                "   \"d\": { \"e\": \"******\" }" +
                "}";
        assertEquals(vars(expected), result);
    }

    @Test
    public void testEmptySensitiveStrings() {
        String input = "hello world";
        String result = SensitiveDataMasker.mask(input, Collections.emptySet());
        assertEquals("hello world", result);
    }

    @Test
    public void testMaskString() {
        String result = SensitiveDataMasker.mask("my secret password is s3cret", Set.of("s3cret"));
        assertEquals("my secret password is ******", result);
    }

    @Test
    public void testMaskMultipleOccurrences() {
        String result = SensitiveDataMasker.mask("abc abc abc", Set.of("abc"));
        assertEquals("****** ****** ******", result);
    }

    @Test
    public void testMaskList() {
        List<String> input = Arrays.asList("hello", "secret", "world");
        List<String> result = SensitiveDataMasker.mask(input, Set.of("secret"));
        assertEquals(Arrays.asList("hello", "******", "world"), result);
    }

    @Test
    public void testMaskSet() {
        Set<String> input = new LinkedHashSet<>(Arrays.asList("hello", "secret"));
        Set<String> result = SensitiveDataMasker.mask(input, Set.of("secret"));
        assertTrue(result.contains("hello"));
        assertTrue(result.contains("******"));
        assertFalse(result.contains("secret"));
    }

    @Test
    public void testMaskMap() {
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("key1", "safe");
        input.put("key2", "secret");

        Map<String, Object> result = SensitiveDataMasker.mask(input, Set.of("secret"));
        assertEquals("safe", result.get("key1"));
        assertEquals("******", result.get("key2"));
    }

    @Test
    public void testMaskNonStringValue() {
        Integer input = 42;
        Integer result = SensitiveDataMasker.mask(input, Set.of("42"));
        assertEquals(42, result);
    }

    @Test
    public void testMaskNestedList() {
        List<Object> input = new ArrayList<>();
        input.add("visible");
        input.add(Arrays.asList("nested-secret", "ok"));

        List<Object> result = SensitiveDataMasker.mask(input, Set.of("nested-secret"));
        assertEquals("visible", result.get(0));
        @SuppressWarnings("unchecked")
        List<String> nested = (List<String>) result.get(1);
        assertEquals("******", nested.get(0));
        assertEquals("ok", nested.get(1));
    }

    private static Map<String, Object> vars(String in) throws JsonProcessingException {
        return new ObjectMapper().readValue(in, new TypeReference<>() {
        });
    }
}
