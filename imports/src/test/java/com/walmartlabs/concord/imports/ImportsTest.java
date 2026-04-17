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

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ImportsTest {

    @Test
    void emptyImportsIsEmpty() {
        Imports imports = Imports.of(Collections.emptyList());
        assertThat(imports.isEmpty()).isTrue();
        assertThat(imports.items()).isEmpty();
    }

    @Test
    void ofCreatesImportsWithItems() {
        Import item = new Import() {
            @Override
            public String type() {
                return "git";
            }
        };
        Imports imports = Imports.of(Collections.singletonList(item));
        assertThat(imports.isEmpty()).isFalse();
        assertThat(imports.items()).hasSize(1);
    }

    @Test
    void mergeWithBothNonNull() {
        Import item1 = new Import() {
            @Override
            public String type() {
                return "git";
            }
        };
        Import item2 = new Import() {
            @Override
            public String type() {
                return "mvn";
            }
        };
        Imports a = Imports.of(Collections.singletonList(item1));
        Imports b = Imports.of(Collections.singletonList(item2));
        Imports result = Imports.merge(a, b);
        assertThat(result.items()).hasSize(2);
    }

    @Test
    void builderCreatesImports() {
        Imports imports = Imports.builder().build();
        assertThat(imports.isEmpty()).isTrue();
    }
}
