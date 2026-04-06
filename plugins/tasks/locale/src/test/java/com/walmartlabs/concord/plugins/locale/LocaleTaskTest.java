package com.walmartlabs.concord.plugins.locale;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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

import static org.junit.jupiter.api.Assertions.*;

class LocaleTaskTest {

    @Test
    void testCountriesNotEmpty() {
        var task = new LocaleTask();
        var countries = task.countries();
        assertNotNull(countries);
        assertTrue(countries.length > 0);
    }

    @Test
    void testCountriesContainsUS() {
        var task = new LocaleTask();
        var countries = task.countries();
        var found = false;
        for (var c : countries) {
            if ("US".equals(c)) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Should contain US country code");
    }

    @Test
    void testCountriesV2NotEmpty() {
        var task = new LocaleTaskV2();
        var countries = task.countries();
        assertNotNull(countries);
        assertTrue(countries.length > 0);
    }

    @Test
    void testCountriesV2ContainsUS() {
        var task = new LocaleTaskV2();
        var countries = task.countries();
        var found = false;
        for (var c : countries) {
            if ("US".equals(c)) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Should contain US country code");
    }
}
