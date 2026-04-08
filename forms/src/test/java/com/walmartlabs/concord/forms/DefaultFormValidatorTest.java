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

import java.io.Serializable;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultFormValidatorTest {

    private final DefaultFormValidator validator = new DefaultFormValidator();

    @Test
    public void testValidateNoFields() {
        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .fields(Collections.emptyList())
                .build();

        List<ValidationError> errors = validator.validate(form, Collections.emptyMap());
        assertEquals(1, errors.size());
        assertEquals(ValidationError.GLOBAL_ERROR, errors.get(0).fieldName());
    }

    @Test
    public void testValidateDefaultFields() {
        // Default form has empty fields list, which is not null but empty
        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .build();

        List<ValidationError> errors = validator.validate(form, Collections.emptyMap());
        // empty list triggers no-fields error
        assertEquals(1, errors.size());
        assertEquals(ValidationError.GLOBAL_ERROR, errors.get(0).fieldName());
    }

    @Test
    public void testValidateStringFieldValid() {
        FormField field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("name", "hello");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateStringFieldInvalidType() {
        FormField field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("name", 123);

        List<ValidationError> errors = validator.validate(form, data);
        assertEquals(1, errors.size());
        assertEquals("name", errors.get(0).fieldName());
    }

    @Test
    public void testValidateStringFieldWithPattern() {
        Map<String, Serializable> options = new HashMap<>();
        options.put("pattern", "^[a-z]+$");

        FormField field = FormField.builder()
                .name("code")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .options(options)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("code", "abc");
        assertTrue(validator.validate(form, data).isEmpty());

        data.put("code", "ABC123");
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateStringFieldWithEmailInputTypeValid() {
        // Email validation only runs when a pattern is also set (StringFieldValidator
        // returns null early when pattern == null, before reaching the email check)
        Map<String, Serializable> options = new HashMap<>();
        options.put("inputType", "email");
        options.put("pattern", ".*");

        FormField field = FormField.builder()
                .name("email")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .options(options)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("email", "test@example.com");
        assertTrue(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateStringFieldWithEmailInputTypeInvalid() {
        Map<String, Serializable> options = new HashMap<>();
        options.put("inputType", "email");
        options.put("pattern", ".*");

        FormField field = FormField.builder()
                .name("email")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .options(options)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("email", "not an email");
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateIntegerFieldValid() {
        FormField field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("age", 25);
        assertTrue(validator.validate(form, data).isEmpty());

        data.put("age", 25L);
        assertTrue(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateIntegerFieldInvalidType() {
        FormField field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("age", "notanumber");
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateIntegerFieldWithBounds() {
        Map<String, Serializable> options = new HashMap<>();
        options.put("min", 1L);
        options.put("max", 100L);

        FormField field = FormField.builder()
                .name("score")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .options(options)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("score", 50);
        assertTrue(validator.validate(form, data).isEmpty());

        data.put("score", 0);
        assertFalse(validator.validate(form, data).isEmpty());

        data.put("score", 101);
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateIntegerFieldWithLongBounds() {
        Map<String, Serializable> options = new HashMap<>();
        options.put("min", 1L);
        options.put("max", 100L);

        FormField field = FormField.builder()
                .name("score")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .options(options)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("score", 50L);
        assertTrue(validator.validate(form, data).isEmpty());

        data.put("score", 0L);
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateDecimalFieldValid() {
        FormField field = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("price", 19.99);
        assertTrue(validator.validate(form, data).isEmpty());

        data.put("price", 19.99f);
        assertTrue(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateDecimalFieldInvalidType() {
        FormField field = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("price", "notadecimal");
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateDecimalFieldWithBounds() {
        Map<String, Serializable> options = new HashMap<>();
        options.put("min", 0.0);
        options.put("max", 100.0);

        FormField field = FormField.builder()
                .name("pct")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .options(options)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("pct", 50.0);
        assertTrue(validator.validate(form, data).isEmpty());

        data.put("pct", -1.0);
        assertFalse(validator.validate(form, data).isEmpty());

        data.put("pct", 101.0);
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateDecimalFieldWithFloatNoBounds() {
        FormField field = FormField.builder()
                .name("pct")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("pct", 50.0f);
        assertTrue(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateBooleanFieldValid() {
        FormField field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("agree", true);
        assertTrue(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateBooleanFieldInvalidType() {
        FormField field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("agree", "notboolean");
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateDateField() {
        FormField field = FormField.builder()
                .name("dob")
                .type(FormFields.DateField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("dob", "2024-01-01");
        assertTrue(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateDateFieldInvalidType() {
        FormField field = FormField.builder()
                .name("dob")
                .type(FormFields.DateField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("dob", 123);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(form, data));
    }

    @Test
    public void testValidateFileField() {
        FormField field = FormField.builder()
                .name("upload")
                .type(FormFields.FileField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("upload", "/path/to/file");
        assertTrue(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateFileFieldInvalidType() {
        FormField field = FormField.builder()
                .name("upload")
                .type(FormFields.FileField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("upload", 123);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(form, data));
    }

    @Test
    public void testCardinalityOneAndOnlyOne() {
        FormField field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        // null value should fail ONE_AND_ONLY_ONE
        Map<String, Object> data = new HashMap<>();
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testCardinalityOneOrNone() {
        FormField field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        // null value should pass ONE_OR_NONE
        Map<String, Object> data = new HashMap<>();
        assertTrue(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testCardinalityAtLeastOne() {
        FormField field = FormField.builder()
                .name("tags")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        // null should fail AT_LEAST_ONE
        Map<String, Object> data = new HashMap<>();
        assertFalse(validator.validate(form, data).isEmpty());

        // single value should pass
        data.put("tags", "tag1");
        assertTrue(validator.validate(form, data).isEmpty());

        // collection should pass
        data.put("tags", Arrays.asList("tag1", "tag2"));
        assertTrue(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testCardinalityAny() {
        FormField field = FormField.builder()
                .name("tags")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        assertTrue(validator.validate(form, data).isEmpty());

        data.put("tags", "single");
        assertTrue(validator.validate(form, data).isEmpty());

        data.put("tags", Arrays.asList("a", "b"));
        assertTrue(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testAllowedValueSingle() {
        FormField field = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue((Serializable) Arrays.asList("red", "green", "blue"))
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("color", "red");
        assertTrue(validator.validate(form, data).isEmpty());

        data.put("color", "yellow");
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testAllowedValueExactMatch() {
        FormField field = FormField.builder()
                .name("answer")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue("yes")
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("answer", "yes");
        assertTrue(validator.validate(form, data).isEmpty());

        data.put("answer", "no");
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateCollectionValues() {
        FormField field = FormField.builder()
                .name("tags")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("tags", Arrays.asList("tag1", "tag2"));
        assertTrue(validator.validate(form, data).isEmpty());

        // mixed types in collection - second value is integer, not string
        data.put("tags", Arrays.asList("tag1", 42));
        assertFalse(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateArrayValues() {
        FormField field = FormField.builder()
                .name("tags")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("tags", new Object[]{"tag1", "tag2"});
        assertTrue(validator.validate(form, data).isEmpty());
    }

    @Test
    public void testValidateWithNullData() {
        FormField field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        List<ValidationError> errors = validator.validate(form, null);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testBoxPrimitiveArrays() {
        FormField field = FormField.builder()
                .name("nums")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        // int array should be boxed
        ValidationError result = validator.validate("testForm", field, new int[]{1, 2, 3}, null);
        assertNull(result);
    }

    @Test
    public void testBoxLongArray() {
        FormField field = FormField.builder()
                .name("nums")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        ValidationError result = validator.validate("testForm", field, new long[]{1L, 2L}, null);
        assertNull(result);
    }

    @Test
    public void testBoxDoubleArray() {
        FormField field = FormField.builder()
                .name("nums")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        ValidationError result = validator.validate("testForm", field, new double[]{1.0, 2.0}, null);
        assertNull(result);
    }

    @Test
    public void testBoxFloatArray() {
        FormField field = FormField.builder()
                .name("nums")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        ValidationError result = validator.validate("testForm", field, new float[]{1.0f, 2.0f}, null);
        assertNull(result);
    }

    @Test
    public void testBoxBooleanArray() {
        FormField field = FormField.builder()
                .name("flags")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        ValidationError result = validator.validate("testForm", field, new boolean[]{true, false}, null);
        assertNull(result);
    }

    @Test
    public void testBoxCharArray() {
        FormField field = FormField.builder()
                .name("chars")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        // char values won't pass string validation, but boxing should work
        ValidationError result = validator.validate("testForm", field, new char[]{'a', 'b'}, null);
        assertNotNull(result);
    }

    @Test
    public void testAllowedValueAsArray() {
        FormField field = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue(new String[]{"red", "green", "blue"})
                .build();

        ValidationError result = validator.validate("testForm", field, "red", new String[]{"red", "green", "blue"});
        assertNull(result);

        result = validator.validate("testForm", field, "yellow", new String[]{"red", "green", "blue"});
        assertNotNull(result);
    }

    @Test
    public void testUnsupportedFieldType() {
        FormField field = FormField.builder()
                .name("custom")
                .type("unsupported_type")
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertThrows(RuntimeException.class,
                () -> validator.validate("testForm", field, "value", null));
    }

    @Test
    public void testDateTimeField() {
        FormField field = FormField.builder()
                .name("ts")
                .type(FormFields.DateTimeField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("ts", "2024-01-01T00:00:00Z");
        assertTrue(validator.validate(form, data).isEmpty());
    }
}
