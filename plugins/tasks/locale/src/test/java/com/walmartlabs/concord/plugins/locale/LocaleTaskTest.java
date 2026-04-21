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

import static org.junit.jupiter.api.Assertions.*;

public class LocaleTaskTest {

    @Test
    public void testCountriesReturnsIsoList() {
        String[] countries = new LocaleTask().countries();

        assertNotNull(countries);
        assertTrue(countries.length > 0);
        // Sanity-check a handful of well-known ISO-3166 alpha-2 codes.
        assertTrue(Arrays.asList(countries).contains("US"));
        assertTrue(Arrays.asList(countries).contains("GB"));
        assertTrue(Arrays.asList(countries).contains("JP"));
    }

    @Test
    public void testCountriesMatchesJdkLocaleList() {
        String[] countries = new LocaleTask().countries();
        assertArrayEquals(Locale.getISOCountries(), countries);
    }
}
