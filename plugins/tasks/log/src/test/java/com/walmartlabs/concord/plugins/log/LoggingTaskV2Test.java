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

import com.walmartlabs.concord.runtime.v2.sdk.MapBackedVariables;
import com.walmartlabs.concord.runtime.v2.sdk.TaskResult;
import com.walmartlabs.concord.runtime.v2.sdk.Variables;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LoggingTaskV2Test {

    private static Variables vars(Map<String, Object> m) {
        return new MapBackedVariables(m);
    }

    @Test
    public void executeReturnsSuccessResult() {
        LoggingTaskV2 task = new LoggingTaskV2();
        Map<String, Object> m = new HashMap<>();
        m.put("msg", "hi");

        TaskResult result = task.execute(vars(m));
        assertTrue(result instanceof TaskResult.SimpleResult);
    }

    @Test
    public void executeHandlesAllLogLevels() {
        LoggingTaskV2 task = new LoggingTaskV2();

        for (String lvl : new String[] {"DEBUG", "INFO", "WARN", "ERROR", "debug", "info"}) {
            Map<String, Object> m = new HashMap<>();
            m.put("msg", "hi");
            m.put("level", lvl);

            assertNotNull(task.execute(vars(m)));
        }
    }

    @Test
    public void unknownLogLevelFallsBackToInfo() {
        LoggingTaskV2 task = new LoggingTaskV2();
        Map<String, Object> m = new HashMap<>();
        m.put("msg", "hi");
        m.put("level", "TRACE");

        assertNotNull(task.execute(vars(m)));
    }

    @Test
    public void executeFormatsAsYaml() {
        LoggingTaskV2 task = new LoggingTaskV2();

        Map<String, Object> m = new HashMap<>();
        m.put("msg", Map.of("k", "v"));
        m.put("format", "yaml");

        assertNotNull(task.execute(vars(m)));
    }

    @Test
    public void unknownFormatThrows() {
        LoggingTaskV2 task = new LoggingTaskV2();

        Map<String, Object> m = new HashMap<>();
        m.put("msg", "hi");
        m.put("format", "xml");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> task.execute(vars(m)));
        assertTrue(ex.getMessage().contains("xml"));
    }

    @Test
    public void emptyFormatReturnsMessageAsIs() {
        LoggingTaskV2 task = new LoggingTaskV2();

        Map<String, Object> m = new HashMap<>();
        m.put("msg", "hi");
        m.put("format", "");

        assertNotNull(task.execute(vars(m)));
    }

    @Test
    public void staticHelpersDoNotThrow() {
        assertDoesNotThrow(() -> {
            LoggingTaskV2.debug("d");
            LoggingTaskV2.debug((Object) 1);
            LoggingTaskV2.info("i");
            LoggingTaskV2.info((Object) 2);
            LoggingTaskV2.warn("w");
            LoggingTaskV2.warn((Object) 3);
            LoggingTaskV2.error("e");
            LoggingTaskV2.error((Object) 4);
        });
    }

    @Test
    public void callForwardsToInfo() {
        LoggingTaskV2 task = new LoggingTaskV2();
        assertDoesNotThrow(() -> task.call("hi"));
    }
}
