package com.walmartlabs.concord.policyengine;

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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EntityPolicyTest {

    @Test
    public void testNullRules() {
        EntityPolicy policy = new EntityPolicy(null);
        CheckResult<EntityRule, Map<String, Object>> result =
                policy.check("project", "create", Collections::emptyMap);
        assertTrue(result.getDeny().isEmpty());
        assertTrue(result.getWarn().isEmpty());
    }

    @Test
    public void testEmptyRules() {
        PolicyRules<EntityRule> rules = new PolicyRules<>(null, null, null);
        EntityPolicy policy = new EntityPolicy(rules);
        CheckResult<EntityRule, Map<String, Object>> result =
                policy.check("project", "create", Collections::emptyMap);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testDenyMatchingEntity() {
        EntityRule denyRule = EntityRule.builder()
                .entity("project")
                .action("create")
                .build();

        PolicyRules<EntityRule> rules = new PolicyRules<>(null, null,
                Collections.singletonList(denyRule));
        EntityPolicy policy = new EntityPolicy(rules);

        CheckResult<EntityRule, Map<String, Object>> result =
                policy.check("project", "create", Collections::emptyMap);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testDenyNonMatchingEntity() {
        EntityRule denyRule = EntityRule.builder()
                .entity("project")
                .action("create")
                .build();

        PolicyRules<EntityRule> rules = new PolicyRules<>(null, null,
                Collections.singletonList(denyRule));
        EntityPolicy policy = new EntityPolicy(rules);

        CheckResult<EntityRule, Map<String, Object>> result =
                policy.check("secret", "create", Collections::emptyMap);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testAllowOverridesDeny() {
        EntityRule allowRule = EntityRule.builder()
                .entity("project")
                .action("create")
                .build();

        EntityRule denyRule = EntityRule.builder()
                .entity("project")
                .action("create")
                .build();

        PolicyRules<EntityRule> rules = new PolicyRules<>(
                Collections.singletonList(allowRule),
                null,
                Collections.singletonList(denyRule));
        EntityPolicy policy = new EntityPolicy(rules);

        CheckResult<EntityRule, Map<String, Object>> result =
                policy.check("project", "create", Collections::emptyMap);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testWarnRule() {
        EntityRule warnRule = EntityRule.builder()
                .entity("project")
                .action("delete")
                .build();

        PolicyRules<EntityRule> rules = new PolicyRules<>(null,
                Collections.singletonList(warnRule), null);
        EntityPolicy policy = new EntityPolicy(rules);

        CheckResult<EntityRule, Map<String, Object>> result =
                policy.check("project", "delete", Collections::emptyMap);
        assertFalse(result.getWarn().isEmpty());
        assertTrue(result.getDeny().isEmpty());
    }
}
