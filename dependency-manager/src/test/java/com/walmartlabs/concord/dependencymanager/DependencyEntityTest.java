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
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class DependencyEntityTest {

    @Test
    public void testArtifactConstructor() {
        Path p = Paths.get("/tmp/artifact.jar");
        DependencyEntity entity = new DependencyEntity(p, "com.example", "my-lib", "1.0");
        assertEquals(p, entity.getPath());
        assertNotNull(entity.getArtifact());
        assertNull(entity.getDirectLink());
        assertEquals("com.example", entity.getArtifact().getGroupId());
        assertEquals("my-lib", entity.getArtifact().getArtifactId());
        assertEquals("1.0", entity.getArtifact().getVersion());
    }

    @Test
    public void testDirectLinkConstructor() {
        Path p = Paths.get("/tmp/direct.jar");
        URI uri = URI.create("https://example.com/direct.jar");
        DependencyEntity entity = new DependencyEntity(p, uri);
        assertEquals(p, entity.getPath());
        assertNull(entity.getArtifact());
        assertEquals(uri, entity.getDirectLink());
    }

    @Test
    public void testToStringArtifact() {
        DependencyEntity entity = new DependencyEntity(
                Paths.get("/tmp/a.jar"), "com.example", "my-lib", "1.0");
        String s = entity.toString();
        assertTrue(s.contains("com.example"));
        assertTrue(s.contains("my-lib"));
        assertTrue(s.contains("1.0"));
    }

    @Test
    public void testToStringDirectLink() {
        URI uri = URI.create("https://example.com/direct.jar");
        DependencyEntity entity = new DependencyEntity(Paths.get("/tmp/d.jar"), uri);
        assertEquals("https://example.com/direct.jar", entity.toString());
    }

    @Test
    public void testEqualsSamePath() {
        DependencyEntity a = new DependencyEntity(Paths.get("/tmp/a.jar"), "g", "a", "1");
        DependencyEntity b = new DependencyEntity(Paths.get("/tmp/a.jar"), "g2", "a2", "2");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testNotEqualsDifferentPath() {
        DependencyEntity a = new DependencyEntity(Paths.get("/tmp/a.jar"), "g", "a", "1");
        DependencyEntity b = new DependencyEntity(Paths.get("/tmp/b.jar"), "g", "a", "1");
        assertNotEquals(a, b);
    }

    @Test
    public void testEqualsNull() {
        DependencyEntity a = new DependencyEntity(Paths.get("/tmp/a.jar"), "g", "a", "1");
        assertNotEquals(null, a);
    }

    @Test
    public void testArtifactToString() {
        DependencyEntity.Artifact artifact = new DependencyEntity.Artifact("com.example", "lib", "2.0");
        String s = artifact.toString();
        assertTrue(s.contains("com.example"));
        assertTrue(s.contains("lib"));
        assertTrue(s.contains("2.0"));
    }
}
