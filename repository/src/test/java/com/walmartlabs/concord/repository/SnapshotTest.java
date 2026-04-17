package com.walmartlabs.concord.repository;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2026 Walmart Inc.
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

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class SnapshotTest {

    @Test
    void singleFileContainsMatchingPath() {
        Path file = Paths.get("/tmp/test.txt");
        Snapshot snapshot = Snapshot.singleFile(file);
        assertThat(snapshot.contains(file)).isTrue();
    }

    @Test
    void singleFileDoesNotContainDifferentPath() {
        Path file = Paths.get("/tmp/test.txt");
        Snapshot snapshot = Snapshot.singleFile(file);
        assertThat(snapshot.contains(Paths.get("/tmp/other.txt"))).isFalse();
    }

    @Test
    void singleFileIsAlwaysModified() {
        Path file = Paths.get("/tmp/test.txt");
        Snapshot snapshot = Snapshot.singleFile(file);
        assertThat(snapshot.isModified(file, null)).isTrue();
    }

    @Test
    void includeAllContainsAnyPath() {
        Snapshot snapshot = Snapshot.includeAll();
        assertThat(snapshot.contains(Paths.get("/any/path"))).isTrue();
        assertThat(snapshot.contains(Paths.get("/another/path"))).isTrue();
    }

    @Test
    void includeAllIsAlwaysModified() {
        Snapshot snapshot = Snapshot.includeAll();
        assertThat(snapshot.isModified(Paths.get("/any"), null)).isTrue();
    }
}
