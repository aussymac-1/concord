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
        byte[] password = "mypassword123456".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "salt1234salt1234".getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = SecretUtils.encrypt(input, password, salt);
        assertNotNull(encrypted);
        assertFalse(java.util.Arrays.equals(input, encrypted));

        byte[] decrypted = SecretUtils.decrypt(encrypted, password, salt);
        assertArrayEquals(input, decrypted);
    }

    @Test
    public void testEncryptDecryptWithSHA256() {
        byte[] input = "another secret".getBytes(StandardCharsets.UTF_8);
        byte[] password = "password12345678".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "saltsaltsaltsalt".getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = SecretUtils.encrypt(input, password, salt, HashAlgorithm.SHA256);
        assertNotNull(encrypted);

        byte[] decrypted = SecretUtils.decrypt(encrypted, password, salt, HashAlgorithm.SHA256);
        assertArrayEquals(input, decrypted);
    }

    @Test
    public void testEncryptDecryptStreamRoundTrip() throws Exception {
        byte[] input = "stream data".getBytes(StandardCharsets.UTF_8);
        byte[] password = "mypassword123456".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "salt1234salt1234".getBytes(StandardCharsets.UTF_8);

        InputStream encryptedStream = SecretUtils.encrypt(new ByteArrayInputStream(input), password, salt);
        byte[] encrypted = encryptedStream.readAllBytes();
        assertNotNull(encrypted);

        InputStream decryptedStream = SecretUtils.decrypt(new ByteArrayInputStream(encrypted), password, salt);
        byte[] decrypted = decryptedStream.readAllBytes();
        assertArrayEquals(input, decrypted);
    }

    @Test
    public void testDecryptWithWrongPasswordProducesDifferentOutput() {
        byte[] input = "test data for encryption".getBytes(StandardCharsets.UTF_8);
        byte[] password = "correctpassword1".getBytes(StandardCharsets.UTF_8);
        byte[] wrongPassword = "wrongpassword123".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "salt1234salt1234".getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = SecretUtils.encrypt(input, password, salt);

        try {
            byte[] decrypted = SecretUtils.decrypt(encrypted, wrongPassword, salt);
            assertFalse(java.util.Arrays.equals(input, decrypted));
        } catch (SecurityException e) {
            // expected if padding check fails
        }
    }

    @Test
    public void testGenerateSalt() {
        byte[] salt = SecretUtils.generateSalt(16);
        assertNotNull(salt);
        assertEquals(16, salt.length);

        byte[] salt2 = SecretUtils.generateSalt(16);
        assertFalse(java.util.Arrays.equals(salt, salt2));
    }

    @Test
    public void testHash() throws NoSuchAlgorithmException {
        byte[] input = "test".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "salt".getBytes(StandardCharsets.UTF_8);

        byte[] hash1 = SecretUtils.hash(input, salt, HashAlgorithm.SHA256);
        assertNotNull(hash1);

        byte[] hash2 = SecretUtils.hash(input, salt, HashAlgorithm.SHA256);
        assertArrayEquals(hash1, hash2);
    }

    @Test
    public void testHashNullInput() throws NoSuchAlgorithmException {
        byte[] salt = "salt".getBytes(StandardCharsets.UTF_8);

        byte[] hash = SecretUtils.hash(null, salt, HashAlgorithm.SHA256);
        assertNotNull(hash);
    }

    @Test
    public void testHashAlgorithmGetByName() {
        assertEquals(HashAlgorithm.SHA256, HashAlgorithm.getByName("SHA-256"));
        assertEquals(HashAlgorithm.LEGACY_MD5, HashAlgorithm.getByName("md5"));
        assertEquals(HashAlgorithm.LEGACY_MD5, HashAlgorithm.getByName("unknown"));
    }
}
