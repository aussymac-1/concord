package com.walmartlabs.concord.common.secret;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2024 Walmart Inc.
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

    private static final byte[] PASSWORD = "testPassword123!".getBytes(StandardCharsets.UTF_8);
    private static final byte[] SALT = "testSalt12345678".getBytes(StandardCharsets.UTF_8);

    @Test
    public void testEncryptDecryptRoundTripMD5() {
        byte[] original = "Hello, World!".getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = SecretUtils.encrypt(original, PASSWORD, SALT);
        assertNotNull(encrypted);

        byte[] decrypted = SecretUtils.decrypt(encrypted, PASSWORD, SALT);
        assertArrayEquals(original, decrypted);
    }

    @Test
    public void testEncryptDecryptRoundTripSHA256() {
        byte[] original = "Secret data with SHA-256".getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = SecretUtils.encrypt(original, PASSWORD, SALT, HashAlgorithm.SHA256);
        assertNotNull(encrypted);

        byte[] decrypted = SecretUtils.decrypt(encrypted, PASSWORD, SALT, HashAlgorithm.SHA256);
        assertArrayEquals(original, decrypted);
    }

    @Test
    public void testEncryptDecryptStreamMD5() throws Exception {
        byte[] original = "Stream test data".getBytes(StandardCharsets.UTF_8);

        InputStream encryptedStream = SecretUtils.encrypt(new ByteArrayInputStream(original), PASSWORD, SALT);
        byte[] encrypted = encryptedStream.readAllBytes();

        InputStream decryptedStream = SecretUtils.decrypt(new ByteArrayInputStream(encrypted), PASSWORD, SALT);
        byte[] decrypted = decryptedStream.readAllBytes();

        assertArrayEquals(original, decrypted);
    }

    @Test
    public void testEncryptDecryptStreamSHA256() throws Exception {
        byte[] original = "Stream SHA-256 data".getBytes(StandardCharsets.UTF_8);

        InputStream encryptedStream = SecretUtils.encrypt(
                new ByteArrayInputStream(original), PASSWORD, SALT, HashAlgorithm.SHA256);
        byte[] encrypted = encryptedStream.readAllBytes();

        InputStream decryptedStream = SecretUtils.decrypt(
                new ByteArrayInputStream(encrypted), PASSWORD, SALT, HashAlgorithm.SHA256);
        byte[] decrypted = decryptedStream.readAllBytes();

        assertArrayEquals(original, decrypted);
    }

    @Test
    public void testDecryptWithWrongPassword() {
        byte[] original = "secret".getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = SecretUtils.encrypt(original, PASSWORD, SALT);

        byte[] wrongPassword = "wrongPassword!!!".getBytes(StandardCharsets.UTF_8);
        assertThrows(SecurityException.class,
                () -> SecretUtils.decrypt(encrypted, wrongPassword, SALT));
    }

    @Test
    public void testHashMD5() throws NoSuchAlgorithmException {
        byte[] result = SecretUtils.hash("test".getBytes(StandardCharsets.UTF_8), SALT, HashAlgorithm.LEGACY_MD5);
        assertNotNull(result);
        assertEquals(16, result.length);
    }

    @Test
    public void testHashSHA256() throws NoSuchAlgorithmException {
        byte[] result = SecretUtils.hash("test".getBytes(StandardCharsets.UTF_8), SALT, HashAlgorithm.SHA256);
        assertNotNull(result);
        assertEquals(32, result.length);
    }

    @Test
    public void testHashNullInput() throws NoSuchAlgorithmException {
        byte[] result = SecretUtils.hash(null, SALT, HashAlgorithm.SHA256);
        assertNotNull(result);
        assertEquals(32, result.length);
    }

    @Test
    public void testGenerateSalt() {
        byte[] salt = SecretUtils.generateSalt(16);
        assertNotNull(salt);
        assertEquals(16, salt.length);
    }

    @Test
    public void testGenerateSaltUniqueness() {
        byte[] salt1 = SecretUtils.generateSalt(16);
        byte[] salt2 = SecretUtils.generateSalt(16);
        assertFalse(java.util.Arrays.equals(salt1, salt2));
    }

    @Test
    public void testEncryptedDifferentFromOriginal() {
        byte[] original = "plaintext".getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = SecretUtils.encrypt(original, PASSWORD, SALT);
        assertFalse(java.util.Arrays.equals(original, encrypted));
    }
}
