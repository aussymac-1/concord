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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MavenRepositoryTest {

    @Test
    public void testDefaults() {
        var r = MavenRepository.builder()
                .id("local")
                .url("https://example.com/")
                .build();

        assertEquals("local", r.id());
        assertEquals("https://example.com/", r.url());
        assertEquals("default", r.contentType());
        assertNull(r.auth());
        assertNull(r.proxy());
        assertNotNull(r.snapshotPolicy());
        assertNotNull(r.releasePolicy());
        assertTrue(r.snapshotPolicy().enabled());
        assertTrue(r.releasePolicy().enabled());
    }

    @Test
    public void testSerializationRoundTrip() throws Exception {
        var r = MavenRepository.builder()
                .id("remote")
                .url("https://example.com/")
                .auth(Map.of("user", "u", "password", "p"))
                .build();

        var om = new ObjectMapper();
        var json = om.writeValueAsString(r);
        var r2 = om.readValue(json, MavenRepository.class);

        assertEquals(r.id(), r2.id());
        assertEquals(r.url(), r2.url());
    }

    @Test
    public void testLayoutAliasFromJson() throws Exception {
        var json = "{\"id\":\"r\",\"url\":\"https://example.com/\",\"layout\":\"default\"}";
        var r = new ObjectMapper().readValue(json, MavenRepository.class);
        assertEquals("default", r.contentType());
    }
}
