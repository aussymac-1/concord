package com.walmartlabs.concord.forms;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2020 Walmart Inc.
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DefaultFormValidatorTest {

    @Test
    void testValidateEmptyForm() {
        var validator = new DefaultFormValidator();
        var form = Form.builder()
                .name("testForm")
                .eventName("testEvent")
                .build();

        List<ValidationError> errors = validator.validate(form, Collections.emptyMap());
        assertFalse(errors.isEmpty());
    }

    @Test
    void testValidateStringFieldValid() {
        var validator = new DefaultFormValidator();

        var field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("myField", "hello");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testValidateIntegerFieldValid() {
        var validator = new DefaultFormValidator();

        var field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("age", 25);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testValidateIntegerFieldWithInvalidType() {
        var validator = new DefaultFormValidator();

        var field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("age", "not-a-number");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
        assertEquals("age", errors.get(0).fieldName());
    }

    @Test
    void testValidateBooleanFieldValid() {
        var validator = new DefaultFormValidator();

        var field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("agree", true);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testValidateBooleanFieldInvalid() {
        var validator = new DefaultFormValidator();

        var field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("agree", "notBoolean");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    void testValidateOptionalFieldAllowsNull() {
        var validator = new DefaultFormValidator();

        var field = FormField.builder()
                .name("optional")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        List<ValidationError> errors = validator.validate(form, Collections.emptyMap());
        assertTrue(errors.isEmpty());
    }

    @Test
    void testValidateWithAllowedValues() {
        var validator = new DefaultFormValidator();

        var field = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue(new java.util.ArrayList<>(java.util.Arrays.asList("red", "green", "blue")))
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("color", "red");
        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());

        data.put("color", "yellow");
        errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    void testValidateDecimalField() {
        var validator = new DefaultFormValidator();

        var field = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("price", 9.99);
        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());

        data.put("price", "not-a-decimal");
        errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }
}
