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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentProcessPolicyTest {

    @Test
    public void testNullRule() {
        ConcurrentProcessPolicy policy = new ConcurrentProcessPolicy(null);
        CheckResult<ConcurrentProcessRule, List<UUID>> result = policy.check(
                Collections::emptyList, Collections::emptyList);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testNullLimits() {
        ConcurrentProcessRule rule = ConcurrentProcessRule.builder().build();
        ConcurrentProcessPolicy policy = new ConcurrentProcessPolicy(rule);
        CheckResult<ConcurrentProcessRule, List<UUID>> result = policy.check(
                Collections::emptyList, Collections::emptyList);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testMaxPerOrgNotExceeded() {
        ConcurrentProcessRule rule = ConcurrentProcessRule.builder()
                .maxPerOrg(5)
                .build();
        ConcurrentProcessPolicy policy = new ConcurrentProcessPolicy(rule);
        CheckResult<ConcurrentProcessRule, List<UUID>> result = policy.check(
                () -> Arrays.asList(UUID.randomUUID(), UUID.randomUUID()),
                Collections::emptyList);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testMaxPerOrgExceeded() {
        ConcurrentProcessRule rule = ConcurrentProcessRule.builder()
                .maxPerOrg(2)
                .build();
        ConcurrentProcessPolicy policy = new ConcurrentProcessPolicy(rule);
        CheckResult<ConcurrentProcessRule, List<UUID>> result = policy.check(
                () -> Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()),
                Collections::emptyList);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testMaxPerProjectNotExceeded() {
        ConcurrentProcessRule rule = ConcurrentProcessRule.builder()
                .maxPerProject(5)
                .build();
        ConcurrentProcessPolicy policy = new ConcurrentProcessPolicy(rule);
        CheckResult<ConcurrentProcessRule, List<UUID>> result = policy.check(
                Collections::emptyList,
                () -> Collections.singletonList(UUID.randomUUID()));
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testMaxPerProjectExceeded() {
        ConcurrentProcessRule rule = ConcurrentProcessRule.builder()
                .maxPerProject(1)
                .build();
        ConcurrentProcessPolicy policy = new ConcurrentProcessPolicy(rule);
        CheckResult<ConcurrentProcessRule, List<UUID>> result = policy.check(
                Collections::emptyList,
                () -> Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
        assertFalse(result.getDeny().isEmpty());
    }
}
