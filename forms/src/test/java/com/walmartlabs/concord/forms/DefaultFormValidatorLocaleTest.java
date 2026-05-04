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

    private static FormField field(String name, String label, FormField.Cardinality c) {
        var b = FormField.builder()
                .name(name)
                .type(FormFields.StringField.TYPE)
                .cardinality(c == null ? FormField.Cardinality.ONE_OR_NONE : c);
        if (label != null) {
            b.label(label);
        }
        return b.build();
    }

    @Test
    public void testNoFieldsDefined() {
        assertEquals("Form (myForm) has no fields", locale.noFieldsDefined("myForm"));
    }

    @Test
    public void testInvalidCardinalityIncludesCardinalityWord() {
        var f = field("username", "Username", FormField.Cardinality.ONE_AND_ONLY_ONE);
        var msg = locale.invalidCardinality("f", f, "x");
        assertTrue(msg.startsWith("Username:"));
        assertTrue(msg.contains("a single value"));
    }

    @Test
    public void testExpectedString() {
        var f = field("u", "User", FormField.Cardinality.ONE_OR_NONE);
        assertEquals("User: expected a string value, got 7", locale.expectedString("f", f, null, 7));
    }

    @Test
    public void testExpectedInteger() {
        var f = field("u", "User", null);
        assertEquals("User: expected an integer value, got x", locale.expectedInteger("f", f, null, "x"));
    }

    @Test
    public void testExpectedDecimal() {
        var f = field("u", "User", null);
        assertEquals("User: expected a decimal value, got x", locale.expectedDecimal("f", f, null, "x"));
    }

    @Test
    public void testExpectedBoolean() {
        var f = field("u", "User", null);
        assertEquals("User: expected a boolean value, got x", locale.expectedBoolean("f", f, null, "x"));
    }

    @Test
    public void testDoesntMatchPattern() {
        var f = field("u", "User", null);
        assertEquals("User: value 'abc' doesn't match pattern '\\d+'",
                locale.doesntMatchPattern("f", f, null, "\\d+", "abc"));
    }

    @Test
    public void testIntegerRangeBothBounds() {
        var f = field("u", "User", null);
        assertEquals("User: value '5' must be within 1 and 10 (inclusive)",
                locale.integerRangeError("f", f, null, 1L, 10L, 5));
    }

    @Test
    public void testIntegerRangeOnlyMin() {
        var f = field("u", "User", null);
        assertEquals("User: value '-3' must be equal or greater than 0",
                locale.integerRangeError("f", f, null, 0L, null, -3));
    }

    @Test
    public void testIntegerRangeOnlyMax() {
        var f = field("u", "User", null);
        assertEquals("User: value '11' must be less or equal than 10",
                locale.integerRangeError("f", f, null, null, 10L, 11));
    }

    @Test
    public void testDecimalRangeOnlyMin() {
        var f = field("u", "User", null);
        assertEquals("User: value '0.5' must be equal or greater than 1.0",
                locale.decimalRangeError("f", f, null, 1.0, null, 0.5));
    }

    @Test
    public void testValueNotAllowed() {
        var f = field("u", "User", null);
        assertEquals("User: value 'foo' is not allowed, valid values: [a, b]",
                locale.valueNotAllowed("f", f, null, "[a, b]", "foo"));
    }

    @Test
    public void testExpectedDate() {
        var f = field("u", "User", null);
        assertEquals("User: expected a date value, got x", locale.expectedDate("f", f, null, "x"));
    }

    @Test
    public void testFieldNameFallsBackToName() {
        var f = field("u", null, null);
        assertEquals("u", DefaultFormValidatorLocale.fieldName(f, null));
    }

    @Test
    public void testFieldNameWithIndex() {
        var f = field("u", "User", null);
        assertEquals("User [3]", DefaultFormValidatorLocale.fieldName(f, 3));
    }

    @Test
    public void testSpellAllCardinalities() {
        assertEquals("any number of values", DefaultFormValidatorLocale.spell(FormField.Cardinality.ANY));
        assertEquals("a single value", DefaultFormValidatorLocale.spell(FormField.Cardinality.ONE_AND_ONLY_ONE));
        assertEquals("a single optional value", DefaultFormValidatorLocale.spell(FormField.Cardinality.ONE_OR_NONE));
        assertEquals("at least a single value", DefaultFormValidatorLocale.spell(FormField.Cardinality.AT_LEAST_ONE));
    }

    @Test
    public void testSpellNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> DefaultFormValidatorLocale.spell(null));
    }

    @Test
    public void testBoundsBothNull() {
        // Both null means equal-or-greater branch fires (else-branch) with min == null, so prints "less or equal than null"
        assertEquals("less or equal than null", DefaultFormValidatorLocale.bounds(null, null));
    }
}
