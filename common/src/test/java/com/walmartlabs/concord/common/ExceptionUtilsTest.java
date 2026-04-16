package com.walmartlabs.concord.common;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionUtilsTest {

    @Test
    void getExceptionListReturnsSingleExceptionWhenNoCause() {
        RuntimeException ex = new RuntimeException("test");
        List<Throwable> result = ExceptionUtils.getExceptionList(ex);
        assertThat(result).containsExactly(ex);
    }

    @Test
    void getExceptionListReturnsChain() {
        Exception root = new Exception("root");
        RuntimeException wrapper = new RuntimeException("wrapper", root);
        List<Throwable> result = ExceptionUtils.getExceptionList(wrapper);
        assertThat(result).containsExactly(wrapper, root);
    }

    @Test
    void getExceptionListHandlesNullInput() {
        List<Throwable> result = ExceptionUtils.getExceptionList(null);
        assertThat(result).isEmpty();
    }

    @Test
    void findLastExceptionReturnsLastMatchInChain() {
        RuntimeException root = new RuntimeException("root");
        RuntimeException middle = new RuntimeException("middle", root);
        RuntimeException top = new RuntimeException("top", middle);

        RuntimeException result = ExceptionUtils.findLastException(top, RuntimeException.class);
        assertThat(result).isEqualTo(root);
    }

    @Test
    void findLastExceptionReturnsSelfWhenNoMatch() {
        IllegalArgumentException top = new IllegalArgumentException("top",
                new NullPointerException("cause"));

        IllegalArgumentException result = ExceptionUtils.findLastException(top, IllegalArgumentException.class);
        assertThat(result).isEqualTo(top);
    }
}
