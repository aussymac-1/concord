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
        Form form = buildForm("testForm",
                buildField("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("name", "John");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testMissingRequiredField() {
        Form form = buildForm("testForm",
                buildField("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = Collections.emptyMap();

        List<ValidationError> errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testOptionalFieldMissing() {
        Form form = buildForm("testForm",
                buildField("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_OR_NONE));

        Map<String, Object> data = Collections.emptyMap();

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidIntegerField() {
        Form form = buildForm("testForm",
                buildField("count", FormFields.IntegerField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("count", 42);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidBooleanField() {
        Form form = buildForm("testForm",
                buildField("active", FormFields.BooleanField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("active", true);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidDecimalField() {
        Form form = buildForm("testForm",
                buildField("rate", FormFields.DecimalField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("rate", 3.14);

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testNoFieldsDefined() {
        Form form = Form.builder()
                .name("emptyForm")
                .eventName("evt")
                .fields(Collections.emptyList())
                .build();

        List<ValidationError> errors = validator.validate(form, Collections.emptyMap());
        assertFalse(errors.isEmpty());
        assertEquals(ValidationError.GLOBAL_ERROR, errors.get(0).fieldName());
    }

    @Test
    public void testNullData() {
        Form form = buildForm("testForm",
                buildField("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_OR_NONE));

        List<ValidationError> errors = validator.validate(form, null);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testAllowedValue() {
        FormField field = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue((java.io.Serializable) Arrays.asList("red", "blue"))
                .build();
        Form form = buildForm("testForm", field);

        Map<String, Object> data = new HashMap<>();
        data.put("color", "red");
        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());

        data.put("color", "green");
        errors = validator.validate(form, data);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testFileField() {
        Form form = buildForm("testForm",
                buildField("upload", FormFields.FileField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("upload", "/path/to/file.txt");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testDateField() {
        Form form = buildForm("testForm",
                buildField("birthday", FormFields.DateField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("birthday", "2024-01-01");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testDateTimeField() {
        Form form = buildForm("testForm",
                buildField("timestamp", FormFields.DateTimeField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", "2024-01-01T12:00:00");

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testCollectionCardinality() {
        Form form = buildForm("testForm",
                buildField("tags", FormFields.StringField.TYPE, FormField.Cardinality.AT_LEAST_ONE));

        Map<String, Object> data = new HashMap<>();
        data.put("tags", Arrays.asList("a", "b"));

        List<ValidationError> errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testAnyCardinalityEmpty() {
        Form form = buildForm("testForm",
                buildField("tags", FormFields.StringField.TYPE, FormField.Cardinality.ANY));

        List<ValidationError> errors = validator.validate(form, Collections.emptyMap());
        assertTrue(errors.isEmpty());
    }

    private static Form buildForm(String name, FormField... fields) {
        return Form.builder()
                .name(name)
                .eventName("evt")
                .addFields(fields)
                .build();
    }

    private static FormField buildField(String name, String type, FormField.Cardinality cardinality) {
        return FormField.builder()
                .name(name)
                .type(type)
                .cardinality(cardinality)
                .build();
    }
}
