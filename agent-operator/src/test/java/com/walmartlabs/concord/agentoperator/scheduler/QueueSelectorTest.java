package com.walmartlabs.concord.agentoperator.scheduler;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class QueueSelectorTest {

    @Test
    void testParseWithFlavor() {
        Map<String, Object> agent = new HashMap<>();
        agent.put("flavor", "default");
        Map<String, Object> selector = new HashMap<>();
        selector.put("agent", agent);

        QueueSelector qs = QueueSelector.parse(selector);
        assertEquals("default", qs.getFlavor());
        assertNull(qs.getQueryParams());
    }

    @Test
    void testParseWithQueryParams() {
        Map<String, Object> selector = new HashMap<>();
        selector.put("queryParams", List.of("status=ENQUEUED", "limit=10"));

        QueueSelector qs = QueueSelector.parse(selector);
        assertNull(qs.getFlavor());
        assertEquals(List.of("status=ENQUEUED", "limit=10"), qs.getQueryParams());
    }

    @Test
    void testParseWithFlavorAndQueryParams() {
        Map<String, Object> agent = new HashMap<>();
        agent.put("flavor", "gpu");
        Map<String, Object> selector = new HashMap<>();
        selector.put("agent", agent);
        selector.put("queryParams", List.of("tag=heavy"));

        QueueSelector qs = QueueSelector.parse(selector);
        assertEquals("gpu", qs.getFlavor());
        assertEquals(List.of("tag=heavy"), qs.getQueryParams());
    }

    @Test
    void testParseEmptyMap() {
        Map<String, Object> selector = new HashMap<>();
        QueueSelector qs = QueueSelector.parse(selector);
        assertNull(qs.getFlavor());
        assertNull(qs.getQueryParams());
    }

    @Test
    void testParseInvalidFlavorTypeThrows() {
        Map<String, Object> agent = new HashMap<>();
        agent.put("flavor", 123);
        Map<String, Object> selector = new HashMap<>();
        selector.put("agent", agent);

        assertThrows(IllegalArgumentException.class, () -> QueueSelector.parse(selector));
    }

    @Test
    void testParseInvalidQueryParamsTypeThrows() {
        Map<String, Object> selector = new HashMap<>();
        selector.put("queryParams", "not-a-list");

        assertThrows(IllegalArgumentException.class, () -> QueueSelector.parse(selector));
    }

    @Test
    void testParseInvalidQueryParamItemThrows() {
        Map<String, Object> selector = new HashMap<>();
        selector.put("queryParams", List.of(123));

        assertThrows(IllegalArgumentException.class, () -> QueueSelector.parse(selector));
    }

    @Test
    void testParseNullFlavor() {
        Map<String, Object> agent = new HashMap<>();
        agent.put("flavor", null);
        Map<String, Object> selector = new HashMap<>();
        selector.put("agent", agent);

        QueueSelector qs = QueueSelector.parse(selector);
        assertNull(qs.getFlavor());
    }
}
