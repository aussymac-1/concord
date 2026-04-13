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

import static org.junit.jupiter.api.Assertions.*;

public class DateTimeTaskTest {

    private final DateTimeTask task = new DateTimeTask();

    @Test
    public void testCurrent() {
        var before = new Date();
        var result = task.current();
        var after = new Date();
        assertNotNull(result);
        assertTrue(!result.before(before) && !result.after(after));
    }

    @Test
    public void testCurrentWithPattern() {
        var result = task.current("yyyy-MM-dd");
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testCurrentWithZone() {
        var result = task.currentWithZone("UTC", "yyyy-MM-dd");
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testFormat() {
        var date = new Date(0);
        var result = task.format(date, "yyyy");
        assertNotNull(result);
        assertEquals("1970", result);
    }

    @Test
    public void testParse() throws ParseException {
        var result = task.parse("2024-01-15", "yyyy-MM-dd");
        assertNotNull(result);
    }

    @Test
    public void testParseInvalid() {
        assertThrows(ParseException.class, () -> task.parse("not-a-date", "yyyy-MM-dd"));
    }
}
