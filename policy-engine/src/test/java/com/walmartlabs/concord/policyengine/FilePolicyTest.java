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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class FilePolicyTest {

    @Test
    public void testNullRules() throws Exception {
        FilePolicy policy = new FilePolicy(null);
        CheckResult<FileRule, Path> result = policy.check(Path.of("/tmp"));
        assertTrue(result.getDeny().isEmpty());
        assertTrue(result.getWarn().isEmpty());
    }

    @Test
    public void testEmptyRules() throws Exception {
        PolicyRules<FileRule> rules = new PolicyRules<>(null, null, null);
        FilePolicy policy = new FilePolicy(rules);
        CheckResult<FileRule, Path> result = policy.check(Path.of("/tmp"));
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testDenyByFileName(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("secret.key"), new byte[10]);

        FileRule denyRule = new FileRule("no key files", null, "FILE", Collections.singletonList("secret\\.key"));

        PolicyRules<FileRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        FilePolicy policy = new FilePolicy(rules);

        CheckResult<FileRule, Path> result = policy.check(tempDir);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testAllowByFileName(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("secret.key"), new byte[10]);

        FileRule allowRule = new FileRule("allowed", null, "FILE", Collections.singletonList("secret\\.key"));
        FileRule denyRule = new FileRule("denied", null, "FILE", Collections.singletonList(".*\\.key"));

        PolicyRules<FileRule> rules = new PolicyRules<>(
                Collections.singletonList(allowRule), null, Collections.singletonList(denyRule));
        FilePolicy policy = new FilePolicy(rules);

        CheckResult<FileRule, Path> result = policy.check(tempDir);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testWarnByFileName(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("large.dat"), new byte[10]);

        FileRule warnRule = new FileRule("warning", null, "FILE", Collections.singletonList("large\\.dat"));

        PolicyRules<FileRule> rules = new PolicyRules<>(null, Collections.singletonList(warnRule), null);
        FilePolicy policy = new FilePolicy(rules);

        CheckResult<FileRule, Path> result = policy.check(tempDir);
        assertTrue(result.getDeny().isEmpty());
        assertFalse(result.getWarn().isEmpty());
    }

    @Test
    public void testDenyByFileSize(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("big.bin"), new byte[2048]);

        FileRule denyRule = new FileRule("too big", "1KB", "FILE", Collections.emptyList());

        PolicyRules<FileRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        FilePolicy policy = new FilePolicy(rules);

        CheckResult<FileRule, Path> result = policy.check(tempDir);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testAllowedFileNotDenied(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("readme.txt"), new byte[10]);

        FileRule denyRule = new FileRule("no binaries", null, "FILE", Collections.singletonList(".*\\.bin"));

        PolicyRules<FileRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        FilePolicy policy = new FilePolicy(rules);

        CheckResult<FileRule, Path> result = policy.check(tempDir);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testDirRuleSkipsSubtree(@TempDir Path tempDir) throws Exception {
        Path subDir = tempDir.resolve("allowed_dir");
        Files.createDirectories(subDir);
        Files.write(subDir.resolve("secret.key"), new byte[10]);

        FileRule allowDirRule = new FileRule("allowed dir", null, "DIR", Collections.singletonList("allowed_dir"));
        FileRule denyFileRule = new FileRule("no key files", null, "FILE", Collections.singletonList(".*\\.key"));

        PolicyRules<FileRule> rules = new PolicyRules<>(
                Collections.singletonList(allowDirRule), null, Collections.singletonList(denyFileRule));
        FilePolicy policy = new FilePolicy(rules);

        CheckResult<FileRule, Path> result = policy.check(tempDir);
        assertTrue(result.getDeny().isEmpty());
    }
}
