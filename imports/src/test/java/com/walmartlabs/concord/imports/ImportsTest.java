package com.walmartlabs.concord.imports;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2019 Walmart Inc.
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

import static org.junit.jupiter.api.Assertions.*;

class ImportsTest {

    @Test
    void testEmptyImports() {
        var imports = Imports.of(Collections.emptyList());
        assertTrue(imports.isEmpty());
        assertTrue(imports.items().isEmpty());
    }

    @Test
    void testNonEmptyImports() {
        var git = Import.GitDefinition.builder()
                .url("https://github.com/example/repo")
                .version("main")
                .build();
        var imports = Imports.of(Collections.singletonList(git));
        assertFalse(imports.isEmpty());
        assertEquals(1, imports.items().size());
    }

    @Test
    void testMerge() {
        var git = Import.GitDefinition.builder()
                .url("https://github.com/example/repo")
                .version("main")
                .build();
        var mvn = Import.MvnDefinition.builder()
                .url("mvn://com.example:artifact:1.0")
                .build();

        var a = Imports.of(Collections.singletonList(git));
        var b = Imports.of(Collections.singletonList(mvn));

        var merged = Imports.merge(a, b);
        assertEquals(2, merged.items().size());
    }

    @Test
    void testMergeWithEmpty() {
        var git = Import.GitDefinition.builder()
                .url("https://github.com/example/repo")
                .version("main")
                .build();

        var a = Imports.of(Collections.singletonList(git));
        var empty = Imports.of(Collections.emptyList());

        var merged = Imports.merge(a, empty);
        assertEquals(1, merged.items().size());
    }

    @Test
    void testGitDefinitionType() {
        var git = Import.GitDefinition.builder()
                .url("https://github.com/example/repo")
                .version("main")
                .path("/sub")
                .dest("target")
                .build();

        assertEquals("git", git.type());
        assertEquals("https://github.com/example/repo", git.url());
        assertEquals("main", git.version());
        assertEquals("/sub", git.path());
        assertEquals("target", git.dest());
    }

    @Test
    void testMvnDefinitionType() {
        var mvn = Import.MvnDefinition.builder()
                .url("mvn://com.example:artifact:1.0")
                .dest("lib")
                .build();

        assertEquals("mvn", mvn.type());
        assertEquals("mvn://com.example:artifact:1.0", mvn.url());
        assertEquals("lib", mvn.dest());
    }

    @Test
    void testDirectoryDefinitionType() {
        var dir = Import.DirectoryDefinition.builder()
                .src("/some/path")
                .dest("out")
                .build();

        assertEquals("dir", dir.type());
        assertEquals("/some/path", dir.src());
        assertEquals("out", dir.dest());
    }

    @Test
    void testHideSensitiveDataNoUserInfo() {
        var url = "https://github.com/example/repo";
        assertEquals(url, Import.hideSensitiveData(url));
    }

    @Test
    void testHideSensitiveDataWithUserInfo() {
        var url = "https://user:password@github.com/example/repo";
        var result = Import.hideSensitiveData(url);
        assertFalse(result.contains("password"));
        assertTrue(result.contains("***"));
    }

    @Test
    void testHideSensitiveDataInvalidUrl() {
        var url = "not-a-url";
        assertEquals(url, Import.hideSensitiveData(url));
    }

    @Test
    void testNoopImportManager() {
        var mgr = new NoopImportManager();
        var result = mgr.process(Imports.of(Collections.emptyList()), null, null);
        assertTrue(result.isEmpty());
    }
}
