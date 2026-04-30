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
        var result = FormUtils.getRunAsUsers("myForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsUsersNoUsernameKey() {
        var params = new HashMap<String, Serializable>();
        params.put("other", "val");
        var result = FormUtils.getRunAsUsers("myForm", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsUsersSingleString() {
        var params = new HashMap<String, Serializable>();
        params.put("username", "alice");
        var result = FormUtils.getRunAsUsers("myForm", params);
        assertEquals(Collections.singleton("alice"), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetRunAsUsersCollection() {
        var params = new HashMap<String, Serializable>();
        params.put("username", (Serializable) Arrays.asList("alice", "bob"));
        var result = FormUtils.getRunAsUsers("myForm", params);
        assertEquals(new HashSet<>(Arrays.asList("alice", "bob")), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetRunAsUsersCollectionNonString() {
        var params = new HashMap<String, Serializable>();
        params.put("username", (Serializable) Arrays.asList("alice", 123));
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("myForm", params));
    }

    @Test
    public void testGetRunAsUsersInvalidType() {
        var params = new HashMap<String, Serializable>();
        params.put("username", 42);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("myForm", params));
    }

    @Test
    public void testGetRunAsLdapGroupsNull() {
        var result = FormUtils.getRunAsLdapGroups("myForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsNoLdapKey() {
        var params = new HashMap<String, Serializable>();
        params.put("other", "val");
        var result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertTrue(result.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetRunAsLdapGroupsMapSingleString() {
        var ldap = new HashMap<Object, Object>();
        ldap.put("group", "admins");
        var params = new HashMap<String, Serializable>();
        params.put("ldap", (Serializable) ldap);
        var result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(Collections.singleton("admins"), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetRunAsLdapGroupsMapCollection() {
        var ldap = new HashMap<Object, Object>();
        ldap.put("group", (Serializable) Arrays.asList("admins", "devs"));
        var params = new HashMap<String, Serializable>();
        params.put("ldap", (Serializable) ldap);
        var result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(new HashSet<>(Arrays.asList("admins", "devs")), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetRunAsLdapGroupsCollectionOfMaps() {
        var item1 = new HashMap<Object, Object>();
        item1.put("group", "admins");
        var item2 = new HashMap<Object, Object>();
        item2.put("group", "devs");
        var ldap = Arrays.asList(item1, item2);
        var params = new HashMap<String, Serializable>();
        params.put("ldap", (Serializable) ldap);
        var result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(new HashSet<>(Arrays.asList("admins", "devs")), result);
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidType() {
        var params = new HashMap<String, Serializable>();
        params.put("ldap", 42);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("myForm", params));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetRunAsLdapGroupsMapNonStringElement() {
        var ldap = new HashMap<Object, Object>();
        ldap.put("group", (Serializable) Arrays.asList("admins", 123));
        var params = new HashMap<String, Serializable>();
        params.put("ldap", (Serializable) ldap);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("myForm", params));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetRunAsLdapGroupsCollectionNonMapItem() {
        var ldap = Arrays.asList("not-a-map");
        var params = new HashMap<String, Serializable>();
        params.put("ldap", (Serializable) ldap);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("myForm", params));
    }

    @Test
    public void testConvertStringToInteger() throws Exception {
        var locale = new DefaultFormValidatorLocale();
        var field = FormField.builder()
                .name("count")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();
        var result = FormUtils.convert(locale, "myForm", field, null, "42");
        assertEquals(42L, result);
    }

    @Test
    public void testConvertEmptyStringToInteger() throws Exception {
        var locale = new DefaultFormValidatorLocale();
        var field = FormField.builder()
                .name("count")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();
        var result = FormUtils.convert(locale, "myForm", field, null, "");
        assertNull(result);
    }

    @Test
    public void testConvertInvalidInteger() {
        var locale = new DefaultFormValidatorLocale();
        var field = FormField.builder()
                .name("count")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();
        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(locale, "myForm", field, null, "notAnInt"));
    }

    @Test
    public void testConvertStringToDecimal() throws Exception {
        var locale = new DefaultFormValidatorLocale();
        var field = FormField.builder()
                .name("amount")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();
        var result = FormUtils.convert(locale, "myForm", field, null, "3.14");
        assertEquals(3.14, result);
    }

    @Test
    public void testConvertInvalidDecimal() {
        var locale = new DefaultFormValidatorLocale();
        var field = FormField.builder()
                .name("amount")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();
        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(locale, "myForm", field, null, "notADecimal"));
    }

    @Test
    public void testConvertEmptyStringToBoolean() throws Exception {
        var locale = new DefaultFormValidatorLocale();
        var field = FormField.builder()
                .name("agree")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();
        var result = FormUtils.convert(locale, "myForm", field, null, "");
        assertEquals(true, result);
    }

    @Test
    public void testConvertEmptyStringReturnsNull() throws Exception {
        var locale = new DefaultFormValidatorLocale();
        var field = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .build();
        var result = FormUtils.convert(locale, "myForm", field, null, "");
        assertNull(result);
    }
}
