package com.walmartlabs.concord.sdk;

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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DockerContainerSpecTest {

    @Test
    public void testDefaults() {
        var spec = DockerContainerSpec.builder().image("alpine").build();
        assertEquals("alpine", spec.image());
        assertEquals(3, spec.pullRetryCount());
        assertEquals(10_000L, spec.pullRetryInterval());
        assertFalse(spec.debug());
        assertTrue(spec.forcePull());
        assertTrue(spec.redirectErrorStream());
        assertNull(spec.options());
    }

    @Test
    public void testOptionsFromNullReturnsEmpty() {
        var opts = DockerContainerSpec.Options.from(null);
        assertNull(opts.hosts());
    }

    @Test
    public void testOptionsFromMissingHostsReturnsEmpty() {
        var opts = DockerContainerSpec.Options.from(Map.of("other", "x"));
        assertNull(opts.hosts());
    }

    @Test
    public void testOptionsFromValidHosts() {
        var opts = DockerContainerSpec.Options.from(Map.of("hosts", List.of("a:1", "b:2")));
        assertEquals(List.of("a:1", "b:2"), opts.hosts());
    }

    @Test
    public void testOptionsFromInvalidHostsThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> DockerContainerSpec.Options.from(Map.of("hosts", "not-iterable")));
    }
}
