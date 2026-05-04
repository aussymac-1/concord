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

public class FormFieldsTest {

    @Test
    public void testStringFieldOptions() {
        assertEquals("string", FormFields.StringField.TYPE);
        assertEquals("pattern", FormFields.StringField.PATTERN.name());
        assertEquals(String.class, FormFields.StringField.PATTERN.type());

        assertEquals("inputType", FormFields.StringField.INPUT_TYPE.name());
        assertEquals("placeholder", FormFields.StringField.PLACEHOLDER.name());
        assertEquals("search", FormFields.StringField.SEARCH.name());
        assertEquals(Boolean.class, FormFields.StringField.SEARCH.type());
    }

    @Test
    public void testIntegerFieldOptions() {
        assertEquals("int", FormFields.IntegerField.TYPE);
        assertEquals(Long.class, FormFields.IntegerField.MIN.type());
        assertEquals(Long.class, FormFields.IntegerField.MAX.type());
        assertEquals(String.class, FormFields.IntegerField.PLACEHOLDER.type());
    }

    @Test
    public void testDecimalFieldOptions() {
        assertEquals("decimal", FormFields.DecimalField.TYPE);
        assertEquals(Double.class, FormFields.DecimalField.MIN.type());
        assertEquals(Double.class, FormFields.DecimalField.MAX.type());
    }

    @Test
    public void testSimpleTypeConstants() {
        assertEquals("boolean", FormFields.BooleanField.TYPE);
        assertEquals("file", FormFields.FileField.TYPE);
        assertEquals("date", FormFields.DateField.TYPE);
        assertEquals("dateTime", FormFields.DateTimeField.TYPE);
    }

    @Test
    public void testDateFieldOptions() {
        assertEquals("popupPosition", FormFields.DateFieldOptions.POPUP_POSITION.name());
        assertEquals(String.class, FormFields.DateFieldOptions.POPUP_POSITION.type());
    }

    @Test
    public void testCommonFieldOptions() {
        assertEquals("readOnly", FormFields.CommonFieldOptions.READ_ONLY.name());
        assertEquals(Boolean.class, FormFields.CommonFieldOptions.READ_ONLY.type());
    }
}
