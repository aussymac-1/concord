package com.walmartlabs.concord.policyengine;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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

class ForkDepthPolicyTest {

    @Test
    void testNullRuleAllows() throws Exception {
        var policy = new ForkDepthPolicy(null);
        var result = policy.check(() -> 100);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    void testDepthBelowMax() throws Exception {
        ForkDepthRule rule = ForkDepthRule.of("too deep", 5);
        var policy = new ForkDepthPolicy(rule);
        var result = policy.check(() -> 3);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    void testDepthAtMax() throws Exception {
        ForkDepthRule rule = ForkDepthRule.of("too deep", 5);
        var policy = new ForkDepthPolicy(rule);
        var result = policy.check(() -> 5);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    void testDepthAboveMax() throws Exception {
        ForkDepthRule rule = ForkDepthRule.of("too deep", 5);
        var policy = new ForkDepthPolicy(rule);
        var result = policy.check(() -> 10);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    void testDepthZero() throws Exception {
        ForkDepthRule rule = ForkDepthRule.of("too deep", 5);
        var policy = new ForkDepthPolicy(rule);
        var result = policy.check(() -> 0);
        assertTrue(result.getDeny().isEmpty());
    }
}
