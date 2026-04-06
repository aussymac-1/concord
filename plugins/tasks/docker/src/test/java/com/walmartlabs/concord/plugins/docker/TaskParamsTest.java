package com.walmartlabs.concord.plugins.docker;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2023 Walmart Inc.
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TaskParamsTest {

    @Test
    void testImage() {
        var params = new TaskParams(Map.of("image", "alpine:latest"));
        assertEquals("alpine:latest", params.image());
    }

    @Test
    void testImageMissing() {
        var params = new TaskParams(Map.<String, Object>of());
        assertThrows(IllegalArgumentException.class, params::image);
    }

    @Test
    void testCmd() {
        var params = new TaskParams(Map.of("cmd", "echo hello"));
        assertEquals("echo hello", params.cmd());
    }

    @Test
    void testCmdDefault() {
        var params = new TaskParams(Map.<String, Object>of());
        assertNull(params.cmd());
    }

    @Test
    void testEnv() {
        var m = new HashMap<String, Object>();
        m.put("env", Map.of("FOO", "bar"));
        var params = new TaskParams(m);
        var env = params.env();
        assertNotNull(env);
        assertEquals("bar", env.get("FOO"));
    }

    @Test
    void testEnvFile() {
        var params = new TaskParams(Map.of("envFile", "/tmp/env"));
        assertEquals("/tmp/env", params.envFile());
    }

    @Test
    void testHosts() {
        var m = new HashMap<String, Object>();
        m.put("hosts", List.of("host1:1.2.3.4", "host2:5.6.7.8"));
        var params = new TaskParams(m);
        var hosts = params.hosts();
        assertNotNull(hosts);
        assertEquals(2, hosts.size());
    }

    @Test
    void testForcePullDefault() {
        var params = new TaskParams(Map.<String, Object>of());
        assertTrue(params.forcePull());
    }

    @Test
    void testForcePullFalse() {
        var params = new TaskParams(Map.of("forcePull", false));
        assertFalse(params.forcePull());
    }

    @Test
    void testDebugDefault() {
        var params = new TaskParams(Map.<String, Object>of());
        assertFalse(params.debug(false));
        assertTrue(params.debug(true));
    }

    @Test
    void testPullRetryCountDefault() {
        var params = new TaskParams(Map.<String, Object>of());
        assertEquals(3, params.pullRetryCount());
    }

    @Test
    void testPullRetryCountCustom() {
        var params = new TaskParams(Map.of("pullRetryCount", 5));
        assertEquals(5, params.pullRetryCount());
    }

    @Test
    void testPullRetryIntervalDefault() {
        var params = new TaskParams(Map.<String, Object>of());
        assertEquals(10_000L, params.pullRetryInterval());
    }

    @Test
    void testPullRetryIntervalCustom() {
        var params = new TaskParams(Map.of("pullRetryInterval", 30_000L));
        assertEquals(30_000L, params.pullRetryInterval());
    }
}
