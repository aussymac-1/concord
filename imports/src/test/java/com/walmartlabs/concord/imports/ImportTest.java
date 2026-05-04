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

import static org.junit.jupiter.api.Assertions.*;

public class ImportTest {

    @Test
    public void testGitDefinitionTypeAndDefaults() {
        var g = Import.GitDefinition.builder()
                .name("repo")
                .url("https://example.com/repo.git")
                .build();

        assertEquals("git", g.type());
        assertEquals("git", Import.GitDefinition.TYPE);
        assertNotNull(g.exclude());
        assertTrue(g.exclude().isEmpty());
        assertEquals("repo", g.name());
        assertNull(g.path());
    }

    @Test
    public void testGitDefinitionToStringHidesUserInfo() {
        var g = Import.GitDefinition.builder()
                .url("https://user:pass@example.com/repo.git")
                .build();
        var s = g.toString();
        assertTrue(s.contains("***"));
        assertFalse(s.contains("user:pass"));
    }

    @Test
    public void testMvnDefinition() {
        var m = Import.MvnDefinition.builder()
                .url("mvn://com.example:foo:1.0")
                .build();

        assertEquals("mvn", m.type());
        assertEquals("mvn", Import.MvnDefinition.TYPE);
        assertEquals("mvn://com.example:foo:1.0", m.url());
        assertNull(m.dest());
        assertTrue(m.toString().startsWith("mvn:"));
    }

    @Test
    public void testDirectoryDefinition() {
        var d = Import.DirectoryDefinition.builder()
                .src("/tmp/foo")
                .dest("inner")
                .build();

        assertEquals("dir", d.type());
        assertEquals("dir", Import.DirectoryDefinition.TYPE);
        assertEquals("/tmp/foo", d.src());
        assertEquals("inner", d.dest());
    }

    @Test
    public void testSecretDefinitionHidesPassword() {
        var s = Import.SecretDefinition.builder()
                .org("o")
                .name("n")
                .password("supersecret")
                .build();

        assertEquals("o", s.org());
        assertEquals("n", s.name());
        assertEquals("supersecret", s.password());

        var str = s.toString();
        assertFalse(str.contains("supersecret"));
        assertTrue(str.contains("***"));
    }

    @Test
    public void testSecretDefinitionWithoutPassword() {
        var s = Import.SecretDefinition.builder()
                .name("n")
                .build();

        assertNull(s.org());
        assertNull(s.password());
        var str = s.toString();
        assertFalse(str.contains("***"));
    }

    @Test
    public void testHideSensitiveDataNoUserInfoLeavesUrlUnchanged() {
        assertEquals("https://example.com/repo.git",
                Import.hideSensitiveData("https://example.com/repo.git"));
    }

    @Test
    public void testHideSensitiveDataMalformedUrlReturnsAsIs() {
        assertEquals("not-a-url", Import.hideSensitiveData("not-a-url"));
    }

    @Test
    public void testHideSensitiveDataReplacesUserInfo() {
        var url = "https://alice:secret@example.com/repo.git";
        var safe = Import.hideSensitiveData(url);
        assertFalse(safe.contains("alice:secret"));
        assertTrue(safe.contains("***@example.com"));
    }
}
