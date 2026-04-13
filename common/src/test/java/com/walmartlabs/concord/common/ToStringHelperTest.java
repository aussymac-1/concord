package com.walmartlabs.concord.common;

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

import static org.junit.jupiter.api.Assertions.*;

public class ToStringHelperTest {

    @Test
    public void testEmptyWithPrefix() {
        var result = ToStringHelper.prefix("MyClass").toString();
        assertEquals("MyClass{}", result);
    }

    @Test
    public void testEmptyWithoutPrefix() {
        var result = new ToStringHelper(null).toString();
        assertEquals("{}", result);
    }

    @Test
    public void testAddObject() {
        var result = ToStringHelper.prefix("X")
                .add("key", (Object) 123)
                .toString();
        assertEquals("X{key=123}", result);
    }

    @Test
    public void testAddNumber() {
        var result = ToStringHelper.prefix("X")
                .add("n", (Number) 42)
                .toString();
        assertEquals("X{n=42}", result);
    }

    @Test
    public void testAddStringQuoted() {
        var result = ToStringHelper.prefix("X")
                .add("s", "hello")
                .toString();
        assertEquals("X{s=\"hello\"}", result);
    }

    @Test
    public void testAddNullStringSkipped() {
        var result = ToStringHelper.prefix("X")
                .add("s", (String) null)
                .toString();
        assertEquals("X{}", result);
    }

    @Test
    public void testAddCollectionSkippedWhenEmpty() {
        var result = ToStringHelper.prefix("X")
                .add("c", Collections.emptyList())
                .toString();
        assertEquals("X{}", result);
    }

    @Test
    public void testAddCollectionIncludedWhenNotEmpty() {
        var result = ToStringHelper.prefix("X")
                .add("c", Arrays.asList("a", "b"))
                .toString();
        assertEquals("X{c=[a, b]}", result);
    }

    @Test
    public void testMultipleFieldsSeparated() {
        var result = ToStringHelper.prefix("T")
                .add("a", (Object) 1)
                .add("b", (Object) 2)
                .toString();
        assertEquals("T{a=1, b=2}", result);
    }

    @Test
    public void testNullObjectSkipped() {
        var result = ToStringHelper.prefix("T")
                .add("a", (Object) null)
                .add("b", (Object) 2)
                .toString();
        assertEquals("T{b=2}", result);
    }

    @Test
    public void testNullCollectionSkipped() {
        var result = ToStringHelper.prefix("T")
                .add("c", (java.util.Collection<?>) null)
                .toString();
        assertEquals("T{}", result);
    }
}
