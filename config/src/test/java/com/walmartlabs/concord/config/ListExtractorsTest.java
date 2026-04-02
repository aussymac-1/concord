package com.walmartlabs.concord.config;

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

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ListExtractorsTest {

    @Test
    public void testStringListExtractor() {
        Config cfg = ConfigFactory.parseString("items = [\"a\", \"b\", \"c\"]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(cfg, String.class, "items");
        assertTrue(result.isPresent());
        assertEquals(List.of("a", "b", "c"), result.get());
    }

    @Test
    public void testIntegerListExtractor() {
        Config cfg = ConfigFactory.parseString("nums = [1, 2, 3]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(cfg, Integer.class, "nums");
        assertTrue(result.isPresent());
        assertEquals(List.of(1, 2, 3), result.get());
    }

    @Test
    public void testBooleanListExtractor() {
        Config cfg = ConfigFactory.parseString("flags = [true, false, true]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(cfg, Boolean.class, "flags");
        assertTrue(result.isPresent());
        assertEquals(List.of(true, false, true), result.get());
    }

    @Test
    public void testDoubleListExtractor() {
        Config cfg = ConfigFactory.parseString("vals = [1.1, 2.2, 3.3]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(cfg, Double.class, "vals");
        assertTrue(result.isPresent());
        assertEquals(List.of(1.1, 2.2, 3.3), result.get());
    }

    @Test
    public void testLongListExtractor() {
        Config cfg = ConfigFactory.parseString("nums = [100, 200, 300]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(cfg, Long.class, "nums");
        assertTrue(result.isPresent());
        assertEquals(List.of(100L, 200L, 300L), result.get());
    }

    @Test
    public void testObjectListExtractor() {
        Config cfg = ConfigFactory.parseString("items = [\"a\", 1, true]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(cfg, Object.class, "items");
        assertTrue(result.isPresent());
        assertEquals(3, result.get().size());
    }

    @Test
    public void testUnknownTypeReturnsEmpty() {
        Optional<List<?>> result = ListExtractors.extractConfigListValue(
                ConfigFactory.empty(), Float.class, "any");
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetMatchingParameterizedType() {
        assertSame(String.class, ListExtractors.STRING.getMatchingParameterizedType());
        assertSame(Integer.class, ListExtractors.INTEGER.getMatchingParameterizedType());
        assertSame(Boolean.class, ListExtractors.BOOLEAN.getMatchingParameterizedType());
    }
}
