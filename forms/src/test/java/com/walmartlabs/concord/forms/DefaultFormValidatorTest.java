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

import java.io.Serializable;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultFormValidatorTest {

    private final DefaultFormValidator validator = new DefaultFormValidator();

    @Test
    public void testValidateNoFields() {
        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .fields(Collections.emptyList())
                .build();

        List<ValidationError> errors = validator.validate(form, Collections.emptyMap());
        assertEquals(1, errors.size());
        assertEquals(ValidationError.GLOBAL_ERROR, errors.get(0).fieldName());
    }

    @Test
    public void testValidateStringFieldValid() {
        FormField field = FormField.builder()
                .name("username")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("username", "john");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateStringFieldInvalidType() {
        FormField field = FormField.builder()
                .name("username")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, 123, null);
        assertNotNull(error);
        assertEquals("username", error.fieldName());
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

        ValidationError error = validator.validate("testForm", field, "abc", null);
        assertNull(error);

        error = validator.validate("testForm", field, "ABC123", null);
        assertNotNull(error);
        assertEquals("code", error.fieldName());
    }

    @Test
    public void testValidateIntegerFieldValid() {
        FormField field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, 25, null);
        assertNull(error);
    }

    @Test
    public void testValidateIntegerFieldWithBounds() {
        Map<String, Serializable> options = new HashMap<>();
        options.put("min", 0L);
        options.put("max", 100L);

        FormField field = FormField.builder()
                .name("score")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .options(options)
                .build();

        ValidationError error = validator.validate("testForm", field, 50, null);
        assertNull(error);

        error = validator.validate("testForm", field, 150, null);
        assertNotNull(error);

        error = validator.validate("testForm", field, -1, null);
        assertNotNull(error);
    }

    @Test
    public void testValidateIntegerFieldInvalidType() {
        FormField field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, "notAnInt", null);
        assertNotNull(error);
    }

    @Test
    public void testValidateDecimalFieldValid() {
        FormField field = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, 19.99, null);
        assertNull(error);
    }

    @Test
    public void testValidateDecimalFieldWithBounds() {
        Map<String, Serializable> options = new HashMap<>();
        options.put("min", 0.0);
        options.put("max", 100.0);

        FormField field = FormField.builder()
                .name("rate")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .options(options)
                .build();

        ValidationError error = validator.validate("testForm", field, 50.5, null);
        assertNull(error);

        error = validator.validate("testForm", field, 150.0, null);
        assertNotNull(error);
    }

    @Test
    public void testValidateDecimalFieldInvalidType() {
        FormField field = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, "notADecimal", null);
        assertNotNull(error);
    }

    @Test
    public void testValidateBooleanFieldValid() {
        FormField field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, true, null);
        assertNull(error);
    }

    @Test
    public void testValidateBooleanFieldInvalidType() {
        FormField field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, "notBool", null);
        assertNotNull(error);
    }

    @Test
    public void testValidateCardinalityOneOrNone() {
        FormField field = FormField.builder()
                .name("optional")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        ValidationError error = validator.validate("testForm", field, null, null);
        assertNull(error);

        error = validator.validate("testForm", field, "value", null);
        assertNull(error);
    }

    @Test
    public void testValidateCardinalityAtLeastOne() {
        FormField field = FormField.builder()
                .name("items")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, null, null);
        assertNotNull(error);

        error = validator.validate("testForm", field, "value", null);
        assertNull(error);

        List<String> multipleValues = Arrays.asList("a", "b");
        error = validator.validate("testForm", field, multipleValues, null);
        assertNull(error);
    }

    @Test
    public void testValidateWithAllowedValues() {
        List<String> allowed = Arrays.asList("red", "green", "blue");
        FormField field = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue((Serializable) allowed)
                .build();

        ValidationError error = validator.validate("testForm", field, "red", allowed);
        assertNull(error);

        error = validator.validate("testForm", field, "yellow", allowed);
        assertNotNull(error);
    }

    @Test
    public void testValidateCollectionValues() {
        FormField field = FormField.builder()
                .name("tags")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        List<String> values = Arrays.asList("tag1", "tag2", "tag3");
        ValidationError error = validator.validate("testForm", field, values, null);
        assertNull(error);
    }

    @Test
    public void testValidateArrayValues() {
        FormField field = FormField.builder()
                .name("ids")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        Object[] values = new Object[]{1, 2, 3};
        ValidationError error = validator.validate("testForm", field, values, null);
        assertNull(error);
    }

    @Test
    public void testValidateNullData() {
        FormField field = FormField.builder()
                .name("field1")
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
    public void testValidateDateField() {
        FormField field = FormField.builder()
                .name("date")
                .type(FormFields.DateField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, "2025-01-01", null);
        assertNull(error);

        assertThrows(IllegalArgumentException.class,
                () -> validator.validate("testForm", field, 12345, null));
    }

    @Test
    public void testValidateFileField() {
        FormField field = FormField.builder()
                .name("upload")
                .type(FormFields.FileField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, "/path/to/file", null);
        assertNull(error);

        assertThrows(IllegalArgumentException.class,
                () -> validator.validate("testForm", field, 12345, null));
    }

    @Test
    public void testValidateUnsupportedType() {
        FormField field = FormField.builder()
                .name("unknown")
                .type("unknownType")
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertThrows(RuntimeException.class,
                () -> validator.validate("testForm", field, "value", null));
    }

    @Test
    public void testValidateLongField() {
        FormField field = FormField.builder()
                .name("bigNum")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, 999999999L, null);
        assertNull(error);
    }

    @Test
    public void testValidateFloatField() {
        FormField field = FormField.builder()
                .name("floatVal")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        ValidationError error = validator.validate("testForm", field, 1.5f, null);
        assertNull(error);
    }

    @Test
    public void testValidateStringFieldEmailInputType() {
        Map<String, Serializable> options = new HashMap<>();
        options.put("inputType", "email");

        FormField field = FormField.builder()
                .name("email")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .options(options)
                .build();

        ValidationError error = validator.validate("testForm", field, "user@example.com", null);
        assertNull(error);

        error = validator.validate("testForm", field, "not-an-email", null);
        assertNotNull(error);
    }

    @Test
    public void testBoxPrimitiveArrays() {
        FormField field = FormField.builder()
                .name("nums")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        int[] intArray = {1, 2, 3};
        ValidationError error = validator.validate("testForm", field, intArray, null);
        assertNull(error);

        long[] longArray = {1L, 2L};
        error = validator.validate("testForm", field, longArray, null);
        assertNull(error);
    }

    @Test
    public void testBoxDecimalPrimitiveArrays() {
        FormField field = FormField.builder()
                .name("vals")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        double[] doubleArray = {1.0, 2.0};
        ValidationError error = validator.validate("testForm", field, doubleArray, null);
        assertNull(error);

        float[] floatArray = {1.0f, 2.0f};
        error = validator.validate("testForm", field, floatArray, null);
        assertNull(error);
    }

    @Test
    public void testBoxBooleanArray() {
        FormField field = FormField.builder()
                .name("flags")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        boolean[] boolArray = {true, false};
        ValidationError error = validator.validate("testForm", field, boolArray, null);
        assertNull(error);
    }

    @Test
    public void testAllowedValueExactMatch() {
        FormField field = FormField.builder()
                .name("status")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue("active")
                .build();

        ValidationError error = validator.validate("testForm", field, "active", "active");
        assertNull(error);

        error = validator.validate("testForm", field, "inactive", "active");
        assertNotNull(error);
    }

    @Test
    public void testAllowedValueArray() {
        String[] allowed = new String[]{"a", "b", "c"};
        FormField field = FormField.builder()
                .name("status")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue(allowed)
                .build();

        ValidationError error = validator.validate("testForm", field, "a", allowed);
        assertNull(error);

        error = validator.validate("testForm", field, "d", allowed);
        assertNotNull(error);
    }
}
