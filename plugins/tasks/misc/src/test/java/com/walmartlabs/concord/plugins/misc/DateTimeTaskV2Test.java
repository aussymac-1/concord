package com.walmartlabs.concord.plugins.misc;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2020 Walmart Inc.
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeTaskV2Test {

    @Test
    void testCurrent() {
        var task = new DateTimeTaskV2();
        var now = task.current();
        assertNotNull(now);
        assertTrue(Math.abs(now.getTime() - System.currentTimeMillis()) < 5000);
    }

    @Test
    void testCurrentWithPattern() {
        var task = new DateTimeTaskV2();
        var result = task.current("yyyy-MM-dd");
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    void testCurrentISO() {
        var task = new DateTimeTaskV2();
        var result = task.currentISO();
        assertNotNull(result);
        assertTrue(result.contains("T"));
    }

    @Test
    void testCurrentWithZone() {
        var task = new DateTimeTaskV2();
        var result = task.currentWithZone("UTC", "yyyy-MM-dd HH:mm");
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"));
    }

    @Test
    void testFormat() {
        var task = new DateTimeTaskV2();
        var date = Date.from(ZonedDateTime.of(1970, 6, 15, 12, 0, 0, 0, ZoneId.systemDefault()).toInstant());
        var result = task.format(date, "yyyy");
        assertNotNull(result);
        assertEquals("1970", result);
    }

    @Test
    void testParse() throws ParseException {
        var task = new DateTimeTaskV2();
        var result = task.parse("2023-06-15", "yyyy-MM-dd");
        assertNotNull(result);
    }
}
