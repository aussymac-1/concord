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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigExtractorsTest {

    private com.typesafe.config.Config configOf(String key, Object value) {
        var map = new HashMap<String, Object>();
        map.put(key, value);
        return ConfigFactory.parseMap(map);
    }

    @Test
    public void testExtractBoolean() {
        var config = configOf("flag", true);
        var result = ConfigExtractors.extractConfigValue(config, boolean.class, "flag");
        assertTrue(result.isPresent());
        assertEquals(true, result.get());
    }

    @Test
    public void testExtractBooleanBoxed() {
        var config = configOf("flag", false);
        var result = ConfigExtractors.extractConfigValue(config, Boolean.class, "flag");
        assertTrue(result.isPresent());
        assertEquals(false, result.get());
    }

    @Test
    public void testExtractInteger() {
        var config = configOf("count", 42);
        var result = ConfigExtractors.extractConfigValue(config, int.class, "count");
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    public void testExtractIntegerBoxed() {
        var config = configOf("count", 42);
        var result = ConfigExtractors.extractConfigValue(config, Integer.class, "count");
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    public void testExtractLong() {
        var config = configOf("big", 999999999999L);
        var result = ConfigExtractors.extractConfigValue(config, long.class, "big");
        assertTrue(result.isPresent());
        assertEquals(999999999999L, result.get());
    }

    @Test
    public void testExtractDouble() {
        var config = configOf("pi", 3.14);
        var result = ConfigExtractors.extractConfigValue(config, double.class, "pi");
        assertTrue(result.isPresent());
        assertEquals(3.14, (double) result.get(), 0.001);
    }

    @Test
    public void testExtractFloat() {
        var config = configOf("val", 2.5);
        var result = ConfigExtractors.extractConfigValue(config, float.class, "val");
        assertTrue(result.isPresent());
        assertEquals(2.5f, (float) result.get(), 0.01f);
    }

    @Test
    public void testExtractString() {
        var config = configOf("name", "hello");
        var result = ConfigExtractors.extractConfigValue(config, String.class, "name");
        assertTrue(result.isPresent());
        assertEquals("hello", result.get());
    }

    @Test
    public void testExtractPath() {
        var config = configOf("dir", "/tmp/data");
        var result = ConfigExtractors.extractConfigValue(config, Path.class, "dir");
        assertTrue(result.isPresent());
        assertEquals(Paths.get("/tmp/data"), result.get());
    }

    @Test
    public void testExtractDuration() {
        var config = ConfigFactory.parseString("timeout = 5s");
        var result = ConfigExtractors.extractConfigValue(config, Duration.class, "timeout");
        assertTrue(result.isPresent());
        assertEquals(Duration.ofSeconds(5), result.get());
    }

    @Test
    public void testExtractByteArray() {
        var encoded = Base64.getEncoder().encodeToString("hello".getBytes());
        var config = configOf("data", encoded);
        var result = ConfigExtractors.extractConfigValue(config, byte[].class, "data");
        assertTrue(result.isPresent());
        assertArrayEquals("hello".getBytes(), (byte[]) result.get());
    }

    @Test
    public void testExtractByte() {
        var config = configOf("b", 7);
        var result = ConfigExtractors.extractConfigValue(config, byte.class, "b");
        assertTrue(result.isPresent());
        assertEquals((byte) 7, result.get());
    }

    @Test
    public void testExtractShort() {
        var config = configOf("s", 123);
        var result = ConfigExtractors.extractConfigValue(config, short.class, "s");
        assertTrue(result.isPresent());
        assertEquals((short) 123, result.get());
    }

    @Test
    public void testMissingPath() {
        var config = configOf("a", "value");
        var result = ConfigExtractors.extractConfigValue(config, String.class, "missing");
        assertFalse(result.isPresent());
    }

    @Test
    public void testUnknownClass() {
        var config = configOf("a", "value");
        var result = ConfigExtractors.extractConfigValue(config, Void.class, "a");
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetMatchingClasses() {
        for (var extractor : ConfigExtractors.values()) {
            assertNotNull(extractor.getMatchingClasses());
            assertTrue(extractor.getMatchingClasses().length > 0);
        }
    }
}
