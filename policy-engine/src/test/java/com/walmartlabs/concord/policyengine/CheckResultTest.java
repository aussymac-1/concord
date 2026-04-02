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

import static org.junit.jupiter.api.Assertions.*;

public class CheckResultTest {

    @Test
    public void testSuccess() {
        CheckResult<String, String> result = CheckResult.success();
        assertTrue(result.getWarn().isEmpty());
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testEmptyConstructor() {
        CheckResult<String, String> result = new CheckResult<>();
        assertTrue(result.getWarn().isEmpty());
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testWarnFactory() {
        CheckResult.Item<String, String> item = new CheckResult.Item<>("rule1", "entity1");
        CheckResult<String, String> result = CheckResult.warn(item);
        assertEquals(1, result.getWarn().size());
        assertTrue(result.getDeny().isEmpty());
        assertEquals("rule1", result.getWarn().get(0).getRule());
        assertEquals("entity1", result.getWarn().get(0).getEntity());
    }

    @Test
    public void testErrorFactory() {
        CheckResult.Item<String, String> item = new CheckResult.Item<>("rule1", "entity1");
        CheckResult<String, String> result = CheckResult.error(item);
        assertTrue(result.getWarn().isEmpty());
        assertEquals(1, result.getDeny().size());
    }

    @Test
    public void testItemWithMsg() {
        CheckResult.Item<String, Integer> item = new CheckResult.Item<>("rule", 42, "exceeded limit");
        assertEquals("rule", item.getRule());
        assertEquals(42, item.getEntity());
        assertEquals("exceeded limit", item.getMsg());
        assertEquals("exceeded limit", item.toString());
    }

    @Test
    public void testItemWithoutMsg() {
        CheckResult.Item<String, Integer> item = new CheckResult.Item<>("rule", 42);
        assertNull(item.getMsg());
        assertNull(item.toString());
    }
}
