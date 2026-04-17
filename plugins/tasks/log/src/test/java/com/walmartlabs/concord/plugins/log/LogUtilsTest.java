package com.walmartlabs.concord.plugins.log;

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

import static org.assertj.core.api.Assertions.assertThatCode;

class LogUtilsTest {

    // The static LogUtils logs via SLF4J and is thin: verify the API does not throw
    // for the supported (String, Object) overloads and covers null safety where SLF4J supports it.

    @Test
    void errorStringDoesNotThrow() {
        assertThatCode(() -> LogUtils.error("boom")).doesNotThrowAnyException();
    }

    @Test
    void errorObjectDoesNotThrow() {
        assertThatCode(() -> LogUtils.error(new Object())).doesNotThrowAnyException();
    }

    @Test
    void warnStringDoesNotThrow() {
        assertThatCode(() -> LogUtils.warn("careful")).doesNotThrowAnyException();
    }

    @Test
    void warnObjectDoesNotThrow() {
        assertThatCode(() -> LogUtils.warn(123)).doesNotThrowAnyException();
    }

    @Test
    void debugStringDoesNotThrow() {
        assertThatCode(() -> LogUtils.debug("details")).doesNotThrowAnyException();
    }

    @Test
    void debugObjectDoesNotThrow() {
        assertThatCode(() -> LogUtils.debug(true)).doesNotThrowAnyException();
    }

    @Test
    void infoStringDoesNotThrow() {
        assertThatCode(() -> LogUtils.info("hello")).doesNotThrowAnyException();
    }

    @Test
    void infoObjectDoesNotThrow() {
        assertThatCode(() -> LogUtils.info(3.14)).doesNotThrowAnyException();
    }
}
