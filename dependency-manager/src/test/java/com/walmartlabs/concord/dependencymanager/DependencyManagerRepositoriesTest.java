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
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DependencyManagerRepositoriesTest {

    @Test
    public void testGetReturnsDefaultMavenCentralWhenNoEnv() {
        var repos = DependencyManagerRepositories.get();
        assertNotNull(repos);
        assertFalse(repos.isEmpty());
        var first = repos.get(0);
        assertEquals("central", first.id());
        assertTrue(first.url().startsWith("https://"));
        assertFalse(first.snapshotPolicy().enabled());
    }

    @Test
    public void testGetMissingFileReturnsDefault(@TempDir Path tmp) {
        var repos = DependencyManagerRepositories.get(tmp.resolve("missing.json"));
        assertEquals("central", repos.get(0).id());
    }

    @Test
    public void testGetReadsCustomConfig(@TempDir Path tmp) throws Exception {
        var cfg = tmp.resolve("repos.json");
        Files.writeString(cfg, """
                {
                  "repositories": [
                    {"id": "internal", "url": "https://internal.example.com/maven"}
                  ]
                }
                """);

        var repos = DependencyManagerRepositories.get(cfg);
        assertEquals(1, repos.size());
        assertEquals("internal", repos.get(0).id());
        assertEquals("https://internal.example.com/maven", repos.get(0).url());
    }

    @Test
    public void testGetMalformedConfigThrows(@TempDir Path tmp) throws Exception {
        var cfg = tmp.resolve("bad.json");
        Files.writeString(cfg, "this-is-not-json");

        assertThrows(RuntimeException.class, () -> DependencyManagerRepositories.get(cfg));
    }
}
