package com.walmartlabs.concord.common;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2021 Walmart Inc.
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

class ToStringHelperTest {

    @Test
    void testEmptyWithPrefix() {
        String result = ToStringHelper.prefix("Foo").toString();
        assertEquals("Foo{}", result);
    }

    @Test
    void testSingleStringField() {
        String result = ToStringHelper.prefix("Foo")
                .add("name", "bar")
                .toString();
        assertEquals("Foo{name=\"bar\"}", result);
    }

    @Test
    void testMultipleFields() {
        String result = ToStringHelper.prefix("Obj")
                .add("id", 42)
                .add("name", "test")
                .toString();
        assertEquals("Obj{id=42, name=\"test\"}", result);
    }

    @Test
    void testNullValuesSkipped() {
        String result = ToStringHelper.prefix("Obj")
                .add("id", 42)
                .add("name", (String) null)
                .add("count", (Number) null)
                .toString();
        assertEquals("Obj{id=42}", result);
    }

    @Test
    void testEmptyCollectionSkipped() {
        String result = ToStringHelper.prefix("Obj")
                .add("items", Collections.emptyList())
                .add("id", 1)
                .toString();
        assertEquals("Obj{id=1}", result);
    }

    @Test
    void testNonEmptyCollection() {
        String result = ToStringHelper.prefix("Obj")
                .add("items", Arrays.asList("a", "b"))
                .toString();
        assertEquals("Obj{items=[a, b]}", result);
    }

    @Test
    void testNullPrefix() {
        String result = new ToStringHelper(null)
                .add("x", 1)
                .toString();
        assertEquals("{x=1}", result);
    }
}
