package com.walmartlabs.concord.imports;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2024 Walmart Inc.
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

public class ImportProcessingExceptionTest {

    @Test
    public void testExceptionMessage() {
        var git = Import.GitDefinition.builder()
                .url("https://example.com/repo.git")
                .version("main")
                .build();

        var cause = new RuntimeException("connection refused");
        var ex = new ImportProcessingException(git, cause);

        assertEquals("connection refused", ex.getMessage());
        assertSame(cause, ex.getCause());
        assertSame(git, ex.getImport());
    }
}
