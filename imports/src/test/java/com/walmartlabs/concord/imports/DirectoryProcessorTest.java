package com.walmartlabs.concord.imports;

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

import static org.junit.jupiter.api.Assertions.*;

public class DirectoryProcessorTest {

    @Test
    public void testTypeIsDir() {
        assertEquals("dir", new DirectoryProcessor().type());
    }

    @Test
    public void testCopiesAbsoluteSourceIntoWorkDir(@TempDir Path tmp) throws Exception {
        var src = tmp.resolve("src");
        Files.createDirectories(src);
        Files.writeString(src.resolve("a.txt"), "hello");

        var workDir = tmp.resolve("work");
        Files.createDirectories(workDir);

        var def = Import.DirectoryDefinition.builder()
                .src(src.toAbsolutePath().toString())
                .build();

        new DirectoryProcessor().process(def, workDir);

        assertEquals("hello", Files.readString(workDir.resolve("a.txt")));
    }

    @Test
    public void testCopiesIntoSpecifiedDest(@TempDir Path tmp) throws Exception {
        var src = tmp.resolve("src");
        Files.createDirectories(src);
        Files.writeString(src.resolve("a.txt"), "x");

        var workDir = tmp.resolve("work");
        Files.createDirectories(workDir);

        var def = Import.DirectoryDefinition.builder()
                .src(src.toAbsolutePath().toString())
                .dest("subdir")
                .build();

        new DirectoryProcessor().process(def, workDir);

        assertTrue(Files.exists(workDir.resolve("subdir/a.txt")));
    }

    @Test
    public void testRelativeSourceResolvedAgainstWorkDir(@TempDir Path tmp) throws Exception {
        var workDir = tmp.resolve("work");
        Files.createDirectories(workDir);
        var sibling = tmp.resolve("../external_src");
        // ensure sibling is outside workDir
        var external = workDir.resolve("../external_src").normalize();
        Files.createDirectories(external);
        Files.writeString(external.resolve("a.txt"), "x");

        var def = Import.DirectoryDefinition.builder()
                .src("../external_src")
                .build();

        new DirectoryProcessor().process(def, workDir);
        assertTrue(Files.exists(workDir.resolve("a.txt")));
    }

    @Test
    public void testMissingSourceThrows(@TempDir Path tmp) {
        var workDir = tmp.resolve("work");
        var def = Import.DirectoryDefinition.builder()
                .src(tmp.resolve("does-not-exist").toAbsolutePath().toString())
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> new DirectoryProcessor().process(def, workDir));
    }

    @Test
    public void testSrcInsideWorkDirRejected(@TempDir Path tmp) throws IOException {
        var workDir = tmp.resolve("work");
        Files.createDirectories(workDir);

        // workDir starts with src means src is a parent of (or equal to) workDir
        var def = Import.DirectoryDefinition.builder()
                .src(tmp.toAbsolutePath().toString())
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> new DirectoryProcessor().process(def, workDir));
    }
}
