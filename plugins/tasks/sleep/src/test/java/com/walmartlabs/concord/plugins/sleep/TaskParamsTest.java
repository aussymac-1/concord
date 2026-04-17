package com.walmartlabs.concord.plugins.sleep;

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

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskParamsTest {

    @Test
    void durationReadsDurationKey() {
        TaskParams p = new TaskParams(Map.of(Constants.DURATION_KEY, 5));

        assertThat(p.duration()).isEqualTo(5);
    }

    @Test
    void durationReturnsNullWhenMissing() {
        TaskParams p = new TaskParams(Map.of());

        assertThat(p.duration()).isNull();
    }

    @Test
    void untilReturnsNullWhenMissing() {
        TaskParams p = new TaskParams(Map.of());

        assertThat(p.until()).isNull();
    }

    @Test
    void untilAcceptsDate() {
        Date expected = Date.from(Instant.parse("2024-01-02T03:04:05Z"));
        TaskParams p = new TaskParams(Map.of(Constants.UNTIL_KEY, expected));

        assertThat(p.until()).isEqualTo(expected.toInstant());
    }

    @Test
    void untilAcceptsIsoString() {
        ZonedDateTime zdt = ZonedDateTime.of(2024, 5, 6, 7, 8, 9, 0, ZoneOffset.UTC);
        String iso = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zdt);
        TaskParams p = new TaskParams(Map.of(Constants.UNTIL_KEY, iso));

        assertThat(p.until()).isEqualTo(zdt.toInstant());
    }

    @Test
    void untilThrowsForMalformedString() {
        TaskParams p = new TaskParams(Map.of(Constants.UNTIL_KEY, "not-a-date"));

        assertThatThrownBy(p::until)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not-a-date");
    }

    @Test
    void untilThrowsForUnsupportedType() {
        Map<String, Object> input = new HashMap<>();
        input.put(Constants.UNTIL_KEY, 42);
        TaskParams p = new TaskParams(input);

        assertThatThrownBy(p::until)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void suspendDefaultsToFalse() {
        TaskParams p = new TaskParams(Map.of());

        assertThat(p.suspend()).isFalse();
    }

    @Test
    void suspendReadsBoolean() {
        TaskParams p = new TaskParams(Map.of(Constants.SUSPEND_KEY, true));

        assertThat(p.suspend()).isTrue();
    }
}
