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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FormUtilsTest {

    @Test
    public void testGetRunAsUsersNull() {
        Set<String> result = FormUtils.getRunAsUsers("myForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsUsersNoKey() {
        Map<String, Serializable> params = new HashMap<>();
        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
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
        params.put("username", (Serializable) Arrays.asList("admin", "user1"));

        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
        assertEquals(2, result.size());
        assertTrue(result.contains("admin"));
        assertTrue(result.contains("user1"));
    }

    @Test
    public void testGetRunAsUsersInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", 42);

        assertThrows(RuntimeException.class,
                () -> FormUtils.getRunAsUsers("myForm", params));
    }

    @Test
    public void testGetRunAsUsersInvalidCollectionItem() {
        Map<String, Serializable> params = new HashMap<>();
        ArrayList<Object> list = new ArrayList<>();
        list.add(42);
        params.put("username", list);

        assertThrows(RuntimeException.class,
                () -> FormUtils.getRunAsUsers("myForm", params));
    }

    @Test
    public void testGetRunAsLdapGroupsNull() {
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsNoKey() {
        Map<String, Serializable> params = new HashMap<>();
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsMapSingleString() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ldap.put("group", "admins");
        params.put("ldap", (Serializable) ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(1, result.size());
        assertTrue(result.contains("admins"));
    }

    @Test
    public void testGetRunAsLdapGroupsMapCollection() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ldap.put("group", (Serializable) Arrays.asList("admins", "users"));
        params.put("ldap", (Serializable) ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(2, result.size());
        assertTrue(result.contains("admins"));
        assertTrue(result.contains("users"));
    }

    @Test
    public void testGetRunAsLdapGroupsOldSyntax() {
        Map<String, Serializable> params = new HashMap<>();
        ArrayList<Object> ldapList = new ArrayList<>();
        HashMap<Object, Object> entry1 = new HashMap<>();
        entry1.put("group", "groupA");
        ldapList.add(entry1);
        HashMap<Object, Object> entry2 = new HashMap<>();
        entry2.put("group", "groupB");
        ldapList.add(entry2);
        params.put("ldap", ldapList);

        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(2, result.size());
        assertTrue(result.contains("groupA"));
        assertTrue(result.contains("groupB"));
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", 42);

        assertThrows(RuntimeException.class,
                () -> FormUtils.getRunAsLdapGroups("myForm", params));
    }

    @Test
    public void testConvertStringField() throws Exception {
        FormField field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "hello");
        assertEquals("hello", result);
    }

    @Test
    public void testConvertEmptyStringToNull() throws Exception {
        FormField field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "");
        assertNull(result);
    }

    @Test
    public void testConvertIntegerField() throws Exception {
        FormField field = FormField.builder()
                .name("count")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "42");
        assertEquals(42L, result);
    }

    @Test
    public void testConvertInvalidInteger() {
        FormField field = FormField.builder()
                .name("count")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "abc"));
    }

    @Test
    public void testConvertDecimalField() throws Exception {
        FormField field = FormField.builder()
                .name("rate")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "3.14");
        assertEquals(3.14, result);
    }

    @Test
    public void testConvertBooleanField() throws Exception {
        FormField field = FormField.builder()
                .name("active")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "true");
        assertEquals(true, result);
    }

    @Test
    public void testConvertBooleanEmptyString() throws Exception {
        FormField field = FormField.builder()
                .name("active")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "");
        assertEquals(true, result);
    }
}
