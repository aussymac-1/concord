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
import com.typesafe.config.ConfigObject;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListExtractorsTest {

    @Test
    public void unknownTypeYieldsEmpty() {
        var config = ConfigFactory.parseString("list = [1,2,3]");
        assertTrue(ListExtractors.extractConfigListValue(config, Thread.class, "list").isEmpty());
    }

    @Test
    public void extractsPrimitiveLists() {
        var config = ConfigFactory.parseString(""
                + "bools = [true, false]\n"
                + "ints  = [1, 2, 3]\n"
                + "doubles = [1.0, 2.0]\n"
                + "longs = [100000000000, 200000000000]\n"
                + "strings = [\"a\", \"b\"]\n"
                + "durations = [1s, 2s]\n"
                + "memories = [1024, 2048]\n"
                + "objects = [1, \"two\", true]\n"
                + "configs = [{a: 1}, {b: 2}]\n");

        assertEquals(List.of(true, false),
                ListExtractors.extractConfigListValue(config, Boolean.class, "bools").orElseThrow());
        assertEquals(List.of(1, 2, 3),
                ListExtractors.extractConfigListValue(config, Integer.class, "ints").orElseThrow());
        assertEquals(List.of(1.0, 2.0),
                ListExtractors.extractConfigListValue(config, Double.class, "doubles").orElseThrow());
        assertEquals(List.of(100000000000L, 200000000000L),
                ListExtractors.extractConfigListValue(config, Long.class, "longs").orElseThrow());
        assertEquals(List.of("a", "b"),
                ListExtractors.extractConfigListValue(config, String.class, "strings").orElseThrow());
        assertEquals(List.of(Duration.ofSeconds(1), Duration.ofSeconds(2)),
                ListExtractors.extractConfigListValue(config, Duration.class, "durations").orElseThrow());
        assertEquals(2,
                ListExtractors.extractConfigListValue(config, com.typesafe.config.ConfigMemorySize.class, "memories").orElseThrow().size());
        assertNotNull(ListExtractors.extractConfigListValue(config, Object.class, "objects").orElseThrow());
        assertNotNull(ListExtractors.extractConfigListValue(config, Config.class, "configs").orElseThrow());
        // ConfigObject is mapped to object list via the enum; just verify that a list comes back.
        var objectList = ListExtractors.extractConfigListValue(config, ConfigObject.class, "configs").orElseThrow();
        assertNotNull(objectList);
    }

    @Test
    public void eachExtractorAdvertisesAMatchingType() {
        for (var ext : ListExtractors.values()) {
            assertNotNull(ext.getMatchingParameterizedType());
        }
    }

    @Test
    public void valueOfReturnsEnumInstance() {
        assertEquals(ListExtractors.STRING, ListExtractors.valueOf("STRING"));
    }
}
