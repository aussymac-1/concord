package com.walmartlabs.concord.agentoperator.scheduler;

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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QueueSelectorTest {

    @Test
    void testParseWithFlavor() {
        var input = Map.<String, Object>of("agent", Map.of("flavor", "default"));
        var selector = QueueSelector.parse(input);
        assertEquals("default", selector.getFlavor());
        assertNull(selector.getQueryParams());
    }

    @Test
    void testParseWithQueryParams() {
        var input = Map.<String, Object>of("queryParams", List.of("status=ENQUEUED", "limit=10"));
        var selector = QueueSelector.parse(input);
        assertNull(selector.getFlavor());
        assertEquals(List.of("status=ENQUEUED", "limit=10"), selector.getQueryParams());
    }

    @Test
    void testParseWithFlavorAndQueryParams() {
        var input = Map.<String, Object>of(
                "agent", Map.of("flavor", "gpu"),
                "queryParams", List.of("status=ENQUEUED"));
        var selector = QueueSelector.parse(input);
        assertEquals("gpu", selector.getFlavor());
        assertEquals(List.of("status=ENQUEUED"), selector.getQueryParams());
    }

    @Test
    void testParseEmpty() {
        var input = Map.<String, Object>of();
        var selector = QueueSelector.parse(input);
        assertNull(selector.getFlavor());
        assertNull(selector.getQueryParams());
    }

    @Test
    void testParseInvalidFlavorType() {
        var input = Map.<String, Object>of("agent", Map.of("flavor", 123));
        assertThrows(IllegalArgumentException.class, () -> QueueSelector.parse(input));
    }

    @Test
    void testParseInvalidQueryParamsType() {
        var input = Map.<String, Object>of("queryParams", "not-a-list");
        assertThrows(IllegalArgumentException.class, () -> QueueSelector.parse(input));
    }

    @Test
    void testParseInvalidQueryParamItemType() {
        var input = Map.<String, Object>of("queryParams", List.of(123));
        assertThrows(IllegalArgumentException.class, () -> QueueSelector.parse(input));
    }
}
