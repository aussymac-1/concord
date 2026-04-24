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
    public void testGetRunAsUsersNoUsername() {
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
        params.put("username", (Serializable) Arrays.asList("user1", "user2"));

        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
        assertEquals(2, result.size());
        assertTrue(result.contains("user1"));
        assertTrue(result.contains("user2"));
    }

    @Test
    public void testGetRunAsUsersCollectionInvalidItem() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", (Serializable) Arrays.asList("user1", 123));

        assertThrows(RuntimeException.class,
                () -> FormUtils.getRunAsUsers("myForm", params));
    }

    @Test
    public void testGetRunAsUsersInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", 123);

        assertThrows(RuntimeException.class,
                () -> FormUtils.getRunAsUsers("myForm", params));
    }

    @Test
    public void testGetRunAsLdapGroupsNull() {
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsNoLdap() {
        Map<String, Serializable> params = new HashMap<>();
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsSingleString() {
        Map<String, Serializable> ldap = new HashMap<>();
        ldap.put("group", "admins");

        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(1, result.size());
        assertTrue(result.contains("admins"));
    }

    @Test
    public void testGetRunAsLdapGroupsMultipleStrings() {
        Map<String, Serializable> ldap = new HashMap<>();
        ldap.put("group", (Serializable) Arrays.asList("admins", "users"));

        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(2, result.size());
        assertTrue(result.contains("admins"));
        assertTrue(result.contains("users"));
    }

    @Test
    public void testGetRunAsLdapGroupsOldSyntax() {
        Map<String, Serializable> entry1 = new HashMap<>();
        entry1.put("group", "groupA");

        Map<String, Serializable> entry2 = new HashMap<>();
        entry2.put("group", "groupB");

        List<Map<String, Serializable>> ldap = Arrays.asList(entry1, entry2);

        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(2, result.size());
        assertTrue(result.contains("groupA"));
        assertTrue(result.contains("groupB"));
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidLdapType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", "invalidString");

        assertThrows(RuntimeException.class,
                () -> FormUtils.getRunAsLdapGroups("myForm", params));
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidGroupItem() {
        Map<String, Serializable> ldap = new HashMap<>();
        ldap.put("group", (Serializable) Arrays.asList("admins", 123));

        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldap);

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
    public void testConvertEmptyStringField() throws Exception {
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
    public void testConvertIntegerFieldInvalid() {
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
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "3.14");
        assertEquals(3.14, result);
    }

    @Test
    public void testConvertDecimalFieldInvalid() {
        FormField field = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "abc"));
    }

    @Test
    public void testConvertBooleanFieldEmpty() throws Exception {
        FormField field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "");
        assertEquals(true, result);
    }

    @Test
    public void testConvertBooleanFieldTrue() throws Exception {
        FormField field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "true");
        assertEquals(true, result);
    }

    @Test
    public void testConvertBooleanFieldFalse() throws Exception {
        FormField field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "false");
        assertEquals(false, result);
    }

    @Test
    public void testConvertNullBooleanField() throws Exception {
        FormField field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, null);
        assertEquals(false, result);
    }

    @Test
    public void testConvertEmptyIntegerField() throws Exception {
        FormField field = FormField.builder()
                .name("count")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "");
        assertNull(result);
    }

    @Test
    public void testConvertEmptyDecimalField() throws Exception {
        FormField field = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, "");
        assertNull(result);
    }

    @Test
    public void testConvertListField() throws Exception {
        FormField field = FormField.builder()
                .name("items")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        List<String> input = Arrays.asList("a", "b", "");
        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, input);
        assertNotNull(result);
    }

    @Test
    public void testConvertEmptyListField() throws Exception {
        FormField field = FormField.builder()
                .name("items")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        List<String> input = Collections.emptyList();
        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "testForm", field, null, input);
        assertNull(result);
    }
}
