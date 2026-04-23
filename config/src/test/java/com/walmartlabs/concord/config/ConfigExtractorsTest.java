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
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigMemorySize;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigExtractorsTest {

    @Test
    public void missingPathYieldsEmptyOptional() {
        var config = ConfigFactory.parseMap(Map.of("x", 1));
        assertTrue(ConfigExtractors.extractConfigValue(config, Integer.class, "y").isEmpty());
    }

    @Test
    public void unknownTypeYieldsEmptyOptional() {
        var config = ConfigFactory.parseMap(Map.of("x", 1));
        assertTrue(ConfigExtractors.extractConfigValue(config, Thread.class, "x").isEmpty());
    }

    @Test
    public void extractsAllPrimitiveLikeTypes() {
        var config = ConfigFactory.parseString(""
                + "b = true\n"
                + "byte = 127\n"
                + "short = 12345\n"
                + "int = 42\n"
                + "long = 9999999999\n"
                + "float = 1.5\n"
                + "double = 2.5\n"
                + "s = \"hello\"\n"
                + "p = \"/tmp/x\"\n"
                + "duration = 10s\n"
                + "memory = 1024\n"
                + "bytes = \"" + Base64.getEncoder().encodeToString(new byte[]{1, 2, 3}) + "\"\n"
                + "nested { k = v }\n"
                + "list = [1,2,3]\n");

        assertEquals(true,
                ConfigExtractors.extractConfigValue(config, boolean.class, "b").orElseThrow());
        assertEquals((byte) 127,
                ConfigExtractors.extractConfigValue(config, byte.class, "byte").orElseThrow());
        assertEquals((byte) 127,
                ConfigExtractors.extractConfigValue(config, Byte.class, "byte").orElseThrow());
        assertEquals((short) 12345,
                ConfigExtractors.extractConfigValue(config, short.class, "short").orElseThrow());
        assertEquals((short) 12345,
                ConfigExtractors.extractConfigValue(config, Short.class, "short").orElseThrow());
        assertEquals(42,
                ConfigExtractors.extractConfigValue(config, int.class, "int").orElseThrow());
        assertEquals(42,
                ConfigExtractors.extractConfigValue(config, Integer.class, "int").orElseThrow());
        assertEquals(9999999999L,
                ConfigExtractors.extractConfigValue(config, long.class, "long").orElseThrow());
        assertEquals(9999999999L,
                ConfigExtractors.extractConfigValue(config, Long.class, "long").orElseThrow());
        assertEquals(1.5f,
                ConfigExtractors.extractConfigValue(config, float.class, "float").orElseThrow());
        assertEquals(1.5f,
                ConfigExtractors.extractConfigValue(config, Float.class, "float").orElseThrow());
        assertEquals(2.5,
                ConfigExtractors.extractConfigValue(config, double.class, "double").orElseThrow());
        assertEquals(2.5,
                ConfigExtractors.extractConfigValue(config, Double.class, "double").orElseThrow());
        assertEquals("hello",
                ConfigExtractors.extractConfigValue(config, String.class, "s").orElseThrow());
        assertEquals(Paths.get("/tmp/x"),
                ConfigExtractors.extractConfigValue(config, Path.class, "p").orElseThrow());
        assertEquals(Duration.ofSeconds(10),
                ConfigExtractors.extractConfigValue(config, Duration.class, "duration").orElseThrow());
        assertInstanceOf(ConfigMemorySize.class,
                ConfigExtractors.extractConfigValue(config, ConfigMemorySize.class, "memory").orElseThrow());
        assertArrayEquals(new byte[]{1, 2, 3}, (byte[])
                ConfigExtractors.extractConfigValue(config, byte[].class, "bytes").orElseThrow());

        // Any-ref / Config / ConfigObject / ConfigValue / ConfigList
        assertNotNull(ConfigExtractors.extractConfigValue(config, Object.class, "int").orElseThrow());
        assertInstanceOf(Config.class,
                ConfigExtractors.extractConfigValue(config, Config.class, "nested").orElseThrow());
        assertInstanceOf(ConfigObject.class,
                ConfigExtractors.extractConfigValue(config, ConfigObject.class, "nested").orElseThrow());
        assertInstanceOf(ConfigValue.class,
                ConfigExtractors.extractConfigValue(config, ConfigValue.class, "int").orElseThrow());
        assertInstanceOf(ConfigList.class,
                ConfigExtractors.extractConfigValue(config, ConfigList.class, "list").orElseThrow());
    }

    @Test
    public void eachExtractorAdvertisesAtLeastOneMatchingClass() {
        for (var ext : ConfigExtractors.values()) {
            assertNotNull(ext.getMatchingClasses());
            assertTrue(ext.getMatchingClasses().length > 0);
        }
    }

    @Test
    public void valueOfReturnsEnumInstance() {
        assertEquals(ConfigExtractors.INTEGER, ConfigExtractors.valueOf("INTEGER"));
    }
}
