package com.walmartlabs.concord.plugins.misc;

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

import com.walmartlabs.concord.runtime.v2.sdk.SensitiveDataHolder;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class Base64TaskV2Test {

    @Test
    void testEncodeAndDecode() {
        var holder = new TestSensitiveDataHolder();
        var task = new Base64TaskV2(holder);

        String encoded = task.encode("hello world");
        assertNotNull(encoded);
        assertNotEquals("hello world", encoded);

        String decoded = task.decode(encoded);
        assertEquals("hello world", decoded);
    }

    @Test
    void testEncodeEmptyString() {
        var holder = new TestSensitiveDataHolder();
        var task = new Base64TaskV2(holder);

        String encoded = task.encode("");
        String decoded = task.decode(encoded);
        assertEquals("", decoded);
    }

    @Test
    void testSensitiveDataTracked() {
        var holder = new TestSensitiveDataHolder();
        holder.add("secret-value");

        var task = new Base64TaskV2(holder);

        String encoded = task.encode("secret-value");
        assertTrue(holder.get().contains(encoded));
    }

    @Test
    void testNonSensitiveDataNotTracked() {
        var holder = new TestSensitiveDataHolder();
        var task = new Base64TaskV2(holder);

        String encoded = task.encode("public-value");
        assertFalse(holder.get().contains(encoded));
    }

    @Test
    void testDecodeSensitiveDataTracked() {
        var holder = new TestSensitiveDataHolder();
        var task = new Base64TaskV2(holder);

        String encoded = task.encode("data");
        holder.add(encoded);

        String decoded = task.decode(encoded);
        assertTrue(holder.get().contains(decoded));
    }

    private static class TestSensitiveDataHolder implements SensitiveDataHolder {
        private final Set<String> data = new HashSet<>();

        @Override
        public Set<String> get() {
            return data;
        }

        @Override
        public void add(String sensitiveData) {
            data.add(sensitiveData);
        }

        @Override
        public void addAll(java.util.Collection<String> sensitiveData) {
            data.addAll(sensitiveData);
        }
    }
}
