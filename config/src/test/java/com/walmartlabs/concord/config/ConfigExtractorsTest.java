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
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigExtractorsTest {

    private Config config(String key, Object value) {
        return ConfigFactory.parseString(key + " = " + formatValue(value));
    }

    private String formatValue(Object value) {
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        return String.valueOf(value);
    }

    @Test
    public void testExtractBoolean() {
        Config cfg = config("flag", true);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, boolean.class, "flag");
        assertTrue(result.isPresent());
        assertEquals(true, result.get());
    }

    @Test
    public void testExtractBooleanBoxed() {
        Config cfg = config("flag", false);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Boolean.class, "flag");
        assertTrue(result.isPresent());
        assertEquals(false, result.get());
    }

    @Test
    public void testExtractInt() {
        Config cfg = config("num", 42);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, int.class, "num");
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    public void testExtractIntegerBoxed() {
        Config cfg = config("num", 100);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Integer.class, "num");
        assertTrue(result.isPresent());
        assertEquals(100, result.get());
    }

    @Test
    public void testExtractLong() {
        Config cfg = config("big", 999999999999L);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, long.class, "big");
        assertTrue(result.isPresent());
        assertEquals(999999999999L, result.get());
    }

    @Test
    public void testExtractLongBoxed() {
        Config cfg = config("big", 123L);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Long.class, "big");
        assertTrue(result.isPresent());
        assertEquals(123L, result.get());
    }

    @Test
    public void testExtractDouble() {
        Config cfg = config("pi", 3.14);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, double.class, "pi");
        assertTrue(result.isPresent());
        assertEquals(3.14, (double) result.get(), 0.001);
    }

    @Test
    public void testExtractDoubleBoxed() {
        Config cfg = config("val", 2.71);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Double.class, "val");
        assertTrue(result.isPresent());
        assertEquals(2.71, (double) result.get(), 0.001);
    }

    @Test
    public void testExtractString() {
        Config cfg = config("name", "hello");
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, String.class, "name");
        assertTrue(result.isPresent());
        assertEquals("hello", result.get());
    }

    @Test
    public void testExtractPath() {
        Config cfg = config("dir", "/tmp/test");
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Path.class, "dir");
        assertTrue(result.isPresent());
        assertEquals(Path.of("/tmp/test"), result.get());
    }

    @Test
    public void testExtractDuration() {
        Config cfg = ConfigFactory.parseString("timeout = 5s");
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Duration.class, "timeout");
        assertTrue(result.isPresent());
        assertEquals(Duration.ofSeconds(5), result.get());
    }

    @Test
    public void testExtractByteArray() {
        String encoded = Base64.getEncoder().encodeToString("hello".getBytes());
        Config cfg = config("data", encoded);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, byte[].class, "data");
        assertTrue(result.isPresent());
        assertArrayEquals("hello".getBytes(), (byte[]) result.get());
    }

    @Test
    public void testMissingPath() {
        Config cfg = ConfigFactory.empty();
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, String.class, "missing");
        assertFalse(result.isPresent());
    }

    @Test
    public void testUnknownType() {
        Config cfg = config("val", 42);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, java.util.Date.class, "val");
        assertFalse(result.isPresent());
    }

    @Test
    public void testExtractShort() {
        Config cfg = config("s", 32);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, short.class, "s");
        assertTrue(result.isPresent());
        assertEquals((short) 32, result.get());
    }

    @Test
    public void testExtractShortBoxed() {
        Config cfg = config("s", 16);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Short.class, "s");
        assertTrue(result.isPresent());
        assertEquals((short) 16, result.get());
    }

    @Test
    public void testExtractByte() {
        Config cfg = config("b", 7);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, byte.class, "b");
        assertTrue(result.isPresent());
        assertEquals((byte) 7, result.get());
    }

    @Test
    public void testExtractByteBoxed() {
        Config cfg = config("b", 3);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Byte.class, "b");
        assertTrue(result.isPresent());
        assertEquals((byte) 3, result.get());
    }

    @Test
    public void testExtractFloat() {
        Config cfg = config("f", 1.5);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, float.class, "f");
        assertTrue(result.isPresent());
        assertEquals(1.5f, (float) result.get(), 0.001);
    }

    @Test
    public void testExtractFloatBoxed() {
        Config cfg = config("f", 2.5);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Float.class, "f");
        assertTrue(result.isPresent());
        assertEquals(2.5f, (float) result.get(), 0.001);
    }

    @Test
    public void testExtractObject() {
        Config cfg = config("val", 42);
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Object.class, "val");
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    public void testExtractConfig() {
        Config cfg = ConfigFactory.parseString("nested { a = 1 }");
        Optional<Object> result = ConfigExtractors.extractConfigValue(cfg, Config.class, "nested");
        assertTrue(result.isPresent());
        Config nested = (Config) result.get();
        assertEquals(1, nested.getInt("a"));
    }

    @Test
    public void testGetMatchingClasses() {
        for (ConfigExtractors extractor : ConfigExtractors.values()) {
            Class<?>[] classes = extractor.getMatchingClasses();
            assertNotNull(classes);
            assertTrue(classes.length > 0);
        }
    }
}
