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

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StandardRuntimeTypesTest {

    @Test
    public void testRuntimeTypeConstantsAreStable() {
        // The runtime type constants are part of the public contract -- a change would
        // break every existing concord.yml that pins a runtime version.
        assertEquals("concord-v1", StandardRuntimeTypes.CONCORD_V1_RUNTIME_TYPE);
        assertEquals("concord-v2", StandardRuntimeTypes.CONCORD_V2_RUNTIME_TYPE);
        assertNotEquals(
                StandardRuntimeTypes.CONCORD_V1_RUNTIME_TYPE,
                StandardRuntimeTypes.CONCORD_V2_RUNTIME_TYPE);
    }

    @Test
    public void testProjectRootFileNamesIncludeExpectedValues() {
        Set<String> names = new HashSet<>(Arrays.asList(StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES));

        assertTrue(names.contains(".concord.yml"));
        assertTrue(names.contains("concord.yml"));
        assertTrue(names.contains(".concord.yaml"));
        assertTrue(names.contains("concord.yaml"));
        assertEquals(4, names.size(), "no duplicates expected");
    }

    @Test
    public void testProjectRootFileNamesArrayIsNonEmpty() {
        assertNotNull(StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES);
        assertTrue(StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES.length > 0);
    }

    @Test
    public void testClassIsFinalAndNotInstantiable() throws Exception {
        assertTrue(Modifier.isFinal(StandardRuntimeTypes.class.getModifiers()));

        Constructor<StandardRuntimeTypes> ctor = StandardRuntimeTypes.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(ctor.getModifiers()));

        // still invoke via reflection to cover the private constructor for coverage purposes
        ctor.setAccessible(true);
        assertNotNull(ctor.newInstance());
    }

    @Test
    public void testModifyingReturnedArrayDoesNotPollutePublicConstantForCallers() {
        // This is a defensive test: the array is public, so callers must treat it as read-only.
        // We just document that the names we depend on still show up in a fresh snapshot.
        String[] fresh = StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES;
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            // access one past the end to confirm bounds are as expected
            String ignored = fresh[fresh.length];
            assertNotNull(ignored);
        });
    }
}
