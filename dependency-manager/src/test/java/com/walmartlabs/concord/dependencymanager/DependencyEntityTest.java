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

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DependencyEntityTest {

    @Test
    public void testArtifactConstructor() {
        Path path = Path.of("/tmp/artifact.jar");
        DependencyEntity entity = new DependencyEntity(path, "com.example", "my-lib", "1.0.0");

        assertEquals(path, entity.getPath());
        assertNotNull(entity.getArtifact());
        assertNull(entity.getDirectLink());
        assertEquals("com.example", entity.getArtifact().getGroupId());
        assertEquals("my-lib", entity.getArtifact().getArtifactId());
        assertEquals("1.0.0", entity.getArtifact().getVersion());
    }

    @Test
    public void testDirectLinkConstructor() {
        Path path = Path.of("/tmp/dep.jar");
        URI link = URI.create("https://example.com/dep.jar");
        DependencyEntity entity = new DependencyEntity(path, link);

        assertEquals(path, entity.getPath());
        assertNull(entity.getArtifact());
        assertEquals(link, entity.getDirectLink());
    }

    @Test
    public void testEqualsSamePath() {
        Path path = Path.of("/tmp/dep.jar");
        DependencyEntity e1 = new DependencyEntity(path, "com.example", "lib", "1.0");
        DependencyEntity e2 = new DependencyEntity(path, "com.other", "other-lib", "2.0");

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    public void testNotEqualsDifferentPath() {
        DependencyEntity e1 = new DependencyEntity(Path.of("/a"), "g", "a", "1");
        DependencyEntity e2 = new DependencyEntity(Path.of("/b"), "g", "a", "1");

        assertNotEquals(e1, e2);
    }

    @Test
    public void testEqualsNull() {
        DependencyEntity e = new DependencyEntity(Path.of("/a"), "g", "a", "1");
        assertNotEquals(null, e);
    }

    @Test
    public void testEqualsSameObject() {
        DependencyEntity e = new DependencyEntity(Path.of("/a"), "g", "a", "1");
        assertEquals(e, e);
    }

    @Test
    public void testToStringArtifact() {
        DependencyEntity entity = new DependencyEntity(Path.of("/tmp/a.jar"), "com.example", "lib", "1.0");
        String str = entity.toString();
        assertTrue(str.contains("com.example"));
        assertTrue(str.contains("lib"));
        assertTrue(str.contains("1.0"));
    }

    @Test
    public void testToStringDirectLink() {
        URI link = URI.create("https://example.com/dep.jar");
        DependencyEntity entity = new DependencyEntity(Path.of("/tmp/a.jar"), link);
        assertEquals("https://example.com/dep.jar", entity.toString());
    }

    @Test
    public void testArtifactToString() {
        DependencyEntity.Artifact artifact = new DependencyEntity.Artifact("org.test", "my-art", "2.0.0");
        String str = artifact.toString();
        assertTrue(str.contains("org.test"));
        assertTrue(str.contains("my-art"));
        assertTrue(str.contains("2.0.0"));
    }

    @Test
    public void testArtifactGetters() {
        DependencyEntity.Artifact artifact = new DependencyEntity.Artifact("g", "a", "v");
        assertEquals("g", artifact.getGroupId());
        assertEquals("a", artifact.getArtifactId());
        assertEquals("v", artifact.getVersion());
    }
}
