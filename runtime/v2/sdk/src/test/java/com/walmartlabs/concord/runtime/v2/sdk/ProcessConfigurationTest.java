package com.walmartlabs.concord.runtime.v2.sdk;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2025 Walmart Inc.
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProcessConfigurationTest {

    @Test
    public void testDefaults() {
        var cfg = ProcessConfiguration.builder().build();

        assertNull(cfg.instanceId());
        assertFalse(cfg.debug());
        assertFalse(cfg.dryRun());
        assertEquals("default", cfg.entryPoint());
        assertTrue(cfg.arguments().isEmpty());
        assertTrue(cfg.meta().isEmpty());
        assertNull(cfg.initiator());
        assertNull(cfg.currentUser());
        assertEquals(ProcessInfo.builder().build(), cfg.processInfo());
        assertEquals(ProjectInfo.builder().build(), cfg.projectInfo());
        assertTrue(cfg.defaultTaskVariables().isEmpty());
        assertTrue(cfg.out().isEmpty());
    }

    @Test
    public void testCustomValues() {
        var instanceId = UUID.randomUUID();
        var args = new HashMap<String, Object>();
        args.put("key", "value");
        args.put("nullable", null);

        var cfg = ProcessConfiguration.builder()
                .instanceId(instanceId)
                .debug(true)
                .dryRun(true)
                .entryPoint("entry")
                .arguments(args)
                .meta(java.util.Map.of("m", "v"))
                .out(List.of("a", "b"))
                .initiator(java.util.Map.of("user", "x"))
                .currentUser(java.util.Map.of("user", "y"))
                .build();

        assertEquals(instanceId, cfg.instanceId());
        assertTrue(cfg.debug());
        assertTrue(cfg.dryRun());
        assertEquals("entry", cfg.entryPoint());
        assertEquals("value", cfg.arguments().get("key"));
        assertTrue(cfg.arguments().containsKey("nullable"));
        assertNull(cfg.arguments().get("nullable"));
        assertEquals("v", cfg.meta().get("m"));
        assertEquals(List.of("a", "b"), cfg.out());
    }
}
