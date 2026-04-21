package com.walmartlabs.concord.plugins.misc;

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

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class DateTimeTaskV2Test {

    @Test
    public void testCurrentReturnsNonNullDate() {
        DateTimeTaskV2 task = new DateTimeTaskV2();
        assertNotNull(task.current());
    }

    @Test
    public void testCurrentWithPatternReturnsFormattedString() {
        DateTimeTaskV2 task = new DateTimeTaskV2();

        String formatted = task.current("yyyy-MM-dd");
        assertTrue(formatted.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testCurrentISOMatchesExpectedShape() {
        DateTimeTaskV2 task = new DateTimeTaskV2();

        String iso = task.currentISO();
        // yyyy-MM-dd'T'HH:mm:ss.SSSXXX
        assertTrue(iso.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}([+-]\\d{2}:\\d{2}|Z)"),
                "unexpected ISO shape: " + iso);
    }

    @Test
    public void testCurrentWithZoneIsDeterministicForFixedOffset() {
        DateTimeTaskV2 task = new DateTimeTaskV2();

        assertEquals("+09:00", task.currentWithZone("Asia/Tokyo", "XXX"));
        assertEquals("-05:00", task.currentWithZone("-05:00", "XXX"));
    }

    @Test
    public void testFormatRoundTripsWithParse() throws ParseException {
        DateTimeTaskV2 task = new DateTimeTaskV2();

        Date parsed = task.parse("2021-06-15", "yyyy-MM-dd");
        assertEquals("2021-06-15", task.format(parsed, "yyyy-MM-dd"));
    }

    @Test
    public void testParseInvalidInputThrows() {
        DateTimeTaskV2 task = new DateTimeTaskV2();
        assertThrows(ParseException.class, () -> task.parse("nope", "yyyy-MM-dd"));
    }
}
