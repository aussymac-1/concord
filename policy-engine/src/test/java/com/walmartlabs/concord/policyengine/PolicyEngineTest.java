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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyEngineTest {

    @Test
    public void testDefaultConstructor() throws Exception {
        PolicyEngineRules rules = readRules("empty-rules.json");
        PolicyEngine engine = new PolicyEngine(rules);
        assertNotNull(engine);
        assertTrue(engine.policyNames().isEmpty());
    }

    @Test
    public void testSingleNameConstructor() throws Exception {
        PolicyEngineRules rules = readRules("empty-rules.json");
        PolicyEngine engine = new PolicyEngine("test-rule", rules);
        assertEquals(1, engine.policyNames().size());
        assertEquals("test-rule", engine.policyNames().get(0));
    }

    @Test
    public void testListNameConstructor() throws Exception {
        PolicyEngineRules rules = readRules("empty-rules.json");
        PolicyEngine engine = new PolicyEngine(Collections.singletonList("policy1"), rules);
        assertEquals(1, engine.policyNames().size());
    }

    @Test
    public void testGetRules() throws Exception {
        PolicyEngineRules rules = readRules("empty-rules.json");
        PolicyEngine engine = new PolicyEngine(rules);
        assertSame(rules, engine.getRules());
    }

    @Test
    public void testAllPoliciesNotNull() throws Exception {
        PolicyEngineRules rules = readRules("empty-rules.json");
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
    }

    @Test
    public void testToStringWithNames() throws Exception {
        PolicyEngineRules rules = readRules("empty-rules.json");
        PolicyEngine engine = new PolicyEngine("my-policy", rules);
        assertEquals("my-policy", engine.toString());
    }

    @Test
    public void testToStringNoNames() throws Exception {
        PolicyEngineRules rules = readRules("empty-rules.json");
        PolicyEngine engine = new PolicyEngine(rules);
        assertEquals("", engine.toString());
    }

    private PolicyEngineRules readRules(String resource) throws Exception {
        return new ObjectMapper().readValue(
                PolicyEngineTest.class.getResourceAsStream(resource),
                PolicyEngineRules.class);
    }
}
