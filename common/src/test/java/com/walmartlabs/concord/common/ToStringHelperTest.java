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
    public void testWithPrefix() {
        String result = ToStringHelper.prefix("MyClass")
                .add("name", "test")
                .toString();

        assertEquals("MyClass{name=\"test\"}", result);
    }

    @Test
    public void testWithNullPrefix() {
        String result = ToStringHelper.prefix(null)
                .add("key", 42)
                .toString();

        assertEquals("{key=42}", result);
    }

    @Test
    public void testMultipleValues() {
        String result = ToStringHelper.prefix("Obj")
                .add("a", 1)
                .add("b", "val")
                .toString();

        assertEquals("Obj{a=1, b=\"val\"}", result);
    }

    @Test
    public void testNullStringValue() {
        String result = ToStringHelper.prefix("Obj")
                .add("name", (String) null)
                .toString();

        assertEquals("Obj{}", result);
    }

    @Test
    public void testNullObjectValue() {
        String result = ToStringHelper.prefix("Obj")
                .add("data", (Object) null)
                .toString();

        assertEquals("Obj{}", result);
    }

    @Test
    public void testNullNumberValue() {
        String result = ToStringHelper.prefix("Obj")
                .add("count", (Number) null)
                .toString();

        assertEquals("Obj{}", result);
    }

    @Test
    public void testEmptyCollection() {
        String result = ToStringHelper.prefix("Obj")
                .add("items", Collections.emptyList())
                .toString();

        assertEquals("Obj{}", result);
    }

    @Test
    public void testNonEmptyCollection() {
        String result = ToStringHelper.prefix("Obj")
                .add("items", Arrays.asList("a", "b"))
                .toString();

        assertEquals("Obj{items=[a, b]}", result);
    }

    @Test
    public void testNullCollection() {
        String result = ToStringHelper.prefix("Obj")
                .add("items", (java.util.Collection<?>) null)
                .toString();

        assertEquals("Obj{}", result);
    }

    @Test
    public void testEmpty() {
        String result = ToStringHelper.prefix("Empty").toString();
        assertEquals("Empty{}", result);
    }
}
