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

import static org.junit.jupiter.api.Assertions.*;

public class ValidationErrorTest {

    @Test
    public void testFactoryOf() {
        var e = ValidationError.of("name", "must not be empty");
        assertEquals("name", e.fieldName());
        assertEquals("must not be empty", e.error());
    }

    @Test
    public void testBuilder() {
        var e = ValidationError.builder()
                .fieldName(ValidationError.GLOBAL_ERROR)
                .error("oops")
                .build();
        assertEquals("_global", e.fieldName());
        assertEquals("oops", e.error());
    }

    @Test
    public void testEqualsAndHashCode() {
        var a = ValidationError.of("x", "y");
        var b = ValidationError.of("x", "y");
        var c = ValidationError.of("x", "z");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    public void testGlobalErrorConstant() {
        assertEquals("_global", ValidationError.GLOBAL_ERROR);
    }
}
