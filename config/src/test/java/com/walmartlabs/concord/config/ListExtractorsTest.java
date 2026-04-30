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

import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListExtractorsTest {

    @Test
    public void testExtractStringList() {
        var map = new HashMap<String, Object>();
        map.put("names", Arrays.asList("a", "b", "c"));
        var config = ConfigFactory.parseMap(map);
        var result = ListExtractors.extractConfigListValue(config, String.class, "names");
        assertTrue(result.isPresent());
        assertEquals(Arrays.asList("a", "b", "c"), result.get());
    }

    @Test
    public void testExtractIntegerList() {
        var map = new HashMap<String, Object>();
        map.put("nums", Arrays.asList(1, 2, 3));
        var config = ConfigFactory.parseMap(map);
        var result = ListExtractors.extractConfigListValue(config, Integer.class, "nums");
        assertTrue(result.isPresent());
        assertEquals(Arrays.asList(1, 2, 3), result.get());
    }

    @Test
    public void testExtractBooleanList() {
        var map = new HashMap<String, Object>();
        map.put("flags", Arrays.asList(true, false));
        var config = ConfigFactory.parseMap(map);
        var result = ListExtractors.extractConfigListValue(config, Boolean.class, "flags");
        assertTrue(result.isPresent());
        assertEquals(Arrays.asList(true, false), result.get());
    }

    @Test
    public void testExtractDoubleList() {
        var map = new HashMap<String, Object>();
        map.put("vals", Arrays.asList(1.1, 2.2));
        var config = ConfigFactory.parseMap(map);
        var result = ListExtractors.extractConfigListValue(config, Double.class, "vals");
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    public void testExtractLongList() {
        var map = new HashMap<String, Object>();
        map.put("big", Arrays.asList(100L, 200L));
        var config = ConfigFactory.parseMap(map);
        var result = ListExtractors.extractConfigListValue(config, Long.class, "big");
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    public void testUnknownType() {
        var result = ListExtractors.extractConfigListValue(
                ConfigFactory.empty(), Void.class, "any");
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetMatchingParameterizedType() {
        for (var extractor : ListExtractors.values()) {
            assertNotNull(extractor.getMatchingParameterizedType());
        }
    }
}
