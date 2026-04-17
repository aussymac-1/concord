package com.walmartlabs.concord.process.loader;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2026 Walmart Inc.
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

import static org.assertj.core.api.Assertions.assertThat;

class ProjectLoaderUtilsTest {

    @TempDir
    Path tempDir;

    @Test
    void getRuntimeTypeFromConcordYml() throws IOException {
        Path concordYml = tempDir.resolve("concord.yml");
        Files.writeString(concordYml, "configuration:\n  runtime: concord-v2\n");

        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertThat(result).isPresent().contains("concord-v2");
    }

    @Test
    void getRuntimeTypeFromDotConcordYml() throws IOException {
        Path concordYml = tempDir.resolve(".concord.yml");
        Files.writeString(concordYml, "configuration:\n  runtime: concord-v1\n");

        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertThat(result).isPresent().contains("concord-v1");
    }

    @Test
    void getRuntimeTypeReturnsEmptyWhenNoFile() throws IOException {
        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertThat(result).isEmpty();
    }

    @Test
    void getRuntimeTypeReturnsEmptyWhenNoRuntimeKey() throws IOException {
        Path concordYml = tempDir.resolve("concord.yml");
        Files.writeString(concordYml, "flows:\n  default:\n    - log: hello\n");

        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertThat(result).isEmpty();
    }

    @Test
    void getRuntimeTypeReturnsEmptyWhenNoConfigurationKey() throws IOException {
        Path concordYml = tempDir.resolve("concord.yml");
        Files.writeString(concordYml, "other: value\n");

        Optional<String> result = ProjectLoaderUtils.getRuntimeType(tempDir);
        assertThat(result).isEmpty();
    }

    @Test
    void isConcordFileExistsReturnsTrueWhenPresent() throws IOException {
        Files.createFile(tempDir.resolve("concord.yml"));
        assertThat(ProjectLoaderUtils.isConcordFileExists(tempDir)).isTrue();
    }

    @Test
    void isConcordFileExistsReturnsTrueForDotYaml() throws IOException {
        Files.createFile(tempDir.resolve(".concord.yaml"));
        assertThat(ProjectLoaderUtils.isConcordFileExists(tempDir)).isTrue();
    }

    @Test
    void isConcordFileExistsReturnsFalseWhenNone() {
        assertThat(ProjectLoaderUtils.isConcordFileExists(tempDir)).isFalse();
    }
}
