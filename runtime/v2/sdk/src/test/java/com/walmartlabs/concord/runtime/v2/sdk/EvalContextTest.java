package com.walmartlabs.concord.runtime.v2.sdk;

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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EvalContextTest {

    @Test
    public void testDefaults() {
        var ctx = EvalContext.builder().build();

        assertNull(ctx.context());
        assertTrue(ctx.variables() instanceof NoopVariables);
        assertFalse(ctx.useIntermediateResults());
        assertFalse(ctx.undefinedVariableAsNull());
    }

    @Test
    public void testCustomBuilderValues() {
        var vars = new MapBackedVariables(java.util.Map.of("k", "v"));

        var ctx = EvalContext.builder()
                .variables(vars)
                .useIntermediateResults(true)
                .undefinedVariableAsNull(true)
                .build();

        assertSame(vars, ctx.variables());
        assertTrue(ctx.useIntermediateResults());
        assertTrue(ctx.undefinedVariableAsNull());
    }
}
