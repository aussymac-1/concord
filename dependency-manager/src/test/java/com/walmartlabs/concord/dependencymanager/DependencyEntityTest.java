package com.walmartlabs.concord.dependencymanager;

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

import java.net.URI;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DependencyEntityTest {

    @Test
    void testArtifactConstructor() {
        var path = Path.of("/tmp/test.jar");
        var entity = new DependencyEntity(path, "com.example", "my-lib", "1.0.0");
        assertEquals(path, entity.getPath());
        assertNotNull(entity.getArtifact());
        assertNull(entity.getDirectLink());
        assertEquals("com.example", entity.getArtifact().getGroupId());
        assertEquals("my-lib", entity.getArtifact().getArtifactId());
        assertEquals("1.0.0", entity.getArtifact().getVersion());
    }

    @Test
    void testDirectLinkConstructor() {
        var path = Path.of("/tmp/test.jar");
        var uri = URI.create("https://example.com/test.jar");
        var entity = new DependencyEntity(path, uri);
        assertEquals(path, entity.getPath());
        assertNull(entity.getArtifact());
        assertEquals(uri, entity.getDirectLink());
    }

    @Test
    void testEqualsByPath() {
        var path = Path.of("/tmp/test.jar");
        var entity1 = new DependencyEntity(path, "com.a", "lib-a", "1.0");
        var entity2 = new DependencyEntity(path, "com.b", "lib-b", "2.0");
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void testNotEqualsDifferentPath() {
        var entity1 = new DependencyEntity(Path.of("/tmp/a.jar"), "com.a", "lib", "1.0");
        var entity2 = new DependencyEntity(Path.of("/tmp/b.jar"), "com.a", "lib", "1.0");
        assertNotEquals(entity1, entity2);
    }

    @Test
    void testToStringArtifact() {
        var entity = new DependencyEntity(Path.of("/tmp/test.jar"), "com.example", "lib", "1.0");
        var str = entity.toString();
        assertTrue(str.contains("com.example"));
        assertTrue(str.contains("lib"));
        assertTrue(str.contains("1.0"));
    }

    @Test
    void testToStringDirectLink() {
        var uri = URI.create("https://example.com/test.jar");
        var entity = new DependencyEntity(Path.of("/tmp/test.jar"), uri);
        assertEquals("https://example.com/test.jar", entity.toString());
    }

    @Test
    void testArtifactToString() {
        var artifact = new DependencyEntity.Artifact("com.example", "my-lib", "1.0.0");
        var str = artifact.toString();
        assertTrue(str.contains("com.example"));
        assertTrue(str.contains("my-lib"));
        assertTrue(str.contains("1.0.0"));
    }
}
