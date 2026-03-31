package com.walmartlabs.concord.policyengine;

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

public class AttachmentsPolicyTest {

    @Test
    public void testNullRule() throws Exception {
        AttachmentsPolicy policy = new AttachmentsPolicy(null);
        CheckResult<AttachmentsRule, Long> result = policy.check(Path.of("/tmp/nonexistent"));
        assertTrue(result.getDeny().isEmpty());
        assertTrue(result.getWarn().isEmpty());
    }

    @Test
    public void testNonExistentDirectory() throws Exception {
        AttachmentsPolicy policy = new AttachmentsPolicy(AttachmentsRule.of("msg", 100L));
        CheckResult<AttachmentsRule, Long> result = policy.check(Path.of("/tmp/nonexistent_dir_xyz"));
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testUnderLimit(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("file1.txt"), new byte[50]);

        AttachmentsPolicy policy = new AttachmentsPolicy(AttachmentsRule.of("msg", 100L));
        CheckResult<AttachmentsRule, Long> result = policy.check(tempDir);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testOverLimit(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("file1.txt"), new byte[60]);
        Files.write(tempDir.resolve("file2.txt"), new byte[60]);

        AttachmentsPolicy policy = new AttachmentsPolicy(AttachmentsRule.of("msg", 100L));
        CheckResult<AttachmentsRule, Long> result = policy.check(tempDir);
        assertFalse(result.getDeny().isEmpty());
        assertEquals(120L, result.getDeny().get(0).getEntity());
    }

    @Test
    public void testExactlyAtLimit(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("file1.txt"), new byte[100]);

        AttachmentsPolicy policy = new AttachmentsPolicy(AttachmentsRule.of("msg", 100L));
        CheckResult<AttachmentsRule, Long> result = policy.check(tempDir);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testMultipleFilesAggregated(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("a.txt"), new byte[30]);
        Files.write(tempDir.resolve("b.txt"), new byte[30]);
        Files.write(tempDir.resolve("c.txt"), new byte[30]);

        AttachmentsPolicy policy = new AttachmentsPolicy(AttachmentsRule.of("msg", 80L));
        CheckResult<AttachmentsRule, Long> result = policy.check(tempDir);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testEmptyDirectory(@TempDir Path tempDir) throws Exception {
        AttachmentsPolicy policy = new AttachmentsPolicy(AttachmentsRule.of("msg", 100L));
        CheckResult<AttachmentsRule, Long> result = policy.check(tempDir);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testFileInsteadOfDirectory(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("file.txt");
        Files.write(file, new byte[50]);

        AttachmentsPolicy policy = new AttachmentsPolicy(AttachmentsRule.of("msg", 10L));
        CheckResult<AttachmentsRule, Long> result = policy.check(file);
        assertTrue(result.getDeny().isEmpty());
    }
}
