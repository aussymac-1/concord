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
        assertEquals("git", imports.items().get(0).type());
    }

    @Test
    public void testMerge() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/test/repo")
                .build();
        Import.MvnDefinition mvn = Import.MvnDefinition.builder()
                .url("mvn://com.example:artifact:1.0")
                .build();

        Imports a = Imports.of(Collections.singletonList(git));
        Imports b = Imports.of(Collections.singletonList(mvn));

        Imports merged = Imports.merge(a, b);
        assertEquals(2, merged.items().size());
        assertEquals("git", merged.items().get(0).type());
        assertEquals("mvn", merged.items().get(1).type());
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
    public void testGitDefinition() {
        Import.SecretDefinition secret = Import.SecretDefinition.builder()
                .name("mySecret")
                .org("myOrg")
                .password("pass")
                .build();

        Import.GitDefinition git = Import.GitDefinition.builder()
                .name("myImport")
                .url("https://github.com/test/repo")
                .version("main")
                .path("/src")
                .dest("/target")
                .secret(secret)
                .addExclude("*.tmp")
                .build();

        assertEquals("git", git.type());
        assertEquals("myImport", git.name());
        assertEquals("https://github.com/test/repo", git.url());
        assertEquals("main", git.version());
        assertEquals("/src", git.path());
        assertEquals("/target", git.dest());
        assertNotNull(git.secret());
        assertEquals("mySecret", git.secret().name());
        assertEquals("myOrg", git.secret().org());
        assertEquals(Collections.singletonList("*.tmp"), git.exclude());
    }

    @Test
    public void testMvnDefinition() {
        Import.MvnDefinition mvn = Import.MvnDefinition.builder()
                .url("mvn://com.example:artifact:1.0")
                .dest("/lib")
                .build();

        assertEquals("mvn", mvn.type());
        assertEquals("mvn://com.example:artifact:1.0", mvn.url());
        assertEquals("/lib", mvn.dest());
    }

    @Test
    public void testDirectoryDefinition() {
        Import.DirectoryDefinition dir = Import.DirectoryDefinition.builder()
                .src("/source/path")
                .dest("/dest/path")
                .build();

        assertEquals("dir", dir.type());
        assertEquals("/source/path", dir.src());
        assertEquals("/dest/path", dir.dest());
    }

    @Test
    public void testSecretDefinition() {
        Import.SecretDefinition secret = Import.SecretDefinition.builder()
                .name("mySecret")
                .org("myOrg")
                .password("secretPass")
                .build();

        assertEquals("mySecret", secret.name());
        assertEquals("myOrg", secret.org());
        assertEquals("secretPass", secret.password());
    }

    @Test
    public void testSecretDefinitionToString() {
        Import.SecretDefinition secret = Import.SecretDefinition.builder()
                .name("mySecret")
                .org("myOrg")
                .password("secretPass")
                .build();

        String str = secret.toString();
        assertTrue(str.contains("mySecret"));
        assertTrue(str.contains("myOrg"));
        assertTrue(str.contains("***"));
        assertFalse(str.contains("secretPass"));
    }

    @Test
    public void testGitDefinitionToString() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://user:pass@github.com/test/repo")
                .version("main")
                .build();

        String str = git.toString();
        assertTrue(str.contains("***"));
        assertFalse(str.contains("user:pass"));
    }

    @Test
    public void testHideSensitiveDataWithCredentials() {
        String result = Import.hideSensitiveData("https://user:pass@github.com/test/repo");
        assertTrue(result.contains("***"));
        assertFalse(result.contains("user:pass"));
    }

    @Test
    public void testHideSensitiveDataWithoutCredentials() {
        String result = Import.hideSensitiveData("https://github.com/test/repo");
        assertEquals("https://github.com/test/repo", result);
    }

    @Test
    public void testHideSensitiveDataInvalidUrl() {
        String result = Import.hideSensitiveData("not-a-url");
        assertEquals("not-a-url", result);
    }

    @Test
    public void testHideSensitiveDataNull() {
        String result = Import.hideSensitiveData(null);
        assertNull(result);
    }

    @Test
    public void testGitDefinitionDefaultExclude() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/test/repo")
                .build();
        assertTrue(git.exclude().isEmpty());
    }

    @Test
    public void testMvnDefinitionToString() {
        Import.MvnDefinition mvn = Import.MvnDefinition.builder()
                .url("https://user:pass@example.com/artifact")
                .dest("/lib")
                .build();

        String str = mvn.toString();
        assertTrue(str.contains("***"));
        assertFalse(str.contains("user:pass"));
    }

    @Test
    public void testDirectoryDefinitionToString() {
        Import.DirectoryDefinition dir = Import.DirectoryDefinition.builder()
                .src("/source")
                .dest("/dest")
                .build();

        String str = dir.toString();
        assertTrue(str.contains("/source"));
        assertTrue(str.contains("/dest"));
    }
}
