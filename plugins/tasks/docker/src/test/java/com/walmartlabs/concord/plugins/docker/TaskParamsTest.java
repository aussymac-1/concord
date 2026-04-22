package com.walmartlabs.concord.plugins.docker;

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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TaskParamsTest {

    private static TaskParams from(Map<String, Object> m) {
        return new TaskParams(m);
    }

    @Test
    public void imageIsRequired() {
        TaskParams p = from(new HashMap<>());
        assertThrows(RuntimeException.class, p::image);
    }

    @Test
    public void imageReturnsProvidedValue() {
        Map<String, Object> m = new HashMap<>();
        m.put("image", "alpine:3");

        assertEquals("alpine:3", from(m).image());
    }

    @Test
    public void defaultsForOptionalFields() {
        TaskParams p = from(new HashMap<>());

        assertNull(p.cmd());
        assertNull(p.env());
        assertNull(p.envFile());
        assertNull(p.hosts());

        // forcePull defaults to true
        assertTrue(p.forcePull());

        assertFalse(p.debug(false));
        assertTrue(p.debug(true));

        assertEquals(3, p.pullRetryCount());
        assertEquals(10_000L, p.pullRetryInterval());
    }

    @Test
    public void overridesAreHonored() {
        Map<String, Object> m = new HashMap<>();
        m.put("image", "alpine");
        m.put("cmd", "sh -c 'echo hi'");
        m.put("env", Map.of("A", "1"));
        m.put("envFile", "env.txt");
        m.put("hosts", List.of("host1", "host2"));
        m.put("forcePull", false);
        m.put("debug", true);
        m.put("pullRetryCount", 5);
        m.put("pullRetryInterval", 1234L);

        TaskParams p = from(m);

        assertEquals("sh -c 'echo hi'", p.cmd());
        assertEquals(Map.of("A", "1"), p.env());
        assertEquals("env.txt", p.envFile());

        Collection<String> hosts = p.hosts();
        assertNotNull(hosts);
        assertIterableEquals(List.of("host1", "host2"), hosts);

        assertFalse(p.forcePull());
        assertTrue(p.debug(false));
        assertEquals(5, p.pullRetryCount());
        assertEquals(1234L, p.pullRetryInterval());
    }
}
