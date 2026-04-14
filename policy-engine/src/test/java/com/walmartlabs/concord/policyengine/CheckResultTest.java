package com.walmartlabs.concord.policyengine;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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
    public void testSuccessResult() {
        CheckResult<String, String> result = CheckResult.success();
        assertTrue(result.getWarn().isEmpty());
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testSuccessIsSingleton() {
        CheckResult<String, String> a = CheckResult.success();
        CheckResult<String, String> b = CheckResult.success();
        assertSame(a, b);
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
    public void testItemWithMessage() {
        CheckResult.Item<String, Integer> item = new CheckResult.Item<>("rule", 42, "some message");
        assertEquals("rule", item.getRule());
        assertEquals(42, item.getEntity());
        assertEquals("some message", item.getMsg());
    }

    @Test
    public void testItemWithoutMessage() {
        CheckResult.Item<String, Integer> item = new CheckResult.Item<>("rule", 42);
        assertNull(item.getMsg());
    }

    @Test
    public void testItemToString() {
        CheckResult.Item<String, String> item = new CheckResult.Item<>("rule", "entity", "display msg");
        assertEquals("display msg", item.toString());
    }

    @Test
    public void testItemToStringNull() {
        CheckResult.Item<String, String> item = new CheckResult.Item<>("rule", "entity");
        assertNull(item.toString());
    }

    @Test
    public void testMultipleWarnItems() {
        CheckResult.Item<String, String> i1 = new CheckResult.Item<>("r1", "e1");
        CheckResult.Item<String, String> i2 = new CheckResult.Item<>("r2", "e2");
        CheckResult<String, String> result = CheckResult.warn(i1, i2);
        assertEquals(2, result.getWarn().size());
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testMultipleErrorItems() {
        CheckResult.Item<String, String> i1 = new CheckResult.Item<>("r1", "e1");
        CheckResult.Item<String, String> i2 = new CheckResult.Item<>("r2", "e2");
        CheckResult<String, String> result = CheckResult.error(i1, i2);
        assertEquals(2, result.getDeny().size());
        assertTrue(result.getWarn().isEmpty());
    }
}
