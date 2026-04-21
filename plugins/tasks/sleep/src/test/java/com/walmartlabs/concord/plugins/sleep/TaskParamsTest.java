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

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TaskParamsTest {

    @Test
    public void testDurationReturnsNullWhenNotSet() {
        TaskParams p = new TaskParams(Collections.emptyMap());
        assertNull(p.duration());
    }

    @Test
    public void testDurationReturnsNumber() {
        Map<String, Object> m = new HashMap<>();
        m.put(Constants.DURATION_KEY, 5);
        TaskParams p = new TaskParams(m);

        assertEquals(5, p.duration().intValue());
    }

    @Test
    public void testUntilReturnsNullWhenNotSet() {
        TaskParams p = new TaskParams(Collections.emptyMap());
        assertNull(p.until());
    }

    @Test
    public void testUntilFromDate() {
        Date d = new Date(1_700_000_000_000L);
        Map<String, Object> m = new HashMap<>();
        m.put(Constants.UNTIL_KEY, d);
        TaskParams p = new TaskParams(m);

        assertEquals(d.toInstant(), p.until());
    }

    @Test
    public void testUntilFromIsoString() {
        Map<String, Object> m = new HashMap<>();
        m.put(Constants.UNTIL_KEY, "2020-01-02T03:04:05+00:00");
        TaskParams p = new TaskParams(m);

        assertEquals(Instant.parse("2020-01-02T03:04:05Z"), p.until());
    }

    @Test
    public void testUntilFromInvalidStringThrows() {
        Map<String, Object> m = new HashMap<>();
        m.put(Constants.UNTIL_KEY, "not-a-datetime");
        TaskParams p = new TaskParams(m);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, p::until);
        assertTrue(ex.getMessage().contains("Invalid datetime string"));
    }

    @Test
    public void testUntilFromUnsupportedTypeThrows() {
        Map<String, Object> m = new HashMap<>();
        m.put(Constants.UNTIL_KEY, 12345);
        TaskParams p = new TaskParams(m);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, p::until);
        assertTrue(ex.getMessage().contains("Invalid variable"));
    }

    @Test
    public void testSuspendDefaultsToFalse() {
        assertFalse(new TaskParams(Collections.emptyMap()).suspend());
    }

    @Test
    public void testSuspendTrue() {
        Map<String, Object> m = new HashMap<>();
        m.put(Constants.SUSPEND_KEY, true);

        assertTrue(new TaskParams(m).suspend());
    }
}
