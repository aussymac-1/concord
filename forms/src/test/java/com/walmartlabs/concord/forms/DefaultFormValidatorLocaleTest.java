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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultFormValidatorLocaleTest {

    private final DefaultFormValidatorLocale locale = new DefaultFormValidatorLocale();

    @Test
    public void noFieldsDefinedMessageIncludesFormId() {
        assertEquals("Form (myForm) has no fields", locale.noFieldsDefined("myForm"));
    }

    @Test
    public void fieldNameUsesLabelWhenSet() {
        var field = formField("id", "My Label", FormField.Cardinality.ONE_OR_NONE);
        assertEquals("My Label", DefaultFormValidatorLocale.fieldName(field, null));
        assertEquals("My Label [2]", DefaultFormValidatorLocale.fieldName(field, 2));
    }

    @Test
    public void fieldNameFallsBackToName() {
        var field = formField("id", null, FormField.Cardinality.ONE_OR_NONE);
        assertEquals("id", DefaultFormValidatorLocale.fieldName(field, null));
    }

    @Test
    public void invalidCardinalityComposesMessage() {
        var field = formField("id", "Label", FormField.Cardinality.ONE_AND_ONLY_ONE);
        var msg = locale.invalidCardinality("f", field, 1);
        assertTrue(msg.contains("Label"));
        assertTrue(msg.contains("a single value"));
    }

    @Test
    public void expectedStringMessageMentionsValue() {
        var field = formField("id", "Label", FormField.Cardinality.ANY);
        var msg = locale.expectedString("f", field, 0, 42);
        assertTrue(msg.contains("string"));
        assertTrue(msg.contains("42"));
    }

    @Test
    public void expectedIntegerMessageMentionsValue() {
        var field = formField("id", "Label", FormField.Cardinality.ANY);
        var msg = locale.expectedInteger("f", field, 1, "abc");
        assertTrue(msg.contains("integer"));
        assertTrue(msg.contains("abc"));
    }

    @Test
    public void expectedDecimalMessageMentionsValue() {
        var field = formField("id", "Label", FormField.Cardinality.ANY);
        var msg = locale.expectedDecimal("f", field, 0, "a");
        assertTrue(msg.contains("decimal"));
    }

    @Test
    public void expectedBooleanMessageMentionsValue() {
        var field = formField("id", "Label", FormField.Cardinality.ANY);
        var msg = locale.expectedBoolean("f", field, 0, "maybe");
        assertTrue(msg.contains("boolean"));
    }

    @Test
    public void expectedDateMessageMentionsValue() {
        var field = formField("id", "Label", FormField.Cardinality.ANY);
        var msg = locale.expectedDate("f", field, 0, "???");
        assertTrue(msg.contains("date"));
    }

    @Test
    public void doesntMatchPatternMessageMentionsPatternAndValue() {
        var field = formField("id", "Label", FormField.Cardinality.ANY);
        var msg = locale.doesntMatchPattern("f", field, 0, "[0-9]+", "abc");
        assertTrue(msg.contains("[0-9]+"));
        assertTrue(msg.contains("abc"));
    }

    @Test
    public void integerRangeErrorMessageIncludesBounds() {
        var field = formField("id", "Label", FormField.Cardinality.ANY);
        var msg = locale.integerRangeError("f", field, 0, 1L, 10L, 100);
        assertTrue(msg.contains("within 1 and 10 (inclusive)"));
    }

    @Test
    public void decimalRangeErrorMessageIncludesBounds() {
        var field = formField("id", "Label", FormField.Cardinality.ANY);
        var msg = locale.decimalRangeError("f", field, 0, 0.0, 1.0, 2.0);
        assertTrue(msg.contains("within 0.0 and 1.0 (inclusive)"));
    }

    @Test
    public void valueNotAllowedMessageIncludesAllowed() {
        var field = formField("id", "Label", FormField.Cardinality.ANY);
        var msg = locale.valueNotAllowed("f", field, 0, java.util.List.of("a", "b"), "c");
        assertTrue(msg.contains("[a, b]"));
        assertTrue(msg.contains("c"));
    }

    @Test
    public void spellReturnsHumanReadableCardinality() {
        assertEquals("any number of values", DefaultFormValidatorLocale.spell(FormField.Cardinality.ANY));
        assertEquals("a single value", DefaultFormValidatorLocale.spell(FormField.Cardinality.ONE_AND_ONLY_ONE));
        assertEquals("a single optional value", DefaultFormValidatorLocale.spell(FormField.Cardinality.ONE_OR_NONE));
        assertEquals("at least a single value", DefaultFormValidatorLocale.spell(FormField.Cardinality.AT_LEAST_ONE));
    }

    @Test
    public void spellNullThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> DefaultFormValidatorLocale.spell(null));
    }

    @Test
    public void boundsHandlesMinOnlyAndMaxOnly() {
        assertEquals("within 0 and 10 (inclusive)", DefaultFormValidatorLocale.bounds(0, 10));
        assertEquals("less or equal than 10", DefaultFormValidatorLocale.bounds(null, 10));
        assertEquals("equal or greater than 0", DefaultFormValidatorLocale.bounds(0, null));
    }

    private static FormField formField(String name, String label, FormField.Cardinality c) {
        var b = FormField.builder().name(name).type("string").cardinality(c);
        if (label != null) {
            b.label(label);
        }
        return b.build();
    }
}
