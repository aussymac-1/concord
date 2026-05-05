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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormTest {

    @Test
    public void testDefaults() {
        var form = Form.builder()
                .name("approval")
                .build();

        assertEquals("approval", form.name());
        assertTrue(form.fields().isEmpty());
        assertNull(form.location());
    }

    @Test
    public void testWithFieldsAndLocation() {
        var field = FormField.builder().name("email").type("string").build();
        var sm = SourceMap.builder().source("a.yml").line(1).column(1).build();

        var form = Form.builder()
                .name("approval")
                .addFields(field)
                .location(sm)
                .build();

        assertEquals(List.of(field), form.fields());
        assertEquals(sm, form.location());
    }

    @Test
    public void testMissingNameThrows() {
        var builder = Form.builder();

        assertThrows(IllegalStateException.class, builder::build);
    }
}
