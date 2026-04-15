package com.walmartlabs.concord.client2.impl;

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

import static org.junit.jupiter.api.Assertions.*;

public class NameValuePairTest {

    @Test
    void testGetNameAndValue() {
        NameValuePair nvp = new NameValuePair("key", "value");
        assertEquals("key", nvp.getName());
        assertEquals("value", nvp.getValue());
    }

    @Test
    void testNullValues() {
        NameValuePair nvp = new NameValuePair(null, null);
        assertNull(nvp.getName());
        assertNull(nvp.getValue());
    }

    @Test
    void testEqualsAndHashCode() {
        NameValuePair a = new NameValuePair("key", "value");
        NameValuePair b = new NameValuePair("key", "value");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testNotEqualsDifferentName() {
        NameValuePair a = new NameValuePair("key1", "value");
        NameValuePair b = new NameValuePair("key2", "value");
        assertNotEquals(a, b);
    }

    @Test
    void testNotEqualsDifferentValue() {
        NameValuePair a = new NameValuePair("key", "value1");
        NameValuePair b = new NameValuePair("key", "value2");
        assertNotEquals(a, b);
    }

    @Test
    void testNotEqualsNull() {
        NameValuePair nvp = new NameValuePair("key", "value");
        assertNotEquals(null, nvp);
    }

    @Test
    void testNotEqualsDifferentType() {
        NameValuePair nvp = new NameValuePair("key", "value");
        assertNotEquals("key=value", nvp);
    }

    @Test
    void testEqualsSameReference() {
        NameValuePair nvp = new NameValuePair("key", "value");
        assertEquals(nvp, nvp);
    }
}
