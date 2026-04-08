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

import java.io.Serializable;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FormUtilsTest {

    private final FormValidatorLocale locale = new DefaultFormValidatorLocale();

    @Test
    public void testGetRunAsUsersNull() {
        Set<String> result = FormUtils.getRunAsUsers("testForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsUsersNoUsername() {
        Map<String, Serializable> params = new HashMap<>();
        Set<String> result = FormUtils.getRunAsUsers("testForm", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsUsersSingleString() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", "admin");
        Set<String> result = FormUtils.getRunAsUsers("testForm", params);
        assertEquals(Collections.singleton("admin"), result);
    }

    @Test
    public void testGetRunAsUsersCollection() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", (Serializable) Arrays.asList("admin", "user1"));
        Set<String> result = FormUtils.getRunAsUsers("testForm", params);
        assertEquals(new HashSet<>(Arrays.asList("admin", "user1")), result);
    }

    @Test
    public void testGetRunAsUsersCollectionInvalidElement() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", (Serializable) Arrays.asList("admin", 123));
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("testForm", params));
    }

    @Test
    public void testGetRunAsUsersInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", 123);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("testForm", params));
    }

    @Test
    public void testGetRunAsLdapGroupsNull() {
        Set<String> result = FormUtils.getRunAsLdapGroups("testForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsNoLdap() {
        Map<String, Serializable> params = new HashMap<>();
        Set<String> result = FormUtils.getRunAsLdapGroups("testForm", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsMapWithSingleString() {
        Map<String, Serializable> params = new HashMap<>();
        Map<String, Serializable> ldap = new HashMap<>();
        ldap.put("group", "admins");
        params.put("ldap", (Serializable) ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("testForm", params);
        assertEquals(Collections.singleton("admins"), result);
    }

    @Test
    public void testGetRunAsLdapGroupsMapWithCollection() {
        Map<String, Serializable> params = new HashMap<>();
        Map<String, Serializable> ldap = new HashMap<>();
        ldap.put("group", (Serializable) Arrays.asList("admins", "devs"));
        params.put("ldap", (Serializable) ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("testForm", params);
        assertEquals(new HashSet<>(Arrays.asList("admins", "devs")), result);
    }

    @Test
    public void testGetRunAsLdapGroupsMapWithCollectionInvalidElement() {
        Map<String, Serializable> params = new HashMap<>();
        Map<String, Serializable> ldap = new HashMap<>();
        ldap.put("group", (Serializable) Arrays.asList("admins", 123));
        params.put("ldap", (Serializable) ldap);

        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("testForm", params));
    }

    @Test
    public void testGetRunAsLdapGroupsCollectionOfMaps() {
        Map<String, Serializable> params = new HashMap<>();
        Map<String, Serializable> group1 = new HashMap<>();
        group1.put("group", "admins");
        Map<String, Serializable> group2 = new HashMap<>();
        group2.put("group", "devs");
        params.put("ldap", (Serializable) Arrays.asList(group1, group2));

        Set<String> result = FormUtils.getRunAsLdapGroups("testForm", params);
        assertEquals(new HashSet<>(Arrays.asList("admins", "devs")), result);
    }

    @Test
    public void testGetRunAsLdapGroupsCollectionOfMapsInvalidElement() {
        Map<String, Serializable> params = new HashMap<>();
        Map<String, Serializable> group1 = new HashMap<>();
        group1.put("group", 123);
        params.put("ldap", (Serializable) Arrays.asList(group1));

        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("testForm", params));
    }

    @Test
    public void testGetRunAsLdapGroupsCollectionOfNonMaps() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) Arrays.asList("invalidFormat"));

        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("testForm", params));
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", 123);

        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("testForm", params));
    }

    @Test
    public void testConvertStringField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertEquals("hello", FormUtils.convert(locale, "testForm", field, null, "hello"));
    }

    @Test
    public void testConvertStringFieldEmpty() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertNull(FormUtils.convert(locale, "testForm", field, null, ""));
    }

    @Test
    public void testConvertIntegerField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertEquals(42L, FormUtils.convert(locale, "testForm", field, null, "42"));
    }

    @Test
    public void testConvertIntegerFieldEmpty() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertNull(FormUtils.convert(locale, "testForm", field, null, ""));
    }

    @Test
    public void testConvertIntegerFieldInvalid() {
        FormField field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(locale, "testForm", field, null, "notanumber"));
    }

    @Test
    public void testConvertDecimalField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertEquals(19.99, FormUtils.convert(locale, "testForm", field, null, "19.99"));
    }

    @Test
    public void testConvertDecimalFieldEmpty() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertNull(FormUtils.convert(locale, "testForm", field, null, ""));
    }

    @Test
    public void testConvertDecimalFieldInvalid() {
        FormField field = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(locale, "testForm", field, null, "notadecimal"));
    }

    @Test
    public void testConvertBooleanFieldEmpty() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertEquals(true, FormUtils.convert(locale, "testForm", field, null, ""));
    }

    @Test
    public void testConvertBooleanFieldString() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertEquals(true, FormUtils.convert(locale, "testForm", field, null, "true"));
        assertEquals(false, FormUtils.convert(locale, "testForm", field, null, "false"));
    }

    @Test
    public void testConvertBooleanFieldNull() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        assertEquals(false, FormUtils.convert(locale, "testForm", field, null, null));
    }

    @Test
    public void testConvertListField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("tags")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        List<?> result = (List<?>) FormUtils.convert(locale, "testForm", field, null, Arrays.asList("a", "b"));
        assertEquals(Arrays.asList("a", "b"), result);
    }

    @Test
    public void testConvertEmptyList() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("tags")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        assertNull(FormUtils.convert(locale, "testForm", field, null, Collections.emptyList()));
    }

    @Test
    public void testConvertNullMap() throws FormUtils.ValidationException {
        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .build();

        Map<String, Object> result = FormUtils.convert(locale, form, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testConvertFormWithReadOnlyField() throws FormUtils.ValidationException {
        Map<String, Serializable> options = new HashMap<>();
        options.put("readOnly", true);

        FormField field = FormField.builder()
                .name("status")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .defaultValue("active")
                .options(options)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("status", "overridden");

        Map<String, Object> result = FormUtils.convert(locale, form, data);
        assertEquals("active", result.get("status"));
    }

    @Test
    public void testConvertFormWithSingleAllowedValue() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("choice")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .allowedValue((Serializable) Collections.singletonList("onlyOption"))
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> result = FormUtils.convert(locale, form, data);
        assertEquals("onlyOption", result.get("choice"));
    }

    @Test
    public void testConvertFormStringToListForAnyCardinality() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("tags")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        Form form = Form.builder()
                .name("testForm")
                .eventName("evt")
                .addFields(field)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("tags", "singleTag");

        Map<String, Object> result = FormUtils.convert(locale, form, data);
        assertEquals(Collections.singletonList("singleTag"), result.get("tags"));
    }

    @Test
    public void testConvertDateField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("dt")
                .type(FormFields.DateField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(locale, "testForm", field, null, "2024-01-15T10:30:00Z");
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testConvertDateFieldEmpty() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("dt")
                .type(FormFields.DateField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        assertNull(FormUtils.convert(locale, "testForm", field, null, ""));
    }

    @Test
    public void testConvertDateTimeField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("dt")
                .type(FormFields.DateTimeField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(locale, "testForm", field, null, "2024-01-15T10:30:00Z");
        assertNotNull(result);
    }

    @Test
    public void testValidationExceptionProperties() {
        FormField field = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        try {
            FormUtils.convert(locale, "testForm", field, null, "notanumber");
            fail("Expected ValidationException");
        } catch (FormUtils.ValidationException e) {
            assertEquals(field, e.getField());
            assertEquals("notanumber", e.getInput());
            assertNotNull(e.getMessage());
        }
    }
}
