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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FormFieldTest {

    @Test
    public void testBuilderProducesImmutableFormField() {
        var f = FormField.builder()
                .name("age")
                .label("Age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertEquals("age", f.name());
        assertEquals("Age", f.label());
        assertEquals(FormFields.IntegerField.TYPE, f.type());
        assertEquals(FormField.Cardinality.ONE_AND_ONLY_ONE, f.cardinality());
        assertTrue(f.options().isEmpty());
        assertNull(f.defaultValue());
        assertNull(f.allowedValue());
    }

    @Test
    public void testGetOptionAbsentReturnsNull() {
        var f = FormField.builder()
                .name("a")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        assertNull(f.getOption(FormFields.StringField.PATTERN));
    }

    @Test
    public void testGetOptionPresentReturnsTypedValue() {
        var f = FormField.builder()
                .name("a")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .options(Map.of("pattern", (Serializable) "\\d+"))
                .build();

        assertEquals("\\d+", f.getOption(FormFields.StringField.PATTERN));
    }

    @Test
    public void testOptionCastReturnsNullForNullInput() {
        var opt = FormField.Option.of("k", String.class);
        assertNull(opt.cast(null));
    }

    @Test
    public void testOptionCastWrongTypeThrows() {
        var opt = FormField.Option.of("k", String.class);
        assertThrows(IllegalArgumentException.class, () -> opt.cast(42));
    }

    @Test
    public void testOptionAccessors() {
        var opt = FormField.Option.of("name", Integer.class);
        assertEquals("name", opt.name());
        assertEquals(Integer.class, opt.type());
    }

    @Test
    public void testCardinalityHasFourValues() {
        assertEquals(4, FormField.Cardinality.values().length);
    }
}
