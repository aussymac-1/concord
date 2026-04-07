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
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class DependencyEntityTest {

    @Test
    public void testArtifactConstructor() {
        Path path = Paths.get("/tmp/test.jar");
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
        Path path = Paths.get("/tmp/test.jar");
        URI link = URI.create("https://example.com/test.jar");
        DependencyEntity entity = new DependencyEntity(path, link);

        assertEquals(path, entity.getPath());
        assertNull(entity.getArtifact());
        assertEquals(link, entity.getDirectLink());
    }

    @Test
    public void testEqualsAndHashCode() {
        Path path = Paths.get("/tmp/test.jar");
        DependencyEntity entity1 = new DependencyEntity(path, "com.example", "lib", "1.0");
        DependencyEntity entity2 = new DependencyEntity(path, "com.other", "other", "2.0");

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    public void testNotEquals() {
        DependencyEntity entity1 = new DependencyEntity(Paths.get("/tmp/a.jar"), "com.example", "lib", "1.0");
        DependencyEntity entity2 = new DependencyEntity(Paths.get("/tmp/b.jar"), "com.example", "lib", "1.0");

        assertNotEquals(entity1, entity2);
    }

    @Test
    public void testNotEqualsNull() {
        DependencyEntity entity = new DependencyEntity(Paths.get("/tmp/a.jar"), "com.example", "lib", "1.0");
        assertNotEquals(null, entity);
    }

    @Test
    public void testToStringArtifact() {
        DependencyEntity entity = new DependencyEntity(Paths.get("/tmp/test.jar"), "com.example", "my-lib", "1.0.0");
        String str = entity.toString();
        assertTrue(str.contains("com.example"));
        assertTrue(str.contains("my-lib"));
        assertTrue(str.contains("1.0.0"));
    }

    @Test
    public void testToStringDirectLink() {
        URI link = URI.create("https://example.com/test.jar");
        DependencyEntity entity = new DependencyEntity(Paths.get("/tmp/test.jar"), link);
        assertEquals("https://example.com/test.jar", entity.toString());
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
