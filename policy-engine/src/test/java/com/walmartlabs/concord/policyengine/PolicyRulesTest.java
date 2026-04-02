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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyRulesTest {

    @Test
    public void testAllNullLists() {
        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, null, null);
        assertTrue(rules.getAllow().isEmpty());
        assertTrue(rules.getWarn().isEmpty());
        assertTrue(rules.getDeny().isEmpty());
        assertTrue(rules.isEmpty());
    }

    @Test
    public void testNonEmptyRules() {
        DependencyRule rule = ImmutableDependencyRule.builder()
                .scheme("http")
                .build();
        PolicyRules<DependencyRule> rules = new PolicyRules<>(
                Collections.singletonList(rule), null, null);
        assertFalse(rules.isEmpty());
        assertEquals(1, rules.getAllow().size());
    }

    @Test
    public void testEquals() {
        PolicyRules<DependencyRule> a = new PolicyRules<>(null, null, null);
        PolicyRules<DependencyRule> b = new PolicyRules<>(null, null, null);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testNotEquals() {
        DependencyRule rule = ImmutableDependencyRule.builder()
                .scheme("http")
                .build();
        PolicyRules<DependencyRule> a = new PolicyRules<>(Collections.singletonList(rule), null, null);
        PolicyRules<DependencyRule> b = new PolicyRules<>(null, null, null);
        assertNotEquals(a, b);
    }

    @Test
    public void testToString() {
        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, null, null);
        String s = rules.toString();
        assertNotNull(s);
        assertTrue(s.contains("allow="));
        assertTrue(s.contains("warn="));
        assertTrue(s.contains("deny="));
    }
}
