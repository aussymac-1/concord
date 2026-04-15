package com.walmartlabs.concord.client2.impl.auth;

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
import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.*;

public class ApiKeyTest {

    @Test
    void testApplyToSetsAuthorizationHeader() {
        ApiKey apiKey = new ApiKey("my-secret-key");
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api"));
        HttpRequest.Builder result = apiKey.applyTo(builder);
        HttpRequest request = result.build();
        assertTrue(request.headers().firstValue("Authorization").isPresent());
        assertEquals("my-secret-key", request.headers().firstValue("Authorization").get());
    }

    @Test
    void testApplyToReturnsBuilder() {
        ApiKey apiKey = new ApiKey("key");
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api"));
        HttpRequest.Builder result = apiKey.applyTo(builder);
        assertNotNull(result);
    }
}
