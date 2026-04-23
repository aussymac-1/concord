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

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProjectLoaderUtilsTest {

    @Test
    public void missingRootReturnsEmpty(@TempDir Path workDir) throws Exception {
        assertTrue(ProjectLoaderUtils.getRuntimeType(workDir).isEmpty());
        assertFalse(ProjectLoaderUtils.isConcordFileExists(workDir));
    }

    @Test
    public void getRuntimeTypeReturnsConfiguredValue(@TempDir Path workDir) throws Exception {
        Files.writeString(workDir.resolve("concord.yml"),
                "configuration:\n" +
                "  runtime: concord-v2\n");

        assertEquals("concord-v2", ProjectLoaderUtils.getRuntimeType(workDir).orElseThrow());
    }

    @Test
    public void getRuntimeTypeReadsHiddenVariant(@TempDir Path workDir) throws Exception {
        Files.writeString(workDir.resolve(".concord.yml"),
                "configuration:\n" +
                "  runtime: concord-v1\n");

        assertEquals("concord-v1", ProjectLoaderUtils.getRuntimeType(workDir).orElseThrow());
    }

    @Test
    public void getRuntimeTypeIgnoresFileWithoutRuntimeKey(@TempDir Path workDir) throws Exception {
        Files.writeString(workDir.resolve("concord.yml"),
                "configuration:\n" +
                "  debug: true\n");

        assertTrue(ProjectLoaderUtils.getRuntimeType(workDir).isEmpty());
    }

    @Test
    public void getRuntimeTypeIgnoresFileWithoutConfigurationKey(@TempDir Path workDir) throws Exception {
        Files.writeString(workDir.resolve("concord.yml"),
                "flows:\n" +
                "  default:\n" +
                "    - log: hi\n");

        assertTrue(ProjectLoaderUtils.getRuntimeType(workDir).isEmpty());
    }

    @Test
    public void getRuntimeTypePrefersHiddenVariantFirst(@TempDir Path workDir) throws Exception {
        // .concord.yml is listed first in PROJECT_ROOT_FILE_NAMES, so it should be picked even when
        // a plain concord.yml is also present with a different runtime.
        Files.writeString(workDir.resolve(".concord.yml"),
                "configuration:\n" +
                "  runtime: concord-v1\n");
        Files.writeString(workDir.resolve("concord.yml"),
                "configuration:\n" +
                "  runtime: concord-v2\n");

        assertEquals("concord-v1", ProjectLoaderUtils.getRuntimeType(workDir).orElseThrow());
    }

    @Test
    public void isConcordFileExistsDetectsAllVariants(@TempDir Path workDir) throws Exception {
        for (var name : StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES) {
            var f = workDir.resolve(name);
            Files.writeString(f, "");
            assertTrue(ProjectLoaderUtils.isConcordFileExists(workDir),
                    () -> "expected to detect " + name);
            Files.delete(f);
            assertFalse(ProjectLoaderUtils.isConcordFileExists(workDir));
        }
    }
}
