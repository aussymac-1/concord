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

public class StatePolicyTest {

    @Test
    public void testNullRules() {
        StatePolicy policy = new StatePolicy(null);
        CheckResult<StateRule, StatePolicy.StateStats> result = policy.check(
                () -> new StatePolicy.StateStats(100L, 5));
        assertTrue(result.getDeny().isEmpty());
        assertTrue(result.getWarn().isEmpty());
    }

    @Test
    public void testEmptyRules() {
        PolicyRules<StateRule> rules = new PolicyRules<>(null, null, null);
        StatePolicy policy = new StatePolicy(rules);
        CheckResult<StateRule, StatePolicy.StateStats> result = policy.check(
                () -> new StatePolicy.StateStats(100L, 5));
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testDenyByMaxSize() {
        StateRule denyRule = StateRule.builder()
                .msg("too big")
                .maxSizeInBytes(100L)
                .build();

        PolicyRules<StateRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        StatePolicy policy = new StatePolicy(rules);

        CheckResult<StateRule, StatePolicy.StateStats> result = policy.check(
                () -> new StatePolicy.StateStats(200L, 5));
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testAllowByMaxSize() {
        StateRule denyRule = StateRule.builder()
                .msg("too big")
                .maxSizeInBytes(500L)
                .build();

        PolicyRules<StateRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        StatePolicy policy = new StatePolicy(rules);

        CheckResult<StateRule, StatePolicy.StateStats> result = policy.check(
                () -> new StatePolicy.StateStats(200L, 5));
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testDenyByMaxFilesCount() {
        StateRule denyRule = StateRule.builder()
                .msg("too many files")
                .maxFilesCount(3)
                .build();

        PolicyRules<StateRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        StatePolicy policy = new StatePolicy(rules);

        CheckResult<StateRule, StatePolicy.StateStats> result = policy.check(
                () -> new StatePolicy.StateStats(100L, 5));
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testWarnByMaxFilesCount() {
        StateRule warnRule = StateRule.builder()
                .msg("many files")
                .maxFilesCount(3)
                .build();

        PolicyRules<StateRule> rules = new PolicyRules<>(null, Collections.singletonList(warnRule), null);
        StatePolicy policy = new StatePolicy(rules);

        CheckResult<StateRule, StatePolicy.StateStats> result = policy.check(
                () -> new StatePolicy.StateStats(100L, 5));
        assertTrue(result.getDeny().isEmpty());
        assertFalse(result.getWarn().isEmpty());
    }

    @Test
    public void testPatternCheckDeny(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("state.dat"), new byte[10]);

        StateRule denyRule = StateRule.builder()
                .msg("denied pattern")
                .addPatterns(".*state\\.dat")
                .build();

        PolicyRules<StateRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        StatePolicy policy = new StatePolicy(rules);

        CheckResult<StateRule, Path> result = policy.check(tempDir, (path, attrs) -> true);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testPatternCheckAllow(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("readme.txt"), new byte[10]);

        StateRule denyRule = StateRule.builder()
                .msg("denied pattern")
                .addPatterns(".*\\.dat")
                .build();

        PolicyRules<StateRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        StatePolicy policy = new StatePolicy(rules);

        CheckResult<StateRule, Path> result = policy.check(tempDir, (path, attrs) -> true);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testPatternCheckNonExistentDir() throws Exception {
        StateRule denyRule = StateRule.builder()
                .addPatterns(".*")
                .build();

        PolicyRules<StateRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        StatePolicy policy = new StatePolicy(rules);

        CheckResult<StateRule, Path> result = policy.check(Path.of("/tmp/nonexistent_xyz"), (path, attrs) -> true);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testPatternCheckWithFilter(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("state.dat"), new byte[10]);

        StateRule denyRule = StateRule.builder()
                .msg("denied pattern")
                .addPatterns(".*state\\.dat")
                .build();

        PolicyRules<StateRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        StatePolicy policy = new StatePolicy(rules);

        // filter rejects all files
        CheckResult<StateRule, Path> result = policy.check(tempDir, (path, attrs) -> false);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testRulesWithOnlyPatternsSkippedInStatsCheck() {
        StateRule denyRule = StateRule.builder()
                .addPatterns(".*\\.dat")
                .build();

        PolicyRules<StateRule> rules = new PolicyRules<>(null, null, Collections.singletonList(denyRule));
        StatePolicy policy = new StatePolicy(rules);

        // should succeed because the deny rule has no stats (maxSize/maxFilesCount)
        CheckResult<StateRule, StatePolicy.StateStats> result = policy.check(
                () -> new StatePolicy.StateStats(999999L, 999));
        assertTrue(result.getDeny().isEmpty());
    }
}
