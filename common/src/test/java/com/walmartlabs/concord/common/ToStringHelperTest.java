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
    public void testEmptyWithPrefix() {
        String result = ToStringHelper.prefix("MyClass").toString();
        assertEquals("MyClass{}", result);
    }

    @Test
    public void testSingleValue() {
        String result = ToStringHelper.prefix("MyClass")
                .add("name", (Object) "value")
                .toString();
        assertEquals("MyClass{name=value}", result);
    }

    @Test
    public void testMultipleValues() {
        String result = ToStringHelper.prefix("MyClass")
                .add("a", (Object) 1)
                .add("b", (Object) 2)
                .toString();
        assertEquals("MyClass{a=1, b=2}", result);
    }

    @Test
    public void testStringValue() {
        String result = ToStringHelper.prefix("MyClass")
                .add("name", "hello")
                .toString();
        assertEquals("MyClass{name=\"hello\"}", result);
    }

    @Test
    public void testNullStringValue() {
        String result = ToStringHelper.prefix("MyClass")
                .add("name", (String) null)
                .toString();
        assertEquals("MyClass{}", result);
    }

    @Test
    public void testNullObjectValue() {
        String result = ToStringHelper.prefix("MyClass")
                .add("name", (Object) null)
                .toString();
        assertEquals("MyClass{}", result);
    }

    @Test
    public void testNullNumberValue() {
        String result = ToStringHelper.prefix("MyClass")
                .add("name", (Number) null)
                .toString();
        assertEquals("MyClass{}", result);
    }

    @Test
    public void testCollectionValue() {
        String result = ToStringHelper.prefix("MyClass")
                .add("items", Arrays.asList("a", "b"))
                .toString();
        assertEquals("MyClass{items=[a, b]}", result);
    }

    @Test
    public void testEmptyCollectionSkipped() {
        String result = ToStringHelper.prefix("MyClass")
                .add("items", Collections.emptyList())
                .toString();
        assertEquals("MyClass{}", result);
    }

    @Test
    public void testNullCollectionSkipped() {
        String result = ToStringHelper.prefix("MyClass")
                .add("items", (java.util.Collection<?>) null)
                .toString();
        assertEquals("MyClass{}", result);
    }

    @Test
    public void testNumberValue() {
        String result = ToStringHelper.prefix("MyClass")
                .add("count", 42)
                .toString();
        assertEquals("MyClass{count=42}", result);
    }
}
