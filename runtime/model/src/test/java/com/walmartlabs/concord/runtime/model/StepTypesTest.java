package com.walmartlabs.concord.runtime.model;

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

import java.io.Serializable;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepTypesTest {

    @Test
    public void testExpressionStepDefaults() {
        var sm = SourceMap.builder().source("a.yml").line(1).column(1).build();

        var step = ExpressionStep.builder()
                .expression("${x}")
                .location(sm)
                .build();

        assertEquals("${x}", step.expression());
        assertSame(sm, step.location());
        assertTrue(step.input().isEmpty());
    }

    @Test
    public void testExpressionStepWithInputAllowsNullValue() {
        var sm = SourceMap.builder().source("a.yml").line(1).column(1).build();
        var input = new HashMap<String, Serializable>();
        input.put("a", "1");
        input.put("b", null);

        var step = ExpressionStep.builder()
                .expression("${a}")
                .input(input)
                .location(sm)
                .build();

        assertEquals(2, step.input().size());
        assertEquals("1", step.input().get("a"));
        assertTrue(step.input().containsKey("b"));
    }

    @Test
    public void testExpressionStepMissingFieldsThrow() {
        var builder = ExpressionStep.builder();

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    public void testTaskCallStepDefaults() {
        var sm = SourceMap.builder().source("a.yml").line(1).column(1).build();

        var step = TaskCallStep.builder()
                .name("docker")
                .location(sm)
                .build();

        assertEquals("docker", step.name());
        assertSame(sm, step.location());
        assertTrue(step.input().isEmpty());
    }

    @Test
    public void testTaskCallStepWithInputAllowsNullValue() {
        var sm = SourceMap.builder().source("a.yml").line(1).column(1).build();
        var input = new HashMap<String, Serializable>();
        input.put("k", null);

        var step = TaskCallStep.builder()
                .name("noop")
                .input(input)
                .location(sm)
                .build();

        assertTrue(step.input().containsKey("k"));
    }

    @Test
    public void testStepEqualityIsValueBased() {
        var sm = SourceMap.builder().source("a.yml").line(1).column(1).build();

        var a = TaskCallStep.builder().name("docker").location(sm).build();
        var b = TaskCallStep.builder().name("docker").location(sm).build();
        var c = TaskCallStep.builder().name("ansible").location(sm).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
