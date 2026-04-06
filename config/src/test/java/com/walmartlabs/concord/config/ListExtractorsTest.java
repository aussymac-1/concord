package com.walmartlabs.concord.config;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2018 Takari
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

class ListExtractorsTest {

    @Test
    void testExtractStringList() {
        var config = ConfigFactory.parseString("myList = [\"a\", \"b\", \"c\"]");
        var result = ListExtractors.extractConfigListValue(config, String.class, "myList");
        assertTrue(result.isPresent());
        assertEquals(List.of("a", "b", "c"), result.get());
    }

    @Test
    void testExtractIntegerList() {
        var config = ConfigFactory.parseString("myList = [1, 2, 3]");
        var result = ListExtractors.extractConfigListValue(config, Integer.class, "myList");
        assertTrue(result.isPresent());
        assertEquals(List.of(1, 2, 3), result.get());
    }

    @Test
    void testExtractBooleanList() {
        var config = ConfigFactory.parseString("myList = [true, false, true]");
        var result = ListExtractors.extractConfigListValue(config, Boolean.class, "myList");
        assertTrue(result.isPresent());
        assertEquals(List.of(true, false, true), result.get());
    }

    @Test
    void testExtractDoubleList() {
        var config = ConfigFactory.parseString("myList = [1.1, 2.2, 3.3]");
        var result = ListExtractors.extractConfigListValue(config, Double.class, "myList");
        assertTrue(result.isPresent());
        assertEquals(3, result.get().size());
    }

    @Test
    void testExtractLongList() {
        var config = ConfigFactory.parseString("myList = [100, 200, 300]");
        var result = ListExtractors.extractConfigListValue(config, Long.class, "myList");
        assertTrue(result.isPresent());
        assertEquals(List.of(100L, 200L, 300L), result.get());
    }

    @Test
    void testUnknownTypeReturnsEmpty() {
        var config = ConfigFactory.parseString("myList = [1, 2]");
        var result = ListExtractors.extractConfigListValue(config, Void.class, "myList");
        assertFalse(result.isPresent());
    }
}
