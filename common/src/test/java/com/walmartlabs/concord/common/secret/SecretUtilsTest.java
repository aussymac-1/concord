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

    private static final byte[] PASSWORD = "test-password-16".getBytes(StandardCharsets.UTF_8);
    private static final byte[] SALT = "test-salt-value!".getBytes(StandardCharsets.UTF_8);

    @Test
    public void testEncryptDecryptRoundTrip() {
        var plaintext = "hello secret world".getBytes(StandardCharsets.UTF_8);

        var encrypted = SecretUtils.encrypt(plaintext, PASSWORD, SALT);
        assertNotNull(encrypted);
        assertFalse(new String(encrypted, StandardCharsets.UTF_8).equals("hello secret world"));

        var decrypted = SecretUtils.decrypt(encrypted, PASSWORD, SALT);
        assertArrayEquals(plaintext, decrypted);
    }

    @Test
    public void testEncryptDecryptWithSHA256() {
        var plaintext = "sha256 data".getBytes(StandardCharsets.UTF_8);

        var encrypted = SecretUtils.encrypt(plaintext, PASSWORD, SALT, HashAlgorithm.SHA256);
        var decrypted = SecretUtils.decrypt(encrypted, PASSWORD, SALT, HashAlgorithm.SHA256);
        assertArrayEquals(plaintext, decrypted);
    }

    @Test
    public void testStreamEncryptDecrypt() throws Exception {
        var plaintext = "stream data".getBytes(StandardCharsets.UTF_8);
        var input = new ByteArrayInputStream(plaintext);

        InputStream encrypted = SecretUtils.encrypt(input, PASSWORD, SALT);
        var encryptedBytes = encrypted.readAllBytes();

        InputStream decrypted = SecretUtils.decrypt(new ByteArrayInputStream(encryptedBytes), PASSWORD, SALT);
        var result = decrypted.readAllBytes();

        assertArrayEquals(plaintext, result);
    }

    @Test
    public void testGenerateSalt() {
        var salt = SecretUtils.generateSalt(16);
        assertNotNull(salt);
        assertEquals(16, salt.length);

        var salt2 = SecretUtils.generateSalt(16);
        assertFalse(java.util.Arrays.equals(salt, salt2));
    }

    @Test
    public void testHash() throws NoSuchAlgorithmException {
        var input = "test".getBytes(StandardCharsets.UTF_8);
        var result = SecretUtils.hash(input, SALT, HashAlgorithm.SHA256);
        assertNotNull(result);
        assertEquals(32, result.length);
    }

    @Test
    public void testHashWithNullInput() throws NoSuchAlgorithmException {
        var result = SecretUtils.hash(null, SALT, HashAlgorithm.SHA256);
        assertNotNull(result);
        assertEquals(32, result.length);
    }

    @Test
    public void testHashAlgorithmGetByName() {
        assertEquals(HashAlgorithm.SHA256, HashAlgorithm.getByName("SHA-256"));
        assertEquals(HashAlgorithm.LEGACY_MD5, HashAlgorithm.getByName("md5"));
        assertEquals(HashAlgorithm.LEGACY_MD5, HashAlgorithm.getByName("unknown"));
    }
}
