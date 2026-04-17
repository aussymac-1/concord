package com.walmartlabs.concord.plugins.misc;

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

import static org.assertj.core.api.Assertions.assertThat;

class CollectionsTaskV2Test {

    @Test
    void concatMergesInOrder() {
        List<Integer> result = CollectionsTaskV2.concat(List.of(1, 2), List.of(3, 4));

        assertThat(result).containsExactly(1, 2, 3, 4);
    }

    @Test
    void concatSkipsNullLists() {
        List<Integer> result = CollectionsTaskV2.concat(List.of(1), null, List.of(2));

        assertThat(result).containsExactly(1, 2);
    }

    @Test
    void concatPreservesDuplicates() {
        List<Integer> result = CollectionsTaskV2.concat(List.of(1, 2), List.of(2, 3));

        assertThat(result).containsExactly(1, 2, 2, 3);
    }

    @Test
    void concatWithNoInputsReturnsEmpty() {
        List<Integer> result = CollectionsTaskV2.concat();

        assertThat(result).isEmpty();
    }
}
