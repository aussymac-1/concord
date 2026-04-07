package com.walmartlabs.concord.config;

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

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ListExtractorsTest {

    private Config configOf(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return ConfigFactory.parseMap(map);
    }

    @Test
    public void testExtractStringList() {
        Config cfg = configOf("names", Arrays.asList("a", "b", "c"));
        Optional<List<?>> result = ListExtractors.extractConfigListValue(cfg, String.class, "names");
        assertTrue(result.isPresent());
        assertEquals(Arrays.asList("a", "b", "c"), result.get());
    }

    @Test
    public void testExtractIntegerList() {
        Config cfg = configOf("nums", Arrays.asList(1, 2, 3));
        Optional<List<?>> result = ListExtractors.extractConfigListValue(cfg, Integer.class, "nums");
        assertTrue(result.isPresent());
        assertEquals(Arrays.asList(1, 2, 3), result.get());
    }

    @Test
    public void testExtractBooleanList() {
        Config cfg = configOf("flags", Arrays.asList(true, false, true));
        Optional<List<?>> result = ListExtractors.extractConfigListValue(cfg, Boolean.class, "flags");
        assertTrue(result.isPresent());
        assertEquals(Arrays.asList(true, false, true), result.get());
    }

    @Test
    public void testExtractDoubleList() {
        Config cfg = configOf("vals", Arrays.asList(1.1, 2.2));
        Optional<List<?>> result = ListExtractors.extractConfigListValue(cfg, Double.class, "vals");
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    public void testExtractLongList() {
        Config cfg = configOf("ids", Arrays.asList(100L, 200L));
        Optional<List<?>> result = ListExtractors.extractConfigListValue(cfg, Long.class, "ids");
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    public void testExtractUnknownTypeReturnsEmpty() {
        Config cfg = configOf("data", Arrays.asList("a"));
        Optional<List<?>> result = ListExtractors.extractConfigListValue(cfg, java.math.BigDecimal.class, "data");
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetMatchingParameterizedType() {
        assertEquals(Boolean.class, ListExtractors.BOOLEAN.getMatchingParameterizedType());
        assertEquals(Integer.class, ListExtractors.INTEGER.getMatchingParameterizedType());
        assertEquals(String.class, ListExtractors.STRING.getMatchingParameterizedType());
        assertEquals(Double.class, ListExtractors.DOUBLE.getMatchingParameterizedType());
        assertEquals(Long.class, ListExtractors.LONG.getMatchingParameterizedType());
    }
}
