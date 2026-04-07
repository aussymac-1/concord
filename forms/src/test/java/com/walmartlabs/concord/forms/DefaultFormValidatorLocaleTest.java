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

import static org.junit.jupiter.api.Assertions.*;

public class DefaultFormValidatorLocaleTest {

    private final DefaultFormValidatorLocale locale = new DefaultFormValidatorLocale();

    @Test
    public void testNoFieldsDefined() {
        String result = locale.noFieldsDefined("myForm");
        assertTrue(result.contains("myForm"));
        assertTrue(result.contains("no fields"));
    }

    @Test
    public void testInvalidCardinality() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.invalidCardinality("myForm", field, "val");
        assertTrue(result.contains("myField"));
        assertTrue(result.contains("single value"));
    }

    @Test
    public void testExpectedString() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.expectedString("myForm", field, null, 123);
        assertTrue(result.contains("myField"));
        assertTrue(result.contains("string"));
    }

    @Test
    public void testExpectedInteger() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.expectedInteger("myForm", field, null, "abc");
        assertTrue(result.contains("myField"));
        assertTrue(result.contains("integer"));
    }

    @Test
    public void testExpectedDecimal() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.expectedDecimal("myForm", field, null, "abc");
        assertTrue(result.contains("myField"));
        assertTrue(result.contains("decimal"));
    }

    @Test
    public void testExpectedBoolean() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.expectedBoolean("myForm", field, null, "abc");
        assertTrue(result.contains("myField"));
        assertTrue(result.contains("boolean"));
    }

    @Test
    public void testDoesntMatchPattern() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.doesntMatchPattern("myForm", field, null, "[A-Z]+", "abc");
        assertTrue(result.contains("myField"));
        assertTrue(result.contains("pattern"));
    }

    @Test
    public void testIntegerRangeError() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.integerRangeError("myForm", field, null, 1L, 100L, 200);
        assertTrue(result.contains("myField"));
        assertTrue(result.contains("200"));
    }

    @Test
    public void testDecimalRangeError() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.decimalRangeError("myForm", field, null, 1.0, 100.0, 200.0);
        assertTrue(result.contains("myField"));
    }

    @Test
    public void testValueNotAllowed() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.valueNotAllowed("myForm", field, null, "allowed", "notAllowed");
        assertTrue(result.contains("myField"));
        assertTrue(result.contains("not allowed"));
    }

    @Test
    public void testFieldNameWithLabel() {
        FormField field = FormField.builder()
                .name("myField")
                .label("My Label")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertEquals("My Label", DefaultFormValidatorLocale.fieldName(field, null));
        assertEquals("My Label [0]", DefaultFormValidatorLocale.fieldName(field, 0));
    }

    @Test
    public void testFieldNameWithoutLabel() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertEquals("myField", DefaultFormValidatorLocale.fieldName(field, null));
        assertEquals("myField [2]", DefaultFormValidatorLocale.fieldName(field, 2));
    }

    @Test
    public void testSpell() {
        assertEquals("any number of values", DefaultFormValidatorLocale.spell(FormField.Cardinality.ANY));
        assertEquals("a single value", DefaultFormValidatorLocale.spell(FormField.Cardinality.ONE_AND_ONLY_ONE));
        assertEquals("a single optional value", DefaultFormValidatorLocale.spell(FormField.Cardinality.ONE_OR_NONE));
        assertEquals("at least a single value", DefaultFormValidatorLocale.spell(FormField.Cardinality.AT_LEAST_ONE));
    }

    @Test
    public void testSpellNull() {
        assertThrows(IllegalArgumentException.class, () -> DefaultFormValidatorLocale.spell(null));
    }

    @Test
    public void testBounds() {
        assertEquals("within 1 and 10 (inclusive)", DefaultFormValidatorLocale.bounds(1, 10));
        assertEquals("less or equal than 10", DefaultFormValidatorLocale.bounds(null, 10));
        assertEquals("equal or greater than 1", DefaultFormValidatorLocale.bounds(1, null));
    }
}
