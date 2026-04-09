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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultFormValidatorTest {

    private final DefaultFormValidator validator = new DefaultFormValidator();

    @Test
    public void testValidStringField() {
        FormField field = ImmutableFormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = ImmutableForm.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("name", "John");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testMissingRequiredField() {
        FormField field = ImmutableFormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = ImmutableForm.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testOptionalFieldMissing() {
        FormField field = ImmutableFormField.builder()
                .name("notes")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Form form = ImmutableForm.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidIntegerField() {
        FormField field = ImmutableFormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = ImmutableForm.builder()
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
    public void testInvalidIntegerFieldType() {
        FormField field = ImmutableFormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = ImmutableForm.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("age", "not a number");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testValidBooleanField() {
        FormField field = ImmutableFormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = ImmutableForm.builder()
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
    public void testInvalidBooleanFieldType() {
        FormField field = ImmutableFormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = ImmutableForm.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("agree", "yes");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testValidDecimalField() {
        FormField field = ImmutableFormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = ImmutableForm.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("price", 19.99);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testNoFieldsDefinedError() {
        Form form = ImmutableForm.builder()
                .name("testForm")
                .eventName("testEvent")
                .build();

        Map<String, Object> data = new HashMap<>();

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
        assertEquals(ValidationError.GLOBAL_ERROR, errors.get(0).fieldName());
    }

    @Test
    public void testNullDataTreatedAsEmpty() {
        FormField field = ImmutableFormField.builder()
                .name("optional")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Form form = ImmutableForm.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        List<ValidationError> errors = validator.validate(form, null);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testAllowedValueValid() {
        FormField field = ImmutableFormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue((java.io.Serializable) Arrays.asList("red", "green", "blue"))
                .build();

        ValidationError error = validator.validate("testForm", field, "red", Arrays.asList("red", "green", "blue"));
        assertNull(error);
    }

    @Test
    public void testAllowedValueInvalid() {
        FormField field = ImmutableFormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue((java.io.Serializable) Arrays.asList("red", "green", "blue"))
                .build();

        ValidationError error = validator.validate("testForm", field, "yellow", Arrays.asList("red", "green", "blue"));
        assertNotNull(error);
    }

    @Test
    public void testCollectionCardinality() {
        FormField field = ImmutableFormField.builder()
                .name("tags")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        Form form = ImmutableForm.builder()
                .name("testForm")
                .eventName("testEvent")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("tags", Arrays.asList("tag1", "tag2"));

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testDateFieldValidString() {
        FormField field = ImmutableFormField.builder()
                .name("dob")
                .type(FormFields.DateField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, "2024-01-01", null);
        assertNull(error);
    }

    @Test
    public void testFileFieldValidString() {
        FormField field = ImmutableFormField.builder()
                .name("doc")
                .type(FormFields.FileField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, "/tmp/file.txt", null);
        assertNull(error);
    }
}
