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

import static org.junit.jupiter.api.Assertions.*;

public class KvPolicyTest {

    @Test
    public void testNullRule() {
        KvPolicy policy = new KvPolicy(null);
        CheckResult<KvRule, Integer> result = policy.check(() -> 10, () -> false);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testUnderLimit() {
        KvPolicy policy = new KvPolicy(KvRule.of("max 10", 10));
        CheckResult<KvRule, Integer> result = policy.check(() -> 5, () -> false);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testAtLimitNewEntry() {
        KvPolicy policy = new KvPolicy(KvRule.of("max 5", 5));
        // count=5, adding new entry (exists=false) -> 5+1=6 > max 5 -> deny
        CheckResult<KvRule, Integer> result = policy.check(() -> 5, () -> false);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testAtLimitExistingEntry() {
        KvPolicy policy = new KvPolicy(KvRule.of("max 5", 5));
        // count=5, updating existing entry (exists=true) -> allowed since count doesn't increase
        CheckResult<KvRule, Integer> result = policy.check(() -> 5, () -> true);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testOverLimit() {
        KvPolicy policy = new KvPolicy(KvRule.of("max 5", 5));
        CheckResult<KvRule, Integer> result = policy.check(() -> 10, () -> false);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testNullCount() {
        KvPolicy policy = new KvPolicy(KvRule.of("max 5", 5));
        CheckResult<KvRule, Integer> result = policy.check(() -> null, () -> false);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testGetMaxEntriesNullRule() {
        KvPolicy policy = new KvPolicy(null);
        assertNull(policy.getMaxEntries());
    }

    @Test
    public void testGetMaxEntries() {
        KvPolicy policy = new KvPolicy(KvRule.of("msg", 42));
        assertEquals(42, (int) policy.getMaxEntries());
    }

    @Test
    public void testZeroMaxEntries() {
        KvPolicy policy = new KvPolicy(KvRule.of("max 0", 0));
        CheckResult<KvRule, Integer> result = policy.check(() -> 0, () -> false);
        assertFalse(result.getDeny().isEmpty());
    }
}
