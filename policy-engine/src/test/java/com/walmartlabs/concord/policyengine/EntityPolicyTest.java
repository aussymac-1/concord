package com.walmartlabs.concord.policyengine;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2019 Walmart Inc.
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EntityPolicyTest {

    @Test
    void testNullRulesAllowsEverything() {
        var policy = new EntityPolicy(null);
        var result = policy.check("project", "create", Collections::emptyMap);
        assertTrue(result.getDeny().isEmpty());
        assertTrue(result.getWarn().isEmpty());
    }

    @Test
    void testEmptyRulesAllowsEverything() {
        var rules = new PolicyRules<EntityRule>(
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        var policy = new EntityPolicy(rules);
        var result = policy.check("project", "create", Collections::emptyMap);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    void testDenyRule() {
        var denyRule = EntityRule.builder()
                .entity("project")
                .action("create")
                .msg("project creation denied")
                .build();

        var rules = new PolicyRules<EntityRule>(
                Collections.emptyList(), Collections.emptyList(), Collections.singletonList(denyRule));
        var policy = new EntityPolicy(rules);

        var result = policy.check("project", "create", Collections::emptyMap);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    void testAllowRuleOverridesDeny() {
        var allowRule = EntityRule.builder()
                .entity("project")
                .action("create")
                .build();

        var denyRule = EntityRule.builder()
                .entity("project")
                .action("create")
                .msg("denied")
                .build();

        var rules = new PolicyRules<EntityRule>(
                Collections.singletonList(allowRule), Collections.emptyList(), Collections.singletonList(denyRule));
        var policy = new EntityPolicy(rules);

        var result = policy.check("project", "create", Collections::emptyMap);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    void testWarnRule() {
        var warnRule = EntityRule.builder()
                .entity("project")
                .action("create")
                .msg("warning")
                .build();

        var rules = new PolicyRules<EntityRule>(
                Collections.emptyList(), Collections.singletonList(warnRule), Collections.emptyList());
        var policy = new EntityPolicy(rules);

        var result = policy.check("project", "create", Collections::emptyMap);
        assertTrue(result.getDeny().isEmpty());
        assertFalse(result.getWarn().isEmpty());
    }

    @Test
    void testDenyWithConditions() {
        var conditions = new HashMap<String, Object>();
        conditions.put("orgName", "restricted-org");

        var denyRule = EntityRule.builder()
                .entity("project")
                .action("create")
                .conditions(conditions)
                .msg("denied for org")
                .build();

        var rules = new PolicyRules<EntityRule>(
                Collections.emptyList(), Collections.emptyList(), Collections.singletonList(denyRule));
        var policy = new EntityPolicy(rules);

        var attrs = new HashMap<String, Object>();
        attrs.put("orgName", "restricted-org");
        var result = policy.check("project", "create", () -> attrs);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    void testDenyRuleDoesNotMatchDifferentEntity() {
        var denyRule = EntityRule.builder()
                .entity("secret")
                .action("create")
                .msg("denied")
                .build();

        var rules = new PolicyRules<EntityRule>(
                Collections.emptyList(), Collections.emptyList(), Collections.singletonList(denyRule));
        var policy = new EntityPolicy(rules);

        var result = policy.check("project", "create", Collections::emptyMap);
        assertTrue(result.getDeny().isEmpty());
    }
}
