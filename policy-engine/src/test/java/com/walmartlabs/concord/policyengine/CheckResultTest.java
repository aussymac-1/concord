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
        assertTrue(result.getDeny().isEmpty());
        assertTrue(result.getWarn().isEmpty());
    }

    @Test
    public void testSuccessSingleton() {
        CheckResult<String, String> result1 = CheckResult.success();
        CheckResult<String, String> result2 = CheckResult.success();
        assertSame(result1, result2);
    }

    @Test
    public void testEmptyConstructor() {
        CheckResult<String, String> result = new CheckResult<>();
        assertTrue(result.getDeny().isEmpty());
        assertTrue(result.getWarn().isEmpty());
    }

    @Test
    public void testWarnFactory() {
        CheckResult.Item<String, String> item = new CheckResult.Item<>("rule", "entity", "msg");
        CheckResult<String, String> result = CheckResult.warn(item);
        assertFalse(result.getWarn().isEmpty());
        assertTrue(result.getDeny().isEmpty());
        assertEquals(1, result.getWarn().size());
        assertEquals("rule", result.getWarn().get(0).getRule());
        assertEquals("entity", result.getWarn().get(0).getEntity());
        assertEquals("msg", result.getWarn().get(0).getMsg());
    }

    @Test
    public void testErrorFactory() {
        CheckResult.Item<String, String> item = new CheckResult.Item<>("rule", "entity");
        CheckResult<String, String> result = CheckResult.error(item);
        assertTrue(result.getWarn().isEmpty());
        assertFalse(result.getDeny().isEmpty());
        assertEquals(1, result.getDeny().size());
        assertNull(result.getDeny().get(0).getMsg());
    }

    @Test
    public void testMultipleWarnItems() {
        CheckResult.Item<String, String> item1 = new CheckResult.Item<>("r1", "e1");
        CheckResult.Item<String, String> item2 = new CheckResult.Item<>("r2", "e2");
        CheckResult<String, String> result = CheckResult.warn(item1, item2);
        assertEquals(2, result.getWarn().size());
    }

    @Test
    public void testMultipleErrorItems() {
        CheckResult.Item<String, String> item1 = new CheckResult.Item<>("r1", "e1");
        CheckResult.Item<String, String> item2 = new CheckResult.Item<>("r2", "e2");
        CheckResult<String, String> result = CheckResult.error(item1, item2);
        assertEquals(2, result.getDeny().size());
    }

    @Test
    public void testItemToString() {
        CheckResult.Item<String, String> item = new CheckResult.Item<>("rule", "entity", "my message");
        assertEquals("my message", item.toString());
    }

    @Test
    public void testItemToStringNull() {
        CheckResult.Item<String, String> item = new CheckResult.Item<>("rule", "entity");
        assertNull(item.toString());
    }

    @Test
    public void testItemGetters() {
        CheckResult.Item<String, Integer> item = new CheckResult.Item<>("myRule", 42, "info");
        assertEquals("myRule", item.getRule());
        assertEquals(42, item.getEntity());
        assertEquals("info", item.getMsg());
    }
}
