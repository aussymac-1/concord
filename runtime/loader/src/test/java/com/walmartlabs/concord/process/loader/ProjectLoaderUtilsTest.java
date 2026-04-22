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
    public void isConcordFileExistsReturnsFalseForEmptyDir(@TempDir Path dir) {
        assertFalse(ProjectLoaderUtils.isConcordFileExists(dir));
    }

    @Test
    public void isConcordFileExistsReturnsTrueForAnyVariant(@TempDir Path dir) throws IOException {
        for (var fn : StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES) {
            var d = Files.createDirectories(dir.resolve(fn.replace('.', '_')));
            Files.createFile(d.resolve(fn));
            assertTrue(ProjectLoaderUtils.isConcordFileExists(d), "expected match for " + fn);
        }
    }

    @Test
    public void getRuntimeTypeReturnsEmptyForEmptyDir(@TempDir Path dir) throws IOException {
        assertEquals(Optional.empty(), ProjectLoaderUtils.getRuntimeType(dir));
    }

    @Test
    public void getRuntimeTypeReadsFromConcordYml(@TempDir Path dir) throws IOException {
        Files.writeString(dir.resolve("concord.yml"), ""
                + "configuration:\n"
                + "  runtime: \"concord-v2\"\n");

        assertEquals(Optional.of("concord-v2"), ProjectLoaderUtils.getRuntimeType(dir));
    }

    @Test
    public void getRuntimeTypeReturnsEmptyWhenConfigurationMissing(@TempDir Path dir) throws IOException {
        Files.writeString(dir.resolve("concord.yml"), "foo: bar\n");

        assertEquals(Optional.empty(), ProjectLoaderUtils.getRuntimeType(dir));
    }

    @Test
    public void getRuntimeTypeReturnsEmptyWhenRuntimeMissing(@TempDir Path dir) throws IOException {
        Files.writeString(dir.resolve("concord.yml"), ""
                + "configuration:\n"
                + "  foo: bar\n");

        assertEquals(Optional.empty(), ProjectLoaderUtils.getRuntimeType(dir));
    }

    @Test
    public void getRuntimeTypePrefersDotYmlExtension(@TempDir Path dir) throws IOException {
        // Order in PROJECT_ROOT_FILE_NAMES is: .concord.yml, concord.yml, .concord.yaml, concord.yaml
        Files.writeString(dir.resolve(".concord.yml"), ""
                + "configuration:\n"
                + "  runtime: \"first\"\n");
        Files.writeString(dir.resolve("concord.yml"), ""
                + "configuration:\n"
                + "  runtime: \"second\"\n");

        assertEquals(Optional.of("first"), ProjectLoaderUtils.getRuntimeType(dir));
    }
}
