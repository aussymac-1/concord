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

    private FormField stringField(String name) {
        return FormField.builder()
                .name(name)
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();
    }

    private FormField labeledField(String name, String label) {
        return FormField.builder()
                .name(name)
                .label(label)
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();
    }

    @Test
    public void testNoFieldsDefined() {
        var msg = locale.noFieldsDefined("formA");
        assertTrue(msg.contains("formA"));
    }

    @Test
    public void testInvalidCardinality() {
        var field = stringField("myField");
        var msg = locale.invalidCardinality("formA", field, null);
        assertTrue(msg.contains("myField"));
    }

    @Test
    public void testExpectedString() {
        var field = stringField("myField");
        var msg = locale.expectedString("formA", field, null, 123);
        assertTrue(msg.contains("myField"));
        assertTrue(msg.contains("123"));
    }

    @Test
    public void testExpectedInteger() {
        var field = stringField("myField");
        var msg = locale.expectedInteger("formA", field, null, "abc");
        assertTrue(msg.contains("myField"));
    }

    @Test
    public void testExpectedDecimal() {
        var field = stringField("myField");
        var msg = locale.expectedDecimal("formA", field, null, "abc");
        assertTrue(msg.contains("myField"));
    }

    @Test
    public void testExpectedBoolean() {
        var field = stringField("myField");
        var msg = locale.expectedBoolean("formA", field, null, "abc");
        assertTrue(msg.contains("myField"));
    }

    @Test
    public void testDoesntMatchPattern() {
        var field = stringField("myField");
        var msg = locale.doesntMatchPattern("formA", field, null, "\\d+", "abc");
        assertTrue(msg.contains("myField"));
        assertTrue(msg.contains("\\d+"));
    }

    @Test
    public void testIntegerRangeError() {
        var field = stringField("myField");
        var msg = locale.integerRangeError("formA", field, null, 1L, 10L, 11);
        assertTrue(msg.contains("myField"));
        assertTrue(msg.contains("11"));
    }

    @Test
    public void testDecimalRangeError() {
        var field = stringField("myField");
        var msg = locale.decimalRangeError("formA", field, null, 1.0, 10.0, 11.0);
        assertTrue(msg.contains("myField"));
    }

    @Test
    public void testValueNotAllowed() {
        var field = stringField("myField");
        var msg = locale.valueNotAllowed("formA", field, null, "a", "b");
        assertTrue(msg.contains("myField"));
        assertTrue(msg.contains("not allowed"));
    }

    @Test
    public void testExpectedDate() {
        var field = stringField("myField");
        var msg = locale.expectedDate("formA", field, null, "abc");
        assertTrue(msg.contains("myField"));
    }

    @Test
    public void testFieldNameUsesLabel() {
        var field = labeledField("fieldName", "Field Label");
        var name = DefaultFormValidatorLocale.fieldName(field, null);
        assertEquals("Field Label", name);
    }

    @Test
    public void testFieldNameFallsBackToName() {
        var field = stringField("fieldName");
        var name = DefaultFormValidatorLocale.fieldName(field, null);
        assertEquals("fieldName", name);
    }

    @Test
    public void testFieldNameWithIndex() {
        var field = stringField("fieldName");
        var name = DefaultFormValidatorLocale.fieldName(field, 2);
        assertEquals("fieldName [2]", name);
    }

    @Test
    public void testSpellCardinalities() {
        assertNotNull(DefaultFormValidatorLocale.spell(FormField.Cardinality.ANY));
        assertNotNull(DefaultFormValidatorLocale.spell(FormField.Cardinality.ONE_AND_ONLY_ONE));
        assertNotNull(DefaultFormValidatorLocale.spell(FormField.Cardinality.ONE_OR_NONE));
        assertNotNull(DefaultFormValidatorLocale.spell(FormField.Cardinality.AT_LEAST_ONE));
    }

    @Test
    public void testSpellNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> DefaultFormValidatorLocale.spell(null));
    }

    @Test
    public void testBoundsBoth() {
        var result = DefaultFormValidatorLocale.bounds(1, 10);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("10"));
    }

    @Test
    public void testBoundsMinOnly() {
        var result = DefaultFormValidatorLocale.bounds(1, null);
        assertTrue(result.contains("1"));
    }

    @Test
    public void testBoundsMaxOnly() {
        var result = DefaultFormValidatorLocale.bounds(null, 10);
        assertTrue(result.contains("10"));
    }
}
