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

import java.nio.file.Path;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigExtractorsTest {

    @Test
    public void testExtractBoolean() {
        Config config = configOf("flag", true);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, boolean.class, "flag");
        assertTrue(result.isPresent());
        assertEquals(true, result.get());
    }

    @Test
    public void testExtractBooleanBoxed() {
        Config config = configOf("flag", true);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Boolean.class, "flag");
        assertTrue(result.isPresent());
        assertEquals(true, result.get());
    }

    @Test
    public void testExtractInteger() {
        Config config = configOf("count", 42);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, int.class, "count");
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    public void testExtractLong() {
        Config config = configOf("big", 123456789L);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, long.class, "big");
        assertTrue(result.isPresent());
        assertEquals(123456789L, result.get());
    }

    @Test
    public void testExtractDouble() {
        Config config = configOf("pi", 3.14);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, double.class, "pi");
        assertTrue(result.isPresent());
        assertEquals(3.14, (double) result.get(), 0.001);
    }

    @Test
    public void testExtractString() {
        Config config = configOf("name", "hello");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, String.class, "name");
        assertTrue(result.isPresent());
        assertEquals("hello", result.get());
    }

    @Test
    public void testExtractPath() {
        Config config = configOf("dir", "/tmp/test");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Path.class, "dir");
        assertTrue(result.isPresent());
        assertEquals(Path.of("/tmp/test"), result.get());
    }

    @Test
    public void testExtractDuration() {
        Config config = ConfigFactory.parseString("timeout = 5s");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Duration.class, "timeout");
        assertTrue(result.isPresent());
        assertEquals(Duration.ofSeconds(5), result.get());
    }

    @Test
    public void testExtractByteArray() {
        String encoded = Base64.getEncoder().encodeToString("hello".getBytes());
        Config config = configOf("data", encoded);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, byte[].class, "data");
        assertTrue(result.isPresent());
        assertArrayEquals("hello".getBytes(), (byte[]) result.get());
    }

    @Test
    public void testMissingPathReturnsEmpty() {
        Config config = configOf("key", "value");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, String.class, "missing");
        assertFalse(result.isPresent());
    }

    @Test
    public void testUnknownClassReturnsEmpty() {
        Config config = configOf("key", "value");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Thread.class, "key");
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetMatchingClasses() {
        Class<?>[] classes = ConfigExtractors.STRING.getMatchingClasses();
        assertEquals(1, classes.length);
        assertEquals(String.class, classes[0]);
    }

    @Test
    public void testBooleanHasTwoMatchingClasses() {
        Class<?>[] classes = ConfigExtractors.BOOLEAN.getMatchingClasses();
        assertEquals(2, classes.length);
    }

    private static Config configOf(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return ConfigFactory.parseMap(map);
    }
}
