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

    @Test
    public void testExtractStringList() {
        Config config = ConfigFactory.parseString("key = [\"a\", \"b\", \"c\"]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, String.class, "key");
        assertTrue(result.isPresent());
        assertEquals(Arrays.asList("a", "b", "c"), result.get());
    }

    @Test
    public void testExtractIntegerList() {
        Config config = ConfigFactory.parseString("key = [1, 2, 3]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Integer.class, "key");
        assertTrue(result.isPresent());
        assertEquals(Arrays.asList(1, 2, 3), result.get());
    }

    @Test
    public void testExtractBooleanList() {
        Config config = ConfigFactory.parseString("key = [true, false]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Boolean.class, "key");
        assertTrue(result.isPresent());
        assertEquals(Arrays.asList(true, false), result.get());
    }

    @Test
    public void testExtractDoubleList() {
        Config config = ConfigFactory.parseString("key = [1.1, 2.2]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Double.class, "key");
        assertTrue(result.isPresent());
        assertEquals(Arrays.asList(1.1, 2.2), result.get());
    }

    @Test
    public void testExtractLongList() {
        Config config = ConfigFactory.parseString("key = [100, 200]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Long.class, "key");
        assertTrue(result.isPresent());
        assertEquals(Arrays.asList(100L, 200L), result.get());
    }

    @Test
    public void testExtractObjectList() {
        Config config = ConfigFactory.parseString("key = [1, \"two\", true]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Object.class, "key");
        assertTrue(result.isPresent());
        assertEquals(3, result.get().size());
    }

    @Test
    public void testUnknownTypeReturnsEmpty() {
        Optional<List<?>> result = ListExtractors.extractConfigListValue(
                ConfigFactory.empty(), java.io.File.class, "key");
        assertFalse(result.isPresent());
    }
}
