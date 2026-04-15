package com.walmartlabs.concord.cli.secrets;

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
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class UncheckedIOTest {

    @Test
    void testWriteAndReadAllBytes(@TempDir Path tempDir) {
        Path file = tempDir.resolve("test.txt");
        byte[] data = "hello world".getBytes();
        UncheckedIO.write(file, data);
        byte[] read = UncheckedIO.readAllBytes(file);
        assertArrayEquals(data, read);
    }

    @Test
    void testCreateTempFile(@TempDir Path tempDir) {
        Path file = UncheckedIO.createTempFile(tempDir, "prefix", ".tmp");
        assertNotNull(file);
        assertTrue(Files.exists(file));
        assertTrue(file.getFileName().toString().startsWith("prefix"));
        assertTrue(file.getFileName().toString().endsWith(".tmp"));
    }

    @Test
    void testCopy(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Files.writeString(source, "content");
        Path target = tempDir.resolve("target.txt");
        UncheckedIO.copy(source, target);
        assertEquals("content", Files.readString(target));
    }

    @Test
    void testAssertTmpDir(@TempDir Path tempDir) {
        Path tmpDir = UncheckedIO.assertTmpDir(tempDir);
        assertNotNull(tmpDir);
        assertTrue(Files.exists(tmpDir));
        assertTrue(Files.isDirectory(tmpDir));
    }

    @Test
    void testAssertTmpDirIdempotent(@TempDir Path tempDir) {
        Path first = UncheckedIO.assertTmpDir(tempDir);
        Path second = UncheckedIO.assertTmpDir(tempDir);
        assertEquals(first, second);
    }

    @Test
    void testReadAllBytesNonExistentThrows(@TempDir Path tempDir) {
        Path nonExistent = tempDir.resolve("nonexistent.txt");
        assertThrows(RuntimeException.class, () -> UncheckedIO.readAllBytes(nonExistent));
    }
}
