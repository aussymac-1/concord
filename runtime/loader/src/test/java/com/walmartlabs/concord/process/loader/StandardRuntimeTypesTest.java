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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StandardRuntimeTypesTest {

    @Test
    public void runtimeTypeConstantsAreStable() {
        assertEquals("concord-v1", StandardRuntimeTypes.CONCORD_V1_RUNTIME_TYPE);
        assertEquals("concord-v2", StandardRuntimeTypes.CONCORD_V2_RUNTIME_TYPE);
    }

    @Test
    public void projectRootFilenamesCoverBothExtensionsAndDotVariants() {
        List<String> names = Arrays.asList(StandardRuntimeTypes.PROJECT_ROOT_FILE_NAMES);

        assertEquals(4, names.size());
        assertTrue(names.contains(".concord.yml"));
        assertTrue(names.contains("concord.yml"));
        assertTrue(names.contains(".concord.yaml"));
        assertTrue(names.contains("concord.yaml"));
    }

    @Test
    public void classIsFinalWithPrivateConstructor() throws Exception {
        assertTrue(Modifier.isFinal(StandardRuntimeTypes.class.getModifiers()));

        Constructor<StandardRuntimeTypes> ctor = StandardRuntimeTypes.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(ctor.getModifiers()));
        ctor.setAccessible(true);
        assertNotNull(ctor.newInstance());
    }
}
