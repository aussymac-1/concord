package com.walmartlabs.concord.plugins.throwex;

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

import java.io.Serializable;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class ConcordExceptionTest {

    @Test
    void singleArgConstructorHasNullPayload() {
        ConcordException ex = new ConcordException("broken");

        assertThat(ex.getMessage()).isEqualTo("broken");
        assertThat(ex.getPayload()).isNull();
    }

    @Test
    void payloadConstructorRetainsPayload() {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("code", "42");

        ConcordException ex = new ConcordException("broken", payload);

        assertThat(ex.getMessage()).isEqualTo("broken");
        assertThat(ex.getPayload()).isEqualTo(payload);
    }

    @Test
    void isCheckedException() {
        assertThat(Exception.class).isAssignableFrom(ConcordException.class);
    }

    @Test
    void stringPayloadIsSerializable() {
        Serializable payload = "the-value";

        ConcordException ex = new ConcordException("msg", payload);

        assertThat(ex.getPayload()).isEqualTo("the-value");
    }
}
