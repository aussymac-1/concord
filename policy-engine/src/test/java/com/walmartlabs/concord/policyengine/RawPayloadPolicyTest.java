package com.walmartlabs.concord.policyengine;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2022 Walmart Inc.
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;

public class RawPayloadPolicyTest {

    @Test
    public void testMaxSize() throws Exception {
        Path p = Files.createTempDirectory("test1");
        Files.write(p.resolve("test.bin"), new byte[]{0, 1, 2, 3, 4, 5}, StandardOpenOption.CREATE_NEW);

        RawPayloadPolicy fiveBytes = new RawPayloadPolicy(RawPayloadRule.of("5 bytes", 5L));
        RawPayloadPolicy tenBytes = new RawPayloadPolicy(RawPayloadRule.of("10 bytes", 10L));

        // ---

        assertDeny(fiveBytes, p.resolve("test.bin"));
        assertAllow(tenBytes, p.resolve("test.bin"));
    }

    @Test
    public void testNullRule(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("test.bin");
        Files.write(file, new byte[100]);

        RawPayloadPolicy policy = new RawPayloadPolicy(null);
        CheckResult<RawPayloadRule, Long> result = policy.check(file);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testNonExistentFile() throws Exception {
        RawPayloadPolicy policy = new RawPayloadPolicy(RawPayloadRule.of("msg", 100L));
        CheckResult<RawPayloadRule, Long> result = policy.check(Path.of("/tmp/nonexistent_xyz"));
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testDirectoryInsteadOfFile(@TempDir Path tempDir) throws Exception {
        RawPayloadPolicy policy = new RawPayloadPolicy(RawPayloadRule.of("msg", 100L));
        CheckResult<RawPayloadRule, Long> result = policy.check(tempDir);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testExactlyAtLimit(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("exact.bin");
        Files.write(file, new byte[100]);

        RawPayloadPolicy policy = new RawPayloadPolicy(RawPayloadRule.of("msg", 100L));
        CheckResult<RawPayloadRule, Long> result = policy.check(file);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testOneByteOverLimit(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("over.bin");
        Files.write(file, new byte[101]);

        RawPayloadPolicy policy = new RawPayloadPolicy(RawPayloadRule.of("msg", 100L));
        CheckResult<RawPayloadRule, Long> result = policy.check(file);
        assertFalse(result.getDeny().isEmpty());
        assertEquals(101L, (long) result.getDeny().get(0).getEntity());
    }

    @Test
    public void testEmptyFile(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("empty.bin");
        Files.write(file, new byte[0]);

        RawPayloadPolicy policy = new RawPayloadPolicy(RawPayloadRule.of("msg", 100L));
        CheckResult<RawPayloadRule, Long> result = policy.check(file);
        assertTrue(result.getDeny().isEmpty());
    }

    private static void assertAllow(RawPayloadPolicy policy, Path p) throws IOException {
        CheckResult<RawPayloadRule, Long> result = policy.check(p);
        assertTrue(result.getDeny().isEmpty());
    }

    private static void assertDeny(RawPayloadPolicy policy, Path p) throws IOException {
        CheckResult<RawPayloadRule, Long> result = policy.check(p);
        assertFalse(result.getDeny().isEmpty());
    }
}
