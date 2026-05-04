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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MavenRepositoryConfigurationTest {

    @Test
    public void testEmptyDefault() {
        var c = ImmutableMavenRepositoryConfiguration.builder().build();
        assertTrue(c.repositories().isEmpty());
    }

    @Test
    public void testWithRepositories() {
        var r = MavenRepository.builder().id("r").url("u").build();
        var c = ImmutableMavenRepositoryConfiguration.builder()
                .repositories(List.of(r))
                .build();
        assertEquals(1, c.repositories().size());
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        var json = "{\"repositories\":[{\"id\":\"r\",\"url\":\"u\"}]}";
        var c = new ObjectMapper().readValue(json, MavenRepositoryConfiguration.class);
        assertEquals(1, c.repositories().size());
        assertEquals("r", c.repositories().get(0).id());
    }
}
