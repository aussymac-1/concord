package com.walmartlabs.concord.plugins.log;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2020 Walmart Inc.
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

import static org.junit.jupiter.api.Assertions.*;

class LogUtilsTest {

    @Test
    void testErrorString() {
        assertDoesNotThrow(() -> LogUtils.error("test error message"));
    }

    @Test
    void testErrorObject() {
        assertDoesNotThrow(() -> LogUtils.error((Object) 42));
    }

    @Test
    void testWarnString() {
        assertDoesNotThrow(() -> LogUtils.warn("test warn message"));
    }

    @Test
    void testWarnObject() {
        assertDoesNotThrow(() -> LogUtils.warn((Object) "warn-obj"));
    }

    @Test
    void testDebugString() {
        assertDoesNotThrow(() -> LogUtils.debug("test debug message"));
    }

    @Test
    void testDebugObject() {
        assertDoesNotThrow(() -> LogUtils.debug((Object) null));
    }

    @Test
    void testInfoString() {
        assertDoesNotThrow(() -> LogUtils.info("test info message"));
    }

    @Test
    void testInfoObject() {
        assertDoesNotThrow(() -> LogUtils.info((Object) "info-obj"));
    }

    @Test
    void testLoggerNotNull() {
        assertNotNull(LogUtils.log);
    }
}
