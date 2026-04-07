package com.walmartlabs.concord.process.loader;

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

import static org.junit.jupiter.api.Assertions.*;

public class StandardRuntimeTypesTest {

    @Test
    public void testConstants() {
        assertEquals("concord-v1", StandardRuntimeTypes.CONCORD_V1_RUNTIME_TYPE);
        assertEquals("concord-v2", StandardRuntimeTypes.CONCORD_V2_RUNTIME_TYPE);
    }

    @Test
    public void testProjectRootFileNames() {
        String[] names = StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES;
        assertEquals(4, names.length);
        assertEquals(".concord.yml", names[0]);
        assertEquals("concord.yml", names[1]);
        assertEquals(".concord.yaml", names[2]);
        assertEquals("concord.yaml", names[3]);
    }
}
