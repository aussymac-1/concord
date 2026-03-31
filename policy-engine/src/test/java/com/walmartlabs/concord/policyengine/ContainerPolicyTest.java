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

import java.util.HashMap;
import java.util.Map;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerPolicyTest {

    @Test
    public void testCpu() {
        ContainerPolicy oneCpu = new ContainerPolicy(ContainerRule.of("1 CPU", null, 1));
        ContainerPolicy twoCpu = new ContainerPolicy(ContainerRule.of("1 CPU", null, 2));

        Map<String, Object> containerParams = new HashMap<>();
        containerParams.put("cpu", 2);

        // ---

        assertDeny(oneCpu, containerParams);
        assertAllow(twoCpu, containerParams);
    }

    @Test
    public void testRam() {
        ContainerPolicy ram1 = new ContainerPolicy(ContainerRule.of("128 RAM", "128m", null));
        ContainerPolicy ram2 = new ContainerPolicy(ContainerRule.of("256 RAM", "256m", null));

        Map<String, Object> containerParams = new HashMap<>();
        containerParams.put("ram", "256m");

        // ---

        assertDeny(ram1, containerParams);
        assertAllow(ram2, containerParams);
    }

    @Test
    public void testNullRule() {
        ContainerPolicy policy = new ContainerPolicy(null);
        CheckResult<ContainerRule, Object> result = policy.check(new HashMap<>());
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testEmptyContainerParams() {
        ContainerPolicy policy = new ContainerPolicy(ContainerRule.of("msg", "256m", 2));
        CheckResult<ContainerRule, Object> result = policy.check(new HashMap<>());
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testBothCpuAndRamExceeded() {
        ContainerPolicy policy = new ContainerPolicy(ContainerRule.of("msg", "128m", 1));

        Map<String, Object> params = new HashMap<>();
        params.put("cpu", 4);
        params.put("ram", "256m");

        CheckResult<ContainerRule, Object> result = policy.check(params);
        assertEquals(2, result.getDeny().size());
    }

    @Test
    public void testCpuExactlyAtLimit() {
        ContainerPolicy policy = new ContainerPolicy(ContainerRule.of("msg", null, 2));

        Map<String, Object> params = new HashMap<>();
        params.put("cpu", 2);

        assertAllow(policy, params);
    }

    @Test
    public void testRamGigabytes() {
        ContainerPolicy policy = new ContainerPolicy(ContainerRule.of("msg", "1g", null));

        Map<String, Object> params = new HashMap<>();
        params.put("ram", "512m");
        assertAllow(policy, params);

        params.put("ram", "2g");
        assertDeny(policy, params);
    }

    @Test
    public void testOnlyMaxCpuSet() {
        ContainerPolicy policy = new ContainerPolicy(ContainerRule.of("msg", null, 2));

        Map<String, Object> params = new HashMap<>();
        params.put("cpu", 1);
        params.put("ram", "999g");

        // ram is not checked because maxRam is null
        assertAllow(policy, params);
    }

    @Test
    public void testOnlyMaxRamSet() {
        ContainerPolicy policy = new ContainerPolicy(ContainerRule.of("msg", "128m", null));

        Map<String, Object> params = new HashMap<>();
        params.put("cpu", 999);
        params.put("ram", "64m");

        // cpu is not checked because maxCpu is null
        assertAllow(policy, params);
    }

    private static void assertAllow(ContainerPolicy policy, Map<String, Object> p) {
        CheckResult<ContainerRule, Object> result = policy.check(p);
        assertTrue(result.getDeny().isEmpty());
    }

    private static void assertDeny(ContainerPolicy policy, Map<String, Object> p) {
        CheckResult<ContainerRule, Object> result = policy.check(p);
        assertFalse(result.getDeny().isEmpty());
    }
}
