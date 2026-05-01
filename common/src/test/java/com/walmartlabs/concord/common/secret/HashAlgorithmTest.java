package com.walmartlabs.concord.common.secret;

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

public class HashAlgorithmTest {

    @Test
    public void testGetName() {
        assertEquals("md5", HashAlgorithm.LEGACY_MD5.getName());
        assertEquals("SHA-256", HashAlgorithm.SHA256.getName());
    }

    @Test
    public void testGetByNameMd5() {
        assertEquals(HashAlgorithm.LEGACY_MD5, HashAlgorithm.getByName("md5"));
    }

    @Test
    public void testGetByNameSha256() {
        assertEquals(HashAlgorithm.SHA256, HashAlgorithm.getByName("SHA-256"));
    }

    @Test
    public void testGetByNameUnknownDefaultsToLegacy() {
        assertEquals(HashAlgorithm.LEGACY_MD5, HashAlgorithm.getByName("unknown"));
    }

    @Test
    public void testValues() {
        HashAlgorithm[] values = HashAlgorithm.values();
        assertEquals(2, values.length);
    }
}
