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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProtectedTasksPolicyTest {

    @Test
    public void testNullRule() {
        ProtectedTasksPolicy policy = new ProtectedTasksPolicy(null);
        assertFalse(policy.isProtected("anything"));
    }

    @Test
    public void testProtectedTask() {
        Set<String> names = new HashSet<>();
        names.add("gatekeeper");
        names.add("ansible");

        ProtectedTasksPolicy policy = new ProtectedTasksPolicy(ProtectedTasksRule.of(names));
        assertTrue(policy.isProtected("gatekeeper"));
        assertTrue(policy.isProtected("ansible"));
    }

    @Test
    public void testUnprotectedTask() {
        ProtectedTasksPolicy policy = new ProtectedTasksPolicy(
                ProtectedTasksRule.of(Collections.singleton("gatekeeper")));
        assertFalse(policy.isProtected("slack"));
    }

    @Test
    public void testEmptyProtectedSet() {
        ProtectedTasksPolicy policy = new ProtectedTasksPolicy(
                ProtectedTasksRule.of(Collections.emptySet()));
        assertFalse(policy.isProtected("anything"));
    }

    @Test
    public void testCaseSensitivity() {
        ProtectedTasksPolicy policy = new ProtectedTasksPolicy(
                ProtectedTasksRule.of(Collections.singleton("Gatekeeper")));
        assertFalse(policy.isProtected("gatekeeper"));
        assertTrue(policy.isProtected("Gatekeeper"));
    }
}
