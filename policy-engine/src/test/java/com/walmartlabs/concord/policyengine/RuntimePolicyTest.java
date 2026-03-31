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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RuntimePolicyTest {

    @Test
    public void testNullRule() {
        RuntimePolicy policy = new RuntimePolicy(null);
        CheckResult<RuntimeRule, String> result = policy.check("concord-v2", () -> null);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testNullRuntime() {
        RuntimeRule rule = RuntimeRule.of(null, Set.of("concord-v2"), null);
        RuntimePolicy policy = new RuntimePolicy(rule);
        CheckResult<RuntimeRule, String> result = policy.check(null, () -> null);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testAllowedRuntime() {
        RuntimeRule rule = RuntimeRule.of(null, Set.of("concord-v1", "concord-v2"), null);
        RuntimePolicy policy = new RuntimePolicy(rule);
        CheckResult<RuntimeRule, String> result = policy.check("concord-v2", () -> null);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testDeniedRuntime() {
        RuntimeRule rule = RuntimeRule.of("only v2 allowed", Set.of("concord-v2"), null);
        RuntimePolicy policy = new RuntimePolicy(rule);
        CheckResult<RuntimeRule, String> result = policy.check("concord-v1", () -> null);
        assertFalse(result.getDeny().isEmpty());
        assertEquals("concord-v1", result.getDeny().get(0).getEntity());
    }

    @Test
    public void testProjectCreatedAfterOldProject() {
        LocalDate cutoff = LocalDate.of(2024, 1, 1);
        RuntimeRule rule = RuntimeRule.of(null, Set.of("concord-v2"), cutoff);
        RuntimePolicy policy = new RuntimePolicy(rule);

        OffsetDateTime createdAt = OffsetDateTime.of(2023, 6, 15, 0, 0, 0, 0, ZoneOffset.UTC);
        CheckResult<RuntimeRule, String> result = policy.check("concord-v1", () -> createdAt);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testProjectCreatedAfterNewProject() {
        LocalDate cutoff = LocalDate.of(2024, 1, 1);
        RuntimeRule rule = RuntimeRule.of("only v2", Set.of("concord-v2"), cutoff);
        RuntimePolicy policy = new RuntimePolicy(rule);

        OffsetDateTime createdAt = OffsetDateTime.of(2024, 6, 15, 0, 0, 0, 0, ZoneOffset.UTC);
        CheckResult<RuntimeRule, String> result = policy.check("concord-v1", () -> createdAt);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testProjectCreatedAfterNewProjectAllowedRuntime() {
        LocalDate cutoff = LocalDate.of(2024, 1, 1);
        RuntimeRule rule = RuntimeRule.of(null, Set.of("concord-v2"), cutoff);
        RuntimePolicy policy = new RuntimePolicy(rule);

        OffsetDateTime createdAt = OffsetDateTime.of(2024, 6, 15, 0, 0, 0, 0, ZoneOffset.UTC);
        CheckResult<RuntimeRule, String> result = policy.check("concord-v2", () -> createdAt);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testProjectCreatedAfterNullCreatedAt() {
        LocalDate cutoff = LocalDate.of(2024, 1, 1);
        RuntimeRule rule = RuntimeRule.of(null, Set.of("concord-v2"), cutoff);
        RuntimePolicy policy = new RuntimePolicy(rule);

        CheckResult<RuntimeRule, String> result = policy.check("concord-v1", () -> null);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testEmptyAllowedRuntimes() {
        RuntimeRule rule = RuntimeRule.of("none allowed", Collections.emptySet(), null);
        RuntimePolicy policy = new RuntimePolicy(rule);
        CheckResult<RuntimeRule, String> result = policy.check("concord-v2", () -> null);
        assertFalse(result.getDeny().isEmpty());
    }
}
