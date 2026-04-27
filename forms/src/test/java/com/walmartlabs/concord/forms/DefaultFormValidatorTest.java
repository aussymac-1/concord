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

    private FormField field(String name, String type, FormField.Cardinality cardinality) {
        return FormField.builder()
                .name(name)
                .type(type)
                .cardinality(cardinality)
                .build();
    }

    private FormField fieldWithOptions(String name, String type, FormField.Cardinality cardinality,
                                       Map<String, Serializable> options) {
        return FormField.builder()
                .name(name)
                .type(type)
                .cardinality(cardinality)
                .options(options)
                .build();
    }

    private Form form(String name, FormField... fields) {
        return Form.builder()
                .name(name)
                .eventName("evt")
                .addFields(fields)
                .build();
    }

    @Test
    public void testValidateEmptyFields() {
        var f = Form.builder()
                .name("test")
                .eventName("evt")
                .build();

        var errors = validator.validate(f, Map.of());
        assertEquals(1, errors.size());
        assertEquals(ValidationError.GLOBAL_ERROR, errors.get(0).fieldName());
    }

    @Test
    public void testValidateStringFieldValid() {
        var f = form("test", field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        var errors = validator.validate(f, Map.of("name", "hello"));
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateStringFieldMissing() {
        var f = form("test", field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        var errors = validator.validate(f, Map.of());
        assertEquals(1, errors.size());
        assertEquals("name", errors.get(0).fieldName());
    }

    @Test
    public void testValidateOptionalFieldMissing() {
        var f = form("test", field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        var errors = validator.validate(f, Map.of());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateIntegerFieldValid() {
        var f = form("test", field("age", FormFields.IntegerField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        var errors = validator.validate(f, Map.of("age", 25));
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateIntegerFieldInvalidType() {
        var f = form("test", field("age", FormFields.IntegerField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        var errors = validator.validate(f, Map.of("age", "not-a-number"));
        assertEquals(1, errors.size());
    }

    @Test
    public void testValidateBooleanFieldValid() {
        var f = form("test", field("flag", FormFields.BooleanField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        var errors = validator.validate(f, Map.of("flag", true));
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateBooleanFieldInvalidType() {
        var f = form("test", field("flag", FormFields.BooleanField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        var errors = validator.validate(f, Map.of("flag", "not-a-bool"));
        assertEquals(1, errors.size());
    }

    @Test
    public void testValidateDecimalFieldValid() {
        var f = form("test", field("price", FormFields.DecimalField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE));
        var errors = validator.validate(f, Map.of("price", 3.14));
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateNullData() {
        var f = form("test", field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        var errors = validator.validate(f, null);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateWithAllowedValue() {
        var ff = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue((Serializable) Arrays.asList("red", "blue"))
                .build();
        var f = form("test", ff);

        var errors = validator.validate(f, Map.of("color", "red"));
        assertTrue(errors.isEmpty());

        errors = validator.validate(f, Map.of("color", "green"));
        assertEquals(1, errors.size());
    }

    @Test
    public void testValidateCollectionCardinality() {
        var ff = field("items", FormFields.StringField.TYPE, FormField.Cardinality.AT_LEAST_ONE);
        var f = form("test", ff);

        var errors = validator.validate(f, Map.of("items", Arrays.asList("a", "b")));
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateArrayValues() {
        var ff = field("items", FormFields.StringField.TYPE, FormField.Cardinality.ANY);
        var f = form("test", ff);

        var data = new HashMap<String, Object>();
        data.put("items", new Object[]{"a", "b"});
        var errors = validator.validate(f, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testDefaultConstructor() {
        var v = new DefaultFormValidator();
        var f = form("test", field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        var errors = v.validate(f, Map.of());
        assertTrue(errors.isEmpty());
    }
}
