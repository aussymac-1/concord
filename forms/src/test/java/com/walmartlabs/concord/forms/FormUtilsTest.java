package com.walmartlabs.concord.forms;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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

class FormUtilsTest {

    @Test
    void testGetRunAsUsersNull() {
        Set<String> result = FormUtils.getRunAsUsers("myForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRunAsUsersNoUsernameKey() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("other", "value");
        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRunAsUsersSingleString() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", "alice");
        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
        assertEquals(Collections.singleton("alice"), result);
    }

    @Test
    void testGetRunAsUsersCollection() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", new ArrayList<>(Arrays.asList("alice", "bob")));
        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
        assertEquals(new HashSet<>(Arrays.asList("alice", "bob")), result);
    }

    @Test
    void testGetRunAsUsersInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", 123);
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("myForm", params));
    }

    @Test
    void testGetRunAsLdapGroupsNull() {
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRunAsLdapGroupsNoLdapKey() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("other", "value");
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRunAsLdapGroupsSingleGroupString() {
        Map<String, Serializable> ldap = new HashMap<>();
        ldap.put("group", "admins");

        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(Collections.singleton("admins"), result);
    }

    @Test
    void testGetRunAsLdapGroupsMultipleGroups() {
        Map<String, Serializable> ldap = new HashMap<>();
        ldap.put("group", new ArrayList<>(Arrays.asList("admins", "developers")));

        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldap);

        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertEquals(new HashSet<>(Arrays.asList("admins", "developers")), result);
    }
}
