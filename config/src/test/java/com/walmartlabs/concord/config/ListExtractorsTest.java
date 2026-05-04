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

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigMemorySize;
import com.typesafe.config.ConfigObject;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListExtractorsTest {

    private static com.typesafe.config.Config cfg(String s) {
        return ConfigFactory.parseString(s);
    }

    @Test
    public void testBooleanList() {
        var v = ListExtractors.extractConfigListValue(cfg("k=[true,false,true]"), Boolean.class, "k").orElseThrow();
        assertEquals(List.of(true, false, true), v);
    }

    @Test
    public void testIntegerList() {
        var v = ListExtractors.extractConfigListValue(cfg("k=[1,2,3]"), Integer.class, "k").orElseThrow();
        assertEquals(List.of(1, 2, 3), v);
    }

    @Test
    public void testDoubleList() {
        var v = ListExtractors.extractConfigListValue(cfg("k=[1.5,2.5]"), Double.class, "k").orElseThrow();
        assertEquals(List.of(1.5, 2.5), v);
    }

    @Test
    public void testLongList() {
        var v = ListExtractors.extractConfigListValue(cfg("k=[10,20]"), Long.class, "k").orElseThrow();
        assertEquals(List.of(10L, 20L), v);
    }

    @Test
    public void testStringList() {
        var v = ListExtractors.extractConfigListValue(cfg("k=[\"a\",\"b\"]"), String.class, "k").orElseThrow();
        assertEquals(List.of("a", "b"), v);
    }

    @Test
    public void testDurationList() {
        var v = ListExtractors.extractConfigListValue(cfg("k=[1s,2s]"), Duration.class, "k").orElseThrow();
        assertEquals(List.of(Duration.ofSeconds(1), Duration.ofSeconds(2)), v);
    }

    @Test
    public void testMemorySizeList() {
        var v = ListExtractors.extractConfigListValue(cfg("k=[1MB,2MB]"), ConfigMemorySize.class, "k").orElseThrow();
        assertEquals(2, v.size());
    }

    @Test
    public void testObjectList() {
        var v = ListExtractors.extractConfigListValue(cfg("k=[1,2,3]"), Object.class, "k").orElseThrow();
        assertEquals(3, v.size());
    }

    @Test
    public void testConfigList() {
        var v = ListExtractors.extractConfigListValue(cfg("k=[{x=1},{x=2}]"), com.typesafe.config.Config.class, "k").orElseThrow();
        assertEquals(2, v.size());
        assertTrue(v.get(0) instanceof com.typesafe.config.Config);
    }

    @Test
    public void testConfigObjectList() {
        var v = ListExtractors.extractConfigListValue(cfg("k=[{x=1},{x=2}]"), ConfigObject.class, "k").orElseThrow();
        assertEquals(2, v.size());
        assertTrue(v.get(0) instanceof ConfigObject);
    }

    @Test
    public void testUnsupportedTypeReturnsEmpty() {
        assertTrue(ListExtractors.extractConfigListValue(cfg("k=[1,2]"), getClass(), "k").isEmpty());
    }

    @Test
    public void testGetMatchingParameterizedTypeNonNull() {
        for (var ex : ListExtractors.values()) {
            assertNotNull(ex.getMatchingParameterizedType(), () -> ex.name() + " must declare a parameterized type");
        }
    }
}
