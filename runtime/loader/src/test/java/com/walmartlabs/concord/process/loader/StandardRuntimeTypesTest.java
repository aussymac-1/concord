package com.walmartlabs.concord.process.loader;

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

import static org.assertj.core.api.Assertions.assertThat;

class StandardRuntimeTypesTest {

    @Test
    void v1RuntimeTypeConstant() {
        assertThat(StandardRuntimeTypes.CONCORD_V1_RUNTIME_TYPE).isEqualTo("concord-v1");
    }

    @Test
    void v2RuntimeTypeConstant() {
        assertThat(StandardRuntimeTypes.CONCORD_V2_RUNTIME_TYPE).isEqualTo("concord-v2");
    }

    @Test
    void projectRootFileNamesContainExpectedFiles() {
        assertThat(StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES).containsExactly(
                ".concord.yml",
                "concord.yml",
                ".concord.yaml",
                "concord.yaml"
        );
    }

    @Test
    void projectRootFileNamesHasExpectedCount() {
        assertThat(StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES).hasSize(4);
    }
}
