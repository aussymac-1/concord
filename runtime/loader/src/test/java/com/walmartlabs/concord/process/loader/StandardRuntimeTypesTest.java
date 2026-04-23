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

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StandardRuntimeTypesTest {

    @Test
    public void runtimeTypeKeysAreStable() {
        assertEquals("concord-v1", StandardRuntimeTypes.CONCORD_V1_RUNTIME_TYPE);
        assertEquals("concord-v2", StandardRuntimeTypes.CONCORD_V2_RUNTIME_TYPE);
        assertNotEquals(StandardRuntimeTypes.CONCORD_V1_RUNTIME_TYPE,
                StandardRuntimeTypes.CONCORD_V2_RUNTIME_TYPE);
    }

    @Test
    public void projectRootFileNamesIncludeAllConventionalVariants() {
        var names = new HashSet<>(Arrays.asList(StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES));
        assertTrue(names.contains("concord.yml"));
        assertTrue(names.contains(".concord.yml"));
        assertTrue(names.contains("concord.yaml"));
        assertTrue(names.contains(".concord.yaml"));
    }

    @Test
    public void projectRootFileNamesAreDistinct() {
        assertEquals(StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES.length,
                new HashSet<>(Arrays.asList(StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES)).size());
    }

    @Test
    public void projectRootFileNamesAreNotEmpty() {
        assertTrue(StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES.length >= 1);
        for (var n : StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES) {
            assertNotNull(n);
            assertTrue(!n.isBlank());
        }
    }

    @Test
    public void constructorIsPrivate() throws Exception {
        var ctor = StandardRuntimeTypes.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(ctor.getModifiers()));
        ctor.setAccessible(true);
        assertNotNull(ctor.newInstance());
    }

    @Test
    public void classIsFinal() {
        assertTrue(Modifier.isFinal(StandardRuntimeTypes.class.getModifiers()));
    }
}
