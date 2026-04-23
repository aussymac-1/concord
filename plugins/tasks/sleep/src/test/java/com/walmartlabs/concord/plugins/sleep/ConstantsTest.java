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

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConstantsTest {

    @Test
    public void keyNamesAreStable() {
        assertEquals("duration", Constants.DURATION_KEY);
        assertEquals("suspend", Constants.SUSPEND_KEY);
        assertEquals("until", Constants.UNTIL_KEY);
    }

    @Test
    public void retryValuesHaveSensibleDefaults() {
        assertTrue(Constants.RETRY_COUNT > 0);
        assertTrue(Constants.RETRY_INTERVAL > 0);
        assertEquals(3, Constants.RETRY_COUNT);
        assertEquals(5000L, Constants.RETRY_INTERVAL);
    }

    @Test
    public void datetimePatternIsIso() {
        assertNotNull(Constants.DATETIME_PATTERN);
        assertTrue(Constants.DATETIME_PATTERN.contains("yyyy"));
        assertTrue(Constants.DATETIME_PATTERN.contains("HH:mm:ss"));
    }

    @Test
    public void allInParamsContainsEveryKey() {
        var params = new HashSet<>(Arrays.asList(Constants.ALL_IN_PARAMS));
        assertEquals(3, params.size());
        assertTrue(params.contains(Constants.DURATION_KEY));
        assertTrue(params.contains(Constants.SUSPEND_KEY));
        assertTrue(params.contains(Constants.UNTIL_KEY));
    }

    @Test
    public void allInParamsAreDistinct() {
        assertEquals(Constants.ALL_IN_PARAMS.length,
                new HashSet<>(Arrays.asList(Constants.ALL_IN_PARAMS)).size());
        assertNotEquals(Constants.DURATION_KEY, Constants.SUSPEND_KEY);
    }

    @Test
    public void constructorIsPrivate() throws Exception {
        var ctor = Constants.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(ctor.getModifiers()));
        ctor.setAccessible(true);
        assertNotNull(ctor.newInstance());
    }

    @Test
    public void allInParamsArrayIsNotEmpty() {
        assertTrue(Constants.ALL_IN_PARAMS.length > 0);
        for (var p : Constants.ALL_IN_PARAMS) {
            assertNotNull(p);
            assertTrue(!p.isEmpty());
        }
    }

    @Test
    public void instantiationViaReflectionDoesNotThrow() {
        assertThrows(NoSuchMethodException.class, () -> Constants.class.getConstructor());
    }
}
