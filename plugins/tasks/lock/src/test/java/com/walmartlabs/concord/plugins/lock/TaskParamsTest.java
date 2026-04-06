package com.walmartlabs.concord.plugins.lock;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2020 Walmart Inc.
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
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TaskParamsTest {

    @Test
    void testLockName() {
        var vars = new MapBackedVariables(Map.of("name", "myLock"));
        var params = new TaskParams(vars);
        assertEquals("myLock", params.lockName());
    }

    @Test
    void testLockNameMissing() {
        var vars = new MapBackedVariables(Map.of("scope", "PROJECT"));
        var params = new TaskParams(vars);
        assertThrows(IllegalArgumentException.class, params::lockName);
    }

    @Test
    void testScopeDefault() {
        var vars = new MapBackedVariables(Map.of("name", "myLock"));
        var params = new TaskParams(vars);
        assertEquals("PROJECT", params.scope());
    }

    @Test
    void testScopeCustom() {
        var vars = new MapBackedVariables(Map.of("name", "myLock", "scope", "ORG"));
        var params = new TaskParams(vars);
        assertEquals("ORG", params.scope());
    }
}
