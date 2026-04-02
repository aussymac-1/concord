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

import static org.junit.jupiter.api.Assertions.*;

public class StatePolicyTest {

    @Test
    public void testNullRulesReturnsSuccess() {
        StatePolicy policy = new StatePolicy(null);
        CheckResult<StateRule, StatePolicy.StateStats> result = policy.check(
                () -> new StatePolicy.StateStats(100, 5));
        assertTrue(result.getDeny().isEmpty());
        assertTrue(result.getWarn().isEmpty());
    }

    @Test
    public void testEmptyRulesReturnsSuccess() {
        PolicyRules<StateRule> rules = new PolicyRules<>(null, null, null);
        StatePolicy policy = new StatePolicy(rules);
        CheckResult<StateRule, StatePolicy.StateStats> result = policy.check(
                () -> new StatePolicy.StateStats(100, 5));
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testMaxFilesCountExceeded() {
        StateRule rule = StateRule.builder()
                .maxFilesCount(5)
                .build();
        PolicyRules<StateRule> rules = new PolicyRules<>(null, null, Collections.singletonList(rule));
        StatePolicy policy = new StatePolicy(rules);
        CheckResult<StateRule, StatePolicy.StateStats> result = policy.check(
                () -> new StatePolicy.StateStats(100, 10));
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testMaxFilesCountNotExceeded() {
        StateRule rule = StateRule.builder()
                .maxFilesCount(20)
                .build();
        PolicyRules<StateRule> rules = new PolicyRules<>(null, null, Collections.singletonList(rule));
        StatePolicy policy = new StatePolicy(rules);
        CheckResult<StateRule, StatePolicy.StateStats> result = policy.check(
                () -> new StatePolicy.StateStats(100, 5));
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testMaxSizeExceeded() {
        StateRule rule = StateRule.builder()
                .maxSizeInBytes(100L)
                .build();
        PolicyRules<StateRule> rules = new PolicyRules<>(null, null, Collections.singletonList(rule));
        StatePolicy policy = new StatePolicy(rules);
        CheckResult<StateRule, StatePolicy.StateStats> result = policy.check(
                () -> new StatePolicy.StateStats(200, 5));
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testWarnRule() {
        StateRule rule = StateRule.builder()
                .maxFilesCount(5)
                .build();
        PolicyRules<StateRule> rules = new PolicyRules<>(null, Collections.singletonList(rule), null);
        StatePolicy policy = new StatePolicy(rules);
        CheckResult<StateRule, StatePolicy.StateStats> result = policy.check(
                () -> new StatePolicy.StateStats(100, 10));
        assertFalse(result.getWarn().isEmpty());
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testStateStatsGetters() {
        StatePolicy.StateStats stats = new StatePolicy.StateStats(1024, 42);
        assertEquals(1024, stats.getSize());
        assertEquals(42, stats.getFilesCount());
    }
}
