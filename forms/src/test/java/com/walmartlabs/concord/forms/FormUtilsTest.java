package com.walmartlabs.concord.forms;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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

    // --- getRunAsUsers ---

    @Test
    public void testGetRunAsUsersNull() {
        Set<String> result = FormUtils.getRunAsUsers("myForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsUsersSingleString() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", "admin");

        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
        assertEquals(1, result.size());
        assertTrue(result.contains("admin"));
    }

    @Test
    public void testGetRunAsUsersCollection() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", (Serializable) Arrays.asList("user1", "user2"));

        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
        assertEquals(2, result.size());
        assertTrue(result.contains("user1"));
        assertTrue(result.contains("user2"));
    }

    @Test
    public void testGetRunAsUsersMissingKey() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("other", "value");

        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsUsersInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", 123);

        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("myForm", params));
    }

    // --- getRunAsLdapGroups ---

    @Test
    public void testGetRunAsLdapGroupsNull() {
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsMissingLdap() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("other", "value");

        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsSingleGroup() {
        Map<String, Serializable> ldap = new HashMap<>();
        ldap.put("group", "admins");

        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(1, result.size());
        assertTrue(result.contains("admins"));
    }

    @Test
    public void testGetRunAsLdapGroupsMultipleGroups() {
        Map<String, Serializable> ldap = new HashMap<>();
        ldap.put("group", (Serializable) Arrays.asList("admins", "devs"));

        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(2, result.size());
        assertTrue(result.contains("admins"));
        assertTrue(result.contains("devs"));
    }

    @Test
    public void testGetRunAsLdapGroupsOldFormat() {
        Map<String, Serializable> group1 = new HashMap<>();
        group1.put("group", "admins");
        Map<String, Serializable> group2 = new HashMap<>();
        group2.put("group", "devs");

        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) Arrays.asList(group1, group2));

        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(2, result.size());
        assertTrue(result.contains("admins"));
        assertTrue(result.contains("devs"));
    }

    // --- convert (single value) ---

    @Test
    public void testConvertStringFieldEmpty() throws Exception {
        FormField f = field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_OR_NONE);
        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "myForm", f, null, "");
        assertNull(result);
    }

    @Test
    public void testConvertStringFieldValue() throws Exception {
        FormField f = field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE);
        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "myForm", f, null, "hello");
        assertEquals("hello", result);
    }

    @Test
    public void testConvertIntegerFieldFromString() throws Exception {
        FormField f = field("age", FormFields.IntegerField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE);
        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "myForm", f, null, "42");
        assertEquals(42L, result);
    }

    @Test
    public void testConvertIntegerFieldEmpty() throws Exception {
        FormField f = field("age", FormFields.IntegerField.TYPE, FormField.Cardinality.ONE_OR_NONE);
        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "myForm", f, null, "");
        assertNull(result);
    }

    @Test
    public void testConvertIntegerFieldInvalid() {
        FormField f = field("age", FormFields.IntegerField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE);
        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(new DefaultFormValidatorLocale(), "myForm", f, null, "abc"));
    }

    @Test
    public void testConvertDecimalFieldFromString() throws Exception {
        FormField f = field("price", FormFields.DecimalField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE);
        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "myForm", f, null, "3.14");
        assertEquals(3.14, result);
    }

    @Test
    public void testConvertDecimalFieldEmpty() throws Exception {
        FormField f = field("price", FormFields.DecimalField.TYPE, FormField.Cardinality.ONE_OR_NONE);
        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "myForm", f, null, "");
        assertNull(result);
    }

    @Test
    public void testConvertDecimalFieldInvalid() {
        FormField f = field("price", FormFields.DecimalField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE);
        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(new DefaultFormValidatorLocale(), "myForm", f, null, "abc"));
    }

    @Test
    public void testConvertBooleanFieldEmpty() throws Exception {
        FormField f = field("flag", FormFields.BooleanField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE);
        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "myForm", f, null, "");
        assertEquals(true, result);
    }

    @Test
    public void testConvertBooleanFieldFromString() throws Exception {
        FormField f = field("flag", FormFields.BooleanField.TYPE, FormField.Cardinality.ONE_AND_ONLY_ONE);
        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "myForm", f, null, "true");
        assertEquals(true, result);
    }

    @Test
    public void testConvertBooleanFieldNull() throws Exception {
        FormField f = field("flag", FormFields.BooleanField.TYPE, FormField.Cardinality.ONE_OR_NONE);
        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "myForm", f, null, null);
        assertEquals(false, result);
    }

    @Test
    public void testConvertEmptyList() throws Exception {
        FormField f = field("tags", FormFields.StringField.TYPE, FormField.Cardinality.ANY);
        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "myForm", f, null, Collections.emptyList());
        assertNull(result);
    }

    @Test
    public void testConvertNullMap() throws Exception {
        FormField f = field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_OR_NONE);
        Form form = Form.builder().name("myForm").eventName("ev").fields(Collections.singletonList(f)).build();
        Map<String, Object> result = FormUtils.convert(new DefaultFormValidatorLocale(), form, null);
        assertTrue(result.isEmpty());
    }

    // --- helpers ---

    private static FormField field(String name, String type, FormField.Cardinality cardinality) {
        return FormField.builder()
                .name(name)
                .type(type)
                .cardinality(cardinality)
                .build();
    }
}
