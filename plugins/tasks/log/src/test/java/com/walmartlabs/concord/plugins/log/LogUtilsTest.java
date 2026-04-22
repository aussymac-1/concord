package com.walmartlabs.concord.plugins.log;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2025 Walmart Inc.
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LogUtilsTest {

    @Test
    public void staticLoggerIsInitialized() {
        assertNotNull(LogUtils.log);
    }

    @Test
    public void allLevelsAcceptStringsWithoutThrowing() {
        assertDoesNotThrow(() -> {
            LogUtils.debug("debug");
            LogUtils.info("info");
            LogUtils.warn("warn");
            LogUtils.error("error");
        });
    }

    @Test
    public void allLevelsAcceptObjectsWithoutThrowing() {
        assertDoesNotThrow(() -> {
            LogUtils.debug((Object) 1);
            LogUtils.info((Object) 2);
            LogUtils.warn((Object) 3L);
            LogUtils.error((Object) 4.0);
        });
    }

    @Test
    public void allLevelsAcceptNullObjectWithoutThrowing() {
        assertDoesNotThrow(() -> {
            LogUtils.debug((Object) null);
            LogUtils.info((Object) null);
            LogUtils.warn((Object) null);
            LogUtils.error((Object) null);
        });
    }
}
