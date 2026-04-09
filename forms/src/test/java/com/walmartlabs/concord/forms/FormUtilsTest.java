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
        assertEquals(Collections.singleton("admin"), result);
    }

    @Test
    public void testGetRunAsUsersCollection() {
        Map<String, Serializable> params = new HashMap<>();
        ArrayList<String> users = new ArrayList<>();
        users.add("user1");
        users.add("user2");
        params.put("username", users);
        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
        assertEquals(new HashSet<>(Arrays.asList("user1", "user2")), result);
    }

    @Test
    public void testGetRunAsUsersInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", 123);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("myForm", params));
    }

    @Test
    public void testGetRunAsUsersCollectionWithInvalidElement() {
        Map<String, Serializable> params = new HashMap<>();
        ArrayList<Object> users = new ArrayList<>();
        users.add("valid");
        users.add(123);
        params.put("username", users);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("myForm", params));
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
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ldap.put("group", "admins");
        params.put("ldap", ldap);
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(Collections.singleton("admins"), result);
    }

    @Test
    public void testGetRunAsLdapGroupsMultipleStrings() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ArrayList<String> groups = new ArrayList<>();
        groups.add("group1");
        groups.add("group2");
        ldap.put("group", groups);
        params.put("ldap", ldap);
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(new HashSet<>(Arrays.asList("group1", "group2")), result);
    }

    @Test
    public void testGetRunAsLdapGroupsOldSyntax() {
        Map<String, Serializable> params = new HashMap<>();
        ArrayList<HashMap<Object, Object>> ldapList = new ArrayList<>();
        HashMap<Object, Object> entry1 = new HashMap<>();
        entry1.put("group", "grpA");
        ldapList.add(entry1);
        HashMap<Object, Object> entry2 = new HashMap<>();
        entry2.put("group", "grpB");
        ldapList.add(entry2);
        params.put("ldap", ldapList);
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(new HashSet<>(Arrays.asList("grpA", "grpB")), result);
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidLdapType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", "invalid");
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("myForm", params));
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidGroupElement() {
        Map<String, Serializable> params = new HashMap<>();
        HashMap<Object, Object> ldap = new HashMap<>();
        ArrayList<Object> groups = new ArrayList<>();
        groups.add("valid");
        groups.add(123);
        ldap.put("group", groups);
        params.put("ldap", ldap);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("myForm", params));
    }
}
