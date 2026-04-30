package com.walmartlabs.concord.forms;

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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultFormValidatorTest {

    private final DefaultFormValidator validator = new DefaultFormValidator();

    @Test
    public void testNoFields() {
        var form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .build();

        var errors = validator.validate(form, Collections.emptyMap());
        assertEquals(1, errors.size());
        assertEquals(ValidationError.GLOBAL_ERROR, errors.get(0).fieldName());
    }

    @Test
    public void testValidStringField() {
        var field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        var data = new HashMap<String, Object>();
        data.put("name", "hello");

        var errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testMissingRequiredField() {
        var field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        var errors = validator.validate(form, Collections.emptyMap());
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testOptionalFieldNoValue() {
        var field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        var errors = validator.validate(form, Collections.emptyMap());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidIntegerField() {
        var field = FormField.builder()
                .name("count")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        var data = new HashMap<String, Object>();
        data.put("count", 42);

        var errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testInvalidIntegerField() {
        var field = FormField.builder()
                .name("count")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        var data = new HashMap<String, Object>();
        data.put("count", "notAnInt");

        var errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testValidBooleanField() {
        var field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        var data = new HashMap<String, Object>();
        data.put("agree", true);

        var errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidDecimalField() {
        var field = FormField.builder()
                .name("amount")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        var data = new HashMap<String, Object>();
        data.put("amount", 3.14);

        var errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testNullDataMap() {
        var field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        var errors = validator.validate(form, null);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testAllowedValueValid() {
        var field = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue("red")
                .build();

        var error = validator.validate("testForm", field, "red", "red");
        assertNull(error);
    }

    @Test
    public void testAllowedValueInvalid() {
        var field = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue("red")
                .build();

        var error = validator.validate("testForm", field, "blue", "red");
        assertNotNull(error);
    }
}
