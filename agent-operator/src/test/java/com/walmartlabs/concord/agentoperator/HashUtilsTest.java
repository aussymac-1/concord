package com.walmartlabs.concord.agentoperator;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2019 Walmart Inc.
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

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HashUtilsTest {

    @Test
    void testHashString() throws IOException {
        var hash = HashUtils.hashAsHexString("hello");
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    void testHashDeterministic() throws IOException {
        var hash1 = HashUtils.hashAsHexString("test");
        var hash2 = HashUtils.hashAsHexString("test");
        assertEquals(hash1, hash2);
    }

    @Test
    void testHashDifferentInputs() throws IOException {
        var hash1 = HashUtils.hashAsHexString("foo");
        var hash2 = HashUtils.hashAsHexString("bar");
        assertNotEquals(hash1, hash2);
    }

    @Test
    void testHashMap() throws IOException {
        var hash = HashUtils.hashAsHexString(Map.of("key", "value"));
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    void testHashMapOrderIndependent() throws IOException {
        var hash1 = HashUtils.hashAsHexString(Map.of("a", "1", "b", "2"));
        var hash2 = HashUtils.hashAsHexString(Map.of("b", "2", "a", "1"));
        assertEquals(hash1, hash2);
    }
}
