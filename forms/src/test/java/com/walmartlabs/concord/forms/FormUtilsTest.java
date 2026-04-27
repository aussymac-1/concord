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
        var result = FormUtils.getRunAsUsers("form1", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsUsersNoUsernameKey() {
        var params = new HashMap<String, Serializable>();
        params.put("other", "value");
        var result = FormUtils.getRunAsUsers("form1", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsUsersSingleString() {
        var params = new HashMap<String, Serializable>();
        params.put("username", "alice");
        var result = FormUtils.getRunAsUsers("form1", params);
        assertEquals(Set.of("alice"), result);
    }

    @Test
    public void testGetRunAsUsersCollection() {
        var params = new HashMap<String, Serializable>();
        params.put("username", (Serializable) Arrays.asList("alice", "bob"));
        var result = FormUtils.getRunAsUsers("form1", params);
        assertEquals(Set.of("alice", "bob"), result);
    }

    @Test
    public void testGetRunAsUsersInvalidCollectionElement() {
        var params = new HashMap<String, Serializable>();
        params.put("username", (Serializable) Arrays.asList("alice", 42));
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("form1", params));
    }

    @Test
    public void testGetRunAsUsersInvalidType() {
        var params = new HashMap<String, Serializable>();
        params.put("username", 42);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("form1", params));
    }

    @Test
    public void testGetRunAsLdapGroupsNull() {
        var result = FormUtils.getRunAsLdapGroups("form1", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsNoLdapKey() {
        var params = new HashMap<String, Serializable>();
        params.put("other", "value");
        var result = FormUtils.getRunAsLdapGroups("form1", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsMapSingleString() {
        var ldap = new HashMap<Object, Object>();
        ldap.put("group", "admins");
        var params = new HashMap<String, Serializable>();
        params.put("ldap", (Serializable) ldap);

        var result = FormUtils.getRunAsLdapGroups("form1", params);
        assertEquals(Set.of("admins"), result);
    }

    @Test
    public void testGetRunAsLdapGroupsMapCollection() {
        var ldap = new HashMap<Object, Object>();
        ldap.put("group", (Serializable) Arrays.asList("admins", "devs"));
        var params = new HashMap<String, Serializable>();
        params.put("ldap", (Serializable) ldap);

        var result = FormUtils.getRunAsLdapGroups("form1", params);
        assertEquals(Set.of("admins", "devs"), result);
    }

    @Test
    public void testGetRunAsLdapGroupsMapInvalidElement() {
        var ldap = new HashMap<Object, Object>();
        ldap.put("group", (Serializable) Arrays.asList("admins", 42));
        var params = new HashMap<String, Serializable>();
        params.put("ldap", (Serializable) ldap);

        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("form1", params));
    }

    @Test
    public void testGetRunAsLdapGroupsOldFormat() {
        var entry1 = new HashMap<Object, Object>();
        entry1.put("group", "group1");
        var entry2 = new HashMap<Object, Object>();
        entry2.put("group", "group2");

        var params = new HashMap<String, Serializable>();
        params.put("ldap", (Serializable) Arrays.asList(entry1, entry2));

        var result = FormUtils.getRunAsLdapGroups("form1", params);
        assertEquals(Set.of("group1", "group2"), result);
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidType() {
        var params = new HashMap<String, Serializable>();
        params.put("ldap", 42);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("form1", params));
    }

    @Test
    public void testGetRunAsLdapGroupsOldFormatInvalidEntry() {
        var params = new HashMap<String, Serializable>();
        params.put("ldap", (Serializable) Arrays.asList("not-a-map"));
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("form1", params));
    }

    @Test
    public void testConvertStringToInteger() throws Exception {
        var locale = new DefaultFormValidatorLocale();
        var ff = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var result = FormUtils.convert(locale, "form1", ff, null, "42");
        assertEquals(42L, result);
    }

    @Test
    public void testConvertStringToDecimal() throws Exception {
        var locale = new DefaultFormValidatorLocale();
        var ff = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var result = FormUtils.convert(locale, "form1", ff, null, "3.14");
        assertEquals(3.14, result);
    }

    @Test
    public void testConvertEmptyStringToNull() throws Exception {
        var locale = new DefaultFormValidatorLocale();
        var ff = FormField.builder()
                .name("name")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var result = FormUtils.convert(locale, "form1", ff, null, "");
        assertNull(result);
    }

    @Test
    public void testConvertInvalidInteger() {
        var locale = new DefaultFormValidatorLocale();
        var ff = FormField.builder()
                .name("age")
                .type(FormFields.IntegerField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(locale, "form1", ff, null, "not-a-number"));
    }

    @Test
    public void testConvertInvalidDecimal() {
        var locale = new DefaultFormValidatorLocale();
        var ff = FormField.builder()
                .name("price")
                .type(FormFields.DecimalField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(locale, "form1", ff, null, "not-a-decimal"));
    }

    @Test
    public void testConvertEmptyBooleanReturnsTrue() throws Exception {
        var locale = new DefaultFormValidatorLocale();
        var ff = FormField.builder()
                .name("flag")
                .type(FormFields.BooleanField.TYPE)
                .cardinality(FormField.Cardinality.ONE_AND_ONLY_ONE)
                .build();

        var result = FormUtils.convert(locale, "form1", ff, null, "");
        assertEquals(true, result);
    }
}
