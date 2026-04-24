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
    public void testFieldNameWithLabel() {
        FormField field = FormField.builder()
                .name("field1")
                .label("My Field")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertEquals("My Field", DefaultFormValidatorLocale.fieldName(field, null));
    }

    @Test
    public void testFieldNameWithoutLabel() {
        FormField field = FormField.builder()
                .name("field1")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertEquals("field1", DefaultFormValidatorLocale.fieldName(field, null));
    }

    @Test
    public void testFieldNameWithIndex() {
        FormField field = FormField.builder()
                .name("field1")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        assertEquals("field1 [2]", DefaultFormValidatorLocale.fieldName(field, 2));
    }

    @Test
    public void testSpellCardinality() {
        assertEquals("any number of values", DefaultFormValidatorLocale.spell(FormField.Cardinality.ANY));
        assertEquals("a single value", DefaultFormValidatorLocale.spell(FormField.Cardinality.ONE_AND_ONLY_ONE));
        assertEquals("a single optional value", DefaultFormValidatorLocale.spell(FormField.Cardinality.ONE_OR_NONE));
        assertEquals("at least a single value", DefaultFormValidatorLocale.spell(FormField.Cardinality.AT_LEAST_ONE));
    }

    @Test
    public void testSpellCardinalityNull() {
        assertThrows(IllegalArgumentException.class,
                () -> DefaultFormValidatorLocale.spell(null));
    }

    @Test
    public void testBoundsBoth() {
        String result = DefaultFormValidatorLocale.bounds(1, 10);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("10"));
    }

    @Test
    public void testBoundsMinOnly() {
        String result = DefaultFormValidatorLocale.bounds(1, null);
        assertTrue(result.contains("1"));
    }

    @Test
    public void testBoundsMaxOnly() {
        String result = DefaultFormValidatorLocale.bounds(null, 10);
        assertTrue(result.contains("10"));
    }

    @Test
    public void testNoFieldsDefined() {
        String result = locale.noFieldsDefined("form1");
        assertTrue(result.contains("form1"));
    }

    @Test
    public void testInvalidCardinality() {
        FormField field = FormField.builder()
                .name("field1")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.invalidCardinality("form1", field, "val");
        assertNotNull(result);
        assertTrue(result.contains("field1"));
    }

    @Test
    public void testExpectedString() {
        FormField field = FormField.builder()
                .name("field1")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.expectedString("form1", field, null, 123);
        assertTrue(result.contains("string"));
    }

    @Test
    public void testExpectedInteger() {
        FormField field = FormField.builder()
                .name("field1")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.expectedInteger("form1", field, null, "abc");
        assertTrue(result.contains("integer"));
    }

    @Test
    public void testExpectedDecimal() {
        FormField field = FormField.builder()
                .name("field1")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.expectedDecimal("form1", field, null, "abc");
        assertTrue(result.contains("decimal"));
    }

    @Test
    public void testExpectedBoolean() {
        FormField field = FormField.builder()
                .name("field1")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.expectedBoolean("form1", field, null, "abc");
        assertTrue(result.contains("boolean"));
    }

    @Test
    public void testDoesntMatchPattern() {
        FormField field = FormField.builder()
                .name("field1")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.doesntMatchPattern("form1", field, null, "^[a-z]+$", "ABC");
        assertTrue(result.contains("pattern"));
    }

    @Test
    public void testValueNotAllowed() {
        FormField field = FormField.builder()
                .name("field1")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.valueNotAllowed("form1", field, null, "allowed", "notAllowed");
        assertTrue(result.contains("not allowed"));
    }

    @Test
    public void testExpectedDate() {
        FormField field = FormField.builder()
                .name("field1")
                .type(FormFields.DateField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String result = locale.expectedDate("form1", field, null, 123);
        assertTrue(result.contains("date"));
    }
}
