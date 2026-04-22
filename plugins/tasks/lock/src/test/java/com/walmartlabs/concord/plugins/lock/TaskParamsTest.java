package com.walmartlabs.concord.plugins.lock;

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

import com.walmartlabs.concord.runtime.v2.sdk.MapBackedVariables;
import com.walmartlabs.concord.runtime.v2.sdk.Variables;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TaskParamsTest {

    private static Variables vars(Map<String, Object> m) {
        return new MapBackedVariables(m);
    }

    @Test
    public void lockNameReturnsConfiguredValue() {
        Map<String, Object> m = new HashMap<>();
        m.put("name", "my-lock");

        TaskParams p = new TaskParams(vars(m));
        assertEquals("my-lock", p.lockName());
    }

    @Test
    public void lockNameThrowsWhenMissing() {
        TaskParams p = new TaskParams(vars(new HashMap<>()));
        assertThrows(RuntimeException.class, p::lockName);
    }

    @Test
    public void scopeDefaultsToProject() {
        Map<String, Object> m = new HashMap<>();
        m.put("name", "my-lock");

        TaskParams p = new TaskParams(vars(m));
        assertEquals("PROJECT", p.scope());
    }

    @Test
    public void scopeUsesProvidedValue() {
        Map<String, Object> m = new HashMap<>();
        m.put("name", "my-lock");
        m.put("scope", "ORG");

        TaskParams p = new TaskParams(vars(m));
        assertEquals("ORG", p.scope());
    }
}
