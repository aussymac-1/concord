package com.walmartlabs.concord.process.loader;

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

class UnsupportedRuntimeTypeExceptionTest {

    @Test
    void messageContainsRuntimeType() {
        UnsupportedRuntimeTypeException ex = new UnsupportedRuntimeTypeException("concord-v3");
        assertThat(ex.getMessage()).isEqualTo("Unsupported runtime type: concord-v3");
    }

    @Test
    void isCheckedException() {
        assertThat(new UnsupportedRuntimeTypeException("test"))
                .isInstanceOf(Exception.class);
    }
}
