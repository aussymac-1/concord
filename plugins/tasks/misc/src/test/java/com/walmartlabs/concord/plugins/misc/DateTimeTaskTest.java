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

public class DateTimeTaskTest {

    @Test
    public void testCurrentReturnsNonNullDate() {
        DateTimeTask task = new DateTimeTask();
        assertNotNull(task.current());
    }

    @Test
    public void testCurrentWithPatternReturnsNonEmptyString() {
        DateTimeTask task = new DateTimeTask();

        String formatted = task.current("yyyy-MM-dd");
        assertNotNull(formatted);
        assertTrue(formatted.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testCurrentWithZoneUsesRequestedZone() {
        DateTimeTask task = new DateTimeTask();

        // A fixed-offset zone keeps the output deterministic.
        String utcOffset = task.current("XXX");
        assertNotNull(utcOffset);

        String tokyoOffset = task.currentWithZone("Asia/Tokyo", "XXX");
        assertEquals("+09:00", tokyoOffset);
    }

    @Test
    public void testFormatWithKnownEpoch() {
        DateTimeTask task = new DateTimeTask();
        Date epoch = new Date(0);

        // SimpleDateFormat uses the default timezone; assert only the parts that
        // do not depend on it (the year must be 1969 or 1970).
        String formatted = task.format(epoch, "yyyy");
        assertTrue(formatted.equals("1969") || formatted.equals("1970"),
                "expected 1969 or 1970 but got: " + formatted);
    }

    @Test
    public void testParseRoundTrips() throws ParseException {
        DateTimeTask task = new DateTimeTask();

        Date d = task.parse("2020-01-02", "yyyy-MM-dd");
        String back = task.format(d, "yyyy-MM-dd");
        assertEquals("2020-01-02", back);
    }

    @Test
    public void testParseInvalidInputThrows() {
        DateTimeTask task = new DateTimeTask();
        assertThrows(ParseException.class, () -> task.parse("not-a-date", "yyyy-MM-dd"));
    }
}
