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

public class ForkDepthPolicyTest {

    @Test
    public void testNullRule() throws Exception {
        ForkDepthPolicy policy = new ForkDepthPolicy(null);
        CheckResult<ForkDepthRule, Integer> result = policy.check(() -> 10);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testUnderLimit() throws Exception {
        ForkDepthPolicy policy = new ForkDepthPolicy(ForkDepthRule.of("max 5", 5));
        CheckResult<ForkDepthRule, Integer> result = policy.check(() -> 3);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testAtLimit() throws Exception {
        ForkDepthPolicy policy = new ForkDepthPolicy(ForkDepthRule.of("max 5", 5));
        CheckResult<ForkDepthRule, Integer> result = policy.check(() -> 5);
        assertFalse(result.getDeny().isEmpty());
        assertEquals(5, result.getDeny().get(0).getEntity());
    }

    @Test
    public void testOverLimit() throws Exception {
        ForkDepthPolicy policy = new ForkDepthPolicy(ForkDepthRule.of("max 5", 5));
        CheckResult<ForkDepthRule, Integer> result = policy.check(() -> 10);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testZeroDepth() throws Exception {
        ForkDepthPolicy policy = new ForkDepthPolicy(ForkDepthRule.of("max 5", 5));
        CheckResult<ForkDepthRule, Integer> result = policy.check(() -> 0);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testMaxOfOne() throws Exception {
        ForkDepthPolicy policy = new ForkDepthPolicy(ForkDepthRule.of("max 1", 1));

        CheckResult<ForkDepthRule, Integer> result = policy.check(() -> 0);
        assertTrue(result.getDeny().isEmpty());

        result = policy.check(() -> 1);
        assertFalse(result.getDeny().isEmpty());
    }
}
