package com.walmartlabs.concord.imports;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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

import static org.junit.jupiter.api.Assertions.*;

public class ImportsTest {

    @Test
    public void testEmptyImports() {
        Imports imports = Imports.builder().build();
        assertTrue(imports.isEmpty());
        assertTrue(imports.items().isEmpty());
    }

    @Test
    public void testOfWithEmptyList() {
        Imports imports = Imports.of(Collections.emptyList());
        assertTrue(imports.isEmpty());
    }

    @Test
    public void testMergeEmptyImports() {
        Imports a = Imports.builder().build();
        Imports b = Imports.builder().build();

        Imports result = Imports.merge(a, b);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIsEmptyFalseWhenItemsPresent() {
        Import imp = Import.GitDefinition.builder()
                .url("https://example.com/repo.git")
                .version("main")
                .dest("dest")
                .build();

        Imports imports = Imports.of(Collections.singletonList(imp));
        assertFalse(imports.isEmpty());
        assertEquals(1, imports.items().size());
    }

    @Test
    public void testMergeWithItems() {
        Import imp1 = Import.GitDefinition.builder()
                .url("https://example.com/repo1.git")
                .version("main")
                .dest("dest1")
                .build();

        Import imp2 = Import.GitDefinition.builder()
                .url("https://example.com/repo2.git")
                .version("main")
                .dest("dest2")
                .build();

        Imports a = Imports.of(Collections.singletonList(imp1));
        Imports b = Imports.of(Collections.singletonList(imp2));

        Imports result = Imports.merge(a, b);
        assertEquals(2, result.items().size());
    }

    @Test
    public void testHideSensitiveDataNoCredentials() {
        String url = "https://example.com/repo.git";
        assertEquals(url, Import.hideSensitiveData(url));
    }

    @Test
    public void testHideSensitiveDataWithCredentials() {
        String url = "https://user:pass@example.com/repo.git";
        String result = Import.hideSensitiveData(url);
        assertFalse(result.contains("user:pass"));
        assertTrue(result.contains("***"));
    }

    @Test
    public void testHideSensitiveDataInvalidUrl() {
        String url = "not-a-url";
        assertEquals(url, Import.hideSensitiveData(url));
    }
}
