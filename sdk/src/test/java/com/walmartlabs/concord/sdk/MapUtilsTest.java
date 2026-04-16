package com.walmartlabs.concord.sdk;

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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MapUtilsTest {

    enum TestEnum { FOO, BAR }

    @Test
    void getEnumReturnsValueWhenPresent() {
        Map<String, Object> m = Map.of("e", "FOO");
        assertThat(MapUtils.getEnum(m, "e", TestEnum.class, TestEnum.BAR)).isEqualTo(TestEnum.FOO);
    }

    @Test
    void getEnumReturnsDefaultWhenMissing() {
        Map<String, Object> m = Map.of();
        assertThat(MapUtils.getEnum(m, "e", TestEnum.class, TestEnum.BAR)).isEqualTo(TestEnum.BAR);
    }

    @Test
    void getEnumThrowsOnInvalidValue() {
        Map<String, Object> m = Map.of("e", "INVALID");
        assertThatThrownBy(() -> MapUtils.getEnum(m, "e", TestEnum.class, TestEnum.BAR))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("INVALID");
    }

    @Test
    void getUUIDFromString() {
        UUID expected = UUID.randomUUID();
        Map<String, Object> m = Map.of("id", expected.toString());
        assertThat(MapUtils.getUUID(m, "id")).isEqualTo(expected);
    }

    @Test
    void getUUIDFromUUID() {
        UUID expected = UUID.randomUUID();
        Map<String, Object> m = Map.of("id", expected);
        assertThat(MapUtils.getUUID(m, "id")).isEqualTo(expected);
    }

    @Test
    void getUUIDReturnsNullWhenMissing() {
        Map<String, Object> m = Map.of();
        assertThat(MapUtils.getUUID(m, "id")).isNull();
    }

    @Test
    void getUUIDThrowsOnInvalidType() {
        Map<String, Object> m = Map.of("id", 123);
        assertThatThrownBy(() -> MapUtils.getUUID(m, "id"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getStringReturnsValue() {
        Map<String, Object> m = Map.of("name", "hello");
        assertThat(MapUtils.getString(m, "name")).isEqualTo("hello");
    }

    @Test
    void getStringReturnsNullWhenMissing() {
        Map<String, Object> m = Map.of();
        assertThat(MapUtils.getString(m, "name")).isNull();
    }

    @Test
    void getStringWithDefault() {
        Map<String, Object> m = Map.of();
        assertThat(MapUtils.getString(m, "name", "default")).isEqualTo("default");
    }

    @Test
    void getMapReturnsValue() {
        Map<String, Object> inner = Map.of("k", "v");
        Map<String, Object> m = Map.of("sub", inner);
        assertThat(MapUtils.getMap(m, "sub", null)).isEqualTo(inner);
    }

    @Test
    void getMapReturnsDefaultWhenMissing() {
        Map<String, Object> m = Map.of();
        Map<String, Object> def = Map.of("d", "v");
        assertThat(MapUtils.getMap(m, "sub", def)).isEqualTo(def);
    }

    @Test
    void getListReturnsValue() {
        List<String> list = List.of("a", "b");
        Map<String, Object> m = Map.of("items", list);
        assertThat(MapUtils.getList(m, "items", null)).isEqualTo(list);
    }

    @Test
    void getBooleanReturnsValue() {
        Map<String, Object> m = Map.of("flag", true);
        assertThat(MapUtils.getBoolean(m, "flag", false)).isTrue();
    }

    @Test
    void getBooleanReturnsDefaultWhenMissing() {
        Map<String, Object> m = Map.of();
        assertThat(MapUtils.getBoolean(m, "flag", true)).isTrue();
    }

    @Test
    void getIntReturnsValue() {
        Map<String, Object> m = Map.of("count", 42);
        assertThat(MapUtils.getInt(m, "count", 0)).isEqualTo(42);
    }

    @Test
    void getIntReturnsDefaultWhenMissing() {
        Map<String, Object> m = Map.of();
        assertThat(MapUtils.getInt(m, "count", 10)).isEqualTo(10);
    }

    @Test
    void getNumberReturnsValue() {
        Map<String, Object> m = Map.of("num", 3.14);
        assertThat(MapUtils.getNumber(m, "num", 0)).isEqualTo(3.14);
    }

    @Test
    void assertUUIDReturnsWhenPresent() {
        UUID expected = UUID.randomUUID();
        Map<String, Object> m = Map.of("id", expected);
        assertThat(MapUtils.assertUUID(m, "id")).isEqualTo(expected);
    }

    @Test
    void assertUUIDThrowsWhenMissing() {
        Map<String, Object> m = Map.of();
        assertThatThrownBy(() -> MapUtils.assertUUID(m, "id"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Mandatory");
    }

    @Test
    void assertIntReturnsValue() {
        Map<String, Object> m = Map.of("n", 5);
        assertThat(MapUtils.assertInt(m, "n")).isEqualTo(5);
    }

    @Test
    void assertNumberReturnsValue() {
        Map<String, Object> m = Map.of("n", 5.5);
        assertThat(MapUtils.assertNumber(m, "n")).isEqualTo(5.5);
    }

    @Test
    void assertStringReturnsValue() {
        Map<String, Object> m = Map.of("s", "test");
        assertThat(MapUtils.assertString(m, "s")).isEqualTo("test");
    }

    @Test
    void assertStringThrowsWhenMissing() {
        Map<String, Object> m = Map.of();
        assertThatThrownBy(() -> MapUtils.assertString(m, "s"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Mandatory");
    }

    @Test
    void assertMapReturnsValue() {
        Map<String, Object> inner = Map.of("k", "v");
        Map<String, Object> m = Map.of("sub", inner);
        assertThat(MapUtils.<String, Object>assertMap(m, "sub")).isEqualTo(inner);
    }

    @Test
    void assertListReturnsValue() {
        List<String> list = List.of("a");
        Map<String, Object> m = Map.of("items", list);
        assertThat(MapUtils.<String>assertList(m, "items")).isEqualTo(list);
    }

    @Test
    void getWithTypeMismatchThrows() {
        Map<String, Object> m = Map.of("val", 123);
        assertThatThrownBy(() -> MapUtils.get(m, "val", null, String.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid variable");
    }

    @Test
    void getWithNullMapReturnsDefault() {
        assertThat(MapUtils.get(null, "key", "default")).isEqualTo("default");
    }

    @Test
    void assertVariableThrowsWhenMissing() {
        Map<String, Object> m = Map.of();
        assertThatThrownBy(() -> MapUtils.assertVariable(m, "x", String.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Mandatory");
    }
}
