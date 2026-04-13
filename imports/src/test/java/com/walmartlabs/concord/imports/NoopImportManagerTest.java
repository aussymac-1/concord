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

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class NoopImportManagerTest {

    @Test
    public void testProcessReturnsEmptyList() {
        var manager = new NoopImportManager();
        var result = manager.process(Imports.builder().build(), Paths.get("/tmp"), null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testProcessWithNullListener() {
        var manager = new NoopImportManager();
        var imports = Imports.builder().build();
        var result = manager.process(imports, Paths.get("/tmp"), null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
