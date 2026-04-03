package com.walmartlabs.concord.config;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2026 Walmart Inc.
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
    public void testExtractBooleanList() {
        Config config = ConfigFactory.parseString("flags = [true, false, true]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Boolean.class, "flags");
        assertTrue(result.isPresent());
        assertEquals(List.of(true, false, true), result.get());
    }

    @Test
    public void testExtractIntegerList() {
        Config config = ConfigFactory.parseString("nums = [1, 2, 3]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Integer.class, "nums");
        assertTrue(result.isPresent());
        assertEquals(List.of(1, 2, 3), result.get());
    }

    @Test
    public void testExtractStringList() {
        Config config = ConfigFactory.parseString("names = [\"a\", \"b\", \"c\"]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, String.class, "names");
        assertTrue(result.isPresent());
        assertEquals(List.of("a", "b", "c"), result.get());
    }

    @Test
    public void testExtractDoubleList() {
        Config config = ConfigFactory.parseString("vals = [1.1, 2.2]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Double.class, "vals");
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    public void testExtractLongList() {
        Config config = ConfigFactory.parseString("nums = [100, 200]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Long.class, "nums");
        assertTrue(result.isPresent());
        assertEquals(List.of(100L, 200L), result.get());
    }

    @Test
    public void testUnknownTypeReturnsEmpty() {
        Optional<List<?>> result = ListExtractors.extractConfigListValue(
                ConfigFactory.empty(), Thread.class, "key");
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetMatchingParameterizedType() {
        assertEquals(String.class, ListExtractors.STRING.getMatchingParameterizedType());
        assertEquals(Boolean.class, ListExtractors.BOOLEAN.getMatchingParameterizedType());
        assertEquals(Integer.class, ListExtractors.INTEGER.getMatchingParameterizedType());
    }
}
