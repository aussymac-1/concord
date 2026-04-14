package com.walmartlabs.concord.forms;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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
        Form form = buildForm("myForm", field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        Map<String, Object> data = Collections.singletonMap("name", "John");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testMissingRequiredField() {
        Form form = buildForm("myForm", field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        Map<String, Object> data = Collections.emptyMap();

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
        assertEquals("name", errors.get(0).fieldName());
    }

    @Test
    public void testOptionalFieldMissing() {
        Form form = buildForm("myForm", field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        Map<String, Object> data = Collections.emptyMap();

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidIntegerField() {
        Form form = buildForm("myForm", field("age", FormFields.IntegerField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        Map<String, Object> data = Collections.singletonMap("age", 25);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testInvalidIntegerField() {
        Form form = buildForm("myForm", field("age", FormFields.IntegerField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        Map<String, Object> data = Collections.singletonMap("age", "not-a-number");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testValidBooleanField() {
        Form form = buildForm("myForm", field("active", FormFields.BooleanField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        Map<String, Object> data = Collections.singletonMap("active", true);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testInvalidBooleanField() {
        Form form = buildForm("myForm", field("active", FormFields.BooleanField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        Map<String, Object> data = Collections.singletonMap("active", "yes");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testValidDecimalField() {
        Form form = buildForm("myForm", field("price", FormFields.DecimalField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        Map<String, Object> data = Collections.singletonMap("price", 3.14);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testInvalidDecimalField() {
        Form form = buildForm("myForm", field("price", FormFields.DecimalField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        Map<String, Object> data = Collections.singletonMap("price", "not-a-decimal");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testNoFieldsDefined() {
        Form form = Form.builder()
                .name("myForm")
                .eventName("ev")
                .fields(Collections.emptyList())
                .build();

        List<ValidationError> errors = validator.validate(form, Collections.emptyMap());
        assertFalse(errors.isEmpty());
        assertEquals(ValidationError.GLOBAL_ERROR, errors.get(0).fieldName());
    }

    @Test
    public void testNullData() {
        Form form = buildForm("myForm", field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        List<ValidationError> errors = validator.validate(form, null);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testAllowedValueMatch() {
        FormField f = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue((java.io.Serializable) Arrays.asList("red", "blue", "green"))
                .build();

        Form form = buildForm("myForm", f);
        Map<String, Object> data = Collections.singletonMap("color", "red");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testAllowedValueMismatch() {
        FormField f = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue((java.io.Serializable) Arrays.asList("red", "blue", "green"))
                .build();

        Form form = buildForm("myForm", f);
        Map<String, Object> data = Collections.singletonMap("color", "yellow");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testDateFieldValid() {
        Form form = buildForm("myForm", field("dt", FormFields.DateField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        Map<String, Object> data = Collections.singletonMap("dt", "2024-01-01");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testCardinalityAny() {
        Form form = buildForm("myForm", field("tags", FormFields.StringField.TYPE, FormField.Cardinality.ANY));
        Map<String, Object> data = Collections.singletonMap("tags", Arrays.asList("a", "b", "c"));

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testCardinalityAtLeastOne() {
        Form form = buildForm("myForm", field("tags", FormFields.StringField.TYPE, FormField.Cardinality.AT_LEAST_ONE));
        Map<String, Object> data = Collections.singletonMap("tags", Collections.emptyList());

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    // --- helpers ---

    private static FormField field(String name, String type, FormField.Cardinality cardinality) {
        return FormField.builder()
                .name(name)
                .type(type)
                .cardinality(cardinality)
                .build();
    }

    private static Form buildForm(String name, FormField... fields) {
        return Form.builder()
                .name(name)
                .eventName("ev")
                .fields(Arrays.asList(fields))
                .build();
    }
}
