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
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ListExtractorsTest {

    @SuppressWarnings("unchecked")
    @Test
    void extractBooleanList() {
        com.typesafe.config.Config config = ConfigFactory.parseString("flags = [true, false, true]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Boolean.class, "flags");
        assertThat(result).isPresent();
        assertThat((List<Boolean>) result.get()).containsExactly(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
    }

    @SuppressWarnings("unchecked")
    @Test
    void extractIntegerList() {
        com.typesafe.config.Config config = ConfigFactory.parseString("nums = [1, 2, 3]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Integer.class, "nums");
        assertThat(result).isPresent();
        assertThat((List<Integer>) result.get()).containsExactly(1, 2, 3);
    }

    @SuppressWarnings("unchecked")
    @Test
    void extractDoubleList() {
        com.typesafe.config.Config config = ConfigFactory.parseString("vals = [1.1, 2.2]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Double.class, "vals");
        assertThat(result).isPresent();
        assertThat((List<Double>) result.get()).containsExactly(1.1, 2.2);
    }

    @SuppressWarnings("unchecked")
    @Test
    void extractLongList() {
        com.typesafe.config.Config config = ConfigFactory.parseString("big = [100, 200]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Long.class, "big");
        assertThat(result).isPresent();
        assertThat((List<Long>) result.get()).containsExactly(100L, 200L);
    }

    @SuppressWarnings("unchecked")
    @Test
    void extractStringList() {
        com.typesafe.config.Config config = ConfigFactory.parseString("names = [\"a\", \"b\"]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, String.class, "names");
        assertThat(result).isPresent();
        assertThat((List<String>) result.get()).containsExactly("a", "b");
    }

    @SuppressWarnings("unchecked")
    @Test
    void extractDurationList() {
        com.typesafe.config.Config config = ConfigFactory.parseString("timeouts = [1s, 2s]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Duration.class, "timeouts");
        assertThat(result).isPresent();
        assertThat((List<Duration>) result.get()).containsExactly(Duration.ofSeconds(1), Duration.ofSeconds(2));
    }

    @Test
    void extractObjectList() {
        com.typesafe.config.Config config = ConfigFactory.parseString("items = [1, \"hello\", true]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Object.class, "items");
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(3);
    }

    @Test
    void extractConfigList() {
        com.typesafe.config.Config config = ConfigFactory.parseString("items = [{ a = 1 }, { b = 2 }]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, com.typesafe.config.Config.class, "items");
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(2);
    }

    @Test
    void unknownTypeReturnsEmpty() {
        com.typesafe.config.Config config = ConfigFactory.parseString("items = [1]");
        Optional<List<?>> result = ListExtractors.extractConfigListValue(config, Thread.class, "items");
        assertThat(result).isEmpty();
    }

    @Test
    void matchingParameterizedTypeReturnsCorrectClass() {
        assertThat(ListExtractors.BOOLEAN.getMatchingParameterizedType()).isEqualTo(Boolean.class);
        assertThat(ListExtractors.STRING.getMatchingParameterizedType()).isEqualTo(String.class);
        assertThat(ListExtractors.INTEGER.getMatchingParameterizedType()).isEqualTo(Integer.class);
    }
}
