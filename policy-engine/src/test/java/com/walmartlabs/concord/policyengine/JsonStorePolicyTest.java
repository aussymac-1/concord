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

public class JsonStorePolicyTest {

    @Test
    public void testNullRule() throws Exception {
        JsonStorePolicy policy = new JsonStorePolicy(null);
        CheckResult<JsonStoreRule.StoreRule, Integer> result = policy.checkStorage(() -> 10);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testNullStoreRule() throws Exception {
        JsonStoreRule rule = JsonStoreRule.of(null, null);
        JsonStorePolicy policy = new JsonStorePolicy(rule);
        CheckResult<JsonStoreRule.StoreRule, Integer> result = policy.checkStorage(() -> 10);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testStorageCountUnderLimit() throws Exception {
        JsonStoreRule.StoreRule storeRule = JsonStoreRule.StoreRule.of("max 5", 5);
        JsonStoreRule rule = JsonStoreRule.of(storeRule, null);
        JsonStorePolicy policy = new JsonStorePolicy(rule);

        CheckResult<JsonStoreRule.StoreRule, Integer> result = policy.checkStorage(() -> 3);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testStorageCountAtLimit() throws Exception {
        JsonStoreRule.StoreRule storeRule = JsonStoreRule.StoreRule.of("max 5", 5);
        JsonStoreRule rule = JsonStoreRule.of(storeRule, null);
        JsonStorePolicy policy = new JsonStorePolicy(rule);

        CheckResult<JsonStoreRule.StoreRule, Integer> result = policy.checkStorage(() -> 5);
        assertFalse(result.getDeny().isEmpty());
        assertEquals(5, result.getDeny().get(0).getEntity());
    }

    @Test
    public void testStorageCountOverLimit() throws Exception {
        JsonStoreRule.StoreRule storeRule = JsonStoreRule.StoreRule.of("max 5", 5);
        JsonStoreRule rule = JsonStoreRule.of(storeRule, null);
        JsonStorePolicy policy = new JsonStorePolicy(rule);

        CheckResult<JsonStoreRule.StoreRule, Integer> result = policy.checkStorage(() -> 10);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testNullDataRule() throws Exception {
        JsonStoreRule rule = JsonStoreRule.of(null, null);
        JsonStorePolicy policy = new JsonStorePolicy(rule);
        CheckResult<JsonStoreRule.StoreDataRule, Long> result = policy.checkStorageData(() -> 100L);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testDataSizeUnderLimit() throws Exception {
        JsonStoreRule.StoreDataRule dataRule = JsonStoreRule.StoreDataRule.of("max 1000", 1000L);
        JsonStoreRule rule = JsonStoreRule.of(null, dataRule);
        JsonStorePolicy policy = new JsonStorePolicy(rule);

        CheckResult<JsonStoreRule.StoreDataRule, Long> result = policy.checkStorageData(() -> 500L);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testDataSizeAtLimit() throws Exception {
        JsonStoreRule.StoreDataRule dataRule = JsonStoreRule.StoreDataRule.of("max 1000", 1000L);
        JsonStoreRule rule = JsonStoreRule.of(null, dataRule);
        JsonStorePolicy policy = new JsonStorePolicy(rule);

        CheckResult<JsonStoreRule.StoreDataRule, Long> result = policy.checkStorageData(() -> 1000L);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testDataSizeOverLimit() throws Exception {
        JsonStoreRule.StoreDataRule dataRule = JsonStoreRule.StoreDataRule.of("max 1000", 1000L);
        JsonStoreRule rule = JsonStoreRule.of(null, dataRule);
        JsonStorePolicy policy = new JsonStorePolicy(rule);

        CheckResult<JsonStoreRule.StoreDataRule, Long> result = policy.checkStorageData(() -> 2000L);
        assertFalse(result.getDeny().isEmpty());
        assertEquals(2000L, (long) result.getDeny().get(0).getEntity());
    }

    @Test
    public void testGetMaxSizeNullRule() {
        JsonStorePolicy policy = new JsonStorePolicy(null);
        assertNull(policy.getMaxSize());
    }

    @Test
    public void testGetMaxSizeNullDataRule() {
        JsonStoreRule rule = JsonStoreRule.of(null, null);
        JsonStorePolicy policy = new JsonStorePolicy(rule);
        assertNull(policy.getMaxSize());
    }

    @Test
    public void testGetMaxSizeWithDataRule() {
        JsonStoreRule.StoreDataRule dataRule = JsonStoreRule.StoreDataRule.of("msg", 5000L);
        JsonStoreRule rule = JsonStoreRule.of(null, dataRule);
        JsonStorePolicy policy = new JsonStorePolicy(rule);
        assertEquals(5000L, (long) policy.getMaxSize());
    }
}
