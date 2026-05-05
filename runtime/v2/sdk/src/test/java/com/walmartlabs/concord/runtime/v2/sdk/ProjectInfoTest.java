package com.walmartlabs.concord.runtime.v2.sdk;

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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProjectInfoTest {

    @Test
    public void testDefaultsAreAllNull() {
        var info = ProjectInfo.builder().build();

        assertNull(info.orgId());
        assertNull(info.orgName());
        assertNull(info.projectId());
        assertNull(info.projectName());
        assertNull(info.repoId());
        assertNull(info.repoName());
        assertNull(info.repoUrl());
        assertNull(info.repoBranch());
        assertNull(info.repoPath());
        assertNull(info.repoCommitId());
        assertNull(info.repoCommitAuthor());
        assertNull(info.repoCommitMessage());
    }

    @Test
    public void testFullyPopulated() {
        var orgId = UUID.randomUUID();
        var projectId = UUID.randomUUID();
        var repoId = UUID.randomUUID();

        var info = ProjectInfo.builder()
                .orgId(orgId)
                .orgName("org")
                .projectId(projectId)
                .projectName("proj")
                .repoId(repoId)
                .repoName("repo")
                .repoUrl("https://example.com/repo.git")
                .repoBranch("main")
                .repoPath("flows")
                .repoCommitId("abc123")
                .repoCommitAuthor("dev")
                .repoCommitMessage("init")
                .build();

        assertEquals(orgId, info.orgId());
        assertEquals("org", info.orgName());
        assertEquals(projectId, info.projectId());
        assertEquals("proj", info.projectName());
        assertEquals(repoId, info.repoId());
        assertEquals("repo", info.repoName());
        assertEquals("https://example.com/repo.git", info.repoUrl());
        assertEquals("main", info.repoBranch());
        assertEquals("flows", info.repoPath());
        assertEquals("abc123", info.repoCommitId());
        assertEquals("dev", info.repoCommitAuthor());
        assertEquals("init", info.repoCommitMessage());
    }

    @Test
    public void testEqualityIsValueBased() {
        var a = ProjectInfo.builder().orgName("a").build();
        var b = ProjectInfo.builder().orgName("a").build();
        var c = ProjectInfo.builder().orgName("b").build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
