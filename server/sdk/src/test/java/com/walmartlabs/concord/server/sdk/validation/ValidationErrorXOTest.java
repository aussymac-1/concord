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

import static org.junit.jupiter.api.Assertions.*;

class ValidationErrorXOTest {

    @Test
    void testDefaultConstructor() {
        var error = new ValidationErrorXO();
        assertEquals("*", error.getId());
        assertNull(error.getMessage());
    }

    @Test
    void testMessageConstructor() {
        var error = new ValidationErrorXO("test message");
        assertEquals("*", error.getId());
        assertEquals("test message", error.getMessage());
    }

    @Test
    void testIdMessageConstructor() {
        var error = new ValidationErrorXO("name", "Name is required");
        assertEquals("name", error.getId());
        assertEquals("Name is required", error.getMessage());
    }

    @Test
    void testIdNullDefaultsToGeneric() {
        var error = new ValidationErrorXO(null, "msg");
        assertEquals("*", error.getId());
    }

    @Test
    void testSetId() {
        var error = new ValidationErrorXO();
        error.setId("field");
        assertEquals("field", error.getId());
    }

    @Test
    void testSetIdNullDefaultsToGeneric() {
        var error = new ValidationErrorXO("field", "msg");
        error.setId(null);
        assertEquals("*", error.getId());
    }

    @Test
    void testWithId() {
        var error = new ValidationErrorXO().withId("field");
        assertEquals("field", error.getId());
    }

    @Test
    void testSetMessage() {
        var error = new ValidationErrorXO();
        error.setMessage("new msg");
        assertEquals("new msg", error.getMessage());
    }

    @Test
    void testWithMessage() {
        var error = new ValidationErrorXO().withMessage("msg");
        assertEquals("msg", error.getMessage());
    }

    @Test
    void testToString() {
        var error = new ValidationErrorXO("id", "msg");
        var str = error.toString();
        assertTrue(str.contains("id"));
        assertTrue(str.contains("msg"));
    }

    @Test
    void testGenericConstant() {
        assertEquals("*", ValidationErrorXO.GENERIC);
    }
}
