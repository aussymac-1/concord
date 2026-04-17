package com.walmartlabs.concord.runtime.model;

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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LocationTest {

    @Test
    void builderCreatesLocationWithDefaults() {
        Location loc = Location.builder().build();
        assertThat(loc.lineNum()).isEqualTo(-1);
        assertThat(loc.column()).isEqualTo(-1);
        assertThat(loc.fileName()).isNull();
    }

    @Test
    void builderSetsAllFields() {
        Location loc = Location.builder()
                .lineNum(10)
                .column(5)
                .fileName("test.yml")
                .build();
        assertThat(loc.lineNum()).isEqualTo(10);
        assertThat(loc.column()).isEqualTo(5);
        assertThat(loc.fileName()).isEqualTo("test.yml");
    }

    @Test
    void toShortStringWithFileName() {
        Location loc = Location.builder()
                .lineNum(10)
                .column(5)
                .fileName("test.yml")
                .build();
        assertThat(Location.toShortString(loc)).isEqualTo("line: 10, col: 5");
    }

    @Test
    void toShortStringWithNullLocation() {
        assertThat(Location.toShortString(null)).isEqualTo("n/a");
    }

    @Test
    void toShortStringWithNullFileName() {
        Location loc = Location.builder().lineNum(1).column(1).build();
        assertThat(Location.toShortString(loc)).isEqualTo("n/a");
    }

    @Test
    void toErrorPrefixWithFileName() {
        Location loc = Location.builder()
                .lineNum(10)
                .column(5)
                .fileName("test.yml")
                .build();
        String result = Location.toErrorPrefix(loc);
        assertThat(result).contains("test.yml").contains("Error @").contains("line: 10");
    }

    @Test
    void toErrorPrefixWithNullLocation() {
        assertThat(Location.toErrorPrefix(null)).isEqualTo("(n/a): Error.");
    }

    @Test
    void toErrorPrefixWithNullFileName() {
        Location loc = Location.builder().build();
        assertThat(Location.toErrorPrefix(loc)).isEqualTo("(n/a): Error.");
    }
}
