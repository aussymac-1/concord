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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImportTest {

    @Test
    public void testGitDefinitionType() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .name("example")
                .url("https://github.com/example/repo.git")
                .version("main")
                .build();

        assertEquals(Import.GitDefinition.TYPE, git.type());
        assertEquals("git", git.type());
        assertEquals("example", git.name());
    }

    @Test
    public void testGitDefinitionToStringHidesCredentials() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://user:secret@github.com/example/repo.git")
                .build();

        String s = git.toString();
        assertFalse(s.contains("secret"), "toString() must not reveal credentials: " + s);
        assertTrue(s.contains("***"), "toString() should mask credentials with ***: " + s);
    }

    @Test
    public void testMvnDefinitionType() {
        Import.MvnDefinition mvn = Import.MvnDefinition.builder()
                .url("mvn://com.example:artifact:1.0.0")
                .build();

        assertEquals(Import.MvnDefinition.TYPE, mvn.type());
        assertEquals("mvn", mvn.type());
        assertNotNull(mvn.toString());
    }

    @Test
    public void testDirectoryDefinitionType() {
        Import.DirectoryDefinition dir = Import.DirectoryDefinition.builder()
                .src("/tmp/source")
                .dest("/tmp/dest")
                .build();

        assertEquals(Import.DirectoryDefinition.TYPE, dir.type());
        assertEquals("dir", dir.type());
        assertEquals("/tmp/source", dir.src());
        assertEquals("/tmp/dest", dir.dest());
    }

    @Test
    public void testSecretDefinitionMasksPassword() {
        Import.SecretDefinition secret = Import.SecretDefinition.builder()
                .org("myorg")
                .name("mySecret")
                .password("hunter2")
                .build();

        String s = secret.toString();
        assertFalse(s.contains("hunter2"), "toString() must not reveal password: " + s);
        assertTrue(s.contains("***"), "toString() should mask password: " + s);
        assertTrue(s.contains("mySecret"));
    }

    @Test
    public void testSecretDefinitionWithoutPassword() {
        Import.SecretDefinition secret = Import.SecretDefinition.builder()
                .name("mySecret")
                .build();

        String s = secret.toString();
        assertFalse(s.contains("***"));
        assertTrue(s.contains("mySecret"));
    }

    @Test
    public void testHideSensitiveDataWithUserInfo() {
        String raw = "https://user:password@github.com/repo.git";
        String hidden = Import.hideSensitiveData(raw);
        assertFalse(hidden.contains("user:password"));
        assertTrue(hidden.contains("***"));
    }

    @Test
    public void testHideSensitiveDataWithoutUserInfo() {
        String raw = "https://github.com/repo.git";
        assertEquals(raw, Import.hideSensitiveData(raw));
    }

    @Test
    public void testHideSensitiveDataWithInvalidUrl() {
        String raw = "not-a-url";
        // invalid URLs are returned unchanged
        assertEquals(raw, Import.hideSensitiveData(raw));
    }

    @Test
    public void testHideSensitiveDataWithNull() {
        assertEquals("null", String.valueOf(Import.hideSensitiveData(null)));
    }
}
