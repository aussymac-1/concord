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
        params.put("username", "admin");
        Set<String> result = FormUtils.getRunAsUsers("form1", params);
        assertEquals(Collections.singleton("admin"), result);
    }

    @Test
    public void testGetRunAsUsersCollection() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", new ArrayList<>(Arrays.asList("user1", "user2")));
        Set<String> result = FormUtils.getRunAsUsers("form1", params);
        assertEquals(Set.of("user1", "user2"), result);
    }

    @Test
    public void testGetRunAsUsersInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", 123);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("form1", params));
    }

    @Test
    public void testGetRunAsUsersCollectionWithInvalidItem() {
        Map<String, Serializable> params = new HashMap<>();
        ArrayList<Object> items = new ArrayList<>();
        items.add(123);
        params.put("username", items);
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
    public void testGetRunAsLdapGroupsMapWithSingleGroup() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ldap.put("group", "myGroup");
        params.put("ldap", ldap);
        Set<String> result = FormUtils.getRunAsLdapGroups("form1", params);
        assertEquals(Collections.singleton("myGroup"), result);
    }

    @Test
    public void testGetRunAsLdapGroupsMapWithGroupList() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ldap.put("group", new ArrayList<>(Arrays.asList("groupA", "groupB")));
        params.put("ldap", ldap);
        Set<String> result = FormUtils.getRunAsLdapGroups("form1", params);
        assertEquals(Set.of("groupA", "groupB"), result);
    }

    @Test
    public void testGetRunAsLdapGroupsCollectionOfMaps() {
        Map<String, Serializable> params = new HashMap<>();
        ArrayList<Object> ldapList = new ArrayList<>();
        HashMap<Object, Object> item1 = new HashMap<>();
        item1.put("group", "groupA");
        HashMap<Object, Object> item2 = new HashMap<>();
        item2.put("group", "groupB");
        ldapList.add(item1);
        ldapList.add(item2);
        params.put("ldap", ldapList);
        Set<String> result = FormUtils.getRunAsLdapGroups("form1", params);
        assertEquals(Set.of("groupA", "groupB"), result);
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", 123);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("form1", params));
    }

    @Test
    public void testConvertStringToIntegerField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("count")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new TestLocale(), "testForm", field, null, "42");
        assertEquals(42L, result);
    }

    @Test
    public void testConvertEmptyStringToIntegerField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("count")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Object result = FormUtils.convert(new TestLocale(), "testForm", field, null, "");
        assertNull(result);
    }

    @Test
    public void testConvertInvalidStringToIntegerField() {
        FormField field = FormField.builder()
                .name("count")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(new TestLocale(), "testForm", field, null, "abc"));
    }

    @Test
    public void testConvertStringToDecimalField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new TestLocale(), "testForm", field, null, "3.14");
        assertEquals(3.14, result);
    }

    @Test
    public void testConvertStringToBooleanField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("flag")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new TestLocale(), "testForm", field, null, "true");
        assertEquals(true, result);
    }

    @Test
    public void testConvertEmptyBooleanField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("flag")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        Object result = FormUtils.convert(new TestLocale(), "testForm", field, null, "");
        assertEquals(true, result);
    }

    @Test
    public void testConvertNullBooleanField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("flag")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Object result = FormUtils.convert(new TestLocale(), "testForm", field, null, null);
        assertEquals(false, result);
    }

    @Test
    public void testConvertEmptyStringField() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();

        Object result = FormUtils.convert(new TestLocale(), "testForm", field, null, "");
        assertNull(result);
    }

    @Test
    public void testConvertEmptyList() throws FormUtils.ValidationException {
        FormField field = FormField.builder()
                .name("items")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ANY)
                .build();

        Object result = FormUtils.convert(new TestLocale(), "testForm", field, null, Collections.emptyList());
        assertNull(result);
    }

    @Test
    public void testConvertNullMap() throws FormUtils.ValidationException {
        Form form = Form.builder()
                .name("testForm")
                .build();
        Map<String, Object> result = FormUtils.convert(new TestLocale(), form, null);
        assertTrue(result.isEmpty());
    }

    private static class TestLocale implements FormValidatorLocale {
        @Override
        public String noFieldsDefined(String formName) { return "no fields"; }
        @Override
        public String fieldIsMandatory(String formName, FormField field) { return "mandatory"; }
        @Override
        public String invalidCardinality(String formName, FormField field, Object value) { return "invalid cardinality"; }
        @Override
        public String doesntMatchPattern(String formName, FormField field, Integer idx, String pattern, Object value) { return "no match"; }
        @Override
        public String integerRangeError(String formName, FormField field, Integer idx, Long min, Long max, Object value) { return "range error"; }
        @Override
        public String decimalRangeError(String formName, FormField field, Integer idx, Double min, Double max, Object value) { return "range error"; }
        @Override
        public String valueNotAllowed(String formName, FormField field, Integer idx, Object allowed, Object value) { return "not allowed"; }
        @Override
        public String expectedString(String formName, FormField field, Integer idx, Object value) { return "expected string"; }
        @Override
        public String expectedInteger(String formName, FormField field, Integer idx, Object value) { return "expected int"; }
        @Override
        public String expectedDecimal(String formName, FormField field, Integer idx, Object value) { return "expected decimal"; }
        @Override
        public String expectedBoolean(String formName, FormField field, Integer idx, Object value) { return "expected boolean"; }
    }
}
