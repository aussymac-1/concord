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
    public void testIsConcordFileExistsTrue(@TempDir Path tempDir) throws IOException {
        Files.createFile(tempDir.resolve("concord.yml"));
        assertTrue(ProjectLoaderUtils.isConcordFileExists(tempDir));
    }

    @Test
    public void testIsConcordFileExistsDotYml(@TempDir Path tempDir) throws IOException {
        Files.createFile(tempDir.resolve(".concord.yml"));
        assertTrue(ProjectLoaderUtils.isConcordFileExists(tempDir));
    }

    @Test
    public void testIsConcordFileExistsYaml(@TempDir Path tempDir) throws IOException {
        Files.createFile(tempDir.resolve("concord.yaml"));
        assertTrue(ProjectLoaderUtils.isConcordFileExists(tempDir));
    }

    @Test
    public void testIsConcordFileExistsFalse(@TempDir Path tempDir) {
        assertFalse(ProjectLoaderUtils.isConcordFileExists(tempDir));
    }

    @Test
    public void testGetRuntimeTypeNoConcordFile(@TempDir Path tempDir) throws IOException {
        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetRuntimeTypeV2(@TempDir Path tempDir) throws IOException {
        String content = "configuration:\n  runtime: concord-v2\n";
        Files.writeString(tempDir.resolve("concord.yml"), content);

        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertTrue(result.isPresent());
        assertEquals("concord-v2", result.get());
    }

    @Test
    public void testGetRuntimeTypeV1(@TempDir Path tempDir) throws IOException {
        String content = "configuration:\n  runtime: concord-v1\n";
        Files.writeString(tempDir.resolve("concord.yml"), content);

        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertTrue(result.isPresent());
        assertEquals("concord-v1", result.get());
    }

    @Test
    public void testGetRuntimeTypeNoConfiguration(@TempDir Path tempDir) throws IOException {
        String content = "flows:\n  default:\n    - log: hello\n";
        Files.writeString(tempDir.resolve("concord.yml"), content);

        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetRuntimeTypeNoRuntime(@TempDir Path tempDir) throws IOException {
        String content = "configuration:\n  entryPoint: main\n";
        Files.writeString(tempDir.resolve("concord.yml"), content);

        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertFalse(result.isPresent());
    }
}
