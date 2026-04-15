package com.walmartlabs.concord.process.loader;

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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectLoaderUtilsTest {

    @Test
    void testIsConcordFileExistsWithConcordYml(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("concord.yml"), "configuration:\n  runtime: concord-v2");
        assertTrue(ProjectLoaderUtils.isConcordFileExists(tempDir));
    }

    @Test
    void testIsConcordFileExistsWithDotConcordYml(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve(".concord.yml"), "configuration:\n  runtime: concord-v2");
        assertTrue(ProjectLoaderUtils.isConcordFileExists(tempDir));
    }

    @Test
    void testIsConcordFileExistsWithConcordYaml(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("concord.yaml"), "configuration:\n  runtime: concord-v2");
        assertTrue(ProjectLoaderUtils.isConcordFileExists(tempDir));
    }

    @Test
    void testIsConcordFileExistsWithDotConcordYaml(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve(".concord.yaml"), "configuration:\n  runtime: concord-v2");
        assertTrue(ProjectLoaderUtils.isConcordFileExists(tempDir));
    }

    @Test
    void testIsConcordFileExistsNone(@TempDir Path tempDir) {
        assertFalse(ProjectLoaderUtils.isConcordFileExists(tempDir));
    }

    @Test
    void testGetRuntimeTypePresent(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("concord.yml"), "configuration:\n  runtime: concord-v2");
        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertTrue(result.isPresent());
        assertEquals("concord-v2", result.get());
    }

    @Test
    void testGetRuntimeTypeMissing(@TempDir Path tempDir) throws IOException {
        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetRuntimeTypeNoConfiguration(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("concord.yml"), "flows:\n  default:\n    - log: hello");
        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetRuntimeTypeNoRuntime(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("concord.yml"), "configuration:\n  entryPoint: main");
        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertFalse(result.isPresent());
    }
}
