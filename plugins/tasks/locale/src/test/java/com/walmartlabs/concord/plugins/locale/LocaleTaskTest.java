package com.walmartlabs.concord.plugins.locale;

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

import java.util.Arrays;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocaleTaskTest {

    @Test
    public void countriesReturnsIsoCountryCodes() {
        var task = new LocaleTask();
        var result = task.countries();

        assertNotNull(result);
        assertTrue(result.length > 0);
        assertEquals(Locale.getISOCountries().length, result.length);
    }

    @Test
    public void countriesContainsWellKnownCountries() {
        var task = new LocaleTask();
        var result = Arrays.asList(task.countries());

        assertTrue(result.contains("US"));
        assertTrue(result.contains("GB"));
        assertTrue(result.contains("JP"));
    }

    @Test
    public void countriesAreUpperCaseTwoLetterCodes() {
        var task = new LocaleTask();
        for (var code : task.countries()) {
            assertEquals(2, code.length(), () -> "expected 2-letter code, got: " + code);
            assertEquals(code.toUpperCase(Locale.ROOT), code);
        }
    }

    @Test
    public void multipleCallsReturnConsistentResults() {
        var task = new LocaleTask();

        var first = task.countries();
        var second = task.countries();

        assertEquals(first.length, second.length);
        // The JDK typically returns a cached array — but we only assert content equality here.
        assertArraysEqual(first, second);
        // Also sanity-check JDK contract: arrays are equal in content to Locale.getISOCountries().
        assertArraysEqual(Locale.getISOCountries(), first);
        assertSame(first.getClass(), second.getClass());
    }

    private static void assertArraysEqual(String[] a, String[] b) {
        assertEquals(a.length, b.length);
        for (var i = 0; i < a.length; i++) {
            assertEquals(a[i], b[i]);
        }
    }
}
