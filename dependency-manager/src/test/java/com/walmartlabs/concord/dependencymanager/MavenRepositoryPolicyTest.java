package com.walmartlabs.concord.dependencymanager;

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

import org.eclipse.aether.repository.RepositoryPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MavenRepositoryPolicyTest {

    @Test
    public void testDefaults() {
        var p = MavenRepositoryPolicy.builder().build();
        assertTrue(p.enabled());
        assertEquals(RepositoryPolicy.UPDATE_POLICY_NEVER, p.updatePolicy());
        assertEquals(RepositoryPolicy.CHECKSUM_POLICY_IGNORE, p.checksumPolicy());
    }

    @Test
    public void testOverrides() {
        var p = MavenRepositoryPolicy.builder()
                .enabled(false)
                .updatePolicy(RepositoryPolicy.UPDATE_POLICY_DAILY)
                .checksumPolicy(RepositoryPolicy.CHECKSUM_POLICY_FAIL)
                .build();

        assertFalse(p.enabled());
        assertEquals(RepositoryPolicy.UPDATE_POLICY_DAILY, p.updatePolicy());
        assertEquals(RepositoryPolicy.CHECKSUM_POLICY_FAIL, p.checksumPolicy());
    }
}
