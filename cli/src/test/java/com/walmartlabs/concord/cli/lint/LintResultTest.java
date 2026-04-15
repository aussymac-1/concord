package com.walmartlabs.concord.cli.lint;

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

import com.walmartlabs.concord.runtime.model.ImmutableSourceMap;
import com.walmartlabs.concord.runtime.model.SourceMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LintResultTest {

    @Test
    void testErrorFactory() {
        SourceMap sm = ImmutableSourceMap.builder()
                .source("test.yml")
                .line(10)
                .column(5)
                .build();
        LintResult result = LintResult.error(sm, "something wrong");
        assertEquals(LintResult.Type.ERROR, result.getType());
        assertEquals("something wrong", result.getMessage());
        assertSame(sm, result.getSourceMap());
    }

    @Test
    void testConstructor() {
        SourceMap sm = ImmutableSourceMap.builder()
                .source("flow.yml")
                .line(1)
                .column(1)
                .build();
        LintResult result = new LintResult(LintResult.Type.WARNING, sm, "warning msg");
        assertEquals(LintResult.Type.WARNING, result.getType());
        assertEquals("warning msg", result.getMessage());
        assertEquals(sm, result.getSourceMap());
    }

    @Test
    void testTypeValues() {
        assertEquals(2, LintResult.Type.values().length);
        assertNotNull(LintResult.Type.valueOf("WARNING"));
        assertNotNull(LintResult.Type.valueOf("ERROR"));
    }
}
