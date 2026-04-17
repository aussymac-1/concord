package com.walmartlabs.concord.server.sdk;

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

class RangeTest {

    @Test
    void buildsInclusiveInclusiveRange() {
        Range r = Range.builder()
                .lowerMode(Range.Mode.INCLUSIVE)
                .lower(0)
                .upper(10)
                .upperMode(Range.Mode.INCLUSIVE)
                .build();

        assertThat(r.lowerMode()).isEqualTo(Range.Mode.INCLUSIVE);
        assertThat(r.upperMode()).isEqualTo(Range.Mode.INCLUSIVE);
        assertThat(r.lower()).isEqualTo(0);
        assertThat(r.upper()).isEqualTo(10);
    }

    @Test
    void buildsExclusiveExclusiveRange() {
        Range r = Range.builder()
                .lowerMode(Range.Mode.EXCLUSIVE)
                .lower(-5)
                .upper(5)
                .upperMode(Range.Mode.EXCLUSIVE)
                .build();

        assertThat(r.lowerMode()).isEqualTo(Range.Mode.EXCLUSIVE);
        assertThat(r.upperMode()).isEqualTo(Range.Mode.EXCLUSIVE);
        assertThat(r.lower()).isEqualTo(-5);
        assertThat(r.upper()).isEqualTo(5);
    }

    @Test
    void equalsAndHashCodeMatchForSameValues() {
        Range r1 = Range.builder()
                .lowerMode(Range.Mode.INCLUSIVE).lower(1)
                .upper(2).upperMode(Range.Mode.EXCLUSIVE)
                .build();
        Range r2 = Range.builder()
                .lowerMode(Range.Mode.INCLUSIVE).lower(1)
                .upper(2).upperMode(Range.Mode.EXCLUSIVE)
                .build();

        assertThat(r1).isEqualTo(r2).hasSameHashCodeAs(r2);
    }

    @Test
    void modeEnumHasExpectedValues() {
        assertThat(Range.Mode.values()).containsExactly(Range.Mode.INCLUSIVE, Range.Mode.EXCLUSIVE);
        assertThat(Range.Mode.valueOf("INCLUSIVE")).isEqualTo(Range.Mode.INCLUSIVE);
        assertThat(Range.Mode.valueOf("EXCLUSIVE")).isEqualTo(Range.Mode.EXCLUSIVE);
    }
}
