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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigExtractorsTest {

    @Test
    public void testExtractBoolean() {
        var config = ConfigFactory.parseString("myBool = true");
        var result = ConfigExtractors.extractConfigValue(config, boolean.class, "myBool");
        assertTrue(result.isPresent());
        assertEquals(true, result.get());
    }

    @Test
    public void testExtractBooleanBoxed() {
        var config = ConfigFactory.parseString("myBool = false");
        var result = ConfigExtractors.extractConfigValue(config, Boolean.class, "myBool");
        assertTrue(result.isPresent());
        assertEquals(false, result.get());
    }

    @Test
    public void testExtractInt() {
        var config = ConfigFactory.parseString("myInt = 42");
        var result = ConfigExtractors.extractConfigValue(config, int.class, "myInt");
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    public void testExtractIntBoxed() {
        var config = ConfigFactory.parseString("myInt = 99");
        var result = ConfigExtractors.extractConfigValue(config, Integer.class, "myInt");
        assertTrue(result.isPresent());
        assertEquals(99, result.get());
    }

    @Test
    public void testExtractLong() {
        var config = ConfigFactory.parseString("myLong = 123456789");
        var result = ConfigExtractors.extractConfigValue(config, long.class, "myLong");
        assertTrue(result.isPresent());
        assertEquals(123456789L, result.get());
    }

    @Test
    public void testExtractDouble() {
        var config = ConfigFactory.parseString("myDouble = 3.14");
        var result = ConfigExtractors.extractConfigValue(config, double.class, "myDouble");
        assertTrue(result.isPresent());
        assertEquals(3.14, (double) result.get(), 0.001);
    }

    @Test
    public void testExtractFloat() {
        var config = ConfigFactory.parseString("myFloat = 2.5");
        var result = ConfigExtractors.extractConfigValue(config, float.class, "myFloat");
        assertTrue(result.isPresent());
        assertEquals(2.5f, (float) result.get(), 0.001f);
    }

    @Test
    public void testExtractString() {
        var config = ConfigFactory.parseString("myStr = \"hello\"");
        var result = ConfigExtractors.extractConfigValue(config, String.class, "myStr");
        assertTrue(result.isPresent());
        assertEquals("hello", result.get());
    }

    @Test
    public void testExtractPath() {
        var config = ConfigFactory.parseString("myPath = \"/tmp/test\"");
        var result = ConfigExtractors.extractConfigValue(config, Path.class, "myPath");
        assertTrue(result.isPresent());
        assertEquals(Path.of("/tmp/test"), result.get());
    }

    @Test
    public void testExtractDuration() {
        var config = ConfigFactory.parseString("myDuration = 5s");
        var result = ConfigExtractors.extractConfigValue(config, Duration.class, "myDuration");
        assertTrue(result.isPresent());
        assertEquals(Duration.ofSeconds(5), result.get());
    }

    @Test
    public void testExtractMissingPath() {
        var config = ConfigFactory.parseString("a = 1");
        var result = ConfigExtractors.extractConfigValue(config, int.class, "missing");
        assertFalse(result.isPresent());
    }

    @Test
    public void testExtractUnsupportedType() {
        var config = ConfigFactory.parseString("a = 1");
        var result = ConfigExtractors.extractConfigValue(config, Thread.class, "a");
        assertFalse(result.isPresent());
    }

    @Test
    public void testExtractByte() {
        var config = ConfigFactory.parseString("myByte = 127");
        var result = ConfigExtractors.extractConfigValue(config, byte.class, "myByte");
        assertTrue(result.isPresent());
        assertEquals((byte) 127, result.get());
    }

    @Test
    public void testExtractShort() {
        var config = ConfigFactory.parseString("myShort = 32000");
        var result = ConfigExtractors.extractConfigValue(config, short.class, "myShort");
        assertTrue(result.isPresent());
        assertEquals((short) 32000, result.get());
    }

    @Test
    public void testGetMatchingClasses() {
        var classes = ConfigExtractors.BOOLEAN.getMatchingClasses();
        assertEquals(2, classes.length);
        assertEquals(boolean.class, classes[0]);
        assertEquals(Boolean.class, classes[1]);
    }

    @Test
    public void testExtractConfig() {
        var config = ConfigFactory.parseString("myConfig { a = 1 }");
        var result = ConfigExtractors.extractConfigValue(config, Config.class, "myConfig");
        assertTrue(result.isPresent());
        assertInstanceOf(Config.class, result.get());
    }

    @Test
    public void testExtractByteArray() {
        var config = ConfigFactory.parseString("myBytes = \"aGVsbG8=\"");
        var result = ConfigExtractors.extractConfigValue(config, byte[].class, "myBytes");
        assertTrue(result.isPresent());
        assertArrayEquals("hello".getBytes(), (byte[]) result.get());
    }
}
