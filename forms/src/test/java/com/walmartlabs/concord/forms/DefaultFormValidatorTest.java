package com.walmartlabs.concord.forms;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2026 Walmart Inc.
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
        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .fields(Collections.emptyList())
                .build();

        var errors = validator.validate(form, Collections.emptyMap());
        assertEquals(1, errors.size());
        assertEquals("_global", errors.get(0).fieldName());
    }

    @Test
    public void testValidateStringFieldValid() {
        var field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var data = Map.of("name", (Object) "hello");
        var errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateStringFieldMissingRequired() {
        var field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var errors = validator.validate(form, Collections.emptyMap());
        assertEquals(1, errors.size());
        assertEquals("name", errors.get(0).fieldName());
    }

    @Test
    public void testValidateIntegerFieldValid() {
        var field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var data = Map.of("age", (Object) 25);
        var errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateIntegerFieldWrongType() {
        var field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var data = Map.of("age", (Object) "notANumber");
        var errors = validator.validate(form, data);
        assertEquals(1, errors.size());
        assertEquals("age", errors.get(0).fieldName());
    }

    @Test
    public void testValidateBooleanFieldValid() {
        var field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var data = Map.of("agree", (Object) true);
        var errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateBooleanFieldWrongType() {
        var field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var data = Map.of("agree", (Object) "notBoolean");
        var errors = validator.validate(form, data);
        assertEquals(1, errors.size());
    }

    @Test
    public void testValidateDecimalFieldValid() {
        var field = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var data = Map.of("price", (Object) 3.14);
        var errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateOptionalFieldMissing() {
        var field = FormField.builder()
                .name("optional")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var errors = validator.validate(form, Collections.emptyMap());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateWithAllowedValues() {
        var field = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue((Serializable) Arrays.asList("red", "blue", "green"))
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var data = Map.of("color", (Object) "red");
        var errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateWithAllowedValuesInvalid() {
        var field = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue((Serializable) Arrays.asList("red", "blue", "green"))
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var data = Map.of("color", (Object) "purple");
        var errors = validator.validate(form, data);
        assertEquals(1, errors.size());
        assertEquals("color", errors.get(0).fieldName());
    }

    @Test
    public void testValidateIntegerWithBounds() {
        Map<String, Serializable> options = new HashMap<>();
        options.put("min", 1L);
        options.put("max", 100L);

        var field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .options(options)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var data = Map.of("age", (Object) 50);
        var errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateIntegerOutOfBounds() {
        Map<String, Serializable> options = new HashMap<>();
        options.put("min", 1L);
        options.put("max", 100L);

        var field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .options(options)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var data = Map.of("age", (Object) 200);
        var errors = validator.validate(form, data);
        assertEquals(1, errors.size());
    }

    @Test
    public void testValidateStringWithPattern() {
        Map<String, Serializable> options = new HashMap<>();
        options.put("pattern", "\\d{3}-\\d{4}");

        var field = FormField.builder()
                .name("phone")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .options(options)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var data = Map.of("phone", (Object) "123-4567");
        var errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateStringPatternMismatch() {
        Map<String, Serializable> options = new HashMap<>();
        options.put("pattern", "\\d{3}-\\d{4}");

        var field = FormField.builder()
                .name("phone")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .options(options)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var data = Map.of("phone", (Object) "abc");
        var errors = validator.validate(form, data);
        assertEquals(1, errors.size());
    }

    @Test
    public void testValidateNullData() {
        var field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var errors = validator.validate(form, null);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateCollectionValues() {
        var field = FormField.builder()
                .name("tags")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        var form = Form.builder()
                .name("testForm")
                .eventName("ev")
                .addFields(field)
                .build();

        var data = Map.of("tags", (Object) Arrays.asList("a", "b"));
        var errors = validator.validate(form, data);
        assertTrue(errors.isEmpty());
    }
}
