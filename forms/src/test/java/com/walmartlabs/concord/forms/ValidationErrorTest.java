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
    public void testOfPopulatesFieldAndError() {
        ValidationError e = ValidationError.of("username", "must not be blank");

        assertEquals("username", e.fieldName());
        assertEquals("must not be blank", e.error());
    }

    @Test
    public void testBuilderProducesSameValueAsOf() {
        ValidationError fromBuilder = ValidationError.builder()
                .fieldName("f")
                .error("e")
                .build();
        ValidationError fromOf = ValidationError.of("f", "e");

        assertEquals(fromBuilder, fromOf);
        assertEquals(fromBuilder.hashCode(), fromOf.hashCode());
    }

    @Test
    public void testGlobalErrorSentinelIsPreserved() {
        assertEquals("_global", ValidationError.GLOBAL_ERROR);

        ValidationError e = ValidationError.of(ValidationError.GLOBAL_ERROR, "oops");
        assertEquals(ValidationError.GLOBAL_ERROR, e.fieldName());
    }

    @Test
    public void testEqualsAndInequality() {
        ValidationError a = ValidationError.of("f", "e");
        ValidationError b = ValidationError.of("f", "e");
        ValidationError c = ValidationError.of("f", "different");

        assertEquals(a, b);
        assertNotEquals(a, c);
    }

    @Test
    public void testOfRejectsNullField() {
        assertThrows(NullPointerException.class, () -> ValidationError.of(null, "e"));
    }

    @Test
    public void testOfRejectsNullError() {
        assertThrows(NullPointerException.class, () -> ValidationError.of("f", null));
    }
}
