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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigExtractorsTest {

    @Test
    public void testExtractBoolean() {
        Config config = configOf("key", true);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, boolean.class, "key");
        assertTrue(result.isPresent());
        assertEquals(true, result.get());
    }

    @Test
    public void testExtractBooleanBoxed() {
        Config config = configOf("key", true);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Boolean.class, "key");
        assertTrue(result.isPresent());
        assertEquals(true, result.get());
    }

    @Test
    public void testExtractInteger() {
        Config config = configOf("key", 42);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, int.class, "key");
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    public void testExtractLong() {
        Config config = configOf("key", 123456789L);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, long.class, "key");
        assertTrue(result.isPresent());
        assertEquals(123456789L, result.get());
    }

    @Test
    public void testExtractDouble() {
        Config config = configOf("key", 3.14);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, double.class, "key");
        assertTrue(result.isPresent());
        assertEquals(3.14, result.get());
    }

    @Test
    public void testExtractString() {
        Config config = configOf("key", "hello");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, String.class, "key");
        assertTrue(result.isPresent());
        assertEquals("hello", result.get());
    }

    @Test
    public void testExtractPath() {
        Config config = configOf("key", "/tmp/test");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Path.class, "key");
        assertTrue(result.isPresent());
        assertEquals(Paths.get("/tmp/test"), result.get());
    }

    @Test
    public void testExtractDuration() {
        Config config = ConfigFactory.parseString("key = 5s");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Duration.class, "key");
        assertTrue(result.isPresent());
        assertEquals(Duration.ofSeconds(5), result.get());
    }

    @Test
    public void testExtractByteArray() {
        String encoded = Base64.getEncoder().encodeToString("hello".getBytes());
        Config config = configOf("key", encoded);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, byte[].class, "key");
        assertTrue(result.isPresent());
        assertArrayEquals("hello".getBytes(), (byte[]) result.get());
    }

    @Test
    public void testExtractMissingPath() {
        Config config = configOf("key", "value");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, String.class, "missing");
        assertFalse(result.isPresent());
    }

    @Test
    public void testExtractByte() {
        Config config = configOf("key", 7);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, byte.class, "key");
        assertTrue(result.isPresent());
        assertEquals((byte) 7, result.get());
    }

    @Test
    public void testExtractShort() {
        Config config = configOf("key", 100);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, short.class, "key");
        assertTrue(result.isPresent());
        assertEquals((short) 100, result.get());
    }

    @Test
    public void testExtractFloat() {
        Config config = configOf("key", 2.5);
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, float.class, "key");
        assertTrue(result.isPresent());
        assertEquals(2.5f, (float) result.get(), 0.001f);
    }

    private static Config configOf(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return ConfigFactory.parseMap(map);
    }
}
