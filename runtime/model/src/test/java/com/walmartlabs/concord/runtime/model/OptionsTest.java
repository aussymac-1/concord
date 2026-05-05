package com.walmartlabs.concord.runtime.model;

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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OptionsTest {

    @Test
    public void testDefaults() {
        var instanceId = UUID.randomUUID();

        var opts = Options.builder()
                .instanceId(instanceId)
                .entryPoint("default")
                .build();

        assertEquals(instanceId, opts.instanceId());
        assertEquals("default", opts.entryPoint());
        assertNull(opts.parentInstanceId());
        assertTrue(opts.configuration().isEmpty());
        assertTrue(opts.activeProfiles().isEmpty());
    }

    @Test
    public void testFullyPopulated() {
        var instanceId = UUID.randomUUID();
        var parentId = UUID.randomUUID();
        var cfg = new HashMap<String, Object>();
        cfg.put("a", 1);
        var profiles = List.of("dev", "qa");

        var opts = Options.builder()
                .instanceId(instanceId)
                .parentInstanceId(parentId)
                .entryPoint("entry")
                .configuration(cfg)
                .activeProfiles(profiles)
                .build();

        assertEquals(instanceId, opts.instanceId());
        assertEquals(parentId, opts.parentInstanceId());
        assertEquals("entry", opts.entryPoint());
        assertEquals(1, opts.configuration().get("a"));
        assertEquals(profiles, opts.activeProfiles());
    }

    @Test
    public void testConfigurationAllowsNullValues() {
        var instanceId = UUID.randomUUID();
        var cfg = new HashMap<String, Object>();
        cfg.put("nullable", null);

        var opts = Options.builder()
                .instanceId(instanceId)
                .entryPoint("default")
                .configuration(cfg)
                .build();

        assertTrue(opts.configuration().containsKey("nullable"));
        assertNull(opts.configuration().get("nullable"));
    }

    @Test
    public void testMissingRequiredAttributesThrow() {
        var builder = Options.builder();

        assertThrows(IllegalStateException.class, builder::build);
    }
}
