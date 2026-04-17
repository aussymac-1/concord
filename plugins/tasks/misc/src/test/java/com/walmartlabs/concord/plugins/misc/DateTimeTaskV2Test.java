package com.walmartlabs.concord.plugins.misc;

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

import java.text.ParseException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateTimeTaskV2Test {

    private static final DateTimeTaskV2 task = new DateTimeTaskV2();

    @Test
    void currentReturnsRecentDate() {
        long before = System.currentTimeMillis();
        Date d = task.current();
        long after = System.currentTimeMillis();

        assertThat(d.getTime()).isBetween(before, after);
    }

    @Test
    void formatAndParseRoundTrip() throws ParseException {
        Date src = new Date(1_700_000_000_000L);
        String pattern = "yyyy-MM-dd HH:mm:ss";

        String formatted = task.format(src, pattern);
        Date parsed = task.parse(formatted, pattern);

        assertThat(parsed).isEqualTo(src);
    }

    @Test
    void currentWithPatternProducesParseableString() throws ParseException {
        String formatted = task.current("yyyy-MM-dd");

        Date parsed = task.parse(formatted, "yyyy-MM-dd");
        assertThat(parsed).isNotNull();
    }

    @Test
    void currentWithZoneUsesZoneFormatting() {
        String utc = task.currentWithZone("UTC", "yyyy-MM-dd'T'HH:mm:ssXXX");

        assertThat(utc).endsWith("Z");
    }

    @Test
    void currentISOMatchesExpectedShape() {
        String iso = task.currentISO();

        // yyyy-MM-ddTHH:mm:ss.SSS + zone suffix
        assertThat(iso).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*");
    }

    @Test
    void parseThrowsParseExceptionForInvalidInput() {
        assertThatThrownBy(() -> task.parse("not-a-date", "yyyy-MM-dd"))
                .isInstanceOf(ParseException.class);
    }
}
