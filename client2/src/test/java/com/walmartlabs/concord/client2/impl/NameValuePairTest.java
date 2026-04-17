package com.walmartlabs.concord.client2.impl;

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

class NameValuePairTest {

    @Test
    void constructorAndGetters() {
        NameValuePair pair = new NameValuePair("key", "value");
        assertThat(pair.getName()).isEqualTo("key");
        assertThat(pair.getValue()).isEqualTo("value");
    }

    @Test
    void equalPairsAreEqual() {
        NameValuePair a = new NameValuePair("k", "v");
        NameValuePair b = new NameValuePair("k", "v");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void unequalPairsAreNotEqual() {
        NameValuePair a = new NameValuePair("k", "v1");
        NameValuePair b = new NameValuePair("k", "v2");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void notEqualToNull() {
        NameValuePair pair = new NameValuePair("k", "v");
        assertThat(pair).isNotEqualTo(null);
    }

    @Test
    void notEqualToDifferentType() {
        NameValuePair pair = new NameValuePair("k", "v");
        assertThat(pair).isNotEqualTo("string");
    }

    @Test
    void equalToSelf() {
        NameValuePair pair = new NameValuePair("k", "v");
        assertThat(pair).isEqualTo(pair);
    }

    @Test
    void differentNameNotEqual() {
        NameValuePair a = new NameValuePair("k1", "v");
        NameValuePair b = new NameValuePair("k2", "v");
        assertThat(a).isNotEqualTo(b);
    }
}
