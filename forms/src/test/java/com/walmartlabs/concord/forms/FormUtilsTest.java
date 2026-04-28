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
        params.put("username", "alice");
        Set<String> result = FormUtils.getRunAsUsers("form1", params);
        assertEquals(Collections.singleton("alice"), result);
    }

    @Test
    public void testGetRunAsUsersCollection() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", new ArrayList<>(Arrays.asList("alice", "bob")));
        Set<String> result = FormUtils.getRunAsUsers("form1", params);
        assertEquals(new HashSet<>(Arrays.asList("alice", "bob")), result);
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
        ArrayList<Object> list = new ArrayList<>();
        list.add("alice");
        list.add(123);
        params.put("username", list);
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
    public void testGetRunAsLdapGroupsSingleString() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ldap.put("group", "admins");
        params.put("ldap", (Serializable) ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("form1", params);
        assertEquals(Collections.singleton("admins"), result);
    }

    @Test
    public void testGetRunAsLdapGroupsListOfStrings() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ldap.put("group", new ArrayList<>(Arrays.asList("admins", "devs")));
        params.put("ldap", (Serializable) ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("form1", params);
        assertEquals(new HashSet<>(Arrays.asList("admins", "devs")), result);
    }

    @Test
    public void testGetRunAsLdapGroupsOldSyntax() {
        Map<String, Serializable> params = new HashMap<>();
        ArrayList<Object> ldapList = new ArrayList<>();
        HashMap<Object, Object> entry1 = new HashMap<>();
        entry1.put("group", "admins");
        ldapList.add(entry1);
        HashMap<Object, Object> entry2 = new HashMap<>();
        entry2.put("group", "devs");
        ldapList.add(entry2);
        params.put("ldap", ldapList);

        Set<String> result = FormUtils.getRunAsLdapGroups("form1", params);
        assertEquals(new HashSet<>(Arrays.asList("admins", "devs")), result);
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", 123);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("form1", params));
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidCollectionItem() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ArrayList<Object> list = new ArrayList<>();
        list.add("admins");
        list.add(123);
        ldap.put("group", list);
        params.put("ldap", (Serializable) ldap);

        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("form1", params));
    }
}
