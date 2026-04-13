package com.walmartlabs.concord.forms;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2026 Walmart Inc.
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
    public void testGetRunAsUsersNoUsername() {
        Map<String, Serializable> params = new HashMap<>();
        var result = FormUtils.getRunAsUsers("myForm", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsUsersSingleString() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", "alice");
        var result = FormUtils.getRunAsUsers("myForm", params);
        assertEquals(1, result.size());
        assertTrue(result.contains("alice"));
    }

    @Test
    public void testGetRunAsUsersCollection() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", new ArrayList<>(Arrays.asList("alice", "bob")));
        var result = FormUtils.getRunAsUsers("myForm", params);
        assertEquals(2, result.size());
        assertTrue(result.contains("alice"));
        assertTrue(result.contains("bob"));
    }

    @Test
    public void testGetRunAsUsersInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", 123);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("myForm", params));
    }

    @Test
    public void testGetRunAsUsersCollectionWithNonString() {
        Map<String, Serializable> params = new HashMap<>();
        ArrayList<Object> items = new ArrayList<>();
        items.add(123);
        params.put("username", items);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("myForm", params));
    }

    @Test
    public void testGetRunAsLdapGroupsNull() {
        var result = FormUtils.getRunAsLdapGroups("myForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsNoLdap() {
        Map<String, Serializable> params = new HashMap<>();
        var result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsSingleString() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ldap.put("group", "admins");
        params.put("ldap", (Serializable) ldap);
        var result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(1, result.size());
        assertTrue(result.contains("admins"));
    }

    @Test
    public void testGetRunAsLdapGroupsListOfStrings() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ldap.put("group", new ArrayList<>(Arrays.asList("admins", "devs")));
        params.put("ldap", (Serializable) ldap);
        var result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(2, result.size());
        assertTrue(result.contains("admins"));
        assertTrue(result.contains("devs"));
    }

    @Test
    public void testGetRunAsLdapGroupsOldSyntax() {
        Map<String, Serializable> params = new HashMap<>();
        ArrayList<Object> ldapList = new ArrayList<>();
        HashMap<Object, Object> entry1 = new HashMap<>();
        entry1.put("group", "admins");
        HashMap<Object, Object> entry2 = new HashMap<>();
        entry2.put("group", "devs");
        ldapList.add(entry1);
        ldapList.add(entry2);
        params.put("ldap", ldapList);
        var result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(2, result.size());
        assertTrue(result.contains("admins"));
        assertTrue(result.contains("devs"));
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", "invalid");
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("myForm", params));
    }

    @Test
    public void testGetRunAsLdapGroupsListWithNonString() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ArrayList<Object> items = new ArrayList<>();
        items.add(123);
        ldap.put("group", items);
        params.put("ldap", (Serializable) ldap);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("myForm", params));
    }
}
