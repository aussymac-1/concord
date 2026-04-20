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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImportsTest {

    @Test
    public void testDefaultBuilderIsEmpty() {
        Imports imports = Imports.builder().build();

        assertTrue(imports.isEmpty());
        assertTrue(imports.items().isEmpty());
    }

    @Test
    public void testOfEmptyList() {
        Imports imports = Imports.of(Collections.emptyList());

        assertTrue(imports.isEmpty());
    }

    @Test
    public void testOfNonEmptyList() {
        Import.MvnDefinition item = Import.MvnDefinition.builder()
                .url("mvn://a:b:1")
                .build();

        Imports imports = Imports.of(Collections.singletonList(item));

        assertFalse(imports.isEmpty());
        assertEquals(1, imports.items().size());
        assertEquals(item, imports.items().get(0));
    }

    @Test
    public void testMergeCombinesItemsInOrder() {
        Import a = Import.MvnDefinition.builder().url("mvn://a").build();
        Import b = Import.MvnDefinition.builder().url("mvn://b").build();
        Import c = Import.MvnDefinition.builder().url("mvn://c").build();

        Imports left = Imports.of(Arrays.asList(a, b));
        Imports right = Imports.of(Collections.singletonList(c));

        Imports merged = Imports.merge(left, right);

        List<Import> items = merged.items();
        assertEquals(3, items.size());
        assertEquals(a, items.get(0));
        assertEquals(b, items.get(1));
        assertEquals(c, items.get(2));
    }

    @Test
    public void testMergeWithEmptyLeft() {
        Import a = Import.MvnDefinition.builder().url("mvn://a").build();
        Imports left = Imports.builder().build();
        Imports right = Imports.of(Collections.singletonList(a));

        Imports merged = Imports.merge(left, right);

        assertEquals(1, merged.items().size());
        assertEquals(a, merged.items().get(0));
    }

    @Test
    public void testMergeWithEmptyRight() {
        Import a = Import.MvnDefinition.builder().url("mvn://a").build();
        Imports left = Imports.of(Collections.singletonList(a));
        Imports right = Imports.builder().build();

        Imports merged = Imports.merge(left, right);

        assertEquals(1, merged.items().size());
        assertEquals(a, merged.items().get(0));
    }

    @Test
    public void testMergeOfTwoEmptyIsEmpty() {
        Imports merged = Imports.merge(
                Imports.builder().build(),
                Imports.builder().build());

        assertTrue(merged.isEmpty());
    }
}
