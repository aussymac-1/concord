package com.walmartlabs.concord.plugins.misc;

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

import com.walmartlabs.concord.runtime.v2.sdk.SensitiveDataHolder;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class Base64TaskV2Test {

    @Test
    void encodeReturnsUtf8Base64() {
        FakeSensitiveDataHolder holder = new FakeSensitiveDataHolder();
        Base64TaskV2 t = new Base64TaskV2(holder);

        String encoded = t.encode("hello");

        assertThat(encoded).isEqualTo("aGVsbG8=");
    }

    @Test
    void decodeRecoversOriginal() {
        FakeSensitiveDataHolder holder = new FakeSensitiveDataHolder();
        Base64TaskV2 t = new Base64TaskV2(holder);

        String decoded = t.decode("aGVsbG8=");

        assertThat(decoded).isEqualTo("hello");
    }

    @Test
    void roundTripPreservesUnicode() {
        FakeSensitiveDataHolder holder = new FakeSensitiveDataHolder();
        Base64TaskV2 t = new Base64TaskV2(holder);

        String raw = "héllo-\u4e16\u754c";
        String roundTripped = t.decode(t.encode(raw));

        assertThat(roundTripped).isEqualTo(raw);
    }

    @Test
    void encodePromotesResultWhenInputIsSensitive() {
        FakeSensitiveDataHolder holder = new FakeSensitiveDataHolder();
        holder.add("secret");
        Base64TaskV2 t = new Base64TaskV2(holder);

        String encoded = t.encode("secret");

        assertThat(holder.get()).contains("secret", encoded);
    }

    @Test
    void encodeDoesNotPromoteNonSensitiveInput() {
        FakeSensitiveDataHolder holder = new FakeSensitiveDataHolder();
        Base64TaskV2 t = new Base64TaskV2(holder);

        String encoded = t.encode("public");

        assertThat(holder.get()).doesNotContain(encoded);
    }

    @Test
    void decodePromotesPlainTextWhenBase64IsSensitive() {
        FakeSensitiveDataHolder holder = new FakeSensitiveDataHolder();
        String base64 = "aGVsbG8=";
        holder.add(base64);
        Base64TaskV2 t = new Base64TaskV2(holder);

        String decoded = t.decode(base64);

        assertThat(holder.get()).contains(base64, decoded);
    }

    private static final class FakeSensitiveDataHolder implements SensitiveDataHolder {
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
