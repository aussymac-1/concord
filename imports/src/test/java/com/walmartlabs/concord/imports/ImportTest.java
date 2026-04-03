package com.walmartlabs.concord.imports;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2026 Walmart Inc.
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
    public void testHideSensitiveDataWithUserInfo() {
        String url = "https://user:pass@example.com/repo.git";
        String result = Import.hideSensitiveData(url);
        assertFalse(result.contains("user:pass"));
        assertTrue(result.contains("***"));
    }

    @Test
    public void testHideSensitiveDataWithoutUserInfo() {
        String url = "https://example.com/repo.git";
        String result = Import.hideSensitiveData(url);
        assertEquals(url, result);
    }

    @Test
    public void testHideSensitiveDataWithInvalidUrl() {
        String url = "not-a-url";
        String result = Import.hideSensitiveData(url);
        assertEquals(url, result);
    }

    @Test
    public void testHideSensitiveDataNull() {
        String result = Import.hideSensitiveData(null);
        assertNull(result);
    }

    @Test
    public void testGitDefinitionType() {
        assertEquals("git", Import.GitDefinition.TYPE);
    }

    @Test
    public void testMvnDefinitionType() {
        assertEquals("mvn", Import.MvnDefinition.TYPE);
    }

    @Test
    public void testDirectoryDefinitionType() {
        assertEquals("dir", Import.DirectoryDefinition.TYPE);
    }
}
