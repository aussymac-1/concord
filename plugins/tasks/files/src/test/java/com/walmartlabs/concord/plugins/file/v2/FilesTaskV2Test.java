package com.walmartlabs.concord.plugins.file.v2;

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

import com.walmartlabs.concord.runtime.v2.sdk.ApiConfiguration;
import com.walmartlabs.concord.runtime.v2.sdk.Compiler;
import com.walmartlabs.concord.runtime.v2.sdk.Context;
import com.walmartlabs.concord.runtime.v2.sdk.DockerService;
import com.walmartlabs.concord.runtime.v2.sdk.Execution;
import com.walmartlabs.concord.runtime.v2.sdk.FileService;
import com.walmartlabs.concord.runtime.v2.sdk.LockService;
import com.walmartlabs.concord.runtime.v2.sdk.ProcessConfiguration;
import com.walmartlabs.concord.runtime.v2.sdk.SecretService;
import com.walmartlabs.concord.runtime.v2.sdk.Variables;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FilesTaskV2Test {

    @Test
    public void existsReturnsTrueForExistingFile(@TempDir Path workDir) throws IOException {
        Path f = Files.createFile(workDir.resolve("a.txt"));

        FilesTaskV2 task = new FilesTaskV2(new StubContext(workDir));

        assertTrue(task.exists("a.txt"));
        assertFalse(task.notExists(f.getFileName().toString()));
    }

    @Test
    public void existsReturnsFalseForMissingFile(@TempDir Path workDir) {
        FilesTaskV2 task = new FilesTaskV2(new StubContext(workDir));

        assertFalse(task.exists("missing.txt"));
        assertTrue(task.notExists("missing.txt"));
    }

    @Test
    public void moveFileMovesIntoTargetDirWithRelativeResult(@TempDir Path workDir) throws IOException {
        Files.createFile(workDir.resolve("src.txt"));

        FilesTaskV2 task = new FilesTaskV2(new StubContext(workDir));

        String result = task.moveFile("src.txt", "sub/dir");

        assertEquals("sub/dir/src.txt", result.replace('\\', '/'));
        assertTrue(Files.exists(workDir.resolve("sub/dir/src.txt")));
        assertFalse(Files.exists(workDir.resolve("src.txt")));
    }

    @Test
    public void moveFileReplacesExisting(@TempDir Path workDir) throws IOException {
        Files.createFile(workDir.resolve("src.txt"));
        Path destDir = Files.createDirectories(workDir.resolve("out"));
        Files.writeString(destDir.resolve("src.txt"), "old");

        FilesTaskV2 task = new FilesTaskV2(new StubContext(workDir));

        String result = task.moveFile("src.txt", "out");

        assertEquals("out/src.txt", result.replace('\\', '/'));
        assertEquals(0L, Files.size(workDir.resolve("out/src.txt")));
    }

    @Test
    public void relativizeProducesRelativePath(@TempDir Path workDir) {
        FilesTaskV2 task = new FilesTaskV2(new StubContext(workDir));

        String rel = task.relativize("a/b", "a/b/c/d.txt").replace('\\', '/');

        assertEquals("c/d.txt", rel);
    }

    @Test
    public void rejectsPathTraversalOutsideWorkingDirectory(@TempDir Path workDir) {
        FilesTaskV2 task = new FilesTaskV2(new StubContext(workDir));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> task.exists("../outside"));
        assertTrue(ex.getMessage().contains("within the working directory"));
    }

    private static final class StubContext implements Context {

        private final Path workingDirectory;

        StubContext(Path workingDirectory) {
            this.workingDirectory = workingDirectory;
        }

        @Override public Path workingDirectory() { return workingDirectory; }

        @Override public UUID processInstanceId() { throw new UnsupportedOperationException(); }
        @Override public Variables variables() { throw new UnsupportedOperationException(); }
        @Override public Variables defaultVariables() { throw new UnsupportedOperationException(); }
        @Override public FileService fileService() { throw new UnsupportedOperationException(); }
        @Override public DockerService dockerService() { throw new UnsupportedOperationException(); }
        @Override public SecretService secretService() { throw new UnsupportedOperationException(); }
        @Override public LockService lockService() { throw new UnsupportedOperationException(); }
        @Override public ApiConfiguration apiConfiguration() { throw new UnsupportedOperationException(); }
        @Override public ProcessConfiguration processConfiguration() { throw new UnsupportedOperationException(); }
        @Override public Execution execution() { throw new UnsupportedOperationException(); }
        @Override public Compiler compiler() { throw new UnsupportedOperationException(); }
        @Override public <T> T eval(Object v, Class<T> type) { throw new UnsupportedOperationException(); }
        @Override public <T> T eval(Object v, Map<String, Object> additionalVariables, Class<T> type) { throw new UnsupportedOperationException(); }
        @Override public void suspend(String eventName) { throw new UnsupportedOperationException(); }
        @Override public void reentrantSuspend(String eventName, Map<String, Serializable> payload) { throw new UnsupportedOperationException(); }
    }
}
