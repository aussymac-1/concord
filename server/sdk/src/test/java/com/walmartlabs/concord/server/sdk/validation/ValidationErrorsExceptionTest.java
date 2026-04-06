package com.walmartlabs.concord.server.sdk.validation;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2024 Walmart Inc.
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidationErrorsExceptionTest {

    @Test
    void testEmptyConstructor() {
        var ex = new ValidationErrorsException();
        assertFalse(ex.hasValidationErrors());
        assertEquals("(No validation errors)", ex.getMessage());
    }

    @Test
    void testMessageConstructor() {
        var ex = new ValidationErrorsException("field is required");
        assertTrue(ex.hasValidationErrors());
        assertEquals("field is required", ex.getMessage());
        assertEquals(1, ex.getValidationErrors().size());
        assertEquals("*", ex.getValidationErrors().get(0).getId());
    }

    @Test
    void testIdMessageConstructor() {
        var ex = new ValidationErrorsException("name", "Name is required");
        assertTrue(ex.hasValidationErrors());
        assertEquals("Name is required", ex.getMessage());
        assertEquals("name", ex.getValidationErrors().get(0).getId());
    }

    @Test
    void testWithError() {
        var ex = new ValidationErrorsException()
                .withError("error1")
                .withError("error2");
        assertEquals(2, ex.getValidationErrors().size());
        assertEquals("error1, error2", ex.getMessage());
    }

    @Test
    void testWithErrorIdMessage() {
        var ex = new ValidationErrorsException()
                .withError("field1", "is required")
                .withError("field2", "is too long");
        assertEquals(2, ex.getValidationErrors().size());
        assertEquals("field1", ex.getValidationErrors().get(0).getId());
        assertEquals("field2", ex.getValidationErrors().get(1).getId());
    }

    @Test
    void testWithErrorsVarargs() {
        var ex = new ValidationErrorsException()
                .withErrors(
                        new ValidationErrorXO("err1"),
                        new ValidationErrorXO("err2"));
        assertEquals(2, ex.getValidationErrors().size());
    }

    @Test
    void testWithErrorsList() {
        var errors = List.of(
                new ValidationErrorXO("err1"),
                new ValidationErrorXO("err2"));
        var ex = new ValidationErrorsException().withErrors(errors);
        assertEquals(2, ex.getValidationErrors().size());
    }

    @Test
    void testIsRuntimeException() {
        var ex = new ValidationErrorsException("test");
        assertInstanceOf(RuntimeException.class, ex);
    }
}
