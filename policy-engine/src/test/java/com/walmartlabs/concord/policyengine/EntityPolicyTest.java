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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EntityPolicyTest {

    @Test
    public void testNullRules() {
        EntityPolicy policy = new EntityPolicy(null);
        CheckResult<EntityRule, Map<String, Object>> result = policy.check("project", "create", HashMap::new);
        assertTrue(result.getDeny().isEmpty());
        assertTrue(result.getWarn().isEmpty());
    }

    @Test
    public void testEmptyRules() {
        PolicyRules<EntityRule> rules = new PolicyRules<>(null, null, null);
        EntityPolicy policy = new EntityPolicy(rules);
        CheckResult<EntityRule, Map<String, Object>> result = policy.check("project", "create", HashMap::new);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testDenyByEntity() {
        EntityRule denyRule = EntityRule.builder()
                .msg("denied")
                .entity("project")
                .build();

        PolicyRules<EntityRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        EntityPolicy policy = new EntityPolicy(rules);

        CheckResult<EntityRule, Map<String, Object>> result = policy.check("project", "create", HashMap::new);
        assertFalse(result.getDeny().isEmpty());
        assertEquals("denied", result.getDeny().get(0).getRule().msg());
    }

    @Test
    public void testDenyByAction() {
        EntityRule denyRule = EntityRule.builder()
                .msg("create denied")
                .entity("project")
                .action("create")
                .build();

        PolicyRules<EntityRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        EntityPolicy policy = new EntityPolicy(rules);

        CheckResult<EntityRule, Map<String, Object>> result = policy.check("project", "create", HashMap::new);
        assertFalse(result.getDeny().isEmpty());

        // different action should not match
        result = policy.check("project", "update", HashMap::new);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testAllowOverridesDeny() {
        EntityRule allowRule = EntityRule.builder()
                .entity("project")
                .action("create")
                .build();

        EntityRule denyRule = EntityRule.builder()
                .msg("denied")
                .entity("project")
                .build();

        PolicyRules<EntityRule> rules = new PolicyRules<>(
                Collections.singletonList(allowRule), null, Collections.singletonList(denyRule));
        EntityPolicy policy = new EntityPolicy(rules);

        CheckResult<EntityRule, Map<String, Object>> result = policy.check("project", "create", HashMap::new);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testWarnResult() {
        EntityRule warnRule = EntityRule.builder()
                .msg("warning")
                .entity("project")
                .build();

        PolicyRules<EntityRule> rules = new PolicyRules<>(null, Collections.singletonList(warnRule), null);
        EntityPolicy policy = new EntityPolicy(rules);

        CheckResult<EntityRule, Map<String, Object>> result = policy.check("project", "create", HashMap::new);
        assertTrue(result.getDeny().isEmpty());
        assertFalse(result.getWarn().isEmpty());
        assertEquals("warning", result.getWarn().get(0).getRule().msg());
    }

    @Test
    public void testEntityDoesNotMatch() {
        EntityRule denyRule = EntityRule.builder()
                .entity("secret")
                .build();

        PolicyRules<EntityRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        EntityPolicy policy = new EntityPolicy(rules);

        CheckResult<EntityRule, Map<String, Object>> result = policy.check("project", "create", HashMap::new);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testWildcardEntityMatch() {
        EntityRule denyRule = EntityRule.builder()
                .entity(".*")
                .action("delete")
                .build();

        PolicyRules<EntityRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        EntityPolicy policy = new EntityPolicy(rules);

        CheckResult<EntityRule, Map<String, Object>> result = policy.check("project", "delete", HashMap::new);
        assertFalse(result.getDeny().isEmpty());

        result = policy.check("secret", "delete", HashMap::new);
        assertFalse(result.getDeny().isEmpty());
    }
}
