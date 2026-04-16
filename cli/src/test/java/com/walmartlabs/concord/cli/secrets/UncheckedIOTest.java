package com.walmartlabs.concord.cli.secrets;

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

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UncheckedIOTest {

    @TempDir
    Path tempDir;

    @Test
    void writeAndReadBytes() {
        Path file = tempDir.resolve("test.txt");
        UncheckedIO.write(file, "hello".getBytes());
        byte[] result = UncheckedIO.readAllBytes(file);
        assertThat(result).isEqualTo("hello".getBytes());
    }

    @Test
    void createTempFileInDirectory() {
        Path file = UncheckedIO.createTempFile(tempDir, "prefix", ".tmp");
        assertThat(file).exists();
        assertThat(file.getFileName().toString()).startsWith("prefix").endsWith(".tmp");
    }

    @Test
    void copyFile() throws Exception {
        Path source = tempDir.resolve("source.txt");
        Files.writeString(source, "content");
        Path target = tempDir.resolve("target.txt");

        UncheckedIO.copy(source, target);
        assertThat(target).exists();
        assertThat(Files.readString(target)).isEqualTo("content");
    }

    @Test
    void assertTmpDirCreatesDirectoryIfNotExists() {
        Path result = UncheckedIO.assertTmpDir(tempDir);
        assertThat(result).exists().isDirectory();
        assertThat(result.getParent().getFileName().toString()).isEqualTo("target");
    }

    @Test
    void assertTmpDirReturnsExistingDirectory() throws Exception {
        Path targetDir = tempDir.resolve("target").resolve(".concord_tmp");
        Files.createDirectories(targetDir);

        Path result = UncheckedIO.assertTmpDir(tempDir);
        assertThat(result).exists().isDirectory();
    }

    @Test
    void readAllBytesThrowsRuntimeOnMissingFile() {
        Path missing = tempDir.resolve("nonexistent.txt");
        assertThatThrownBy(() -> UncheckedIO.readAllBytes(missing))
                .isInstanceOf(RuntimeException.class);
    }
}
