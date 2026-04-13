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

import static org.junit.jupiter.api.Assertions.*;

public class ResultTest {

    @Test
    public void testCreateResponseWithData() {
        var result = Result.createResponse("data");
        assertTrue(result.isOk());
        assertEquals("data", result.getData());
    }

    @Test
    public void testCreateResponseWithNull() {
        var result = Result.createResponse(null);
        assertFalse(result.isOk());
        assertNull(result.getData());
    }

    @Test
    public void testConstructor() {
        var result = new Result(true, "test");
        assertTrue(result.isOk());
        assertEquals("test", result.getData());
    }

    @Test
    public void testToString() {
        var result = new Result(true, "hello");
        var str = result.toString();
        assertTrue(str.contains("ok=true"));
        assertTrue(str.contains("hello"));
    }

    @Test
    public void testNotOk() {
        var result = new Result(false, null);
        assertFalse(result.isOk());
        assertNull(result.getData());
    }
}
