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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NoopVariablesTest {

    @Test
    void getAlwaysReturnsNull() {
        NoopVariables v = new NoopVariables();

        assertThat(v.get("anything")).isNull();
    }

    @Test
    void hasAlwaysReturnsFalse() {
        NoopVariables v = new NoopVariables();

        assertThat(v.has("anything")).isFalse();
    }

    @Test
    void toMapReturnsEmptyMap() {
        NoopVariables v = new NoopVariables();

        assertThat(v.toMap()).isEmpty();
    }

    @Test
    void setIsNotSupported() {
        NoopVariables v = new NoopVariables();

        assertThatThrownBy(() -> v.set("k", "v"))
                .isInstanceOf(IllegalStateException.class);
    }
}
