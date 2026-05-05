package com.walmartlabs.concord.runtime.v2.sdk;

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

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DockerContainerSpecTest {

    @Test
    public void testDefaults() {
        var spec = DockerContainerSpec.builder()
                .image("ubuntu:22.04")
                .build();

        assertEquals("ubuntu:22.04", spec.image());
        assertEquals(3, spec.pullRetryCount());
        assertEquals(10_000, spec.pullRetryInterval());
        assertEquals(false, spec.debug());
        assertEquals(true, spec.forcePull());
        assertEquals(true, spec.redirectErrorStream());
        assertNull(spec.name());
        assertNull(spec.options());
    }

    @Test
    public void testFullyPopulated() {
        var options = DockerContainerSpec.Options.builder()
                .hosts(List.of("a:1.2.3.4"))
                .build();

        var spec = DockerContainerSpec.builder()
                .image("img")
                .name("n")
                .user("u")
                .workdir("/w")
                .entryPoint("/bin/sh")
                .cpu("2")
                .memory("512m")
                .stdOutFilePath("/tmp/out")
                .args(List.of("-c", "echo"))
                .env(java.util.Map.of("X", "1"))
                .envFile("/tmp/env")
                .labels(java.util.Map.of("k", "v"))
                .options(options)
                .pullRetryCount(7)
                .pullRetryInterval(123)
                .debug(true)
                .forcePull(false)
                .redirectErrorStream(false)
                .build();

        assertEquals("img", spec.image());
        assertEquals("n", spec.name());
        assertEquals("u", spec.user());
        assertEquals("/w", spec.workdir());
        assertEquals("/bin/sh", spec.entryPoint());
        assertEquals("2", spec.cpu());
        assertEquals("512m", spec.memory());
        assertEquals("/tmp/out", spec.stdOutFilePath());
        assertEquals(List.of("-c", "echo"), spec.args());
        assertEquals("1", spec.env().get("X"));
        assertEquals("/tmp/env", spec.envFile());
        assertEquals("v", spec.labels().get("k"));
        assertEquals(7, spec.pullRetryCount());
        assertEquals(123, spec.pullRetryInterval());
        assertEquals(true, spec.debug());
        assertEquals(false, spec.forcePull());
        assertEquals(false, spec.redirectErrorStream());
        assertEquals(options, spec.options());
    }

    @Test
    public void testMissingImageThrows() {
        var b = DockerContainerSpec.builder();

        assertThrows(IllegalStateException.class, b::build);
    }

    @Test
    public void testOptionsFromNullMap() {
        var options = DockerContainerSpec.Options.from(null);

        assertNull(options.hosts());
    }

    @Test
    public void testOptionsFromMissingHosts() {
        var options = DockerContainerSpec.Options.from(new HashMap<>());

        assertNull(options.hosts());
    }

    @Test
    public void testOptionsFromIterableHosts() {
        var options = DockerContainerSpec.Options.from(java.util.Map.of("hosts", List.of("a:1.2.3.4")));

        assertEquals(List.of("a:1.2.3.4"), options.hosts());
    }

    @Test
    public void testOptionsFromInvalidHostsType() {
        var ex = assertThrows(IllegalArgumentException.class,
                () -> DockerContainerSpec.Options.from(java.util.Map.of("hosts", "single-host")));

        assertTrue(ex.getMessage().contains("Unexpected 'hosts' value"));
    }
}
