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
import com.typesafe.config.ConfigObject;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ListExtractorsTest {

    private Config parseConfig(String hocon) {
        return ConfigFactory.parseString(hocon);
    }

    @Test
    public void testExtractBooleanList() {
        var config = parseConfig("vals = [true, false, true]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Boolean.class, "vals");
        assertTrue(result.isPresent());
        assertEquals(List.of(true, false, true), result.get());
    }

    @Test
    public void testExtractIntegerList() {
        var config = parseConfig("vals = [1, 2, 3]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Integer.class, "vals");
        assertTrue(result.isPresent());
        assertEquals(List.of(1, 2, 3), result.get());
    }

    @Test
    public void testExtractDoubleList() {
        var config = parseConfig("vals = [1.1, 2.2, 3.3]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Double.class, "vals");
        assertTrue(result.isPresent());
        assertEquals(3, result.get().size());
    }

    @Test
    public void testExtractLongList() {
        var config = parseConfig("vals = [100, 200, 300]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Long.class, "vals");
        assertTrue(result.isPresent());
        assertEquals(List.of(100L, 200L, 300L), result.get());
    }

    @Test
    public void testExtractStringList() {
        var config = parseConfig("vals = [\"a\", \"b\", \"c\"]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, String.class, "vals");
        assertTrue(result.isPresent());
        assertEquals(List.of("a", "b", "c"), result.get());
    }

    @Test
    public void testExtractDurationList() {
        var config = parseConfig("vals = [1s, 2s, 3s]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Duration.class, "vals");
        assertTrue(result.isPresent());
        assertEquals(List.of(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(3)), result.get());
    }

    @Test
    public void testExtractObjectList() {
        var config = parseConfig("vals = [1, \"two\", 3.0]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Object.class, "vals");
        assertTrue(result.isPresent());
        assertEquals(3, result.get().size());
    }

    @Test
    public void testExtractConfigList() {
        var config = parseConfig("vals = [{ a = 1 }, { a = 2 }]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Config.class, "vals");
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    public void testExtractConfigObjectList() {
        var config = parseConfig("vals = [{ a = 1 }, { a = 2 }]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, ConfigObject.class, "vals");
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    public void testExtractUnknownType() {
        var config = parseConfig("vals = [1, 2]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Thread.class, "vals");
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetMatchingParameterizedType() {
        assertEquals(Boolean.class, ListExtractors.BOOLEAN.getMatchingParameterizedType());
        assertEquals(Integer.class, ListExtractors.INTEGER.getMatchingParameterizedType());
        assertEquals(Double.class, ListExtractors.DOUBLE.getMatchingParameterizedType());
        assertEquals(Long.class, ListExtractors.LONG.getMatchingParameterizedType());
        assertEquals(String.class, ListExtractors.STRING.getMatchingParameterizedType());
    }
}
