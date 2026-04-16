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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FormUtilsTest {

    @Test
    void getRunAsUsersReturnsEmptySetWhenNull() {
        Set<String> result = FormUtils.getRunAsUsers("myForm", null);
        assertThat(result).isEmpty();
    }

    @Test
    void getRunAsUsersReturnsEmptySetWhenKeyMissing() {
        Map<String, Serializable> params = new HashMap<>();
        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
        assertThat(result).isEmpty();
    }

    @Test
    void getRunAsUsersReturnsSingletonForString() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", "user1");
        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
        assertThat(result).containsExactly("user1");
    }

    @Test
    void getRunAsUsersReturnsSetForCollection() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", new ArrayList<>(List.of("user1", "user2")));
        Set<String> result = FormUtils.getRunAsUsers("myForm", params);
        assertThat(result).containsExactlyInAnyOrder("user1", "user2");
    }

    @Test
    void getRunAsUsersThrowsOnInvalidCollectionItem() {
        Map<String, Serializable> params = new HashMap<>();
        ArrayList<Object> list = new ArrayList<>();
        list.add(123);
        params.put("username", list);
        assertThatThrownBy(() -> FormUtils.getRunAsUsers("myForm", params))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("string");
    }

    @Test
    void getRunAsUsersThrowsOnInvalidType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", 42);
        assertThatThrownBy(() -> FormUtils.getRunAsUsers("myForm", params))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("username");
    }

    @Test
    void getRunAsLdapGroupsReturnsEmptyWhenNull() {
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", null);
        assertThat(result).isEmpty();
    }

    @Test
    void getRunAsLdapGroupsReturnsEmptyWhenLdapKeyMissing() {
        Map<String, Serializable> params = new HashMap<>();
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertThat(result).isEmpty();
    }

    @Test
    void getRunAsLdapGroupsFromMapWithStringGroup() {
        Map<String, Serializable> ldap = new HashMap<>();
        ldap.put("group", "admins");
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldap);
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertThat(result).containsExactly("admins");
    }

    @Test
    void getRunAsLdapGroupsFromMapWithListGroup() {
        Map<String, Serializable> ldap = new HashMap<>();
        ldap.put("group", new ArrayList<>(List.of("admins", "users")));
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldap);
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertThat(result).containsExactlyInAnyOrder("admins", "users");
    }

    @Test
    void getRunAsLdapGroupsFromListOfMaps() {
        List<Map<String, String>> ldap = new ArrayList<>();
        ldap.add(Map.of("group", "group1"));
        ldap.add(Map.of("group", "group2"));
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldap);
        Set<String> result = FormUtils.getRunAsLdapGroups("myForm", params);
        assertThat(result).containsExactlyInAnyOrder("group1", "group2");
    }

    @Test
    void getRunAsLdapGroupsThrowsOnInvalidGroupItem() {
        Map<String, Serializable> ldap = new HashMap<>();
        ArrayList<Object> groups = new ArrayList<>();
        groups.add(123);
        ldap.put("group", groups);
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", (Serializable) ldap);
        assertThatThrownBy(() -> FormUtils.getRunAsLdapGroups("myForm", params))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getRunAsLdapGroupsThrowsOnInvalidLdapType() {
        Map<String, Serializable> params = new HashMap<>();
        params.put("ldap", "invalid");
        assertThatThrownBy(() -> FormUtils.getRunAsLdapGroups("myForm", params))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("LDAP group");
    }
}
