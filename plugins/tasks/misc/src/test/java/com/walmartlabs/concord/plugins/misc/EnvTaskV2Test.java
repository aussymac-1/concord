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

import static org.junit.jupiter.api.Assertions.*;

public class EnvTaskV2Test {

    /**
     * Picks an environment variable name that is guaranteed not to be set in the test process.
     */
    private static String unsetEnvVarName() {
        for (int i = 0; i < 1000; i++) {
            String candidate = "CONCORD_ENV_TASK_V2_TEST_UNSET_" + i;
            if (System.getenv(candidate) == null) {
                return candidate;
            }
        }
        throw new IllegalStateException("could not find an unset env var name");
    }

    /**
     * Picks an environment variable that is known to be set in the test process.
     * Falls back gracefully if PATH is not available.
     */
    private static String knownSetEnvVarName() {
        for (String name : new String[]{"PATH", "HOME", "USER", "JAVA_HOME"}) {
            if (System.getenv(name) != null) {
                return name;
            }
        }
        return null;
    }

    @Test
    public void testGetReturnsNullForUnsetVariable() {
        EnvTaskV2 task = new EnvTaskV2();
        assertNull(task.get(unsetEnvVarName()));
    }

    @Test
    public void testGetOrDefaultReturnsDefaultForUnsetVariable() {
        EnvTaskV2 task = new EnvTaskV2();
        assertEquals("default-val", task.getOrDefault(unsetEnvVarName(), "default-val"));
    }

    @Test
    public void testGetOrDefaultReturnsNullDefault() {
        EnvTaskV2 task = new EnvTaskV2();
        assertNull(task.getOrDefault(unsetEnvVarName(), null));
    }

    @Test
    public void testGetReturnsValueForSetVariable() {
        String name = knownSetEnvVarName();
        if (name == null) {
            // no common env var is set; rely on the unset-var tests for coverage
            return;
        }

        EnvTaskV2 task = new EnvTaskV2();
        assertEquals(System.getenv(name), task.get(name));
    }

    @Test
    public void testGetOrDefaultIgnoresDefaultWhenSet() {
        String name = knownSetEnvVarName();
        if (name == null) {
            return;
        }

        EnvTaskV2 task = new EnvTaskV2();
        assertEquals(System.getenv(name), task.getOrDefault(name, "fallback"));
    }
}
