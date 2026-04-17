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

import static org.assertj.core.api.Assertions.assertThat;

class DependencyEntityTest {

    @Test
    void constructorWithArtifactSetsFields() {
        Path path = Paths.get("/tmp/dep.jar");
        DependencyEntity entity = new DependencyEntity(path, "com.example", "lib", "1.0");

        assertThat(entity.getPath()).isEqualTo(path);
        assertThat(entity.getArtifact()).isNotNull();
        assertThat(entity.getArtifact().getGroupId()).isEqualTo("com.example");
        assertThat(entity.getArtifact().getArtifactId()).isEqualTo("lib");
        assertThat(entity.getArtifact().getVersion()).isEqualTo("1.0");
        assertThat(entity.getDirectLink()).isNull();
    }

    @Test
    void constructorWithDirectLinkSetsFields() {
        Path path = Paths.get("/tmp/dep.jar");
        URI link = URI.create("https://example.com/dep.jar");
        DependencyEntity entity = new DependencyEntity(path, link);

        assertThat(entity.getPath()).isEqualTo(path);
        assertThat(entity.getArtifact()).isNull();
        assertThat(entity.getDirectLink()).isEqualTo(link);
    }

    @Test
    void equalsBasedOnPath() {
        Path path = Paths.get("/tmp/dep.jar");
        DependencyEntity a = new DependencyEntity(path, "g", "a", "1");
        DependencyEntity b = new DependencyEntity(path, URI.create("https://x.com/y.jar"));

        assertThat(a).isEqualTo(b);
    }

    @Test
    void notEqualWithDifferentPath() {
        DependencyEntity a = new DependencyEntity(Paths.get("/a"), "g", "a", "1");
        DependencyEntity b = new DependencyEntity(Paths.get("/b"), "g", "a", "1");

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void hashCodeBasedOnPath() {
        Path path = Paths.get("/tmp/dep.jar");
        DependencyEntity a = new DependencyEntity(path, "g", "a", "1");
        DependencyEntity b = new DependencyEntity(path, URI.create("https://x.com/y.jar"));

        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void toStringWithArtifact() {
        DependencyEntity entity = new DependencyEntity(Paths.get("/tmp"), "com.example", "lib", "1.0");
        String result = entity.toString();
        assertThat(result).contains("com.example").contains("lib").contains("1.0");
    }

    @Test
    void toStringWithDirectLink() {
        URI link = URI.create("https://example.com/dep.jar");
        DependencyEntity entity = new DependencyEntity(Paths.get("/tmp"), link);
        assertThat(entity.toString()).isEqualTo("https://example.com/dep.jar");
    }

    @Test
    void artifactToStringContainsAllFields() {
        DependencyEntity.Artifact artifact = new DependencyEntity.Artifact("g", "a", "1");
        String result = artifact.toString();
        assertThat(result).contains("g").contains("a").contains("1");
    }
}
