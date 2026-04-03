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
        Path path = Paths.get("/tmp/artifact.jar");
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
        Path path = Paths.get("/tmp/artifact.jar");
        URI link = URI.create("https://example.com/artifact.jar");
        DependencyEntity entity = new DependencyEntity(path, link);

        assertEquals(path, entity.getPath());
        assertNull(entity.getArtifact());
        assertEquals(link, entity.getDirectLink());
    }

    @Test
    public void testEqualsBasedOnPath() {
        Path path = Paths.get("/tmp/artifact.jar");
        DependencyEntity e1 = new DependencyEntity(path, "com.a", "lib-a", "1.0");
        DependencyEntity e2 = new DependencyEntity(path, URI.create("https://example.com/b.jar"));

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    public void testNotEqualsDifferentPath() {
        DependencyEntity e1 = new DependencyEntity(Paths.get("/tmp/a.jar"), "com.a", "lib-a", "1.0");
        DependencyEntity e2 = new DependencyEntity(Paths.get("/tmp/b.jar"), "com.a", "lib-a", "1.0");

        assertNotEquals(e1, e2);
    }

    @Test
    public void testToStringWithArtifact() {
        DependencyEntity entity = new DependencyEntity(Paths.get("/tmp/a.jar"), "com.example", "my-lib", "1.0.0");
        String str = entity.toString();
        assertTrue(str.contains("com.example"));
        assertTrue(str.contains("my-lib"));
        assertTrue(str.contains("1.0.0"));
    }

    @Test
    public void testToStringWithDirectLink() {
        URI link = URI.create("https://example.com/artifact.jar");
        DependencyEntity entity = new DependencyEntity(Paths.get("/tmp/a.jar"), link);
        assertEquals("https://example.com/artifact.jar", entity.toString());
    }

    @Test
    public void testEqualsWithNull() {
        DependencyEntity entity = new DependencyEntity(Paths.get("/tmp/a.jar"), "com.a", "lib", "1.0");
        assertNotEquals(null, entity);
    }

    @Test
    public void testEqualsWithSelf() {
        DependencyEntity entity = new DependencyEntity(Paths.get("/tmp/a.jar"), "com.a", "lib", "1.0");
        assertEquals(entity, entity);
    }
}
