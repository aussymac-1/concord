package com.walmartlabs.concord.plugins.misc;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EnvTaskV2Test {

    @Test
    public void getReturnsNullForMissingVariable() {
        var task = new EnvTaskV2();
        // Pick a name that is extremely unlikely to ever be set in the test JVM.
        assertNull(task.get("__concord_env_task_definitely_missing__"));
    }

    @Test
    public void getOrDefaultReturnsDefaultForMissing() {
        var task = new EnvTaskV2();
        assertEquals("fallback",
                task.getOrDefault("__concord_env_task_definitely_missing__", "fallback"));
    }

    @Test
    public void getOrDefaultAllowsNullDefault() {
        var task = new EnvTaskV2();
        assertNull(task.getOrDefault("__concord_env_task_definitely_missing__", null));
    }

    @Test
    public void getReturnsRealEnvironmentVariableWhenSet() {
        var task = new EnvTaskV2();
        // Find any env var that is set and verify the task reads it back.
        var any = System.getenv().entrySet().stream()
                .filter(e -> e.getValue() != null)
                .findFirst()
                .orElse(null);

        if (any == null) {
            // Nothing to assert if the JVM has no env vars (very unusual).
            return;
        }

        assertEquals(any.getValue(), task.get(any.getKey()));
        assertEquals(any.getValue(), task.getOrDefault(any.getKey(), "fallback"));
    }
}
