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
        Import.MvnDefinition mvn = Import.MvnDefinition.builder()
                .url("mvn://com.example:lib:1.0")
                .build();
        Imports imports = Imports.of(List.of(mvn));
        assertFalse(imports.isEmpty());
        assertEquals(1, imports.items().size());
    }

    @Test
    public void testMerge() {
        Import.MvnDefinition mvn1 = Import.MvnDefinition.builder()
                .url("mvn://com.example:lib1:1.0")
                .build();
        Import.MvnDefinition mvn2 = Import.MvnDefinition.builder()
                .url("mvn://com.example:lib2:2.0")
                .build();

        Imports a = Imports.of(List.of(mvn1));
        Imports b = Imports.of(List.of(mvn2));
        Imports merged = Imports.merge(a, b);

        assertEquals(2, merged.items().size());
    }

    @Test
    public void testMergeWithEmpty() {
        Import.MvnDefinition mvn = Import.MvnDefinition.builder()
                .url("mvn://com.example:lib:1.0")
                .build();

        Imports a = Imports.of(List.of(mvn));
        Imports b = Imports.builder().build();
        Imports merged = Imports.merge(a, b);

        assertEquals(1, merged.items().size());
    }
}
