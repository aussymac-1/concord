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

import static org.junit.jupiter.api.Assertions.*;

public class ImportsTest {

    private static Import gitImport(String name) {
        return Import.GitDefinition.builder().name(name).url("https://example.com/" + name).build();
    }

    @Test
    public void testEmptyImports() {
        var imports = Imports.builder().build();
        assertTrue(imports.isEmpty());
        assertEquals(List.of(), imports.items());
    }

    @Test
    public void testFromList() {
        var i = gitImport("a");
        var imports = Imports.of(List.of(i));
        assertFalse(imports.isEmpty());
        assertEquals(1, imports.items().size());
        assertSame(i, imports.items().get(0));
    }

    @Test
    public void testMergeKeepsOrder() {
        var a = Imports.of(List.of(gitImport("a"), gitImport("b")));
        var b = Imports.of(List.of(gitImport("c")));

        var merged = Imports.merge(a, b);

        assertEquals(3, merged.items().size());
        var names = merged.items().stream()
                .map(i -> ((Import.GitDefinition) i).name())
                .toList();
        assertEquals(List.of("a", "b", "c"), names);
    }

    @Test
    public void testMergeWithEmpty() {
        var a = Imports.builder().build();
        var b = Imports.of(List.of(gitImport("x")));

        assertEquals(1, Imports.merge(a, b).items().size());
        assertEquals(1, Imports.merge(b, a).items().size());
    }
}
