package com.walmartlabs.concord.dependencymanager;

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
        Path path = Path.of("/tmp/artifact.jar");
        URI uri = URI.create("https://example.com/artifact.jar");
        DependencyEntity entity = new DependencyEntity(path, uri);

        assertEquals(path, entity.getPath());
        assertNull(entity.getArtifact());
        assertEquals(uri, entity.getDirectLink());
    }

    @Test
    public void testEqualsAndHashCode() {
        Path path1 = Path.of("/tmp/artifact.jar");
        Path path2 = Path.of("/tmp/artifact.jar");
        Path path3 = Path.of("/tmp/other.jar");

        DependencyEntity e1 = new DependencyEntity(path1, "g", "a", "1.0");
        DependencyEntity e2 = new DependencyEntity(path2, "g2", "a2", "2.0");
        DependencyEntity e3 = new DependencyEntity(path3, "g", "a", "1.0");

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotEquals(e1, e3);
    }

    @Test
    public void testEqualsSameObject() {
        DependencyEntity entity = new DependencyEntity(Path.of("/tmp/a.jar"), "g", "a", "1.0");
        assertEquals(entity, entity);
    }

    @Test
    public void testEqualsNull() {
        DependencyEntity entity = new DependencyEntity(Path.of("/tmp/a.jar"), "g", "a", "1.0");
        assertNotEquals(null, entity);
    }

    @Test
    public void testEqualsDifferentClass() {
        DependencyEntity entity = new DependencyEntity(Path.of("/tmp/a.jar"), "g", "a", "1.0");
        assertNotEquals("string", entity);
    }

    @Test
    public void testToStringArtifact() {
        DependencyEntity entity = new DependencyEntity(Path.of("/tmp/a.jar"), "com.example", "my-lib", "1.0.0");
        String str = entity.toString();
        assertTrue(str.contains("com.example"));
        assertTrue(str.contains("my-lib"));
        assertTrue(str.contains("1.0.0"));
    }

    @Test
    public void testToStringDirectLink() {
        URI uri = URI.create("https://example.com/artifact.jar");
        DependencyEntity entity = new DependencyEntity(Path.of("/tmp/a.jar"), uri);
        assertEquals("https://example.com/artifact.jar", entity.toString());
    }

    @Test
    public void testArtifactToString() {
        DependencyEntity.Artifact artifact = new DependencyEntity.Artifact("com.example", "my-lib", "1.0.0");
        String str = artifact.toString();
        assertTrue(str.contains("com.example"));
        assertTrue(str.contains("my-lib"));
        assertTrue(str.contains("1.0.0"));
    }
}
