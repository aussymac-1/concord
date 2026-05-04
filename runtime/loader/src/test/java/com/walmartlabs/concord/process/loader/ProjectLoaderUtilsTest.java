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
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectLoaderUtilsTest {

    @Test
    public void testGetRuntimeType_noConcordFile(@TempDir Path workDir) throws IOException {
        var result = ProjectLoaderUtils.getRuntimeType(workDir);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRuntimeType_concordYmlWithRuntimeV2(@TempDir Path workDir) throws IOException {
        Files.writeString(workDir.resolve("concord.yml"),
                "configuration:\n  runtime: concord-v2\n",
                StandardOpenOption.CREATE);

        var result = ProjectLoaderUtils.getRuntimeType(workDir);
        assertTrue(result.isPresent());
        assertEquals("concord-v2", result.get());
    }

    @Test
    public void testGetRuntimeType_dotConcordYamlVariant(@TempDir Path workDir) throws IOException {
        Files.writeString(workDir.resolve(".concord.yaml"),
                "configuration:\n  runtime: concord-v1\n",
                StandardOpenOption.CREATE);

        assertEquals("concord-v1", ProjectLoaderUtils.getRuntimeType(workDir).orElseThrow());
    }

    @Test
    public void testGetRuntimeType_emptyConfigurationSection(@TempDir Path workDir) throws IOException {
        Files.writeString(workDir.resolve("concord.yml"),
                "flows:\n  default:\n    - log: hi\n",
                StandardOpenOption.CREATE);

        assertTrue(ProjectLoaderUtils.getRuntimeType(workDir).isEmpty());
    }

    @Test
    public void testGetRuntimeType_configurationWithoutRuntime(@TempDir Path workDir) throws IOException {
        Files.writeString(workDir.resolve("concord.yml"),
                "configuration:\n  arguments:\n    foo: bar\n",
                StandardOpenOption.CREATE);

        assertTrue(ProjectLoaderUtils.getRuntimeType(workDir).isEmpty());
    }

    @Test
    public void testGetRuntimeType_falsBackToNextFileWhenFirstHasNoRuntime(@TempDir Path workDir) throws IOException {
        // .concord.yml exists but has no runtime; concord.yml has runtime
        Files.writeString(workDir.resolve(".concord.yml"),
                "configuration:\n  arguments:\n    foo: bar\n",
                StandardOpenOption.CREATE);
        Files.writeString(workDir.resolve("concord.yml"),
                "configuration:\n  runtime: concord-v2\n",
                StandardOpenOption.CREATE);

        assertEquals("concord-v2", ProjectLoaderUtils.getRuntimeType(workDir).orElseThrow());
    }

    @Test
    public void testIsConcordFileExists_notFound(@TempDir Path workDir) {
        assertFalse(ProjectLoaderUtils.isConcordFileExists(workDir));
    }

    @Test
    public void testIsConcordFileExists_findsConcordYml(@TempDir Path workDir) throws IOException {
        Files.writeString(workDir.resolve("concord.yml"), "");
        assertTrue(ProjectLoaderUtils.isConcordFileExists(workDir));
    }

    @Test
    public void testIsConcordFileExists_findsHiddenYaml(@TempDir Path workDir) throws IOException {
        Files.writeString(workDir.resolve(".concord.yaml"), "");
        assertTrue(ProjectLoaderUtils.isConcordFileExists(workDir));
    }

    @Test
    public void testCannotInstantiate() throws Exception {
        Constructor<ProjectLoaderUtils> ctor = ProjectLoaderUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(ctor.getModifiers()));
        ctor.setAccessible(true);
        assertNotNull(ctor.newInstance());
    }
}
