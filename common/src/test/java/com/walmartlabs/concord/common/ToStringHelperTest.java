package com.walmartlabs.concord.common;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2024 Walmart Inc.
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
    public void testEmptyWithNullPrefix() {
        String result = new ToStringHelper(null).toString();
        assertEquals("{}", result);
    }

    @Test
    public void testAddObject() {
        String result = ToStringHelper.prefix("T")
                .add("key", (Object) 42)
                .toString();
        assertEquals("T{key=42}", result);
    }

    @Test
    public void testAddNumber() {
        String result = ToStringHelper.prefix("T")
                .add("num", (Number) 3.14)
                .toString();
        assertEquals("T{num=3.14}", result);
    }

    @Test
    public void testAddString() {
        String result = ToStringHelper.prefix("T")
                .add("name", "hello")
                .toString();
        assertEquals("T{name=\"hello\"}", result);
    }

    @Test
    public void testAddNullStringSkipped() {
        String result = ToStringHelper.prefix("T")
                .add("name", (String) null)
                .toString();
        assertEquals("T{}", result);
    }

    @Test
    public void testAddCollection() {
        String result = ToStringHelper.prefix("T")
                .add("items", Arrays.asList("a", "b"))
                .toString();
        assertEquals("T{items=[a, b]}", result);
    }

    @Test
    public void testAddEmptyCollectionSkipped() {
        String result = ToStringHelper.prefix("T")
                .add("items", Collections.emptyList())
                .toString();
        assertEquals("T{}", result);
    }

    @Test
    public void testAddNullCollectionSkipped() {
        String result = ToStringHelper.prefix("T")
                .add("items", (java.util.Collection<?>) null)
                .toString();
        assertEquals("T{}", result);
    }

    @Test
    public void testMultipleFields() {
        String result = ToStringHelper.prefix("T")
                .add("a", (Object) 1)
                .add("b", "two")
                .toString();
        assertEquals("T{a=1, b=\"two\"}", result);
    }

    @Test
    public void testNullObjectSkipped() {
        String result = ToStringHelper.prefix("T")
                .add("key", (Object) null)
                .toString();
        assertEquals("T{}", result);
    }

    @Test
    public void testNullNumberSkipped() {
        String result = ToStringHelper.prefix("T")
                .add("num", (Number) null)
                .toString();
        assertEquals("T{}", result);
    }
}
