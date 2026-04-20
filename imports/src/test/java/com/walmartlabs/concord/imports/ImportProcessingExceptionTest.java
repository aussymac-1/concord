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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ImportProcessingExceptionTest {

    @Test
    public void testExceptionCarriesImportAndCause() {
        Import item = Import.MvnDefinition.builder()
                .url("mvn://example:artifact:1.0.0")
                .build();
        Exception cause = new RuntimeException("boom");

        ImportProcessingException ex = new ImportProcessingException(item, cause);

        assertSame(item, ex.getImport());
        assertSame(cause, ex.getCause());
        assertEquals("boom", ex.getMessage());
    }

    @Test
    public void testExceptionPropagatesCauseMessage() {
        Import item = Import.DirectoryDefinition.builder()
                .src("/src")
                .build();
        Exception cause = new IllegalStateException("invalid directory");

        ImportProcessingException ex = new ImportProcessingException(item, cause);

        assertEquals("invalid directory", ex.getMessage());
        assertSame(item, ex.getImport());
    }
}
