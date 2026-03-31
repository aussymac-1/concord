package com.walmartlabs.concord.policyengine;

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

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyRulesTest {

    @Test
    public void testNullLists() {
        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, null, null);
        assertTrue(rules.getAllow().isEmpty());
        assertTrue(rules.getWarn().isEmpty());
        assertTrue(rules.getDeny().isEmpty());
        assertTrue(rules.isEmpty());
    }

    @Test
    public void testNonEmptyRules() {
        DependencyRule r = DependencyRule.builder().groupId("com.example").build();
        PolicyRules<DependencyRule> rules = new PolicyRules<>(
                Collections.singletonList(r), null, null);
        assertFalse(rules.isEmpty());
        assertEquals(1, rules.getAllow().size());
        assertTrue(rules.getWarn().isEmpty());
        assertTrue(rules.getDeny().isEmpty());
    }

    @Test
    public void testIsEmptyWithWarnOnly() {
        DependencyRule r = DependencyRule.builder().groupId("com.example").build();
        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, Collections.singletonList(r), null);
        assertFalse(rules.isEmpty());
    }

    @Test
    public void testIsEmptyWithDenyOnly() {
        DependencyRule r = DependencyRule.builder().groupId("com.example").build();
        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, null, Collections.singletonList(r));
        assertFalse(rules.isEmpty());
    }

    @Test
    public void testEquals() {
        DependencyRule r1 = DependencyRule.builder().groupId("com.example").build();
        PolicyRules<DependencyRule> rules1 = new PolicyRules<>(
                Collections.singletonList(r1), null, null);
        PolicyRules<DependencyRule> rules2 = new PolicyRules<>(
                Collections.singletonList(r1), null, null);
        assertEquals(rules1, rules2);
        assertEquals(rules1.hashCode(), rules2.hashCode());
    }

    @Test
    public void testNotEquals() {
        DependencyRule r1 = DependencyRule.builder().groupId("com.example1").build();
        DependencyRule r2 = DependencyRule.builder().groupId("com.example2").build();
        PolicyRules<DependencyRule> rules1 = new PolicyRules<>(
                Collections.singletonList(r1), null, null);
        PolicyRules<DependencyRule> rules2 = new PolicyRules<>(
                Collections.singletonList(r2), null, null);
        assertNotEquals(rules1, rules2);
    }

    @Test
    public void testEqualsSameObject() {
        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, null, null);
        assertEquals(rules, rules);
    }

    @Test
    public void testEqualsNull() {
        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, null, null);
        assertNotEquals(null, rules);
    }

    @Test
    public void testEqualsDifferentType() {
        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, null, null);
        assertNotEquals("string", rules);
    }

    @Test
    public void testToString() {
        DependencyRule r = DependencyRule.builder().groupId("com.example").build();
        PolicyRules<DependencyRule> rules = new PolicyRules<>(
                Collections.singletonList(r), null, Collections.singletonList(r));
        String s = rules.toString();
        assertTrue(s.contains("allow="));
        assertTrue(s.contains("deny="));
        assertTrue(s.contains("warn="));
    }

    @Test
    public void testAllThreeLists() {
        DependencyRule r1 = DependencyRule.builder().groupId("allow").build();
        DependencyRule r2 = DependencyRule.builder().groupId("warn").build();
        DependencyRule r3 = DependencyRule.builder().groupId("deny").build();
        PolicyRules<DependencyRule> rules = new PolicyRules<>(
                Collections.singletonList(r1),
                Collections.singletonList(r2),
                Collections.singletonList(r3));
        assertEquals(1, rules.getAllow().size());
        assertEquals(1, rules.getWarn().size());
        assertEquals(1, rules.getDeny().size());
        assertFalse(rules.isEmpty());
    }
}
