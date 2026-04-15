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
    void testEmptyHelper() {
        String result = ToStringHelper.prefix("MyClass").toString();
        assertEquals("MyClass{}", result);
    }

    @Test
    void testSingleStringField() {
        String result = ToStringHelper.prefix("Obj")
                .add("name", "hello")
                .toString();
        assertEquals("Obj{name=\"hello\"}", result);
    }

    @Test
    void testMultipleFields() {
        String result = ToStringHelper.prefix("Obj")
                .add("name", "hello")
                .add("count", 42)
                .toString();
        assertEquals("Obj{name=\"hello\", count=42}", result);
    }

    @Test
    void testNullStringOmitted() {
        String result = ToStringHelper.prefix("Obj")
                .add("name", (String) null)
                .add("other", "value")
                .toString();
        assertEquals("Obj{other=\"value\"}", result);
    }

    @Test
    void testZeroNumberNotOmitted() {
        String result = ToStringHelper.prefix("Obj")
                .add("count", 0)
                .add("other", "value")
                .toString();
        assertEquals("Obj{count=0, other=\"value\"}", result);
    }

    @Test
    void testEmptyCollectionOmitted() {
        String result = ToStringHelper.prefix("Obj")
                .add("items", Collections.emptyList())
                .add("other", "value")
                .toString();
        assertEquals("Obj{other=\"value\"}", result);
    }

    @Test
    void testNonEmptyCollection() {
        String result = ToStringHelper.prefix("Obj")
                .add("items", Arrays.asList("a", "b"))
                .toString();
        assertEquals("Obj{items=[a, b]}", result);
    }

    @Test
    void testNullCollectionOmitted() {
        String result = ToStringHelper.prefix("Obj")
                .add("items", (java.util.Collection<?>) null)
                .add("other", "value")
                .toString();
        assertEquals("Obj{other=\"value\"}", result);
    }

    @Test
    void testObjectValue() {
        String result = ToStringHelper.prefix("Obj")
                .add("data", (Object) "test")
                .toString();
        assertEquals("Obj{data=test}", result);
    }

    @Test
    void testNullObjectOmitted() {
        String result = ToStringHelper.prefix("Obj")
                .add("data", (Object) null)
                .toString();
        assertEquals("Obj{}", result);
    }
}
