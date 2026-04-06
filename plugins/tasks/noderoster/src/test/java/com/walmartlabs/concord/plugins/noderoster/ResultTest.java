package com.walmartlabs.concord.plugins.noderoster;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void testCreateResponseWithData() {
        var result = Result.createResponse("someData");
        assertTrue(result.isOk());
        assertEquals("someData", result.getData());
    }

    @Test
    void testCreateResponseWithNull() {
        var result = Result.createResponse(null);
        assertFalse(result.isOk());
        assertNull(result.getData());
    }

    @Test
    void testCreateResponseWithList() {
        var data = List.of("a", "b", "c");
        var result = Result.createResponse(data);
        assertTrue(result.isOk());
        assertEquals(data, result.getData());
    }

    @Test
    void testConstructorDirectly() {
        var result = new Result(false, "error");
        assertFalse(result.isOk());
        assertEquals("error", result.getData());
    }

    @Test
    void testToString() {
        var result = new Result(true, "test");
        var str = result.toString();
        assertTrue(str.contains("ok=true"));
        assertTrue(str.contains("test"));
    }
}
