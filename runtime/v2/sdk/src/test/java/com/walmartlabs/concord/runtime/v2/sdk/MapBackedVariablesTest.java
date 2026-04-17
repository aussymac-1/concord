package com.walmartlabs.concord.runtime.v2.sdk;

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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MapBackedVariablesTest {

    @Test
    void getReturnsValueFromBackingMap() {
        Map<String, Object> data = Map.of("a", 1, "b", "two");
        MapBackedVariables v = new MapBackedVariables(data);

        assertThat(v.get("a")).isEqualTo(1);
        assertThat(v.get("b")).isEqualTo("two");
    }

    @Test
    void getReturnsNullForMissingKey() {
        MapBackedVariables v = new MapBackedVariables(Map.of("a", 1));

        assertThat(v.get("missing")).isNull();
    }

    @Test
    void hasReturnsTrueOnlyForExistingKeys() {
        MapBackedVariables v = new MapBackedVariables(Map.of("a", 1));

        assertThat(v.has("a")).isTrue();
        assertThat(v.has("missing")).isFalse();
    }

    @Test
    void toMapReturnsUnderlyingData() {
        Map<String, Object> data = Map.of("a", 1);
        MapBackedVariables v = new MapBackedVariables(data);

        assertThat(v.toMap()).isEqualTo(data);
    }

    @Test
    void nullDelegateResultsInEmptyMap() {
        MapBackedVariables v = new MapBackedVariables(null);

        assertThat(v.toMap()).isEmpty();
        assertThat(v.has("x")).isFalse();
        assertThat(v.get("x")).isNull();
    }

    @Test
    void setIsNotSupported() {
        MapBackedVariables v = new MapBackedVariables(Map.of("a", 1));

        assertThatThrownBy(() -> v.set("a", 2))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void toMapIsUnmodifiable() {
        Map<String, Object> src = new HashMap<>();
        src.put("a", 1);
        MapBackedVariables v = new MapBackedVariables(src);

        Map<String, Object> view = v.toMap();
        assertThatThrownBy(() -> view.put("b", 2))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
