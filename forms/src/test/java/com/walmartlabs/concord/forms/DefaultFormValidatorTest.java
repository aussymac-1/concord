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

    private Form createForm(String formName, FormField... fields) {
        return Form.builder()
                .name(formName)
                .eventName("evt")
                .addFields(fields)
                .build();
    }

    private FormField stringField(String name, FormField.Cardinality cardinality) {
        return FormField.builder()
                .name(name)
                .type(FormFields.StringField.TYPE)
                .cardinality(cardinality)
                .build();
    }

    private FormField intField(String name, FormField.Cardinality cardinality) {
        return FormField.builder()
                .name(name)
                .type(FormFields.IntegerField.TYPE)
                .cardinality(cardinality)
                .build();
    }

    private FormField decimalField(String name, FormField.Cardinality cardinality) {
        return FormField.builder()
                .name(name)
                .type(FormFields.DecimalField.TYPE)
                .cardinality(cardinality)
                .build();
    }

    private FormField booleanField(String name, FormField.Cardinality cardinality) {
        return FormField.builder()
                .name(name)
                .type(FormFields.BooleanField.TYPE)
                .cardinality(cardinality)
                .build();
    }

    @Test
    public void testValidStringField() {
        Form form = createForm("testForm",
                stringField("name", FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("name", "Alice");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testMissingRequiredStringField() {
        Form form = createForm("testForm",
                stringField("name", FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
        assertEquals("name", errors.get(0).fieldName());
    }

    @Test
    public void testOptionalStringFieldMissing() {
        Form form = createForm("testForm",
                stringField("nickname", FormField.Cardinality.ONE_OR_NONE));

        Map<String, Object> data = new HashMap<>();

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidIntegerField() {
        Form form = createForm("testForm",
                intField("age", FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("age", 25);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testInvalidIntegerField() {
        Form form = createForm("testForm",
                intField("age", FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("age", "not-a-number");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testValidDecimalField() {
        Form form = createForm("testForm",
                decimalField("price", FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("price", 9.99);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidBooleanField() {
        Form form = createForm("testForm",
                booleanField("active", FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("active", true);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testInvalidBooleanField() {
        Form form = createForm("testForm",
                booleanField("active", FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("active", "not-a-boolean");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testNoFieldsDefined() {
        Form form = Form.builder()
                .name("emptyForm")
                .eventName("evt")
                .build();

        List<ValidationError> errors = validator.validate(form, Collections.emptyMap());
        assertFalse(errors.isEmpty());
        assertEquals(ValidationError.GLOBAL_ERROR, errors.get(0).fieldName());
    }

    @Test
    public void testNullData() {
        Form form = createForm("testForm",
                stringField("name", FormField.Cardinality.ONE_OR_NONE));

        List<ValidationError> errors = validator.validate(form, null);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testMultipleFieldsPartialValid() {
        Form form = createForm("testForm",
                stringField("name", FormField.Cardinality.ONE_AND_ONLY_ONE),
                intField("age", FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("name", "Alice");
        // age is missing

        List<ValidationError> errors = validator.validate(form, data);
        assertEquals(1, errors.size());
        assertEquals("age", errors.get(0).fieldName());
    }

    @Test
    public void testAllowedValue() {
        FormField field = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue("red")
                .build();

        Form form = createForm("testForm", field);

        Map<String, Object> data = new HashMap<>();
        data.put("color", "red");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testDisallowedValue() {
        FormField field = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue("red")
                .build();

        Form form = createForm("testForm", field);

        Map<String, Object> data = new HashMap<>();
        data.put("color", "blue");

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testCollectionInput() {
        FormField field = FormField.builder()
                .name("tags")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        Form form = createForm("testForm", field);

        Map<String, Object> data = new HashMap<>();
        data.put("tags", Arrays.asList("a", "b", "c"));

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testArrayInput() {
        FormField field = FormField.builder()
                .name("tags")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        Form form = createForm("testForm", field);

        Map<String, Object> data = new HashMap<>();
        data.put("tags", new Object[]{"a", "b"});

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testCustomLocale() {
        DefaultFormValidator customValidator = new DefaultFormValidator(new DefaultFormValidatorLocale());

        Form form = createForm("testForm",
                stringField("name", FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();

        List<ValidationError> errors = customValidator.validate(form, data);
        assertFalse(errors.isEmpty());
    }
}
