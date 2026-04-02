package com.walmartlabs.concord.cli.secrets;

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
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class UncheckedIOTest {

    @Test
    public void testWriteAndReadAllBytes(@TempDir Path tempDir) {
        Path file = tempDir.resolve("test.txt");
        byte[] data = "hello world".getBytes();
        UncheckedIO.write(file, data);
        byte[] result = UncheckedIO.readAllBytes(file);
        assertArrayEquals(data, result);
    }

    @Test
    public void testCreateTempFile(@TempDir Path tempDir) {
        Path file = UncheckedIO.createTempFile(tempDir, "prefix", ".txt");
        assertNotNull(file);
        assertTrue(Files.exists(file));
        assertTrue(file.getFileName().toString().startsWith("prefix"));
        assertTrue(file.getFileName().toString().endsWith(".txt"));
    }

    @Test
    public void testCopy(@TempDir Path tempDir) {
        Path source = tempDir.resolve("source.txt");
        Path target = tempDir.resolve("target.txt");
        byte[] data = "copy test".getBytes();
        UncheckedIO.write(source, data);
        UncheckedIO.copy(source, target);
        assertArrayEquals(data, UncheckedIO.readAllBytes(target));
    }

    @Test
    public void testAssertTmpDir(@TempDir Path tempDir) {
        Path result = UncheckedIO.assertTmpDir(tempDir);
        assertNotNull(result);
        assertTrue(Files.isDirectory(result));
    }
}
