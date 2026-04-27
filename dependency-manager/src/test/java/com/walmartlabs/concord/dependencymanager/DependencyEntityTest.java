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
        var path = Path.of("/tmp/artifact.jar");
        var entity = new DependencyEntity(path, "com.example", "my-lib", "1.0.0");

        assertEquals(path, entity.getPath());
        assertNotNull(entity.getArtifact());
        assertNull(entity.getDirectLink());
        assertEquals("com.example", entity.getArtifact().getGroupId());
        assertEquals("my-lib", entity.getArtifact().getArtifactId());
        assertEquals("1.0.0", entity.getArtifact().getVersion());
    }

    @Test
    public void testDirectLinkConstructor() {
        var path = Path.of("/tmp/download.jar");
        var uri = URI.create("https://example.com/download.jar");
        var entity = new DependencyEntity(path, uri);

        assertEquals(path, entity.getPath());
        assertNull(entity.getArtifact());
        assertEquals(uri, entity.getDirectLink());
    }

    @Test
    public void testEqualsBasedOnPath() {
        var path = Path.of("/tmp/same.jar");
        var a = new DependencyEntity(path, "g1", "a1", "1.0");
        var b = new DependencyEntity(path, "g2", "a2", "2.0");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testNotEqualDifferentPaths() {
        var a = new DependencyEntity(Path.of("/tmp/a.jar"), "g", "a", "1.0");
        var b = new DependencyEntity(Path.of("/tmp/b.jar"), "g", "a", "1.0");
        assertNotEquals(a, b);
    }

    @Test
    public void testToStringArtifact() {
        var entity = new DependencyEntity(Path.of("/tmp/a.jar"), "com.example", "lib", "2.0");
        var s = entity.toString();
        assertTrue(s.contains("com.example"));
        assertTrue(s.contains("lib"));
        assertTrue(s.contains("2.0"));
    }

    @Test
    public void testToStringDirectLink() {
        var uri = URI.create("https://example.com/lib.jar");
        var entity = new DependencyEntity(Path.of("/tmp/a.jar"), uri);
        assertEquals(uri.toString(), entity.toString());
    }

    @Test
    public void testEqualsSameInstance() {
        var entity = new DependencyEntity(Path.of("/tmp/a.jar"), "g", "a", "1.0");
        assertEquals(entity, entity);
    }

    @Test
    public void testEqualsNull() {
        var entity = new DependencyEntity(Path.of("/tmp/a.jar"), "g", "a", "1.0");
        assertNotEquals(null, entity);
    }

    @Test
    public void testEqualsDifferentClass() {
        var entity = new DependencyEntity(Path.of("/tmp/a.jar"), "g", "a", "1.0");
        assertNotEquals("string", entity);
    }
}
