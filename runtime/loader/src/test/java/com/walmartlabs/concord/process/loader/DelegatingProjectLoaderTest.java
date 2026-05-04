package com.walmartlabs.concord.process.loader;

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

import com.walmartlabs.concord.imports.ImportsListener;
import com.walmartlabs.concord.repository.Snapshot;
import com.walmartlabs.concord.runtime.model.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DelegatingProjectLoaderTest {

    @Test
    public void testNullDelegatesRejected() {
        assertThrows(NullPointerException.class, () -> new DelegatingProjectLoader(null));
    }

    @Test
    public void testSupports_emptyDelegates() {
        var loader = new DelegatingProjectLoader(Set.of());
        assertFalse(loader.supports("concord-v1"));
    }

    @Test
    public void testSupports_matchingDelegate() {
        var loader = new DelegatingProjectLoader(Set.of(new FakeLoader("concord-v1")));

        assertTrue(loader.supports("concord-v1"));
        assertFalse(loader.supports("concord-v2"));
    }

    @Test
    public void testSupports_multipleDelegates() {
        var delegates = new HashSet<ProjectLoader>();
        delegates.add(new FakeLoader("concord-v1"));
        delegates.add(new FakeLoader("concord-v2"));

        var loader = new DelegatingProjectLoader(delegates);

        assertTrue(loader.supports("concord-v1"));
        assertTrue(loader.supports("concord-v2"));
        assertFalse(loader.supports("unknown"));
    }

    @Test
    public void testLoadProject_delegatesToMatchingLoader() throws Exception {
        var v2 = new FakeLoader("concord-v2");
        var loader = new DelegatingProjectLoader(Set.of(new FakeLoader("concord-v1"), v2));

        var result = loader.loadProject(Path.of("/tmp/x"), "concord-v2", imports -> imports, ImportsListener.NOP_LISTENER);

        assertNotNull(result);
        assertEquals("concord-v2", v2.lastRuntime);
    }

    @Test
    public void testLoadProject_unsupportedRuntimeThrows() {
        var loader = new DelegatingProjectLoader(Set.of(new FakeLoader("concord-v1")));

        var ex = assertThrows(UnsupportedRuntimeTypeException.class,
                () -> loader.loadProject(Path.of("/tmp/x"), "concord-v3", imports -> imports, ImportsListener.NOP_LISTENER));
        assertTrue(ex.getMessage().contains("concord-v3"));
    }

    @Test
    public void testLoadProject_autoDetectsFromConcordYml(@TempDir Path workDir) throws Exception {
        Files.writeString(workDir.resolve("concord.yml"), "configuration:\n  runtime: concord-v2\n");

        var v2 = new FakeLoader("concord-v2");
        var loader = new DelegatingProjectLoader(Set.of(new FakeLoader("concord-v1"), v2));

        loader.loadProject(workDir, imports -> imports, ImportsListener.NOP_LISTENER);

        assertEquals("concord-v2", v2.lastRuntime);
    }

    @Test
    public void testLoadProject_autoDetectFallsBackToV1WhenNoRuntimeDeclared(@TempDir Path workDir) throws Exception {
        Files.writeString(workDir.resolve("concord.yml"), "flows:\n  default:\n    - log: hi\n");

        var v1 = new FakeLoader("concord-v1");
        var loader = new DelegatingProjectLoader(Set.of(v1, new FakeLoader("concord-v2")));

        loader.loadProject(workDir, imports -> imports, ImportsListener.NOP_LISTENER);

        assertEquals("concord-v1", v1.lastRuntime);
    }

    @Test
    public void testLoadProject_autoDetectThrowsWhenNoMatchingLoader(@TempDir Path workDir) throws IOException {
        Files.writeString(workDir.resolve("concord.yml"), "flows:\n  default:\n    - log: hi\n");

        var loader = new DelegatingProjectLoader(Set.of(new FakeLoader("concord-v2")));

        assertThrows(UnsupportedRuntimeTypeException.class,
                () -> loader.loadProject(workDir, imports -> imports, ImportsListener.NOP_LISTENER));
    }

    private static final class FakeLoader implements ProjectLoader {

        private final String supportedRuntime;
        private String lastRuntime;

        FakeLoader(String supportedRuntime) {
            this.supportedRuntime = supportedRuntime;
        }

        @Override
        public boolean supports(String runtime) {
            return Objects.equals(supportedRuntime, runtime);
        }

        @Override
        public Result loadProject(Path workDir, String runtime, ImportsNormalizer importsNormalizer, ImportsListener listener) {
            this.lastRuntime = runtime;
            return new Result() {
                @Override
                public List<Snapshot> snapshots() {
                    return List.of();
                }

                @Override
                public ProcessDefinition projectDefinition() {
                    return null;
                }
            };
        }
    }
}
