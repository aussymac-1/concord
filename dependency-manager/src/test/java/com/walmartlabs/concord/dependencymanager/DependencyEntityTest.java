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
        var p = Path.of("/tmp/foo.jar");
        var e = new DependencyEntity(p, "g", "a", "1.0");
        assertEquals(p, e.getPath());
        assertNotNull(e.getArtifact());
        assertEquals("g", e.getArtifact().getGroupId());
        assertEquals("a", e.getArtifact().getArtifactId());
        assertEquals("1.0", e.getArtifact().getVersion());
        assertNull(e.getDirectLink());
        assertTrue(e.toString().contains("g"));
    }

    @Test
    public void testDirectLinkConstructor() {
        var p = Path.of("/tmp/foo.jar");
        var u = URI.create("https://example.com/foo.jar");
        var e = new DependencyEntity(p, u);
        assertEquals(p, e.getPath());
        assertSame(u, e.getDirectLink());
        assertNull(e.getArtifact());
        assertEquals(u.toString(), e.toString());
    }

    @Test
    public void testEqualsAndHashCodeBasedOnPath() {
        var p1 = Path.of("/tmp/foo.jar");
        var p2 = Path.of("/tmp/bar.jar");

        var a = new DependencyEntity(p1, "g", "a", "1.0");
        var b = new DependencyEntity(p1, URI.create("u"));
        var c = new DependencyEntity(p2, "g", "a", "1.0");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "a string");
        assertEquals(a, a);
    }

    @Test
    public void testArtifactToString() {
        var art = new DependencyEntity.Artifact("g", "a", "1.0");
        var s = art.toString();
        assertTrue(s.contains("groupId='g'"));
        assertTrue(s.contains("artifactId='a'"));
        assertTrue(s.contains("version='1.0'"));
    }
}
