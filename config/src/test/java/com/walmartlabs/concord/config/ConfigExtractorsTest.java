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

    private Config configOf(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return ConfigFactory.parseMap(map);
    }

    @Test
    public void testExtractBoolean() {
        Config cfg = configOf("flag", true);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, boolean.class, "flag");
        assertTrue(result.isPresent());
        assertEquals(true, result.get());
    }

    @Test
    public void testExtractBooleanBoxed() {
        Config cfg = configOf("flag", false);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Boolean.class, "flag");
        assertTrue(result.isPresent());
        assertEquals(false, result.get());
    }

    @Test
    public void testExtractInteger() {
        Config cfg = configOf("count", 42);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, int.class, "count");
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    public void testExtractIntegerBoxed() {
        Config cfg = configOf("count", 42);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Integer.class, "count");
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    public void testExtractLong() {
        Config cfg = configOf("bigNum", 999999999999L);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, long.class, "bigNum");
        assertTrue(result.isPresent());
        assertEquals(999999999999L, result.get());
    }

    @Test
    public void testExtractDouble() {
        Config cfg = configOf("ratio", 3.14);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, double.class, "ratio");
        assertTrue(result.isPresent());
        assertEquals(3.14, (double) result.get(), 0.001);
    }

    @Test
    public void testExtractString() {
        Config cfg = configOf("name", "hello");
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, String.class, "name");
        assertTrue(result.isPresent());
        assertEquals("hello", result.get());
    }

    @Test
    public void testExtractPath() {
        Config cfg = configOf("dir", "/tmp/test");
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Path.class, "dir");
        assertTrue(result.isPresent());
        assertEquals(Paths.get("/tmp/test"), result.get());
    }

    @Test
    public void testExtractDuration() {
        Config cfg = configOf("timeout", "5s");
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Duration.class, "timeout");
        assertTrue(result.isPresent());
        assertEquals(Duration.ofSeconds(5), result.get());
    }

    @Test
    public void testExtractByteArray() {
        String encoded = Base64.getEncoder().encodeToString("secret".getBytes());
        Config cfg = configOf("data", encoded);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, byte[].class, "data");
        assertTrue(result.isPresent());
        assertArrayEquals("secret".getBytes(), (byte[]) result.get());
    }

    @Test
    public void testExtractMissingPath() {
        Config cfg = configOf("name", "hello");
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, String.class, "missing");
        assertFalse(result.isPresent());
    }

    @Test
    public void testExtractByte() {
        Config cfg = configOf("b", 127);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, byte.class, "b");
        assertTrue(result.isPresent());
        assertEquals((byte) 127, result.get());
    }

    @Test
    public void testExtractShort() {
        Config cfg = configOf("s", 32000);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, short.class, "s");
        assertTrue(result.isPresent());
        assertEquals((short) 32000, result.get());
    }

    @Test
    public void testExtractFloat() {
        Config cfg = configOf("f", 2.5);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, float.class, "f");
        assertTrue(result.isPresent());
        assertEquals(2.5f, (float) result.get(), 0.001f);
    }

    @Test
    public void testGetMatchingClasses() {
        Class<?>[] classes = ConfigExtractors.BOOLEAN.getMatchingClasses();
        assertEquals(2, classes.length);
        assertEquals(boolean.class, classes[0]);
        assertEquals(Boolean.class, classes[1]);
    }

    @Test
    public void testGetMatchingClassesString() {
        Class<?>[] classes = ConfigExtractors.STRING.getMatchingClasses();
        assertEquals(1, classes.length);
        assertEquals(String.class, classes[0]);
    }
}
