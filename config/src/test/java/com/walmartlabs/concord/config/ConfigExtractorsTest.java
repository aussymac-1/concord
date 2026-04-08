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

import com.typesafe.config.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigExtractorsTest {

    private com.typesafe.config.Config parseConfig(String hocon) {
        return ConfigFactory.parseString(hocon);
    }

    @Test
    public void testExtractBoolean() {
        var config = parseConfig("flag = true");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, boolean.class, "flag");
        assertTrue(result.isPresent());
        assertEquals(true, result.get());
    }

    @Test
    public void testExtractBooleanBoxed() {
        var config = parseConfig("flag = false");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Boolean.class, "flag");
        assertTrue(result.isPresent());
        assertEquals(false, result.get());
    }

    @Test
    public void testExtractByte() {
        var config = parseConfig("val = 42");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, byte.class, "val");
        assertTrue(result.isPresent());
        assertEquals((byte) 42, result.get());
    }

    @Test
    public void testExtractByteBoxed() {
        var config = parseConfig("val = 42");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Byte.class, "val");
        assertTrue(result.isPresent());
        assertEquals((byte) 42, result.get());
    }

    @Test
    public void testExtractShort() {
        var config = parseConfig("val = 1000");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, short.class, "val");
        assertTrue(result.isPresent());
        assertEquals((short) 1000, result.get());
    }

    @Test
    public void testExtractShortBoxed() {
        var config = parseConfig("val = 1000");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Short.class, "val");
        assertTrue(result.isPresent());
        assertEquals((short) 1000, result.get());
    }

    @Test
    public void testExtractInt() {
        var config = parseConfig("val = 12345");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, int.class, "val");
        assertTrue(result.isPresent());
        assertEquals(12345, result.get());
    }

    @Test
    public void testExtractIntBoxed() {
        var config = parseConfig("val = 12345");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Integer.class, "val");
        assertTrue(result.isPresent());
        assertEquals(12345, result.get());
    }

    @Test
    public void testExtractLong() {
        var config = parseConfig("val = 9999999999");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, long.class, "val");
        assertTrue(result.isPresent());
        assertEquals(9999999999L, result.get());
    }

    @Test
    public void testExtractLongBoxed() {
        var config = parseConfig("val = 9999999999");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Long.class, "val");
        assertTrue(result.isPresent());
        assertEquals(9999999999L, result.get());
    }

    @Test
    public void testExtractFloat() {
        var config = parseConfig("val = 3.14");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, float.class, "val");
        assertTrue(result.isPresent());
        assertEquals(3.14f, (float) result.get(), 0.001f);
    }

    @Test
    public void testExtractFloatBoxed() {
        var config = parseConfig("val = 3.14");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Float.class, "val");
        assertTrue(result.isPresent());
        assertEquals(3.14f, (float) result.get(), 0.001f);
    }

    @Test
    public void testExtractDouble() {
        var config = parseConfig("val = 3.14159");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, double.class, "val");
        assertTrue(result.isPresent());
        assertEquals(3.14159, (double) result.get(), 0.00001);
    }

    @Test
    public void testExtractDoubleBoxed() {
        var config = parseConfig("val = 3.14159");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Double.class, "val");
        assertTrue(result.isPresent());
        assertEquals(3.14159, (double) result.get(), 0.00001);
    }

    @Test
    public void testExtractString() {
        var config = parseConfig("val = hello");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, String.class, "val");
        assertTrue(result.isPresent());
        assertEquals("hello", result.get());
    }

    @Test
    public void testExtractPath() {
        var config = parseConfig("val = \"/tmp/test\"");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Path.class, "val");
        assertTrue(result.isPresent());
        assertEquals(Path.of("/tmp/test"), result.get());
    }

    @Test
    public void testExtractAnyRef() {
        var config = parseConfig("val = 42");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Object.class, "val");
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    public void testExtractConfig() {
        var config = parseConfig("sub { a = 1, b = 2 }");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, com.typesafe.config.Config.class, "sub");
        assertTrue(result.isPresent());
        var subConfig = (com.typesafe.config.Config) result.get();
        assertEquals(1, subConfig.getInt("a"));
    }

    @Test
    public void testExtractConfigObject() {
        var config = parseConfig("sub { a = 1 }");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, ConfigObject.class, "sub");
        assertTrue(result.isPresent());
        assertInstanceOf(ConfigObject.class, result.get());
    }

    @Test
    public void testExtractConfigValue() {
        var config = parseConfig("val = 42");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, ConfigValue.class, "val");
        assertTrue(result.isPresent());
        assertInstanceOf(ConfigValue.class, result.get());
    }

    @Test
    public void testExtractConfigList() {
        var config = parseConfig("val = [1, 2, 3]");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, ConfigList.class, "val");
        assertTrue(result.isPresent());
        assertInstanceOf(ConfigList.class, result.get());
    }

    @Test
    public void testExtractDuration() {
        var config = parseConfig("val = 5s");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Duration.class, "val");
        assertTrue(result.isPresent());
        assertEquals(Duration.ofSeconds(5), result.get());
    }

    @Test
    public void testExtractMemorySize() {
        var config = parseConfig("val = 1024k");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, ConfigMemorySize.class, "val");
        assertTrue(result.isPresent());
        assertInstanceOf(ConfigMemorySize.class, result.get());
    }

    @Test
    public void testExtractByteArray() {
        String encoded = Base64.getEncoder().encodeToString("hello".getBytes());
        var config = parseConfig("val = \"" + encoded + "\"");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, byte[].class, "val");
        assertTrue(result.isPresent());
        assertArrayEquals("hello".getBytes(), (byte[]) result.get());
    }

    @Test
    public void testExtractMissingPath() {
        var config = parseConfig("other = 1");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, int.class, "missing");
        assertFalse(result.isPresent());
    }

    @Test
    public void testExtractUnknownType() {
        var config = parseConfig("val = 1");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Thread.class, "val");
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetMatchingClasses() {
        Class<?>[] classes = ConfigExtractors.BOOLEAN.getMatchingClasses();
        assertEquals(2, classes.length);
        assertEquals(boolean.class, classes[0]);
        assertEquals(Boolean.class, classes[1]);
    }

    @Test
    public void testGetMatchingClassesSingle() {
        Class<?>[] classes = ConfigExtractors.STRING.getMatchingClasses();
        assertEquals(1, classes.length);
        assertEquals(String.class, classes[0]);
    }
}
