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
        Set<String> result = FormUtils.getRunAsUsers("myForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsUsersNoUsernameKey() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("otherKey", "value");
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
        params.put("username", (Serializable) Arrays.asList("user1", "user2"));
        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
        assertEquals(Set.of("user1", "user2"), result);
    }

    @Test
    public void testGetRunAsUsersCollectionWithNonString() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", (Serializable) Arrays.asList("user1", 123));
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("myForm", params));
    }

    @Test
    public void testGetRunAsUsersInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", 123);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("myForm", params));
    }

    @Test
    public void testGetRunAsLdapGroupsNull() {
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsNoLdapKey() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("otherKey", "value");
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsSingleStringInMap() {
        Map<String, Serializable> ldapMap = new HashMap<>();
        ldapMap.put("group", "admins");
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldapMap);
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(Collections.singleton("admins"), result);
    }

    @Test
    public void testGetRunAsLdapGroupsCollectionInMap() {
        Map<String, Serializable> ldapMap = new HashMap<>();
        ldapMap.put("group", (Serializable) Arrays.asList("group1", "group2"));
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldapMap);
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(Set.of("group1", "group2"), result);
    }

    @Test
    public void testGetRunAsLdapGroupsCollectionWithNonString() {
        Map<String, Serializable> ldapMap = new HashMap<>();
        ldapMap.put("group", (Serializable) Arrays.asList("group1", 123));
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldapMap);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("myForm", params));
    }

    @Test
    public void testGetRunAsLdapGroupsOldSyntax() {
        Map<String, Serializable> groupDef1 = new HashMap<>();
        groupDef1.put("group", "groupA");
        Map<String, Serializable> groupDef2 = new HashMap<>();
        groupDef2.put("group", "groupB");

        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) Arrays.asList(groupDef1, groupDef2));
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(Set.of("groupA", "groupB"), result);
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", "invalid");
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("myForm", params));
    }
}
