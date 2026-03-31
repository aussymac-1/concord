package com.walmartlabs.concord.policyengine;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WorkspacePolicyTest {

    @Test
    public void testMaxSize() throws Exception {
        Path p = Files.createTempDirectory("test1");
        Files.write(p.resolve("test.bin"), new byte[]{0, 1, 2, 3, 4, 5}, StandardOpenOption.CREATE_NEW);

        WorkspacePolicy fiveBytes = new WorkspacePolicy(WorkspaceRule.of("5 bytes", 5L, null));
        WorkspacePolicy tenBytes = new WorkspacePolicy(WorkspaceRule.of("10 bytes", 10L, null));

        // ---

        assertDeny(fiveBytes, p);
        assertAllow(tenBytes, p);
    }

    @Test
    public void testNullRule() throws Exception {
        WorkspacePolicy policy = new WorkspacePolicy(null);
        CheckResult<WorkspaceRule, Path> result = policy.check(Path.of("/tmp"));
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testNonExistentPath() throws Exception {
        WorkspacePolicy policy = new WorkspacePolicy(WorkspaceRule.of("msg", 100L, null));
        CheckResult<WorkspaceRule, Path> result = policy.check(Path.of("/tmp/nonexistent_xyz_abc"));
        assertFalse(result.getDeny().isEmpty());
        assertTrue(result.getDeny().get(0).getMsg().contains("File not found"));
    }

    @Test
    public void testFileInsteadOfDirectory(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("file.txt");
        Files.write(file, new byte[10]);

        WorkspacePolicy policy = new WorkspacePolicy(WorkspaceRule.of("msg", 100L, null));
        CheckResult<WorkspaceRule, Path> result = policy.check(file);
        assertFalse(result.getDeny().isEmpty());
        assertTrue(result.getDeny().get(0).getMsg().contains("Not a directory"));
    }

    @Test
    public void testExactlyAtSizeLimit(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("file.bin"), new byte[100]);

        WorkspacePolicy policy = new WorkspacePolicy(WorkspaceRule.of("msg", 100L, null));
        assertAllow(policy, tempDir);
    }

    @Test
    public void testIgnoredFiles(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("keep.txt"), new byte[10]);
        Files.write(tempDir.resolve("ignore.log"), new byte[200]);

        Set<String> ignored = Collections.singleton(".*ignore\\.log");
        WorkspacePolicy policy = new WorkspacePolicy(WorkspaceRule.of("msg", 50L, ignored));
        assertAllow(policy, tempDir);
    }

    @Test
    public void testEmptyWorkspace(@TempDir Path tempDir) throws Exception {
        WorkspacePolicy policy = new WorkspacePolicy(WorkspaceRule.of("msg", 0L, null));
        assertAllow(policy, tempDir);
    }

    @Test
    public void testNestedDirectories(@TempDir Path tempDir) throws Exception {
        Path sub = tempDir.resolve("sub1/sub2");
        Files.createDirectories(sub);
        Files.write(sub.resolve("file.txt"), new byte[60]);
        Files.write(tempDir.resolve("root.txt"), new byte[60]);

        WorkspacePolicy policy = new WorkspacePolicy(WorkspaceRule.of("msg", 100L, null));
        assertDeny(policy, tempDir);
    }

    @Test
    public void testNullMaxSize() throws Exception {
        WorkspacePolicy policy = new WorkspacePolicy(WorkspaceRule.of("msg", null, null));
        Path p = Files.createTempDirectory("test_null_max");
        Files.write(p.resolve("file.txt"), new byte[1000]);
        assertAllow(policy, p);
    }

    private static void assertAllow(WorkspacePolicy policy, Path p) throws IOException {
        CheckResult<WorkspaceRule, Path> result = policy.check(p);
        assertTrue(result.getDeny().isEmpty());
    }

    private static void assertDeny(WorkspacePolicy policy, Path p) throws IOException {
        CheckResult<WorkspaceRule, Path> result = policy.check(p);
        assertFalse(result.getDeny().isEmpty());
    }
}
