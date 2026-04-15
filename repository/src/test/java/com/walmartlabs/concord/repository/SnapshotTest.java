package com.walmartlabs.concord.repository;

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

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SnapshotTest {

    @Test
    void testSingleFileContainsMatchingPath() {
        Path file = Path.of("/tmp/test/file.txt");
        Snapshot snapshot = Snapshot.singleFile(file);
        assertTrue(snapshot.contains(file));
    }

    @Test
    void testSingleFileDoesNotContainOtherPath() {
        Path file = Path.of("/tmp/test/file.txt");
        Snapshot snapshot = Snapshot.singleFile(file);
        assertFalse(snapshot.contains(Path.of("/tmp/test/other.txt")));
    }

    @Test
    void testSingleFileIsModifiedAlwaysTrue() {
        Path file = Path.of("/tmp/test/file.txt");
        Snapshot snapshot = Snapshot.singleFile(file);
        assertTrue(snapshot.isModified(file, null));
    }

    @Test
    void testIncludeAllContainsAnyPath() {
        Snapshot snapshot = Snapshot.includeAll();
        assertTrue(snapshot.contains(Path.of("/any/path")));
        assertTrue(snapshot.contains(Path.of("/another/path")));
    }

    @Test
    void testIncludeAllIsModifiedAlwaysTrue() {
        Snapshot snapshot = Snapshot.includeAll();
        assertTrue(snapshot.isModified(Path.of("/any/path"), null));
    }
}
