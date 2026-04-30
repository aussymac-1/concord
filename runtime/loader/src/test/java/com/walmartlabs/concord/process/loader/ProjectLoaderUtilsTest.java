package com.walmartlabs.concord.process.loader;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2024 Walmart Inc.
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

import static org.junit.jupiter.api.Assertions.*;

public class ProjectLoaderUtilsTest {

    @Test
    public void testIsConcordFileExistsWithYml(@TempDir Path tmpDir) throws IOException {
        Files.writeString(tmpDir.resolve("concord.yml"), "configuration:\n  runtime: concord-v2\n");
        assertTrue(ProjectLoaderUtils.isConcordFileExists(tmpDir));
    }

    @Test
    public void testIsConcordFileExistsWithYaml(@TempDir Path tmpDir) throws IOException {
        Files.writeString(tmpDir.resolve("concord.yaml"), "configuration:\n  runtime: concord-v2\n");
        assertTrue(ProjectLoaderUtils.isConcordFileExists(tmpDir));
    }

    @Test
    public void testIsConcordFileExistsWithDotPrefix(@TempDir Path tmpDir) throws IOException {
        Files.writeString(tmpDir.resolve(".concord.yml"), "configuration:\n  runtime: concord-v2\n");
        assertTrue(ProjectLoaderUtils.isConcordFileExists(tmpDir));
    }

    @Test
    public void testIsConcordFileExistsFalse(@TempDir Path tmpDir) {
        assertFalse(ProjectLoaderUtils.isConcordFileExists(tmpDir));
    }

    @Test
    public void testGetRuntimeTypeV2(@TempDir Path tmpDir) throws IOException {
        Files.writeString(tmpDir.resolve("concord.yml"), "configuration:\n  runtime: concord-v2\n");
        var result = ProjectLoaderUtils.getRuntimeType(tmpDir);
        assertTrue(result.isPresent());
        assertEquals("concord-v2", result.get());
    }

    @Test
    public void testGetRuntimeTypeV1(@TempDir Path tmpDir) throws IOException {
        Files.writeString(tmpDir.resolve("concord.yml"), "configuration:\n  runtime: concord-v1\n");
        var result = ProjectLoaderUtils.getRuntimeType(tmpDir);
        assertTrue(result.isPresent());
        assertEquals("concord-v1", result.get());
    }

    @Test
    public void testGetRuntimeTypeNoFile(@TempDir Path tmpDir) throws IOException {
        var result = ProjectLoaderUtils.getRuntimeType(tmpDir);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetRuntimeTypeNoConfiguration(@TempDir Path tmpDir) throws IOException {
        Files.writeString(tmpDir.resolve("concord.yml"), "flows:\n  default:\n    - log: hello\n");
        var result = ProjectLoaderUtils.getRuntimeType(tmpDir);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetRuntimeTypeNoRuntimeKey(@TempDir Path tmpDir) throws IOException {
        Files.writeString(tmpDir.resolve("concord.yml"), "configuration:\n  debug: true\n");
        var result = ProjectLoaderUtils.getRuntimeType(tmpDir);
        assertFalse(result.isPresent());
    }
}
