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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigExtractorsTest {

    @Test
    void extractBooleanValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("flag = true");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, boolean.class, "flag");
        assertThat(result).isPresent().contains(true);
    }

    @Test
    void extractBooleanBoxedValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("flag = false");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Boolean.class, "flag");
        assertThat(result).isPresent().contains(false);
    }

    @Test
    void extractByteValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("val = 42");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, byte.class, "val");
        assertThat(result).isPresent().contains((byte) 42);
    }

    @Test
    void extractShortValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("val = 1000");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, short.class, "val");
        assertThat(result).isPresent().contains((short) 1000);
    }

    @Test
    void extractIntegerValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("count = 99");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, int.class, "count");
        assertThat(result).isPresent().contains(99);
    }

    @Test
    void extractIntegerBoxedValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("count = 99");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Integer.class, "count");
        assertThat(result).isPresent().contains(99);
    }

    @Test
    void extractLongValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("big = 9999999999");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, long.class, "big");
        assertThat(result).isPresent().contains(9999999999L);
    }

    @Test
    void extractFloatValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("ratio = 3.14");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, float.class, "ratio");
        assertThat(result).isPresent();
        assertThat((float) result.get()).isEqualTo(3.14f, org.assertj.core.data.Offset.offset(0.001f));
    }

    @Test
    void extractDoubleValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("ratio = 3.14159");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, double.class, "ratio");
        assertThat(result).isPresent().contains(3.14159);
    }

    @Test
    void extractStringValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("name = hello");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, String.class, "name");
        assertThat(result).isPresent().contains("hello");
    }

    @Test
    void extractPathValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("dir = /tmp/test");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Path.class, "dir");
        assertThat(result).isPresent().contains(Paths.get("/tmp/test"));
    }

    @Test
    void extractAnyRefValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("val = 123");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Object.class, "val");
        assertThat(result).isPresent();
    }

    @Test
    void extractConfigValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("sub { key = value }");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, com.typesafe.config.Config.class, "sub");
        assertThat(result).isPresent();
        assertThat(((com.typesafe.config.Config) result.get()).getString("key")).isEqualTo("value");
    }

    @Test
    void extractConfigObjectValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("sub { key = value }");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, ConfigObject.class, "sub");
        assertThat(result).isPresent();
    }

    @Test
    void extractConfigValueType() {
        com.typesafe.config.Config config = ConfigFactory.parseString("val = hello");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, ConfigValue.class, "val");
        assertThat(result).isPresent();
    }

    @Test
    void extractConfigListValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("items = [1, 2, 3]");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, ConfigList.class, "items");
        assertThat(result).isPresent();
    }

    @Test
    void extractDurationValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("timeout = 5s");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Duration.class, "timeout");
        assertThat(result).isPresent().contains(Duration.ofSeconds(5));
    }

    @Test
    void extractMemorySizeValue() {
        com.typesafe.config.Config config = ConfigFactory.parseString("size = 1024k");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, ConfigMemorySize.class, "size");
        assertThat(result).isPresent();
    }

    @Test
    void extractByteArrayValue() {
        String encoded = Base64.getEncoder().encodeToString("hello".getBytes());
        com.typesafe.config.Config config = ConfigFactory.parseString("data = \"" + encoded + "\"");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, byte[].class, "data");
        assertThat(result).isPresent();
        assertThat((byte[]) result.get()).isEqualTo("hello".getBytes());
    }

    @Test
    void extractMissingPathReturnsEmpty() {
        com.typesafe.config.Config config = ConfigFactory.parseString("a = 1");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, int.class, "missing");
        assertThat(result).isEmpty();
    }

    @Test
    void extractUnknownClassReturnsEmpty() {
        com.typesafe.config.Config config = ConfigFactory.parseString("val = 1");
        Optional<Object> result = ConfigExtractors.extractConfigValue(config, Thread.class, "val");
        assertThat(result).isEmpty();
    }

    @Test
    void matchingClassesContainBothPrimitiveAndBoxed() {
        Class<?>[] classes = ConfigExtractors.BOOLEAN.getMatchingClasses();
        assertThat(classes).contains(boolean.class, Boolean.class);
    }
}
