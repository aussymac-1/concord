package com.walmartlabs.concord.common;

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

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ToStringHelperTest {

    @Test
    public void testPrefixOnly() {
        var result = ToStringHelper.prefix("Foo").toString();
        assertEquals("Foo{}", result);
    }

    @Test
    public void testNullPrefix() {
        var result = new ToStringHelper(null).toString();
        assertEquals("{}", result);
    }

    @Test
    public void testAddStringValue() {
        var result = ToStringHelper.prefix("Obj")
                .add("name", "hello")
                .toString();
        assertEquals("Obj{name=\"hello\"}", result);
    }

    @Test
    public void testAddStringNullSkipped() {
        var result = ToStringHelper.prefix("Obj")
                .add("name", (String) null)
                .toString();
        assertEquals("Obj{}", result);
    }

    @Test
    public void testAddNumberValue() {
        var result = ToStringHelper.prefix("Obj")
                .add("count", 42)
                .toString();
        assertEquals("Obj{count=42}", result);
    }

    @Test
    public void testAddNumberNullSkipped() {
        var result = ToStringHelper.prefix("Obj")
                .add("count", (Number) null)
                .toString();
        assertEquals("Obj{}", result);
    }

    @Test
    public void testAddObjectValue() {
        var result = ToStringHelper.prefix("Obj")
                .add("flag", true)
                .toString();
        assertEquals("Obj{flag=true}", result);
    }

    @Test
    public void testAddObjectNullSkipped() {
        var result = ToStringHelper.prefix("Obj")
                .add("obj", (Object) null)
                .toString();
        assertEquals("Obj{}", result);
    }

    @Test
    public void testAddCollectionValue() {
        var result = ToStringHelper.prefix("Obj")
                .add("items", Arrays.asList("a", "b"))
                .toString();
        assertEquals("Obj{items=[a, b]}", result);
    }

    @Test
    public void testAddCollectionNullSkipped() {
        var result = ToStringHelper.prefix("Obj")
                .add("items", (java.util.Collection<?>) null)
                .toString();
        assertEquals("Obj{}", result);
    }

    @Test
    public void testAddCollectionEmptySkipped() {
        var result = ToStringHelper.prefix("Obj")
                .add("items", Collections.emptyList())
                .toString();
        assertEquals("Obj{}", result);
    }

    @Test
    public void testMultipleFields() {
        var result = ToStringHelper.prefix("Step")
                .add("name", "myStep")
                .add("retry", 3)
                .toString();
        assertEquals("Step{name=\"myStep\", retry=3}", result);
    }
}
