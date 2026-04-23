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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImportTest {

    @Test
    public void gitDefinitionTypeIsStable() {
        var g = Import.GitDefinition.builder()
                .name("n")
                .url("https://github.com/u/r.git")
                .version("main")
                .build();
        assertEquals("git", g.type());
        assertEquals(Import.GitDefinition.TYPE, g.type());
    }

    @Test
    public void mvnDefinitionTypeIsStable() {
        var m = Import.MvnDefinition.builder().url("mvn://x").build();
        assertEquals("mvn", m.type());
        assertEquals(Import.MvnDefinition.TYPE, m.type());
    }

    @Test
    public void directoryDefinitionTypeIsStable() {
        var d = Import.DirectoryDefinition.builder().src("/tmp/a").build();
        assertEquals("dir", d.type());
        assertEquals(Import.DirectoryDefinition.TYPE, d.type());
    }

    @Test
    public void gitDefinitionDefaultsExcludeListIsEmpty() {
        var g = Import.GitDefinition.builder().url("https://g/r").build();
        assertTrue(g.exclude().isEmpty());
    }

    @Test
    public void gitDefinitionKeepsGivenExcludeList() {
        var g = Import.GitDefinition.builder().url("https://g/r").exclude(List.of("**/*.zip")).build();
        assertEquals(List.of("**/*.zip"), g.exclude());
    }

    @Test
    public void secretDefinitionHidesPasswordInToString() {
        var s = Import.SecretDefinition.builder()
                .org("myOrg")
                .name("mySecret")
                .password("super-secret")
                .build();
        assertFalse(s.toString().contains("super-secret"));
        assertTrue(s.toString().contains("***"));
        assertTrue(s.toString().contains("mySecret"));
    }

    @Test
    public void secretDefinitionToStringHandlesNullPassword() {
        var s = Import.SecretDefinition.builder().name("mySecret").build();
        assertFalse(s.toString().contains("***"));
    }

    @Test
    public void hideSensitiveDataMasksUserInfoInUrl() {
        var masked = Import.hideSensitiveData("https://user:pw@example.com/r");
        assertFalse(masked.contains("user:pw"));
        assertTrue(masked.contains("***"));
    }

    @Test
    public void hideSensitiveDataLeavesPlainUrlUnchanged() {
        assertEquals("https://example.com/r", Import.hideSensitiveData("https://example.com/r"));
    }

    @Test
    public void hideSensitiveDataReturnsOriginalOnInvalidUrl() {
        assertEquals("not a url", Import.hideSensitiveData("not a url"));
    }

    @Test
    public void gitDefinitionToStringIncludesKeys() {
        var g = Import.GitDefinition.builder()
                .name("n")
                .url("https://g/r")
                .version("v")
                .path("p")
                .dest("d")
                .build();
        var s = g.toString();
        assertTrue(s.contains("git"));
        assertTrue(s.contains("name"));
        assertTrue(s.contains("url"));
        assertTrue(s.contains("version"));
    }

    @Test
    public void mvnDefinitionToStringHidesUrlUserInfo() {
        var m = Import.MvnDefinition.builder().url("https://user:pw@example.com/a").build();
        assertFalse(m.toString().contains("user:pw"));
    }
}
