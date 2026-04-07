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
        Set<String> result = FormUtils.getRunAsUsers("form1", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsUsersNoUsername() {
        Map<String, Serializable> params = new HashMap<>();
        Set<String> result = FormUtils.getRunAsUsers("form1", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsUsersSingleString() {
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.RUN_AS_USERNAME_KEY, "user1");
        Set<String> result = FormUtils.getRunAsUsers("form1", params);
        assertEquals(Collections.singleton("user1"), result);
    }

    @Test
    public void testGetRunAsUsersCollection() {
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.RUN_AS_USERNAME_KEY, new ArrayList<>(Arrays.asList("user1", "user2")));
        Set<String> result = FormUtils.getRunAsUsers("form1", params);
        assertEquals(new HashSet<>(Arrays.asList("user1", "user2")), result);
    }

    @Test
    public void testGetRunAsUsersInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.RUN_AS_USERNAME_KEY, 123);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("form1", params));
    }

    @Test
    public void testGetRunAsUsersCollectionWithInvalidItem() {
        Map<String, Serializable> params = new HashMap<>();
        ArrayList<Object> list = new ArrayList<>();
        list.add(123);
        params.put(Constants.RUN_AS_USERNAME_KEY, list);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("form1", params));
    }

    @Test
    public void testGetRunAsLdapGroupsNull() {
        Set<String> result = FormUtils.getRunAsLdapGroups("form1", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsNoLdap() {
        Map<String, Serializable> params = new HashMap<>();
        Set<String> result = FormUtils.getRunAsLdapGroups("form1", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsSingleMapString() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ldap.put(Constants.RUN_AS_GROUP_KEY, "group1");
        params.put(Constants.RUN_AS_LDAP_KEY, ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("form1", params);
        assertEquals(Collections.singleton("group1"), result);
    }

    @Test
    public void testGetRunAsLdapGroupsMapCollection() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ldap.put(Constants.RUN_AS_GROUP_KEY, new ArrayList<>(Arrays.asList("group1", "group2")));
        params.put(Constants.RUN_AS_LDAP_KEY, ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("form1", params);
        assertEquals(new HashSet<>(Arrays.asList("group1", "group2")), result);
    }

    @Test
    public void testGetRunAsLdapGroupsOldSyntax() {
        Map<String, Serializable> params = new HashMap<>();
        ArrayList<Object> ldapList = new ArrayList<>();
        HashMap<Object, Object> item1 = new HashMap<>();
        item1.put(Constants.RUN_AS_GROUP_KEY, "group1");
        ldapList.add(item1);
        HashMap<Object, Object> item2 = new HashMap<>();
        item2.put(Constants.RUN_AS_GROUP_KEY, "group2");
        ldapList.add(item2);
        params.put(Constants.RUN_AS_LDAP_KEY, ldapList);

        Set<String> result = FormUtils.getRunAsLdapGroups("form1", params);
        assertEquals(new HashSet<>(Arrays.asList("group1", "group2")), result);
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.RUN_AS_LDAP_KEY, "invalid");
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("form1", params));
    }

    @Test
    public void testConvertStringFieldEmpty() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "form1", field, null, "");
        assertNull(result);
    }

    @Test
    public void testConvertIntegerField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "form1", field, null, "42");
        assertEquals(42L, result);
    }

    @Test
    public void testConvertIntegerFieldEmpty() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "form1", field, null, "");
        assertNull(result);
    }

    @Test
    public void testConvertIntegerFieldInvalid() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(new DefaultFormValidatorLocale(), "form1", field, null, "notANumber"));
    }

    @Test
    public void testConvertDecimalField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "form1", field, null, "3.14");
        assertEquals(3.14, result);
    }

    @Test
    public void testConvertDecimalFieldInvalid() {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(new DefaultFormValidatorLocale(), "form1", field, null, "notADecimal"));
    }

    @Test
    public void testConvertBooleanFieldEmpty() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "form1", field, null, "");
        assertEquals(true, result);
    }

    @Test
    public void testConvertBooleanFieldTrue() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "form1", field, null, "true");
        assertEquals(true, result);
    }

    @Test
    public void testConvertBooleanFieldNull() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "form1", field, null, null);
        assertEquals(false, result);
    }

    @Test
    public void testConvertListField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.AT_LEAST_ONE)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "form1", field, null, Arrays.asList("1", "2", "3"));
        assertEquals(Arrays.asList(1L, 2L, 3L), result);
    }

    @Test
    public void testConvertEmptyList() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("myField")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        Object result = FormUtils.convert(new DefaultFormValidatorLocale(), "form1", field, null, Collections.emptyList());
        assertNull(result);
    }
}
