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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TriggerTest {

    @Test
    public void testRequiredFieldsAndDefaults() {
        var trigger = Trigger.builder().name("github").build();

        assertEquals("github", trigger.name());
        assertNull(trigger.arguments());
        assertNull(trigger.conditions());
        assertNull(trigger.configuration());
        assertNull(trigger.activeProfiles());
        assertNull(trigger.sourceMap());
    }

    @Test
    public void testFullyPopulated() {
        var sm = SourceMap.builder().source("a.yml").line(1).column(1).build();
        Map<String, Object> args = Map.of("a", 1);
        Map<String, Object> conds = Map.of("type", "push");
        Map<String, Object> cfg = Map.of("useInitiator", true);
        var profiles = List.of("dev");

        var trigger = Trigger.builder()
                .name("github")
                .arguments(args)
                .conditions(conds)
                .configuration(cfg)
                .activeProfiles(profiles)
                .sourceMap(sm)
                .build();

        assertEquals("github", trigger.name());
        assertEquals(args, trigger.arguments());
        assertEquals(conds, trigger.conditions());
        assertEquals(cfg, trigger.configuration());
        assertEquals(profiles, trigger.activeProfiles());
        assertSame(sm, trigger.sourceMap());
    }

    @Test
    public void testMissingNameThrows() {
        var builder = Trigger.builder();

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    public void testEqualsAndHashCode() {
        var a = Trigger.builder().name("github").build();
        var b = Trigger.builder().name("github").build();
        var c = Trigger.builder().name("cron").build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
