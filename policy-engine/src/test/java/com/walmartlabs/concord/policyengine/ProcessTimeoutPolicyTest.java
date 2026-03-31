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

public class ProcessTimeoutPolicyTest {

    @Test
    public void testNullRule() {
        ProcessTimeoutPolicy policy = new ProcessTimeoutPolicy(null);
        CheckResult<ProcessTimeoutRule, Object> result = policy.check("PT1H");
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testNullTimeout() {
        ProcessTimeoutPolicy policy = new ProcessTimeoutPolicy(ProcessTimeoutRule.of("max 2h", "PT2H"));
        CheckResult<ProcessTimeoutRule, Object> result = policy.check(null);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testTimeoutUnderLimit() {
        ProcessTimeoutPolicy policy = new ProcessTimeoutPolicy(ProcessTimeoutRule.of("max 2h", "PT2H"));
        CheckResult<ProcessTimeoutRule, Object> result = policy.check("PT1H");
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testTimeoutAtLimit() {
        ProcessTimeoutPolicy policy = new ProcessTimeoutPolicy(ProcessTimeoutRule.of("max 2h", "PT2H"));
        CheckResult<ProcessTimeoutRule, Object> result = policy.check("PT2H");
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testTimeoutOverLimit() {
        ProcessTimeoutPolicy policy = new ProcessTimeoutPolicy(ProcessTimeoutRule.of("max 2h", "PT2H"));
        CheckResult<ProcessTimeoutRule, Object> result = policy.check("PT3H");
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testInvalidTimeoutType() {
        ProcessTimeoutPolicy policy = new ProcessTimeoutPolicy(ProcessTimeoutRule.of("max 2h", "PT2H"));
        assertThrows(IllegalArgumentException.class, () -> policy.check(123));
    }

    @Test
    public void testTimeoutInMinutes() {
        ProcessTimeoutPolicy policy = new ProcessTimeoutPolicy(ProcessTimeoutRule.of("max 30m", "PT30M"));
        CheckResult<ProcessTimeoutRule, Object> result = policy.check("PT20M");
        assertTrue(result.getDeny().isEmpty());

        result = policy.check("PT45M");
        assertFalse(result.getDeny().isEmpty());
    }
}
