package com.walmartlabs.concord.imports;

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

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ImportsTest {

    @Test
    public void testEmptyImports() {
        var imports = Imports.builder().build();
        assertTrue(imports.isEmpty());
        assertTrue(imports.items().isEmpty());
    }

    @Test
    public void testImportsOf() {
        var git = Import.GitDefinition.builder()
                .url("https://example.com/repo.git")
                .version("main")
                .build();
        var imports = Imports.of(Collections.singletonList(git));
        assertFalse(imports.isEmpty());
        assertEquals(1, imports.items().size());
    }

    @Test
    public void testMerge() {
        var git = Import.GitDefinition.builder()
                .url("https://example.com/repo.git")
                .version("main")
                .build();
        var mvn = Import.MvnDefinition.builder()
                .url("mvn://com.example:lib:1.0")
                .build();

        var a = Imports.of(Collections.singletonList(git));
        var b = Imports.of(Collections.singletonList(mvn));

        var merged = Imports.merge(a, b);
        assertEquals(2, merged.items().size());
    }

    @Test
    public void testMergeWithEmpty() {
        var git = Import.GitDefinition.builder()
                .url("https://example.com/repo.git")
                .version("main")
                .build();
        var a = Imports.of(Collections.singletonList(git));
        var empty = Imports.builder().build();

        var merged = Imports.merge(a, empty);
        assertEquals(1, merged.items().size());
    }

    @Test
    public void testGitDefinition() {
        var git = Import.GitDefinition.builder()
                .name("myImport")
                .url("https://example.com/repo.git")
                .version("v1.0")
                .path("/sub")
                .dest("target")
                .build();

        assertEquals(Import.GitDefinition.TYPE, git.type());
        assertEquals("git", git.type());
        assertEquals("myImport", git.name());
        assertEquals("https://example.com/repo.git", git.url());
        assertEquals("v1.0", git.version());
        assertEquals("/sub", git.path());
        assertEquals("target", git.dest());
        assertTrue(git.exclude().isEmpty());
        assertNotNull(git.toString());
    }

    @Test
    public void testGitDefinitionWithExclude() {
        var git = Import.GitDefinition.builder()
                .url("https://example.com/repo.git")
                .version("main")
                .addExclude("*.tmp")
                .addExclude("*.log")
                .build();

        assertEquals(2, git.exclude().size());
    }

    @Test
    public void testGitDefinitionWithSecret() {
        var secret = Import.SecretDefinition.builder()
                .name("mySecret")
                .org("myOrg")
                .password("pass")
                .build();

        var git = Import.GitDefinition.builder()
                .url("https://example.com/repo.git")
                .version("main")
                .secret(secret)
                .build();

        assertNotNull(git.secret());
        assertEquals("mySecret", git.secret().name());
        assertEquals("myOrg", git.secret().org());
    }

    @Test
    public void testMvnDefinition() {
        var mvn = Import.MvnDefinition.builder()
                .url("mvn://com.example:lib:1.0")
                .dest("libs")
                .build();

        assertEquals(Import.MvnDefinition.TYPE, mvn.type());
        assertEquals("mvn", mvn.type());
        assertEquals("mvn://com.example:lib:1.0", mvn.url());
        assertEquals("libs", mvn.dest());
        assertNotNull(mvn.toString());
    }

    @Test
    public void testDirectoryDefinition() {
        var dir = Import.DirectoryDefinition.builder()
                .src("/path/to/dir")
                .dest("target")
                .build();

        assertEquals(Import.DirectoryDefinition.TYPE, dir.type());
        assertEquals("dir", dir.type());
        assertEquals("/path/to/dir", dir.src());
        assertEquals("target", dir.dest());
        assertNotNull(dir.toString());
    }

    @Test
    public void testSecretDefinitionToString() {
        var secret = Import.SecretDefinition.builder()
                .name("mySecret")
                .org("myOrg")
                .password("pass")
                .build();

        var s = secret.toString();
        assertNotNull(s);
        assertFalse(s.contains("\"pass\""));
        assertTrue(s.contains("***"));
    }

    @Test
    public void testHideSensitiveDataPlainUrl() {
        var result = Import.hideSensitiveData("https://example.com/repo.git");
        assertEquals("https://example.com/repo.git", result);
    }

    @Test
    public void testHideSensitiveDataWithUserInfo() {
        var result = Import.hideSensitiveData("https://user:pass@example.com/repo.git");
        assertFalse(result.contains("user:pass"));
        assertTrue(result.contains("***"));
    }

    @Test
    public void testHideSensitiveDataInvalidUrl() {
        var result = Import.hideSensitiveData("not-a-url");
        assertEquals("not-a-url", result);
    }
}
