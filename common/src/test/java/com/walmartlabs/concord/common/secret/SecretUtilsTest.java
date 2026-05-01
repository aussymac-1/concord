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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class SecretUtilsTest {

    @Test
    public void testEncryptDecryptRoundTrip() {
        byte[] input = "secret data".getBytes(StandardCharsets.UTF_8);
        byte[] password = "password123".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "saltsalt".getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = SecretUtils.encrypt(input, password, salt);
        assertNotNull(encrypted);
        assertFalse(java.util.Arrays.equals(input, encrypted));

        byte[] decrypted = SecretUtils.decrypt(encrypted, password, salt);
        assertArrayEquals(input, decrypted);
    }

    @Test
    public void testEncryptDecryptWithSHA256() {
        byte[] input = "hello world".getBytes(StandardCharsets.UTF_8);
        byte[] password = "pass".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "mysalt12".getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = SecretUtils.encrypt(input, password, salt, HashAlgorithm.SHA256);
        byte[] decrypted = SecretUtils.decrypt(encrypted, password, salt, HashAlgorithm.SHA256);
        assertArrayEquals(input, decrypted);
    }

    @Test
    public void testEncryptDecryptStream() throws Exception {
        byte[] input = "stream data".getBytes(StandardCharsets.UTF_8);
        byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "saltsalt".getBytes(StandardCharsets.UTF_8);

        InputStream encryptedStream = SecretUtils.encrypt(new ByteArrayInputStream(input), password, salt);
        byte[] encrypted = encryptedStream.readAllBytes();

        InputStream decryptedStream = SecretUtils.decrypt(new ByteArrayInputStream(encrypted), password, salt);
        byte[] decrypted = decryptedStream.readAllBytes();

        assertArrayEquals(input, decrypted);
    }

    @Test
    public void testDecryptWithWrongPasswordThrows() {
        byte[] input = "data".getBytes(StandardCharsets.UTF_8);
        byte[] password = "correct".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "saltsalt".getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = SecretUtils.encrypt(input, password, salt);

        byte[] wrongPassword = "wrong!!!".getBytes(StandardCharsets.UTF_8);
        assertThrows(SecurityException.class,
                () -> SecretUtils.decrypt(encrypted, wrongPassword, salt));
    }

    @Test
    public void testHash() throws NoSuchAlgorithmException {
        byte[] input = "test".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "salt".getBytes(StandardCharsets.UTF_8);

        byte[] hash = SecretUtils.hash(input, salt, HashAlgorithm.SHA256);
        assertNotNull(hash);
        assertEquals(32, hash.length);
    }

    @Test
    public void testHashWithNullInput() throws NoSuchAlgorithmException {
        byte[] salt = "salt".getBytes(StandardCharsets.UTF_8);
        byte[] hash = SecretUtils.hash(null, salt, HashAlgorithm.SHA256);
        assertNotNull(hash);
    }

    @Test
    public void testGenerateSalt() {
        byte[] salt = SecretUtils.generateSalt(16);
        assertNotNull(salt);
        assertEquals(16, salt.length);

        byte[] salt2 = SecretUtils.generateSalt(16);
        assertFalse(java.util.Arrays.equals(salt, salt2));
    }
}
