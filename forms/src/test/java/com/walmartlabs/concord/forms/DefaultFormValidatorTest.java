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
    public void testValidateNoFields() {
        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .build();

        List<ValidationError> errors = validator.validate(form, Collections.emptyMap());
        assertFalse(errors.isEmpty());
        assertEquals(ValidationError.GLOBAL_ERROR, errors.get(0).fieldName());
    }

    @Test
    public void testValidateEmptyFields() {
        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .fields(Collections.emptyList())
                .build();

        List<ValidationError> errors = validator.validate(form, Collections.emptyMap());
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testValidateStringFieldValid() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("myField", "hello");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateStringFieldInvalid() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("myField", 123);

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testValidateIntegerFieldValid() {
        FormField field = FormField.builder()
                .name("myInt")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("myInt", 42);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateIntegerFieldInvalid() {
        FormField field = FormField.builder()
                .name("myInt")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("myInt", "notAnInt");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testValidateIntegerFieldWithBounds() {
        FormField field = FormField.builder()
                .name("myInt")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .putOptions(FormFields.IntegerField.MIN.name(), 10L)
                .putOptions(FormFields.IntegerField.MAX.name(), 100L)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        // value within bounds
        Map<String, Object> data = new HashMap<>();
        data.put("myInt", 50);
        assertTrue(validator.validate(form, data).isEmpty());

        // value below min
        data.put("myInt", 5);
        assertFalse(validator.validate(form, data).isEmpty());

        // value above max
        data.put("myInt", 200);
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateDecimalFieldValid() {
        FormField field = FormField.builder()
                .name("myDecimal")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("myDecimal", 3.14);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateDecimalFieldInvalid() {
        FormField field = FormField.builder()
                .name("myDecimal")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("myDecimal", "notADecimal");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testValidateBooleanFieldValid() {
        FormField field = FormField.builder()
                .name("myBool")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("myBool", true);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateBooleanFieldInvalid() {
        FormField field = FormField.builder()
                .name("myBool")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("myBool", "notBoolean");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testValidateOptionalFieldMissing() {
        FormField field = FormField.builder()
                .name("optField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        List<ValidationError> errors = validator.validate(form, Collections.emptyMap());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateRequiredFieldMissing() {
        FormField field = FormField.builder()
                .name("reqField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        List<ValidationError> errors = validator.validate(form, Collections.emptyMap());
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testValidateWithNullData() {
        FormField field = FormField.builder()
                .name("optField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        List<ValidationError> errors = validator.validate(form, null);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateWithAllowedValues() {
        FormField field = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue((java.io.Serializable) Arrays.asList("red", "green", "blue"))
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        // allowed value
        Map<String, Object> data = new HashMap<>();
        data.put("color", "red");
        assertTrue(validator.validate(form, data).isEmpty());

        // disallowed value
        data.put("color", "yellow");
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateCollectionValues() {
        FormField field = FormField.builder()
                .name("items")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("items", Arrays.asList("a", "b", "c"));

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateStringWithPattern() {
        FormField field = FormField.builder()
                .name("code")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .putOptions(FormFields.StringField.PATTERN.name(), "[A-Z]{3}")
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        // matching pattern
        Map<String, Object> data = new HashMap<>();
        data.put("code", "ABC");
        assertTrue(validator.validate(form, data).isEmpty());

        // not matching pattern
        data.put("code", "abc");
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateFileField() {
        FormField field = FormField.builder()
                .name("myFile")
                .type(FormFields.FileField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("myFile", "/path/to/file");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateDateField() {
        FormField field = FormField.builder()
                .name("myDate")
                .type(FormFields.DateField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("myDate", "2024-01-01");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateDateTimeField() {
        FormField field = FormField.builder()
                .name("myDateTime")
                .type(FormFields.DateTimeField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("myDateTime", "2024-01-01T12:00:00");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }
}
