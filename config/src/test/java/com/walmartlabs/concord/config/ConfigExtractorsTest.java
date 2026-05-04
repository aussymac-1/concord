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
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigMemorySize;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigExtractorsTest {

    private static com.typesafe.config.Config cfg(String s) {
        return ConfigFactory.parseString(s);
    }

    @Test
    public void testEachExtractorMatchingClassesNonEmpty() {
        for (var ex : ConfigExtractors.values()) {
            assertNotNull(ex.getMatchingClasses(), () -> ex.name() + " getMatchingClasses() must not be null");
            assertTrue(ex.getMatchingClasses().length >= 1, () -> ex.name() + " must declare at least one matching class");
        }
    }

    @Test
    public void testBoolean() {
        assertEquals(true, ConfigExtractors.extractConfigValue(cfg("k=true"), boolean.class, "k").orElseThrow());
        assertEquals(true, ConfigExtractors.extractConfigValue(cfg("k=true"), Boolean.class, "k").orElseThrow());
    }

    @Test
    public void testInteger() {
        assertEquals(7, ConfigExtractors.extractConfigValue(cfg("k=7"), int.class, "k").orElseThrow());
        assertEquals(7, ConfigExtractors.extractConfigValue(cfg("k=7"), Integer.class, "k").orElseThrow());
    }

    @Test
    public void testByte() {
        assertEquals((byte) 5, ConfigExtractors.extractConfigValue(cfg("k=5"), byte.class, "k").orElseThrow());
        assertEquals((byte) 5, ConfigExtractors.extractConfigValue(cfg("k=5"), Byte.class, "k").orElseThrow());
    }

    @Test
    public void testShort() {
        assertEquals((short) 9, ConfigExtractors.extractConfigValue(cfg("k=9"), short.class, "k").orElseThrow());
        assertEquals((short) 9, ConfigExtractors.extractConfigValue(cfg("k=9"), Short.class, "k").orElseThrow());
    }

    @Test
    public void testLong() {
        assertEquals(123456789012L, ConfigExtractors.extractConfigValue(cfg("k=123456789012"), long.class, "k").orElseThrow());
        assertEquals(123456789012L, ConfigExtractors.extractConfigValue(cfg("k=123456789012"), Long.class, "k").orElseThrow());
    }

    @Test
    public void testDouble() {
        assertEquals(3.5d, (double) ConfigExtractors.extractConfigValue(cfg("k=3.5"), double.class, "k").orElseThrow());
        assertEquals(3.5d, (double) ConfigExtractors.extractConfigValue(cfg("k=3.5"), Double.class, "k").orElseThrow());
    }

    @Test
    public void testFloat() {
        assertEquals(2.5f, (float) ConfigExtractors.extractConfigValue(cfg("k=2.5"), float.class, "k").orElseThrow());
        assertEquals(2.5f, (float) ConfigExtractors.extractConfigValue(cfg("k=2.5"), Float.class, "k").orElseThrow());
    }

    @Test
    public void testString() {
        assertEquals("hello", ConfigExtractors.extractConfigValue(cfg("k=hello"), String.class, "k").orElseThrow());
    }

    @Test
    public void testPath() {
        var v = ConfigExtractors.extractConfigValue(cfg("k=\"/tmp/x\""), Path.class, "k").orElseThrow();
        assertEquals(Paths.get("/tmp/x"), v);
    }

    @Test
    public void testAnyRef() {
        var v = ConfigExtractors.extractConfigValue(cfg("k.x=1\nk.y=2"), Object.class, "k").orElseThrow();
        assertNotNull(v);
    }

    @Test
    public void testConfig() {
        var v = ConfigExtractors.extractConfigValue(cfg("k.x=1"), com.typesafe.config.Config.class, "k").orElseThrow();
        assertTrue(v instanceof com.typesafe.config.Config);
        assertEquals(1, ((com.typesafe.config.Config) v).getInt("x"));
    }

    @Test
    public void testConfigObject() {
        var v = ConfigExtractors.extractConfigValue(cfg("k.x=1"), ConfigObject.class, "k").orElseThrow();
        assertTrue(v instanceof ConfigObject);
    }

    @Test
    public void testConfigValue() {
        var v = ConfigExtractors.extractConfigValue(cfg("k=1"), ConfigValue.class, "k").orElseThrow();
        assertTrue(v instanceof ConfigValue);
    }

    @Test
    public void testConfigList() {
        var v = ConfigExtractors.extractConfigValue(cfg("k=[1,2,3]"), ConfigList.class, "k").orElseThrow();
        assertTrue(v instanceof ConfigList);
        assertEquals(3, ((ConfigList) v).size());
    }

    @Test
    public void testDuration() {
        var v = ConfigExtractors.extractConfigValue(cfg("k=5s"), Duration.class, "k").orElseThrow();
        assertEquals(Duration.ofSeconds(5), v);
    }

    @Test
    public void testMemorySize() {
        var v = ConfigExtractors.extractConfigValue(cfg("k=1MiB"), ConfigMemorySize.class, "k").orElseThrow();
        assertTrue(v instanceof ConfigMemorySize);
        assertEquals(1024L * 1024L, ((ConfigMemorySize) v).toBytes());
    }

    @Test
    public void testByteArray() {
        var raw = "hello".getBytes();
        var b64 = Base64.getEncoder().encodeToString(raw);
        var v = (byte[]) ConfigExtractors.extractConfigValue(cfg("k=\"" + b64 + "\""), byte[].class, "k").orElseThrow();
        assertArrayEquals(raw, v);
    }

    @Test
    public void testMissingPathReturnsEmpty() {
        assertTrue(ConfigExtractors.extractConfigValue(cfg("k=1"), Integer.class, "missing").isEmpty());
    }

    @Test
    public void testUnsupportedClassReturnsEmpty() {
        assertTrue(ConfigExtractors.extractConfigValue(cfg("k=1"), getClass(), "k").isEmpty());
    }

    @Test
    public void testEachClassMapsToOneExtractor() {
        // Sanity: every primitive class declared by extractors lookups back to a registered extractor
        Set<Class<?>> mapped = new HashSet<>();
        for (var ex : ConfigExtractors.values()) {
            mapped.addAll(Arrays.asList(ex.getMatchingClasses()));
        }
        assertTrue(mapped.contains(Integer.class));
        assertTrue(mapped.contains(int.class));
        assertTrue(mapped.contains(byte[].class));
        assertTrue(mapped.contains(com.typesafe.config.Config.class));
    }

    @Test
    public void testStringListExtractedViaConfigList() {
        // Validate that ConfigList extractor returns wrapped list (smoke test for 'CONFIG_LIST' branch).
        var list = (ConfigList) ConfigExtractors.extractConfigValue(cfg("k=[\"a\",\"b\"]"), ConfigList.class, "k").orElseThrow();
        List<String> values = list.unwrapped().stream().map(String.class::cast).toList();
        assertEquals(List.of("a", "b"), values);
    }
}
