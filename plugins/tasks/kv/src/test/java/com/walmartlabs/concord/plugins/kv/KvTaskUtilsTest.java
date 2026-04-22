package com.walmartlabs.concord.plugins.kv;

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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KvTaskUtilsTest {

    @Test
    public void removeRejectsNullKey() {
        UUID txId = UUID.randomUUID();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> KvTaskUtils.remove(null, txId, null));
        assertTrue(ex.getMessage().contains("Keys cannot be empty or null"));
    }

    @Test
    public void removeRejectsEmptyKey() {
        UUID txId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class,
                () -> KvTaskUtils.remove(null, txId, ""));
    }

    @Test
    public void putStringRejectsInvalidKey() {
        UUID txId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class,
                () -> KvTaskUtils.putString(null, txId, "", "v"));
        assertThrows(IllegalArgumentException.class,
                () -> KvTaskUtils.putString(null, txId, null, "v"));
    }

    @Test
    public void getStringRejectsInvalidKey() {
        UUID txId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class,
                () -> KvTaskUtils.getString(null, txId, ""));
    }

    @Test
    public void putLongRejectsInvalidKey() {
        UUID txId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class,
                () -> KvTaskUtils.putLong(null, txId, "", 1L));
    }

    @Test
    public void getLongRejectsInvalidKey() {
        UUID txId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class,
                () -> KvTaskUtils.getLong(null, txId, null));
    }

    @Test
    public void incLongRejectsInvalidKey() {
        UUID txId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class,
                () -> KvTaskUtils.incLong(null, txId, ""));
    }
}
