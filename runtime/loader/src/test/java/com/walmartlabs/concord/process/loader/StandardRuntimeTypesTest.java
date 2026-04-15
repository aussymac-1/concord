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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StandardRuntimeTypesTest {

    @Test
    void testConcordV1RuntimeType() {
        assertEquals("concord-v1", StandardRuntimeTypes.CONCORD_V1_RUNTIME_TYPE);
    }

    @Test
    void testConcordV2RuntimeType() {
        assertEquals("concord-v2", StandardRuntimeTypes.CONCORD_V2_RUNTIME_TYPE);
    }

    @Test
    void testProjectRootFileNames() {
        String[] names = StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES;
        assertNotNull(names);
        assertTrue(names.length > 0);

        List<String> nameList = Arrays.asList(names);
        assertTrue(nameList.contains(".concord.yml"));
        assertTrue(nameList.contains("concord.yml"));
        assertTrue(nameList.contains(".concord.yaml"));
        assertTrue(nameList.contains("concord.yaml"));
    }

    @Test
    void testProjectRootFileNamesCount() {
        assertEquals(4, StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES.length);
    }
}
