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
    public void testMultipleFields() {
        String result = ToStringHelper.prefix("Obj")
                .add("a", "x")
                .add("b", 42)
                .toString();
        assertEquals("Obj{a=\"x\", b=42}", result);
    }

    @Test
    public void testNullStringSkipped() {
        String result = ToStringHelper.prefix("Obj")
                .add("a", (String) null)
                .add("b", "val")
                .toString();
        assertEquals("Obj{b=\"val\"}", result);
    }

    @Test
    public void testNullObjectSkipped() {
        String result = ToStringHelper.prefix("Obj")
                .add("a", (Object) null)
                .add("b", 1)
                .toString();
        assertEquals("Obj{b=1}", result);
    }

    @Test
    public void testEmptyCollectionSkipped() {
        String result = ToStringHelper.prefix("Obj")
                .add("list", Collections.emptyList())
                .add("b", "val")
                .toString();
        assertEquals("Obj{b=\"val\"}", result);
    }

    @Test
    public void testNonEmptyCollection() {
        String result = ToStringHelper.prefix("Obj")
                .add("list", Arrays.asList("a", "b"))
                .toString();
        assertEquals("Obj{list=[a, b]}", result);
    }

    @Test
    public void testNoPrefix() {
        String result = new ToStringHelper(null)
                .add("x", 1)
                .toString();
        assertEquals("{x=1}", result);
    }

    @Test
    public void testEmpty() {
        String result = ToStringHelper.prefix("Empty").toString();
        assertEquals("Empty{}", result);
    }

    @Test
    public void testNumberField() {
        String result = ToStringHelper.prefix("N")
                .add("count", 99)
                .toString();
        assertEquals("N{count=99}", result);
    }

    @Test
    public void testNullNumberSkipped() {
        String result = ToStringHelper.prefix("N")
                .add("count", (Number) null)
                .add("val", 1)
                .toString();
        assertEquals("N{val=1}", result);
    }
}
