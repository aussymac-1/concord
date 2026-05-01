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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ImportsTest {

    @Test
    public void testEmptyImports() {
        Imports imports = Imports.builder().build();
        assertTrue(imports.isEmpty());
        assertTrue(imports.items().isEmpty());
    }

    @Test
    public void testOfWithItems() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/test/repo")
                .version("main")
                .build();

        Imports imports = Imports.of(Collections.singletonList(git));
        assertFalse(imports.isEmpty());
        assertEquals(1, imports.items().size());
    }

    @Test
    public void testMerge() {
        Import.GitDefinition git1 = Import.GitDefinition.builder()
                .url("https://github.com/test/repo1")
                .build();

        Import.GitDefinition git2 = Import.GitDefinition.builder()
                .url("https://github.com/test/repo2")
                .build();

        Imports a = Imports.of(Collections.singletonList(git1));
        Imports b = Imports.of(Collections.singletonList(git2));

        Imports merged = Imports.merge(a, b);
        assertEquals(2, merged.items().size());
    }

    @Test
    public void testMergeWithEmpty() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/test/repo")
                .build();

        Imports a = Imports.of(Collections.singletonList(git));
        Imports b = Imports.builder().build();

        Imports merged = Imports.merge(a, b);
        assertEquals(1, merged.items().size());
    }

    @Test
    public void testMvnDefinition() {
        Import.MvnDefinition mvn = Import.MvnDefinition.builder()
                .url("mvn://com.example:artifact:1.0")
                .dest("lib")
                .build();

        assertEquals("mvn", mvn.type());
        assertEquals("mvn://com.example:artifact:1.0", mvn.url());
        assertEquals("lib", mvn.dest());
    }

    @Test
    public void testDirectoryDefinition() {
        Import.DirectoryDefinition dir = Import.DirectoryDefinition.builder()
                .src("/some/path")
                .dest("target")
                .build();

        assertEquals("dir", dir.type());
        assertEquals("/some/path", dir.src());
        assertEquals("target", dir.dest());
    }

    @Test
    public void testGitDefinitionWithExcludes() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/test/repo")
                .version("main")
                .path("sub/dir")
                .dest("target")
                .addExclude("*.tmp")
                .build();

        assertEquals("git", git.type());
        assertEquals(1, git.exclude().size());
        assertEquals("*.tmp", git.exclude().get(0));
    }

    @Test
    public void testGitDefinitionToString() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/test/repo")
                .version("main")
                .build();

        String str = git.toString();
        assertNotNull(str);
        assertTrue(str.contains("git"));
    }
}
