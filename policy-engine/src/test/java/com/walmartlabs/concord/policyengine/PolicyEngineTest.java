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

import static org.junit.jupiter.api.Assertions.*;

public class PolicyEngineTest {

    @Test
    public void testConstructionWithEmptyRules() {
        PolicyEngineRules rules = PolicyEngineRules.builder().build();
        PolicyEngine engine = new PolicyEngine(rules);

        assertNotNull(engine.getDependencyPolicy());
        assertNotNull(engine.getDependencyRewritePolicy());
        assertNotNull(engine.getFilePolicy());
        assertNotNull(engine.getTaskPolicy());
        assertNotNull(engine.getWorkspacePolicy());
        assertNotNull(engine.getAttachmentsPolicy());
        assertNotNull(engine.getContainerPolicy());
        assertNotNull(engine.getConcurrentProcessPolicy());
        assertNotNull(engine.getForkDepthPolicy());
        assertNotNull(engine.getProcessTimeoutPolicy());
        assertNotNull(engine.getProtectedTasksPolicy());
        assertNotNull(engine.getEntityPolicy());
        assertNotNull(engine.getProcessCfgPolicy());
        assertNotNull(engine.getJsonStoragePolicy());
        assertNotNull(engine.getDefaultProcessCfgPolicy());
        assertNotNull(engine.getDefaultDependencyVersionsPolicy());
        assertNotNull(engine.getStatePolicy());
        assertNotNull(engine.getRawPayloadPolicy());
        assertNotNull(engine.getRuntimePolicy());
        assertNotNull(engine.getCronTriggerPolicy());
        assertNotNull(engine.getKvPolicy());
        assertNotNull(engine.getRules());
        assertEquals(rules, engine.getRules());
    }

    @Test
    public void testConstructionWithSingleRuleName() {
        PolicyEngineRules rules = PolicyEngineRules.builder().build();
        PolicyEngine engine = new PolicyEngine("testPolicy", rules);

        assertEquals(Collections.singletonList("testPolicy"), engine.policyNames());
    }

    @Test
    public void testConstructionWithMultipleRuleNames() {
        PolicyEngineRules rules = PolicyEngineRules.builder().build();
        List<String> names = Arrays.asList("policy1", "policy2", "policy3");
        PolicyEngine engine = new PolicyEngine(names, rules);

        assertEquals(names, engine.policyNames());
    }

    @Test
    public void testConstructionWithNoRuleNames() {
        PolicyEngineRules rules = PolicyEngineRules.builder().build();
        PolicyEngine engine = new PolicyEngine(rules);

        assertTrue(engine.policyNames().isEmpty());
    }

    @Test
    public void testToStringWithRuleNames() {
        PolicyEngineRules rules = PolicyEngineRules.builder().build();
        PolicyEngine engine = new PolicyEngine(Arrays.asList("a", "b"), rules);

        assertEquals("a, b", engine.toString());
    }

    @Test
    public void testToStringWithNullRuleNames() {
        PolicyEngineRules rules = PolicyEngineRules.builder().build();
        PolicyEngine engine = new PolicyEngine((List<String>) null, rules);

        assertEquals("no rules defined", engine.toString());
    }

    @Test
    public void testConstructionWithQueueRules() {
        ConcurrentProcessRule concurrent = ConcurrentProcessRule.builder()
                .maxPerOrg(10)
                .build();
        ForkDepthRule forkDepth = ForkDepthRule.of("max 5", 5);
        ProcessTimeoutRule timeout = ProcessTimeoutRule.of("max 2h", "PT2H");
        QueueRule queueRule = QueueRule.of(concurrent, forkDepth, timeout);

        PolicyEngineRules rules = PolicyEngineRules.builder()
                .queueRules(queueRule)
                .build();
        PolicyEngine engine = new PolicyEngine(rules);

        assertNotNull(engine.getConcurrentProcessPolicy());
        assertNotNull(engine.getForkDepthPolicy());
        assertNotNull(engine.getProcessTimeoutPolicy());
    }

    @Test
    public void testConstructionWithNullQueueRules() {
        PolicyEngineRules rules = PolicyEngineRules.builder().build();
        PolicyEngine engine = new PolicyEngine(rules);

        // should not throw, should use QueueRule.empty()
        assertNotNull(engine.getConcurrentProcessPolicy());
        assertNotNull(engine.getForkDepthPolicy());
        assertNotNull(engine.getProcessTimeoutPolicy());
    }
}
