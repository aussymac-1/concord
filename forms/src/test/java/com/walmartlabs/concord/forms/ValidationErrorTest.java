package com.walmartlabs.concord.forms;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ValidationErrorTest {

    @Test
    public void ofBuildsFieldAndErrorMessages() {
        var e = ValidationError.of("name", "required");
        assertEquals("name", e.fieldName());
        assertEquals("required", e.error());
    }

    @Test
    public void builderYieldsSameResultAsOf() {
        var a = ValidationError.of("x", "bad");
        var b = ValidationError.builder().fieldName("x").error("bad").build();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void globalErrorConstantHasStableValue() {
        assertEquals("_global", ValidationError.GLOBAL_ERROR);
    }

    @Test
    public void toStringIsInformative() {
        var s = ValidationError.of("foo", "required").toString();
        assertNotNull(s);
    }
}
