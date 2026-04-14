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
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class DependencyEntityTest {

    @Test
    public void testArtifactConstructor() {
        Path path = Paths.get("/tmp/test.jar");
        DependencyEntity entity = new DependencyEntity(path, "com.example", "my-lib", "1.0.0");

        assertEquals(path, entity.getPath());
        assertNotNull(entity.getArtifact());
        assertEquals("com.example", entity.getArtifact().getGroupId());
        assertEquals("my-lib", entity.getArtifact().getArtifactId());
        assertEquals("1.0.0", entity.getArtifact().getVersion());
        assertNull(entity.getDirectLink());
    }

    @Test
    public void testDirectLinkConstructor() {
        Path path = Paths.get("/tmp/test.jar");
        URI uri = URI.create("https://example.com/test.jar");
        DependencyEntity entity = new DependencyEntity(path, uri);

        assertEquals(path, entity.getPath());
        assertNull(entity.getArtifact());
        assertEquals(uri, entity.getDirectLink());
    }

    @Test
    public void testEqualsByPath() {
        Path path = Paths.get("/tmp/test.jar");
        DependencyEntity e1 = new DependencyEntity(path, "com.example", "lib1", "1.0");
        DependencyEntity e2 = new DependencyEntity(path, "com.other", "lib2", "2.0");

        assertEquals(e1, e2);
    }

    @Test
    public void testNotEqualsDifferentPath() {
        DependencyEntity e1 = new DependencyEntity(Paths.get("/tmp/a.jar"), "com.example", "lib1", "1.0");
        DependencyEntity e2 = new DependencyEntity(Paths.get("/tmp/b.jar"), "com.example", "lib1", "1.0");

        assertNotEquals(e1, e2);
    }

    @Test
    public void testEqualsNull() {
        DependencyEntity e = new DependencyEntity(Paths.get("/tmp/a.jar"), "g", "a", "1.0");
        assertNotEquals(null, e);
    }

    @Test
    public void testEqualsSelf() {
        DependencyEntity e = new DependencyEntity(Paths.get("/tmp/a.jar"), "g", "a", "1.0");
        assertEquals(e, e);
    }

    @Test
    public void testHashCodeConsistency() {
        Path path = Paths.get("/tmp/test.jar");
        DependencyEntity e1 = new DependencyEntity(path, "com.example", "lib1", "1.0");
        DependencyEntity e2 = new DependencyEntity(path, "com.other", "lib2", "2.0");

        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    public void testToStringWithArtifact() {
        DependencyEntity entity = new DependencyEntity(Paths.get("/tmp/test.jar"), "com.example", "my-lib", "1.0.0");
        String str = entity.toString();
        assertTrue(str.contains("com.example"));
        assertTrue(str.contains("my-lib"));
        assertTrue(str.contains("1.0.0"));
    }

    @Test
    public void testToStringWithDirectLink() {
        URI uri = URI.create("https://example.com/test.jar");
        DependencyEntity entity = new DependencyEntity(Paths.get("/tmp/test.jar"), uri);
        assertEquals("https://example.com/test.jar", entity.toString());
    }
}
