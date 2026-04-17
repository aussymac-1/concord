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

import static org.assertj.core.api.Assertions.assertThat;

class ToStringHelperTest {

    @Test
    void emptyHelperWithPrefix() {
        String result = ToStringHelper.prefix("MyClass").toString();
        assertThat(result).isEqualTo("MyClass{}");
    }

    @Test
    void addObjectValue() {
        String result = ToStringHelper.prefix("T")
                .add("key", (Object) 42)
                .toString();
        assertThat(result).isEqualTo("T{key=42}");
    }

    @Test
    void addNumberValue() {
        String result = ToStringHelper.prefix("T")
                .add("num", (Number) 3.14)
                .toString();
        assertThat(result).isEqualTo("T{num=3.14}");
    }

    @Test
    void addStringValueWrapsInQuotes() {
        String result = ToStringHelper.prefix("T")
                .add("name", "hello")
                .toString();
        assertThat(result).isEqualTo("T{name=\"hello\"}");
    }

    @Test
    void addNullStringSkipsEntry() {
        String result = ToStringHelper.prefix("T")
                .add("name", (String) null)
                .toString();
        assertThat(result).isEqualTo("T{}");
    }

    @Test
    void addCollectionValue() {
        String result = ToStringHelper.prefix("T")
                .add("items", Arrays.asList("a", "b"))
                .toString();
        assertThat(result).isEqualTo("T{items=[a, b]}");
    }

    @Test
    void addEmptyCollectionSkipsEntry() {
        String result = ToStringHelper.prefix("T")
                .add("items", Collections.emptyList())
                .toString();
        assertThat(result).isEqualTo("T{}");
    }

    @Test
    void addNullCollectionSkipsEntry() {
        String result = ToStringHelper.prefix("T")
                .add("items", (java.util.Collection<?>) null)
                .toString();
        assertThat(result).isEqualTo("T{}");
    }

    @Test
    void multipleEntriesSeparatedByComma() {
        String result = ToStringHelper.prefix("T")
                .add("a", (Object) 1)
                .add("b", (Object) 2)
                .toString();
        assertThat(result).isEqualTo("T{a=1, b=2}");
    }

    @Test
    void nullPrefixProducesNoPrefixString() {
        String result = new ToStringHelper(null)
                .add("x", (Object) 1)
                .toString();
        assertThat(result).isEqualTo("{x=1}");
    }

    @Test
    void addNullObjectSkipsEntry() {
        String result = ToStringHelper.prefix("T")
                .add("key", (Object) null)
                .toString();
        assertThat(result).isEqualTo("T{}");
    }
}
