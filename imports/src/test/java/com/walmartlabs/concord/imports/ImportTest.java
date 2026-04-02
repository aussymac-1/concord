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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ImportTest {

    @Test
    public void testGitDefinitionType() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/example/repo")
                .version("main")
                .build();
        assertEquals("git", git.type());
        assertEquals("https://github.com/example/repo", git.url());
        assertEquals("main", git.version());
    }

    @Test
    public void testGitDefinitionToString() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .name("test")
                .url("https://github.com/example/repo")
                .version("main")
                .path("/flows")
                .dest("myDest")
                .build();
        String s = git.toString();
        assertNotNull(s);
        assertTrue(s.contains("git:"));
    }

    @Test
    public void testGitDefinitionExcludeDefault() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/example/repo")
                .build();
        assertTrue(git.exclude().isEmpty());
    }

    @Test
    public void testMvnDefinitionType() {
        Import.MvnDefinition mvn = Import.MvnDefinition.builder()
                .url("mvn://com.example:artifact:1.0")
                .build();
        assertEquals("mvn", mvn.type());
        assertEquals("mvn://com.example:artifact:1.0", mvn.url());
    }

    @Test
    public void testMvnDefinitionToString() {
        Import.MvnDefinition mvn = Import.MvnDefinition.builder()
                .url("mvn://com.example:artifact:1.0")
                .dest("lib")
                .build();
        String s = mvn.toString();
        assertNotNull(s);
        assertTrue(s.contains("mvn:"));
    }

    @Test
    public void testDirectoryDefinitionType() {
        Import.DirectoryDefinition dir = Import.DirectoryDefinition.builder()
                .src("/src/flows")
                .build();
        assertEquals("dir", dir.type());
        assertEquals("/src/flows", dir.src());
    }

    @Test
    public void testDirectoryDefinitionToString() {
        Import.DirectoryDefinition dir = Import.DirectoryDefinition.builder()
                .src("/src")
                .dest("/dest")
                .build();
        String s = dir.toString();
        assertNotNull(s);
        assertTrue(s.contains("dir:"));
    }

    @Test
    public void testSecretDefinition() {
        Import.SecretDefinition secret = Import.SecretDefinition.builder()
                .name("mySecret")
                .org("myOrg")
                .build();
        assertEquals("mySecret", secret.name());
        assertEquals("myOrg", secret.org());
        assertNull(secret.password());
    }

    @Test
    public void testSecretDefinitionToStringHidesPassword() {
        Import.SecretDefinition secret = Import.SecretDefinition.builder()
                .name("mySecret")
                .password("secret123")
                .build();
        String s = secret.toString();
        assertNotNull(s);
        assertTrue(s.contains("***"));
        assertFalse(s.contains("secret123"));
    }

    @Test
    public void testHideSensitiveDataNoUserInfo() {
        assertEquals("https://github.com/repo", Import.hideSensitiveData("https://github.com/repo"));
    }

    @Test
    public void testHideSensitiveDataWithUserInfo() {
        String result = Import.hideSensitiveData("https://user:pass@github.com/repo");
        assertTrue(result.contains("***"));
        assertFalse(result.contains("user:pass"));
    }

    @Test
    public void testHideSensitiveDataInvalidUrl() {
        assertEquals("not-a-url", Import.hideSensitiveData("not-a-url"));
    }

    @Test
    public void testImportsEmpty() {
        Imports imports = Imports.of(Collections.emptyList());
        assertTrue(imports.isEmpty());
        assertTrue(imports.items().isEmpty());
    }

    @Test
    public void testImportsMerge() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/example/repo")
                .build();
        Import.MvnDefinition mvn = Import.MvnDefinition.builder()
                .url("mvn://com.example:artifact:1.0")
                .build();

        Imports a = Imports.of(List.of(git));
        Imports b = Imports.of(List.of(mvn));
        Imports merged = Imports.merge(a, b);

        assertFalse(merged.isEmpty());
        assertEquals(2, merged.items().size());
    }

    @Test
    public void testImportsBuilder() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/example/repo")
                .build();
        Imports imports = Imports.builder()
                .addItems(git)
                .build();
        assertEquals(1, imports.items().size());
        assertFalse(imports.isEmpty());
    }
}
