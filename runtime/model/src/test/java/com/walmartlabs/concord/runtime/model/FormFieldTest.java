package com.walmartlabs.concord.runtime.model;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormFieldTest {

    @Test
    public void testRequiredFieldsAndDefaults() {
        var f = FormField.builder()
                .name("email")
                .type("string")
                .build();

        assertEquals("email", f.name());
        assertEquals("string", f.type());
        assertNull(f.label());
        assertNull(f.defaultValue());
        assertNull(f.allowedValue());
        assertNull(f.location());
        assertTrue(f.options().isEmpty());
    }

    @Test
    public void testFullyPopulated() {
        Serializable defaultValue = "foo";
        Serializable allowedValue = (Serializable) java.util.List.of("foo", "bar");
        Map<String, Serializable> options = Map.of("required", true);
        var sm = SourceMap.builder().source("a.yml").line(1).column(1).build();

        var f = FormField.builder()
                .name("email")
                .label("E-mail")
                .type("string")
                .defaultValue(defaultValue)
                .allowedValue(allowedValue)
                .options(options)
                .location(sm)
                .build();

        assertEquals("email", f.name());
        assertEquals("E-mail", f.label());
        assertEquals("string", f.type());
        assertEquals(defaultValue, f.defaultValue());
        assertEquals(allowedValue, f.allowedValue());
        assertEquals(options, f.options());
        assertSame(sm, f.location());
    }

    @Test
    public void testMissingRequiredAttributesThrow() {
        var builder = FormField.builder().type("string");

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    public void testEqualsAndHashCode() {
        var a = FormField.builder().name("n").type("string").build();
        var b = FormField.builder().name("n").type("string").build();
        var c = FormField.builder().name("n").type("int").build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
