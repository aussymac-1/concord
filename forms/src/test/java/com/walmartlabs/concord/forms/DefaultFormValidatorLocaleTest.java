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

import static org.junit.jupiter.api.Assertions.*;

public class DefaultFormValidatorLocaleTest {

    private final DefaultFormValidatorLocale locale = new DefaultFormValidatorLocale();

    @Test
    public void testNoFieldsDefined() {
        String msg = locale.noFieldsDefined("myForm");
        assertEquals("Form (myForm) has no fields", msg);
    }

    @Test
    public void testInvalidCardinality() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String msg = locale.invalidCardinality("myForm", field, null);
        assertTrue(msg.contains("myField"));
        assertTrue(msg.contains("a single value"));
    }

    @Test
    public void testExpectedString() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String msg = locale.expectedString("myForm", field, null, 42);
        assertTrue(msg.contains("myField"));
        assertTrue(msg.contains("expected a string"));
    }

    @Test
    public void testExpectedInteger() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String msg = locale.expectedInteger("myForm", field, null, "abc");
        assertTrue(msg.contains("myField"));
        assertTrue(msg.contains("expected an integer"));
    }

    @Test
    public void testExpectedDecimal() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String msg = locale.expectedDecimal("myForm", field, null, "abc");
        assertTrue(msg.contains("expected a decimal"));
    }

    @Test
    public void testExpectedBoolean() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String msg = locale.expectedBoolean("myForm", field, null, "abc");
        assertTrue(msg.contains("expected a boolean"));
    }

    @Test
    public void testDoesntMatchPattern() {
        FormField field = FormField.builder()
                .name("code")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String msg = locale.doesntMatchPattern("myForm", field, null, "^[a-z]+$", "ABC");
        assertTrue(msg.contains("doesn't match pattern"));
    }

    @Test
    public void testIntegerRangeError() {
        FormField field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String msg = locale.integerRangeError("myForm", field, null, 1L, 100L, 200);
        assertTrue(msg.contains("must be"));
    }

    @Test
    public void testDecimalRangeError() {
        FormField field = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String msg = locale.decimalRangeError("myForm", field, null, 0.0, 100.0, 200.0);
        assertTrue(msg.contains("must be"));
    }

    @Test
    public void testValueNotAllowed() {
        FormField field = FormField.builder()
                .name("color")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String msg = locale.valueNotAllowed("myForm", field, null, "red, blue", "yellow");
        assertTrue(msg.contains("not allowed"));
    }

    @Test
    public void testExpectedDate() {
        FormField field = FormField.builder()
                .name("date")
                .type(FormFields.DateField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        String msg = locale.expectedDate("myForm", field, null, 42);
        assertTrue(msg.contains("expected a date"));
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
    }

    @Test
    public void testFieldNameWithIndex() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertEquals("myField [0]", DefaultFormValidatorLocale.fieldName(field, 0));
    }

    @Test
    public void testFieldNameNoLabel() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertEquals("myField", DefaultFormValidatorLocale.fieldName(field, null));
    }

    @Test
    public void testSpellCardinality() {
        assertEquals("any number of values", DefaultFormValidatorLocale.spell(FormField.Cardinality.ANY));
        assertEquals("a single value", DefaultFormValidatorLocale.spell(FormField.Cardinality.ONE_AND_ONLY_ONE));
        assertEquals("a single optional value", DefaultFormValidatorLocale.spell(FormField.Cardinality.ONE_OR_NONE));
        assertEquals("at least a single value", DefaultFormValidatorLocale.spell(FormField.Cardinality.AT_LEAST_ONE));
    }

    @Test
    public void testSpellNullCardinality() {
        assertThrows(IllegalArgumentException.class, () -> DefaultFormValidatorLocale.spell(null));
    }

    @Test
    public void testBoundsBothMinAndMax() {
        assertEquals("within 1 and 100 (inclusive)", DefaultFormValidatorLocale.bounds(1, 100));
    }

    @Test
    public void testBoundsOnlyMin() {
        assertEquals("equal or greater than 1", DefaultFormValidatorLocale.bounds(1, null));
    }

    @Test
    public void testBoundsOnlyMax() {
        assertEquals("less or equal than 100", DefaultFormValidatorLocale.bounds(null, 100));
    }
}
