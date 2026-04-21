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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class Base64TaskV2Test {

    @Test
    public void testEncodeDecodeRoundTrip() {
        Base64TaskV2 task = new Base64TaskV2(new NoopSensitiveDataHolder());

        String encoded = task.encode("hello");
        assertEquals("aGVsbG8=", encoded);
        assertEquals("hello", task.decode(encoded));
    }

    @Test
    public void testEncodeEmptyString() {
        Base64TaskV2 task = new Base64TaskV2(new NoopSensitiveDataHolder());
        assertEquals("", task.encode(""));
        assertEquals("", task.decode(""));
    }

    @Test
    public void testEncodeUtf8() {
        Base64TaskV2 task = new Base64TaskV2(new NoopSensitiveDataHolder());

        String input = "héllo wörld";
        String encoded = task.encode(input);
        assertEquals(input, task.decode(encoded));
    }

    @Test
    public void testEncodeMarksResultSensitiveWhenInputSensitive() {
        RecordingSensitiveDataHolder holder = new RecordingSensitiveDataHolder();
        holder.add("secret");

        Base64TaskV2 task = new Base64TaskV2(holder);
        String encoded = task.encode("secret");

        assertTrue(holder.get().contains(encoded));
    }

    @Test
    public void testEncodeDoesNotMarkResultWhenInputNotSensitive() {
        RecordingSensitiveDataHolder holder = new RecordingSensitiveDataHolder();

        Base64TaskV2 task = new Base64TaskV2(holder);
        String encoded = task.encode("public");

        assertFalse(holder.get().contains(encoded));
    }

    @Test
    public void testDecodeMarksResultSensitiveWhenInputSensitive() {
        RecordingSensitiveDataHolder holder = new RecordingSensitiveDataHolder();
        String encoded = "c2VjcmV0"; // "secret"
        holder.add(encoded);

        Base64TaskV2 task = new Base64TaskV2(holder);
        String decoded = task.decode(encoded);

        assertEquals("secret", decoded);
        assertTrue(holder.get().contains(decoded));
    }

    @Test
    public void testDecodeDoesNotMarkResultWhenInputNotSensitive() {
        RecordingSensitiveDataHolder holder = new RecordingSensitiveDataHolder();

        Base64TaskV2 task = new Base64TaskV2(holder);
        String decoded = task.decode("aGVsbG8=");

        assertFalse(holder.get().contains(decoded));
    }

    private static class NoopSensitiveDataHolder implements SensitiveDataHolder {

        @Override
        public Set<String> get() {
            return new HashSet<>();
        }

        @Override
        public void add(String sensitiveData) {
        }

        @Override
        public void addAll(Collection<String> sensitiveData) {
        }
    }

    private static class RecordingSensitiveDataHolder implements SensitiveDataHolder {

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
        public void addAll(Collection<String> sensitiveData) {
            data.addAll(sensitiveData);
        }
    }
}
