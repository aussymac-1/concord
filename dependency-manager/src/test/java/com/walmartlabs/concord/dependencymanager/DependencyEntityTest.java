package com.walmartlabs.concord.dependencymanager;

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

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class DependencyEntityTest {

    @Test
    public void testArtifactConstructor() {
        var path = Paths.get("/tmp/test.jar");
        var entity = new DependencyEntity(path, "com.example", "my-lib", "1.0");

        assertEquals(path, entity.getPath());
        assertNotNull(entity.getArtifact());
        assertEquals("com.example", entity.getArtifact().getGroupId());
        assertEquals("my-lib", entity.getArtifact().getArtifactId());
        assertEquals("1.0", entity.getArtifact().getVersion());
        assertNull(entity.getDirectLink());
    }

    @Test
    public void testDirectLinkConstructor() {
        var path = Paths.get("/tmp/test.jar");
        var uri = URI.create("https://example.com/test.jar");
        var entity = new DependencyEntity(path, uri);

        assertEquals(path, entity.getPath());
        assertNull(entity.getArtifact());
        assertEquals(uri, entity.getDirectLink());
    }

    @Test
    public void testEqualsAndHashCodeByPath() {
        var path = Paths.get("/tmp/test.jar");
        var entity1 = new DependencyEntity(path, "com.example", "a", "1.0");
        var entity2 = new DependencyEntity(path, "com.other", "b", "2.0");

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    public void testNotEqualsDifferentPath() {
        var entity1 = new DependencyEntity(Paths.get("/tmp/a.jar"), "com.example", "a", "1.0");
        var entity2 = new DependencyEntity(Paths.get("/tmp/b.jar"), "com.example", "a", "1.0");

        assertNotEquals(entity1, entity2);
    }

    @Test
    public void testEqualsSameInstance() {
        var entity = new DependencyEntity(Paths.get("/tmp/a.jar"), "com.example", "a", "1.0");
        assertEquals(entity, entity);
    }

    @Test
    public void testEqualsNull() {
        var entity = new DependencyEntity(Paths.get("/tmp/a.jar"), "com.example", "a", "1.0");
        assertNotEquals(null, entity);
    }

    @Test
    public void testToStringArtifact() {
        var entity = new DependencyEntity(Paths.get("/tmp/test.jar"), "com.example", "my-lib", "1.0");
        var str = entity.toString();
        assertTrue(str.contains("com.example"));
        assertTrue(str.contains("my-lib"));
        assertTrue(str.contains("1.0"));
    }

    @Test
    public void testToStringDirectLink() {
        var uri = URI.create("https://example.com/test.jar");
        var entity = new DependencyEntity(Paths.get("/tmp/test.jar"), uri);
        assertEquals("https://example.com/test.jar", entity.toString());
    }

    @Test
    public void testArtifactToString() {
        var artifact = new DependencyEntity.Artifact("com.example", "my-lib", "1.0");
        var str = artifact.toString();
        assertTrue(str.contains("com.example"));
        assertTrue(str.contains("my-lib"));
        assertTrue(str.contains("1.0"));
    }

    @Test
    public void testArtifactGetters() {
        var artifact = new DependencyEntity.Artifact("g", "a", "v");
        assertEquals("g", artifact.getGroupId());
        assertEquals("a", artifact.getArtifactId());
        assertEquals("v", artifact.getVersion());
    }
}
