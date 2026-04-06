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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilePolicyTest {

    @TempDir
    Path tempDir;

    @Test
    void testNullRulesAllowsEverything() throws IOException {
        var policy = new FilePolicy(null);
        var result = policy.check(tempDir);
        assertTrue(result.getDeny().isEmpty());
        assertTrue(result.getWarn().isEmpty());
    }

    @Test
    void testEmptyRulesAllowsEverything() throws IOException {
        Files.writeString(tempDir.resolve("test.txt"), "hello");

        var rules = new PolicyRules<FileRule>(
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        var policy = new FilePolicy(rules);
        var result = policy.check(tempDir);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    void testDenyByFileName() throws IOException {
        Files.writeString(tempDir.resolve("secret.key"), "sensitive");

        FileRule denyRule = new FileRule("no key files", null, "FILE", List.of(".*\\.key"));
        var rules = new PolicyRules<>(
                Collections.emptyList(), Collections.emptyList(), Collections.singletonList(denyRule));
        var policy = new FilePolicy(rules);

        var result = policy.check(tempDir);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    void testAllowOverridesDeny() throws IOException {
        Files.writeString(tempDir.resolve("secret.key"), "sensitive");

        FileRule allowRule = new FileRule(null, null, "FILE", List.of(".*\\.key"));
        FileRule denyRule = new FileRule("no key files", null, "FILE", List.of(".*\\.key"));
        var rules = new PolicyRules<>(
                Collections.singletonList(allowRule), Collections.emptyList(), Collections.singletonList(denyRule));
        var policy = new FilePolicy(rules);

        var result = policy.check(tempDir);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    void testWarnByFileName() throws IOException {
        Files.writeString(tempDir.resolve("large.dat"), "data");

        FileRule warnRule = new FileRule("large file warning", null, "FILE", List.of(".*\\.dat"));
        var rules = new PolicyRules<>(
                Collections.emptyList(), Collections.singletonList(warnRule), Collections.emptyList());
        var policy = new FilePolicy(rules);

        var result = policy.check(tempDir);
        assertTrue(result.getDeny().isEmpty());
        assertFalse(result.getWarn().isEmpty());
    }

    @Test
    void testNoMatchingFiles() throws IOException {
        Files.writeString(tempDir.resolve("readme.txt"), "hello");

        FileRule denyRule = new FileRule("no key files", null, "FILE", List.of(".*\\.key"));
        var rules = new PolicyRules<>(
                Collections.emptyList(), Collections.emptyList(), Collections.singletonList(denyRule));
        var policy = new FilePolicy(rules);

        var result = policy.check(tempDir);
        assertTrue(result.getDeny().isEmpty());
    }
}
