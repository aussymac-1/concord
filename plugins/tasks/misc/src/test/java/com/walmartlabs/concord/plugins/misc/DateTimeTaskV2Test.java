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
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateTimeTaskV2Test {

    private final DateTimeTaskV2 task = new DateTimeTaskV2();

    @Test
    public void currentReturnsDateNearNow() {
        var before = new Date();
        var result = task.current();
        var after = new Date();
        assertNotNull(result);
        assertTrue(result.getTime() >= before.getTime());
        assertTrue(result.getTime() <= after.getTime());
    }

    @Test
    public void currentWithPatternReturnsFormattedString() {
        var result = task.current("yyyy");
        assertNotNull(result);
        var year = Integer.parseInt(result);
        assertTrue(year >= 2024 && year <= 2100);
    }

    @Test
    public void currentISOReturnsIsoFormattedString() {
        var result = task.currentISO();
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
    }

    @Test
    public void currentWithZoneUsesGivenZone() {
        var result = task.currentWithZone("America/New_York", "yyyy-MM-dd");
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void formatProducesExpectedOutput() {
        var d = new Date(0);
        var result = task.format(d, "yyyy");
        assertNotNull(result);
        assertEquals("1970", result);
    }

    @Test
    public void parseRoundtripsWithFormat() throws ParseException {
        var pattern = "yyyy-MM-dd";
        var src = "2025-06-15";
        var d = task.parse(src, pattern);
        assertNotNull(d);
        assertEquals(src, new SimpleDateFormat(pattern).format(d));
    }
}
