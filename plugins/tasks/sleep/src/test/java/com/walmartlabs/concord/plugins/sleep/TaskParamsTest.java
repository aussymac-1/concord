package com.walmartlabs.concord.plugins.sleep;

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

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TaskParamsTest {

    @Test
    void testDuration() {
        var input = new HashMap<String, Object>();
        input.put("duration", 5);
        var params = new TaskParams(input);
        assertEquals(5, params.duration());
    }

    @Test
    void testDurationNull() {
        var input = new HashMap<String, Object>();
        var params = new TaskParams(input);
        assertNull(params.duration());
    }

    @Test
    void testUntilWithDate() {
        var input = new HashMap<String, Object>();
        var future = new Date(System.currentTimeMillis() + 60000);
        input.put("until", future);
        var params = new TaskParams(input);
        var result = params.until();
        assertNotNull(result);
    }

    @Test
    void testUntilWithString() {
        var input = new HashMap<String, Object>();
        var dateStr = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now().plusHours(1));
        input.put("until", dateStr);
        var params = new TaskParams(input);
        var result = params.until();
        assertNotNull(result);
    }

    @Test
    void testUntilNull() {
        var input = new HashMap<String, Object>();
        var params = new TaskParams(input);
        assertNull(params.until());
    }

    @Test
    void testUntilInvalidString() {
        var input = new HashMap<String, Object>();
        input.put("until", "not-a-date");
        var params = new TaskParams(input);
        assertThrows(IllegalArgumentException.class, params::until);
    }

    @Test
    void testUntilInvalidType() {
        var input = new HashMap<String, Object>();
        input.put("until", 12345);
        var params = new TaskParams(input);
        assertThrows(IllegalArgumentException.class, params::until);
    }

    @Test
    void testSuspendDefault() {
        var input = new HashMap<String, Object>();
        var params = new TaskParams(input);
        assertFalse(params.suspend());
    }

    @Test
    void testSuspendTrue() {
        var input = new HashMap<String, Object>();
        input.put("suspend", true);
        var params = new TaskParams(input);
        assertTrue(params.suspend());
    }
}
