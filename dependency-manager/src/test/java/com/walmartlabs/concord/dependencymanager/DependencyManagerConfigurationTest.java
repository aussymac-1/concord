package com.walmartlabs.concord.dependencymanager;

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

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DependencyManagerConfigurationTest {

    @Test
    public void testOfCacheDirOnly() {
        var p = Path.of("/tmp/cache");
        var c = DependencyManagerConfiguration.of(p);
        assertEquals(p, c.cacheDir());
        assertFalse(c.strictRepositories());
        assertFalse(c.explicitlyResolveV1Client());
        assertFalse(c.offlineMode());
        assertNotNull(c.repositories());
        assertTrue(c.exclusions().isEmpty());
    }

    @Test
    public void testOfCacheDirAndRepositories() {
        var p = Path.of("/tmp/cache");
        var repo = MavenRepository.builder().id("r").url("u").build();
        var c = DependencyManagerConfiguration.of(p, List.of(repo));
        assertEquals(List.of(repo), c.repositories());
    }

    @Test
    public void testFullBuilder() {
        var p = Path.of("/tmp/cache");
        var c = DependencyManagerConfiguration.builder()
                .cacheDir(p)
                .strictRepositories(true)
                .explicitlyResolveV1Client(true)
                .offlineMode(true)
                .exclusions(List.of("a:b"))
                .build();

        assertTrue(c.strictRepositories());
        assertTrue(c.explicitlyResolveV1Client());
        assertTrue(c.offlineMode());
        assertEquals(List.of("a:b"), c.exclusions());
    }
}
