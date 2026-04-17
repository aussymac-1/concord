package com.walmartlabs.concord.agentoperator.scheduler;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2026 Walmart Inc.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QueueSelectorTest {

    @Test
    void parseWithFlavorAndQueryParams() {
        Map<String, Object> agent = new HashMap<>();
        agent.put("flavor", "default");
        Map<String, Object> input = new HashMap<>();
        input.put("agent", agent);
        input.put("queryParams", List.of("param1=val1", "param2=val2"));

        QueueSelector result = QueueSelector.parse(input);
        assertThat(result.getFlavor()).isEqualTo("default");
        assertThat(result.getQueryParams()).containsExactly("param1=val1", "param2=val2");
    }

    @Test
    void parseWithNullFlavor() {
        Map<String, Object> input = new HashMap<>();
        input.put("agent", new HashMap<>());

        QueueSelector result = QueueSelector.parse(input);
        assertThat(result.getFlavor()).isNull();
    }

    @Test
    void parseWithNullQueryParams() {
        Map<String, Object> agent = new HashMap<>();
        agent.put("flavor", "test");
        Map<String, Object> input = new HashMap<>();
        input.put("agent", agent);

        QueueSelector result = QueueSelector.parse(input);
        assertThat(result.getFlavor()).isEqualTo("test");
        assertThat(result.getQueryParams()).isNull();
    }

    @Test
    void parseThrowsWhenFlavorIsNotString() {
        Map<String, Object> agent = new HashMap<>();
        agent.put("flavor", 123);
        Map<String, Object> input = new HashMap<>();
        input.put("agent", agent);

        assertThatThrownBy(() -> QueueSelector.parse(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("agent.flavor");
    }

    @Test
    void parseThrowsWhenQueryParamsNotList() {
        Map<String, Object> input = new HashMap<>();
        input.put("queryParams", "not-a-list");

        assertThatThrownBy(() -> QueueSelector.parse(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("queryParams");
    }

    @Test
    void parseThrowsWhenQueryParamItemNotString() {
        Map<String, Object> input = new HashMap<>();
        input.put("queryParams", List.of(123));

        assertThatThrownBy(() -> QueueSelector.parse(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("queryParams");
    }

    @Test
    void parseWithEmptyMap() {
        Map<String, Object> input = new HashMap<>();
        QueueSelector result = QueueSelector.parse(input);
        assertThat(result.getFlavor()).isNull();
        assertThat(result.getQueryParams()).isNull();
    }
}
