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

import java.util.Base64;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Base64TaskV2Test {

    @Test
    public void encodeEmitsStandardBase64() {
        var task = new Base64TaskV2(new StubHolder());
        assertEquals(Base64.getEncoder().encodeToString("hello".getBytes(UTF_8)),
                task.encode("hello"));
    }

    @Test
    public void decodeRoundtripsEncode() {
        var task = new Base64TaskV2(new StubHolder());
        var raw = "round trip me";
        var encoded = task.encode(raw);
        assertEquals(raw, task.decode(encoded));
    }

    @Test
    public void encodeHandlesUtf8() {
        var task = new Base64TaskV2(new StubHolder());
        var raw = "héllo 🌍";
        assertEquals(raw, task.decode(task.encode(raw)));
    }

    @Test
    public void encodePropagatesSensitiveFlag() {
        var holder = new StubHolder();
        holder.add("secret");
        var task = new Base64TaskV2(holder);

        var encoded = task.encode("secret");

        assertTrue(holder.get().contains(encoded), "encoded value should be marked sensitive");
    }

    @Test
    public void encodeDoesNotFlagNonSensitiveInputs() {
        var holder = new StubHolder();
        var task = new Base64TaskV2(holder);

        var encoded = task.encode("public data");
        assertFalse(holder.get().contains(encoded));
    }

    @Test
    public void decodePropagatesSensitiveFlag() {
        var holder = new StubHolder();
        var encoded = Base64.getEncoder().encodeToString("secret".getBytes(UTF_8));
        holder.add(encoded);
        var task = new Base64TaskV2(holder);

        var decoded = task.decode(encoded);

        assertEquals("secret", decoded);
        assertTrue(holder.get().contains(decoded));
    }

    @Test
    public void decodeDoesNotFlagNonSensitiveInputs() {
        var holder = new StubHolder();
        var task = new Base64TaskV2(holder);

        var encoded = Base64.getEncoder().encodeToString("public".getBytes(UTF_8));
        var decoded = task.decode(encoded);

        assertEquals("public", decoded);
        assertFalse(holder.get().contains(decoded));
    }

    @Test
    public void encodeEmptyString() {
        var task = new Base64TaskV2(new StubHolder());
        assertEquals("", task.encode(""));
        assertEquals("", task.decode(""));
    }

    private static final class StubHolder implements SensitiveDataHolder {

        private final Set<String> entries = new LinkedHashSet<>();

        @Override
        public Set<String> get() {
            return entries;
        }

        @Override
        public void add(String value) {
            entries.add(value);
        }

        @Override
        public void addAll(Collection<String> values) {
            entries.addAll(values);
        }
    }
}
