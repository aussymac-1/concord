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

    private FormField stringField(String name, String label) {
        var builder = FormField.builder()
                .name(name)
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE);
        if (label != null) {
            builder.label(label);
        }
        return builder.build();
    }

    @Test
    public void testNoFieldsDefined() {
        var msg = locale.noFieldsDefined("myForm");
        assertTrue(msg.contains("myForm"));
        assertTrue(msg.contains("no fields"));
    }

    @Test
    public void testInvalidCardinality() {
        var field = stringField("name", null);
        var msg = locale.invalidCardinality("form1", field, "bad");
        assertTrue(msg.contains("name"));
        assertTrue(msg.contains("single value"));
    }

    @Test
    public void testExpectedString() {
        var field = stringField("name", "Name");
        var msg = locale.expectedString("form1", field, null, 42);
        assertTrue(msg.contains("Name"));
        assertTrue(msg.contains("42"));
    }

    @Test
    public void testExpectedInteger() {
        var field = stringField("age", null);
        var msg = locale.expectedInteger("form1", field, null, "abc");
        assertTrue(msg.contains("age"));
        assertTrue(msg.contains("integer"));
    }

    @Test
    public void testExpectedDecimal() {
        var field = stringField("price", null);
        var msg = locale.expectedDecimal("form1", field, null, "abc");
        assertTrue(msg.contains("price"));
        assertTrue(msg.contains("decimal"));
    }

    @Test
    public void testExpectedBoolean() {
        var field = stringField("flag", null);
        var msg = locale.expectedBoolean("form1", field, null, "abc");
        assertTrue(msg.contains("flag"));
        assertTrue(msg.contains("boolean"));
    }

    @Test
    public void testDoesntMatchPattern() {
        var field = stringField("email", null);
        var msg = locale.doesntMatchPattern("form1", field, null, ".*@.*", "bad");
        assertTrue(msg.contains("email"));
        assertTrue(msg.contains(".*@.*"));
    }

    @Test
    public void testIntegerRangeError() {
        var field = stringField("count", null);
        var msg = locale.integerRangeError("form1", field, null, 1L, 10L, 15);
        assertTrue(msg.contains("count"));
        assertTrue(msg.contains("15"));
        assertTrue(msg.contains("within"));
    }

    @Test
    public void testDecimalRangeError() {
        var field = stringField("price", null);
        var msg = locale.decimalRangeError("form1", field, null, 0.0, 100.0, 150.0);
        assertTrue(msg.contains("price"));
        assertTrue(msg.contains("within"));
    }

    @Test
    public void testValueNotAllowed() {
        var field = stringField("color", null);
        var msg = locale.valueNotAllowed("form1", field, null, "[red, blue]", "green");
        assertTrue(msg.contains("color"));
        assertTrue(msg.contains("green"));
        assertTrue(msg.contains("not allowed"));
    }

    @Test
    public void testExpectedDate() {
        var field = stringField("dob", null);
        var msg = locale.expectedDate("form1", field, null, "not-a-date");
        assertTrue(msg.contains("dob"));
        assertTrue(msg.contains("date"));
    }

    @Test
    public void testFieldNameUsesLabel() {
        var field = stringField("f1", "My Label");
        var name = DefaultFormValidatorLocale.fieldName(field, null);
        assertEquals("My Label", name);
    }

    @Test
    public void testFieldNameFallsBackToName() {
        var field = stringField("f1", null);
        var name = DefaultFormValidatorLocale.fieldName(field, null);
        assertEquals("f1", name);
    }

    @Test
    public void testFieldNameWithIndex() {
        var field = stringField("items", null);
        var name = DefaultFormValidatorLocale.fieldName(field, 3);
        assertEquals("items [3]", name);
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
    public void testBoundsMinAndMax() {
        assertEquals("within 1 and 10 (inclusive)", DefaultFormValidatorLocale.bounds(1, 10));
    }

    @Test
    public void testBoundsOnlyMax() {
        assertEquals("less or equal than 10", DefaultFormValidatorLocale.bounds(null, 10));
    }

    @Test
    public void testBoundsOnlyMin() {
        assertEquals("equal or greater than 1", DefaultFormValidatorLocale.bounds(1, null));
    }
}
