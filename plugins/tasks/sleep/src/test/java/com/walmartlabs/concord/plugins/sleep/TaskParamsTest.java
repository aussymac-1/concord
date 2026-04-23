package com.walmartlabs.concord.plugins.sleep;

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

import com.walmartlabs.concord.runtime.v2.sdk.MapBackedVariables;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskParamsTest {

    @Test
    public void emptyInputHasDefaults() {
        var p = new TaskParams(Collections.emptyMap());
        assertNull(p.duration());
        assertNull(p.until());
        assertFalse(p.suspend());
    }

    @Test
    public void durationIsReadAsNumber() {
        var p = new TaskParams(Map.of(Constants.DURATION_KEY, 10));
        assertEquals(10L, p.duration().longValue());
    }

    @Test
    public void suspendHonoursExplicitTrue() {
        var p = new TaskParams(Map.of(Constants.SUSPEND_KEY, true));
        assertTrue(p.suspend());
    }

    @Test
    public void untilFromDate() {
        var now = new Date();
        var p = new TaskParams(Map.of(Constants.UNTIL_KEY, now));
        assertEquals(now.toInstant(), p.until());
    }

    @Test
    public void untilFromIsoString() {
        var in = ZonedDateTime.of(2025, 6, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        var s = in.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        var p = new TaskParams(Map.of(Constants.UNTIL_KEY, s));
        assertEquals(in.toInstant(), p.until());
    }

    @Test
    public void untilFromInvalidStringThrows() {
        var p = new TaskParams(Map.of(Constants.UNTIL_KEY, "not-a-date"));
        var ex = assertThrows(IllegalArgumentException.class, p::until);
        assertTrue(ex.getMessage().contains("Invalid datetime"));
    }

    @Test
    public void untilFromUnsupportedTypeThrows() {
        var p = new TaskParams(Map.of(Constants.UNTIL_KEY, 42));
        var ex = assertThrows(IllegalArgumentException.class, p::until);
        assertTrue(ex.getMessage().contains("Invalid variable"));
    }

    @Test
    public void nullUntilYieldsNullInstant() {
        // getBoolean and getNumber tolerate missing, but UNTIL read uses get(..): a missing key yields null.
        var raw = new HashMap<String, Object>();
        raw.put(Constants.UNTIL_KEY, null);
        var p = new TaskParams(new MapBackedVariables(raw));
        assertNull(p.until());
    }

    @Test
    public void variablesConstructorBypassesMapBackedAdapter() {
        var p = new TaskParams(new MapBackedVariables(Map.of(Constants.DURATION_KEY, 5,
                Constants.SUSPEND_KEY, true)));
        assertEquals(5L, p.duration().longValue());
        assertTrue(p.suspend());
    }

    @Test
    public void untilEpochSecondsRoundtrip() {
        var target = Instant.parse("2030-01-02T03:04:05Z");
        var s = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(target.atOffset(ZoneOffset.UTC));
        var p = new TaskParams(Map.of(Constants.UNTIL_KEY, s));
        assertEquals(target, p.until());
    }
}
