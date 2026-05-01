package com.walmartlabs.concord.policyengine;

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

public class CheckResultTest {

    @Test
    public void testSuccess() {
        CheckResult<String, String> result = CheckResult.success();
        assertTrue(result.getWarn().isEmpty());
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testError() {
        CheckResult.Item<String, String> item = new CheckResult.Item<>("rule", "entity");
        CheckResult<String, String> result = CheckResult.error(item);
        assertTrue(result.getWarn().isEmpty());
        assertFalse(result.getDeny().isEmpty());
        assertEquals("rule", result.getDeny().get(0).getRule());
        assertEquals("entity", result.getDeny().get(0).getEntity());
    }

    @Test
    public void testWarn() {
        CheckResult.Item<String, String> item = new CheckResult.Item<>("rule", "entity");
        CheckResult<String, String> result = CheckResult.warn(item);
        assertFalse(result.getWarn().isEmpty());
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testItemWithMessage() {
        CheckResult.Item<String, String> item = new CheckResult.Item<>("rule", "entity", "custom msg");
        assertEquals("rule", item.getRule());
        assertEquals("entity", item.getEntity());
        assertEquals("custom msg", item.getMsg());
        assertEquals("custom msg", item.toString());
    }

    @Test
    public void testItemWithoutMessage() {
        CheckResult.Item<String, String> item = new CheckResult.Item<>("rule", "entity");
        assertNull(item.getMsg());
        assertNull(item.toString());
    }

    @Test
    public void testEmptyConstructor() {
        CheckResult<String, String> result = new CheckResult<>();
        assertTrue(result.getWarn().isEmpty());
        assertTrue(result.getDeny().isEmpty());
    }
}
