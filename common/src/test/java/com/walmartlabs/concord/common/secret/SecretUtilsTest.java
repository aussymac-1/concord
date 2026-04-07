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

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class SecretUtilsTest {

    @Test
    public void testEncryptDecryptLegacyMd5() {
        byte[] input = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        byte[] password = "myPassword".getBytes(StandardCharsets.UTF_8);
        byte[] salt = SecretUtils.generateSalt(16);

        byte[] encrypted = SecretUtils.encrypt(input, password, salt);
        assertNotNull(encrypted);
        assertNotEquals(0, encrypted.length);

        byte[] decrypted = SecretUtils.decrypt(encrypted, password, salt);
        assertArrayEquals(input, decrypted);
    }

    @Test
    public void testEncryptDecryptSHA256() {
        byte[] input = "Secret data".getBytes(StandardCharsets.UTF_8);
        byte[] password = "password123".getBytes(StandardCharsets.UTF_8);
        byte[] salt = SecretUtils.generateSalt(16);

        byte[] encrypted = SecretUtils.encrypt(input, password, salt, HashAlgorithm.SHA256);
        assertNotNull(encrypted);

        byte[] decrypted = SecretUtils.decrypt(encrypted, password, salt, HashAlgorithm.SHA256);
        assertArrayEquals(input, decrypted);
    }

    @Test
    public void testDecryptWithWrongPassword() {
        byte[] input = "Hello".getBytes(StandardCharsets.UTF_8);
        byte[] password = "correct".getBytes(StandardCharsets.UTF_8);
        byte[] wrongPassword = "wrong".getBytes(StandardCharsets.UTF_8);
        byte[] salt = SecretUtils.generateSalt(16);

        byte[] encrypted = SecretUtils.encrypt(input, password, salt);
        assertThrows(SecurityException.class, () -> SecretUtils.decrypt(encrypted, wrongPassword, salt));
    }

    @Test
    public void testGenerateSalt() {
        byte[] salt1 = SecretUtils.generateSalt(16);
        byte[] salt2 = SecretUtils.generateSalt(16);

        assertEquals(16, salt1.length);
        assertEquals(16, salt2.length);
        assertFalse(java.util.Arrays.equals(salt1, salt2));
    }

    @Test
    public void testHash() throws NoSuchAlgorithmException {
        byte[] input = "test".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "salt".getBytes(StandardCharsets.UTF_8);

        byte[] hash1 = SecretUtils.hash(input, salt, HashAlgorithm.SHA256);
        byte[] hash2 = SecretUtils.hash(input, salt, HashAlgorithm.SHA256);

        assertNotNull(hash1);
        assertArrayEquals(hash1, hash2);
    }

    @Test
    public void testHashNullInput() throws NoSuchAlgorithmException {
        byte[] salt = "salt".getBytes(StandardCharsets.UTF_8);
        byte[] hash = SecretUtils.hash(null, salt, HashAlgorithm.SHA256);
        assertNotNull(hash);
    }

    @Test
    public void testEncryptDecryptEmptyInput() {
        byte[] input = new byte[0];
        byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        byte[] salt = SecretUtils.generateSalt(16);

        byte[] encrypted = SecretUtils.encrypt(input, password, salt);
        byte[] decrypted = SecretUtils.decrypt(encrypted, password, salt);
        assertArrayEquals(input, decrypted);
    }
}
