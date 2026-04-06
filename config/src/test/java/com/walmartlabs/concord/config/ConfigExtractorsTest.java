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

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ConfigExtractorsTest {

    @Test
    void testExtractBoolean() {
        Config config = ConfigFactory.parseString("myBool = true");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, boolean.class, "myBool");
        assertTrue(result.isPresent());
        assertEquals(true, result.get());
    }

    @Test
    void testExtractInteger() {
        Config config = ConfigFactory.parseString("myInt = 42");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, int.class, "myInt");
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    void testExtractLong() {
        Config config = ConfigFactory.parseString("myLong = 999999999999");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, long.class, "myLong");
        assertTrue(result.isPresent());
        assertEquals(999999999999L, result.get());
    }

    @Test
    void testExtractDouble() {
        Config config = ConfigFactory.parseString("myDouble = 3.14");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, double.class, "myDouble");
        assertTrue(result.isPresent());
        assertEquals(3.14, (double) result.get(), 0.001);
    }

    @Test
    void testExtractString() {
        Config config = ConfigFactory.parseString("myStr = hello");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, String.class, "myStr");
        assertTrue(result.isPresent());
        assertEquals("hello", result.get());
    }

    @Test
    void testExtractPath() {
        Config config = ConfigFactory.parseString("myPath = /tmp/test");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Path.class, "myPath");
        assertTrue(result.isPresent());
        assertEquals(Path.of("/tmp/test"), result.get());
    }

    @Test
    void testExtractDuration() {
        Config config = ConfigFactory.parseString("myDuration = 5s");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Duration.class, "myDuration");
        assertTrue(result.isPresent());
        assertEquals(Duration.ofSeconds(5), result.get());
    }

    @Test
    void testExtractMissingPath() {
        Config config = ConfigFactory.parseString("other = 1");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, int.class, "missing");
        assertFalse(result.isPresent());
    }

    @Test
    void testExtractByteArray() {
        String base64 = java.util.Base64.getEncoder().encodeToString("hello".getBytes());
        Config config = ConfigFactory.parseString("myBytes = \"" + base64 + "\"");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, byte[].class, "myBytes");
        assertTrue(result.isPresent());
        assertArrayEquals("hello".getBytes(), (byte[]) result.get());
    }

    @Test
    void testExtractFloat() {
        Config config = ConfigFactory.parseString("myFloat = 1.5");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, float.class, "myFloat");
        assertTrue(result.isPresent());
        assertEquals(1.5f, (float) result.get(), 0.001);
    }

    @Test
    void testExtractShort() {
        Config config = ConfigFactory.parseString("myShort = 100");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, short.class, "myShort");
        assertTrue(result.isPresent());
        assertEquals((short) 100, result.get());
    }

    @Test
    void testExtractByte() {
        Config config = ConfigFactory.parseString("myByte = 7");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, byte.class, "myByte");
        assertTrue(result.isPresent());
        assertEquals((byte) 7, result.get());
    }
}
