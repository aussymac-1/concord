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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigExtractorsTest {

    private Config config(String key, Object value) {
        Map<String, Object> m = new HashMap<>();
        m.put(key, value);
        return ConfigFactory.parseMap(m);
    }

    @Test
    public void testBooleanExtractor() {
        Config cfg = config("flag", true);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, boolean.class, "flag");
        assertTrue(result.isPresent());
        assertEquals(true, result.get());
    }

    @Test
    public void testBooleanBoxedExtractor() {
        Config cfg = config("flag", false);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Boolean.class, "flag");
        assertTrue(result.isPresent());
        assertEquals(false, result.get());
    }

    @Test
    public void testIntegerExtractor() {
        Config cfg = config("num", 42);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, int.class, "num");
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    public void testIntegerBoxedExtractor() {
        Config cfg = config("num", 42);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Integer.class, "num");
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    public void testLongExtractor() {
        Config cfg = config("num", 100L);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, long.class, "num");
        assertTrue(result.isPresent());
        assertEquals(100L, result.get());
    }

    @Test
    public void testDoubleExtractor() {
        Config cfg = config("num", 3.14);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, double.class, "num");
        assertTrue(result.isPresent());
        assertEquals(3.14, result.get());
    }

    @Test
    public void testStringExtractor() {
        Config cfg = config("name", "hello");
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, String.class, "name");
        assertTrue(result.isPresent());
        assertEquals("hello", result.get());
    }

    @Test
    public void testPathExtractor() {
        Config cfg = config("dir", "/tmp/test");
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Path.class, "dir");
        assertTrue(result.isPresent());
        assertEquals(Paths.get("/tmp/test"), result.get());
    }

    @Test
    public void testDurationExtractor() {
        Config cfg = ConfigFactory.parseString("dur = 5s");
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Duration.class, "dur");
        assertTrue(result.isPresent());
        assertEquals(Duration.ofSeconds(5), result.get());
    }

    @Test
    public void testByteArrayExtractor() {
        String encoded = Base64.getEncoder().encodeToString("hello".getBytes());
        Config cfg = config("data", encoded);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, byte[].class, "data");
        assertTrue(result.isPresent());
        assertArrayEquals("hello".getBytes(), (byte[]) result.get());
    }

    @Test
    public void testMissingPathReturnsEmpty() {
        Config cfg = config("other", "value");
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, String.class, "missing");
        assertFalse(result.isPresent());
    }

    @Test
    public void testByteExtractor() {
        Config cfg = config("b", 10);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, byte.class, "b");
        assertTrue(result.isPresent());
        assertEquals((byte) 10, result.get());
    }

    @Test
    public void testShortExtractor() {
        Config cfg = config("s", 100);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, short.class, "s");
        assertTrue(result.isPresent());
        assertEquals((short) 100, result.get());
    }

    @Test
    public void testFloatExtractor() {
        Config cfg = config("f", 1.5);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, float.class, "f");
        assertTrue(result.isPresent());
        assertEquals(1.5f, (float) result.get(), 0.001);
    }

    @Test
    public void testAnyRefExtractor() {
        Config cfg = config("val", "anything");
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Object.class, "val");
        assertTrue(result.isPresent());
        assertEquals("anything", result.get());
    }

    @Test
    public void testGetMatchingClasses() {
        Class<?>[] classes = ConfigExtractors.BOOLEAN.getMatchingClasses();
        assertEquals(2, classes.length);
        assertEquals(boolean.class, classes[0]);
        assertEquals(Boolean.class, classes[1]);
    }
}
