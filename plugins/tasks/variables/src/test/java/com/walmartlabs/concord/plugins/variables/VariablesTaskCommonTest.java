package com.walmartlabs.concord.plugins.variables;

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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VariablesTaskCommonTest {

    @Test
    public void testConcatBothNonNull() {
        List<Object> a = Arrays.asList("x", "y");
        List<Object> b = Arrays.asList("z");
        var result = VariablesTaskCommon.concat(a, b);
        assertEquals(3, result.size());
        assertEquals("x", result.get(0));
        assertEquals("y", result.get(1));
        assertEquals("z", result.get(2));
    }

    @Test
    public void testConcatFirstNull() {
        List<Object> b = Arrays.asList("a", "b");
        var result = VariablesTaskCommon.concat(null, b);
        assertEquals(2, result.size());
        assertEquals("a", result.get(0));
    }

    @Test
    public void testConcatSecondNull() {
        List<Object> a = Arrays.asList("a");
        var result = VariablesTaskCommon.concat(a, null);
        assertEquals(1, result.size());
        assertEquals("a", result.get(0));
    }

    @Test
    public void testConcatBothNull() {
        var result = VariablesTaskCommon.concat(null, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testConcatBothEmpty() {
        var result = VariablesTaskCommon.concat(Collections.emptyList(), Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testEvalVariableGetters() {
        var ev = new VariablesTaskCommon.EvalVariable("myName", "myValue", String.class);
        assertEquals("myName", ev.name());
        assertEquals("myValue", ev.value());
        assertEquals(String.class, ev.clazz());
    }
}
