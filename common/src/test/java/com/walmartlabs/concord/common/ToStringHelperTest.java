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
    public void testWithNullPrefix() {
        String result = new ToStringHelper(null).toString();
        assertEquals("{}", result);
    }

    @Test
    public void testAddObject() {
        String result = ToStringHelper.prefix("Test")
                .add("key", (Object) 42)
                .toString();
        assertEquals("Test{key=42}", result);
    }

    @Test
    public void testAddNullObject() {
        String result = ToStringHelper.prefix("Test")
                .add("key", (Object) null)
                .toString();
        assertEquals("Test{}", result);
    }

    @Test
    public void testAddNumber() {
        String result = ToStringHelper.prefix("Test")
                .add("count", (Number) 10)
                .toString();
        assertEquals("Test{count=10}", result);
    }

    @Test
    public void testAddNullNumber() {
        String result = ToStringHelper.prefix("Test")
                .add("count", (Number) null)
                .toString();
        assertEquals("Test{}", result);
    }

    @Test
    public void testAddString() {
        String result = ToStringHelper.prefix("Test")
                .add("name", "hello")
                .toString();
        assertEquals("Test{name=\"hello\"}", result);
    }

    @Test
    public void testAddNullString() {
        String result = ToStringHelper.prefix("Test")
                .add("name", (String) null)
                .toString();
        assertEquals("Test{}", result);
    }

    @Test
    public void testAddCollection() {
        String result = ToStringHelper.prefix("Test")
                .add("items", Arrays.asList("a", "b"))
                .toString();
        assertEquals("Test{items=[a, b]}", result);
    }

    @Test
    public void testAddEmptyCollection() {
        String result = ToStringHelper.prefix("Test")
                .add("items", Collections.emptyList())
                .toString();
        assertEquals("Test{}", result);
    }

    @Test
    public void testAddNullCollection() {
        String result = ToStringHelper.prefix("Test")
                .add("items", (java.util.Collection<?>) null)
                .toString();
        assertEquals("Test{}", result);
    }

    @Test
    public void testMultipleFields() {
        String result = ToStringHelper.prefix("Obj")
                .add("a", (Object) 1)
                .add("b", "two")
                .add("c", (Number) 3)
                .toString();
        assertEquals("Obj{a=1, b=\"two\", c=3}", result);
    }
}
