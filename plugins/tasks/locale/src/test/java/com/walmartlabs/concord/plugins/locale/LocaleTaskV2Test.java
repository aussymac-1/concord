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

import com.walmartlabs.concord.runtime.v2.sdk.DryRunReady;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocaleTaskV2Test {

    @Test
    public void countriesReturnsAllIsoCountryCodes() {
        var task = new LocaleTaskV2();
        var result = task.countries();

        assertNotNull(result);
        assertEquals(Locale.getISOCountries().length, result.length);
    }

    @Test
    public void countriesContainsWellKnownCountries() {
        var task = new LocaleTaskV2();
        var list = Arrays.asList(task.countries());

        assertTrue(list.contains("US"));
        assertTrue(list.contains("DE"));
        assertTrue(list.contains("IN"));
    }

    @Test
    public void classIsAnnotatedDryRunReady() {
        assertTrue(LocaleTaskV2.class.isAnnotationPresent(DryRunReady.class));
    }
}
