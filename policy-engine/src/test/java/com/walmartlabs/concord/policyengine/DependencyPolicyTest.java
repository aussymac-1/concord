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

import com.walmartlabs.concord.dependencymanager.DependencyEntity;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class DependencyPolicyTest {

    @Test
    public void testSingleDepDeny() {
        DependencyRule di = DependencyRule.builder()
                .groupId(".*")
                .build();

        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, null, Collections.singletonList(di));

        DependencyPolicy policy = new DependencyPolicy(rules);

        DependencyEntity dependency = buildDependency("com.walmartlabs.concord.plugins.basic",
                "ansible-tasks",
                "0.57.1-SNAPSHOT");

        // ---
        assertDeny(policy, dependency);
    }

    @Test
    public void testSingleDepAllow() {
        DependencyRule di = DependencyRule.builder()
                .groupId("com.walmartlabs.concord.plugins.basic")
                .build();

        PolicyRules<DependencyRule> rules = new PolicyRules<>(Collections.singletonList(di), null, null);

        DependencyPolicy policy = new DependencyPolicy(rules);

        DependencyEntity dependency = buildDependency("com.walmartlabs.concord.plugins.basic",
                "ansible-tasks",
                "0.57.1-SNAPSHOT");

        // ---

        assertAllow(policy, dependency);
    }

    @Test
    public void testSingleDepAllowVersion() {
        DependencyRule allow = DependencyRule.builder()
                .groupId("com.walmartlabs.concord.plugins.basic")
                .fromVersion("0.5.0")
                .toVersion("1.0.0")
                .build();
        DependencyRule deny = DependencyRule.builder()
                .groupId("com.walmartlabs.concord.plugins.basic")
                .build();

        PolicyRules<DependencyRule> rules = new PolicyRules<>(Collections.singletonList(allow), null, Collections.singletonList(deny));

        DependencyPolicy policy = new DependencyPolicy(rules);

        // ---
        DependencyEntity dep1 = buildDependency("com.walmartlabs.concord.plugins.basic",
                "ansible-tasks",
                "0.57.1-SNAPSHOT");

        assertAllow(policy, dep1);

        // ---
        DependencyEntity dep2 = buildDependency("com.walmartlabs.concord.plugins.basic",
                "ansible-tasks",
                "1.1.1-SNAPSHOT");

        assertDeny(policy, dep2);

        // ---
        DependencyEntity dep3 = buildDependency("com.walmartlabs.concord.plugins.basic",
                "ansible-tasks",
                "0.4.9");

        assertDeny(policy, dep3);

        // ---
        DependencyEntity dep4 = buildDependency("com.walmartlabs.concord.plugins.basic",
                "ansible-tasks",
                "1.0.1");

        assertDeny(policy, dep4);
    }

    @Test
    public void testNullRules() {
        DependencyPolicy policy = new DependencyPolicy(null);
        DependencyEntity dep = buildDependency("com.example", "artifact", "1.0.0");
        CheckResult<DependencyRule, DependencyEntity> result = policy.check(Collections.singletonList(dep));
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testEmptyRules() {
        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, null, null);
        DependencyPolicy policy = new DependencyPolicy(rules);
        DependencyEntity dep = buildDependency("com.example", "artifact", "1.0.0");
        CheckResult<DependencyRule, DependencyEntity> result = policy.check(Collections.singletonList(dep));
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testEmptyDependencyList() {
        DependencyRule deny = DependencyRule.builder().groupId(".*").build();
        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, null, Collections.singletonList(deny));
        DependencyPolicy policy = new DependencyPolicy(rules);

        CheckResult<DependencyRule, DependencyEntity> result = policy.check(Collections.emptyList());
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testWarnResult() {
        DependencyRule warnRule = DependencyRule.builder()
                .groupId("com\\.example")
                .build();

        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, Collections.singletonList(warnRule), null);
        DependencyPolicy policy = new DependencyPolicy(rules);

        DependencyEntity dep = buildDependency("com.example", "artifact", "1.0.0");
        CheckResult<DependencyRule, DependencyEntity> result = policy.check(Collections.singletonList(dep));
        assertTrue(result.getDeny().isEmpty());
        assertFalse(result.getWarn().isEmpty());
    }

    @Test
    public void testMultipleDependencies() {
        DependencyRule deny = DependencyRule.builder()
                .groupId("com\\.bad")
                .build();

        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, null, Collections.singletonList(deny));
        DependencyPolicy policy = new DependencyPolicy(rules);

        DependencyEntity good = buildDependency("com.good", "artifact", "1.0.0");
        DependencyEntity bad = buildDependency("com.bad", "artifact", "1.0.0");

        CheckResult<DependencyRule, DependencyEntity> result = policy.check(Arrays.asList(good, bad));
        assertFalse(result.getDeny().isEmpty());
        assertEquals(1, result.getDeny().size());
    }

    @Test
    public void testWildcardArtifactId() {
        DependencyRule deny = DependencyRule.builder()
                .groupId("com\\.example")
                .artifactId(".*")
                .build();

        PolicyRules<DependencyRule> rules = new PolicyRules<>(null, null, Collections.singletonList(deny));
        DependencyPolicy policy = new DependencyPolicy(rules);

        DependencyEntity dep = buildDependency("com.example", "anything", "1.0.0");
        assertDeny(policy, dep);
    }

    private static void assertDeny(DependencyPolicy policy, DependencyEntity entity) {
        CheckResult<DependencyRule, DependencyEntity> result = policy.check(Collections.singletonList(entity));
        assertFalse(result.getDeny().isEmpty());
    }

    private static void assertAllow(DependencyPolicy policy, DependencyEntity entity) {
        CheckResult<DependencyRule, DependencyEntity> result = policy.check(Collections.singletonList(entity));
        assertTrue(result.getDeny().isEmpty());
    }

    private static DependencyEntity buildDependency(String groupId, String artifactId, String version) {
        return new DependencyEntity(Paths.get("/whatever"), groupId, artifactId, version);
    }
}
