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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImportsTest {

    @Test
    public void defaultBuilderYieldsEmpty() {
        var imports = Imports.builder().build();
        assertTrue(imports.isEmpty());
        assertEquals(Collections.emptyList(), imports.items());
    }

    @Test
    public void ofWrapsItems() {
        var mvn = Import.MvnDefinition.builder().url("mvn://group/artifact").build();
        var imports = Imports.of(List.of(mvn));
        assertFalse(imports.isEmpty());
        assertEquals(1, imports.items().size());
        assertEquals(mvn, imports.items().get(0));
    }

    @Test
    public void mergeConcatenatesItemsFromBothSides() {
        var a = Imports.of(List.of(Import.MvnDefinition.builder().url("mvn://a").build()));
        var b = Imports.of(List.of(Import.MvnDefinition.builder().url("mvn://b").build()));

        var merged = Imports.merge(a, b);
        assertEquals(2, merged.items().size());
        assertEquals("mvn://a", ((Import.MvnDefinition) merged.items().get(0)).url());
        assertEquals("mvn://b", ((Import.MvnDefinition) merged.items().get(1)).url());
    }

    @Test
    public void mergePreservesOrder() {
        var items = new java.util.ArrayList<Import>();
        for (var i = 0; i < 3; i++) {
            items.add(Import.MvnDefinition.builder().url("mvn://" + i).build());
        }
        var a = Imports.of(items.subList(0, 2));
        var b = Imports.of(items.subList(2, 3));

        var merged = Imports.merge(a, b);
        assertEquals(3, merged.items().size());
        for (var i = 0; i < 3; i++) {
            assertEquals("mvn://" + i, ((Import.MvnDefinition) merged.items().get(i)).url());
        }
    }

    @Test
    public void mergeWithEmptyIsIdentity() {
        var a = Imports.of(List.of(Import.MvnDefinition.builder().url("mvn://a").build()));
        var empty = Imports.builder().build();
        assertEquals(a.items(), Imports.merge(a, empty).items());
        assertEquals(a.items(), Imports.merge(empty, a).items());
    }
}
