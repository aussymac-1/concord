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

public class LoggingTaskTest {

    @Test
    public void allLevelsLogWithoutThrowing() {
        LoggingTask task = new LoggingTask();

        assertDoesNotThrow(() -> {
            task.debug("d");
            task.info("i");
            task.info("scope", "msg");
            task.warn("w");
            task.warn("scope", "msg");
            task.error("e");
            task.error("scope", "msg");
            task.call("hello");
        });
    }

    @Test
    public void logDebugTaskLogsWithoutThrowing() {
        LogDebugTask task = new LogDebugTask();
        assertDoesNotThrow(() -> task.call("d"));
    }

    @Test
    public void logErrorTaskLogsWithoutThrowing() {
        LogErrorTask task = new LogErrorTask();
        assertDoesNotThrow(() -> task.call("e"));
    }

    @Test
    public void logWarnTaskLogsWithoutThrowing() {
        LogWarnTask task = new LogWarnTask();
        assertDoesNotThrow(() -> task.call("w"));
    }
}
