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
    public void testDefaultItemsEmpty() {
        Imports imports = Imports.builder().build();
        assertTrue(imports.items().isEmpty());
        assertTrue(imports.isEmpty());
    }

    @Test
    public void testOfWithItems() {
        Import item = Import.GitDefinition.builder()
                .url("https://example.com/repo.git")
                .build();

        Imports imports = Imports.of(Collections.singletonList(item));
        assertEquals(1, imports.items().size());
        assertFalse(imports.isEmpty());
    }

    @Test
    public void testMerge() {
        Import item1 = Import.GitDefinition.builder()
                .url("https://example.com/repo1.git")
                .build();
        Import item2 = Import.GitDefinition.builder()
                .url("https://example.com/repo2.git")
                .build();

        Imports a = Imports.of(Collections.singletonList(item1));
        Imports b = Imports.of(Collections.singletonList(item2));

        Imports merged = Imports.merge(a, b);
        assertEquals(2, merged.items().size());
    }

    @Test
    public void testMergeWithEmpty() {
        Import item = Import.GitDefinition.builder()
                .url("https://example.com/repo.git")
                .build();

        Imports a = Imports.of(Collections.singletonList(item));
        Imports b = Imports.builder().build();

        Imports merged = Imports.merge(a, b);
        assertEquals(1, merged.items().size());
    }

    @Test
    public void testMergeBothEmpty() {
        Imports a = Imports.builder().build();
        Imports b = Imports.builder().build();

        Imports merged = Imports.merge(a, b);
        assertTrue(merged.isEmpty());
    }
}
