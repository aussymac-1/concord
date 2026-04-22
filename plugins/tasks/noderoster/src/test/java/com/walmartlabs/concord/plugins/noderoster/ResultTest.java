package com.walmartlabs.concord.plugins.noderoster;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ResultTest {

    @Test
    public void createResponseWithNonNullDataIsOk() {
        Result r = Result.createResponse(List.of(1, 2, 3));

        assertTrue(r.isOk());
        assertEquals(List.of(1, 2, 3), r.getData());
    }

    @Test
    public void createResponseWithNullDataIsNotOk() {
        Result r = Result.createResponse(null);

        assertFalse(r.isOk());
        assertNull(r.getData());
    }

    @Test
    public void explicitConstructorExposesBothFields() {
        Result r = new Result(true, "hello");
        assertTrue(r.isOk());
        assertEquals("hello", r.getData());

        Result r2 = new Result(false, null);
        assertFalse(r2.isOk());
        assertNull(r2.getData());
    }

    @Test
    public void toStringContainsOkFlagAndData() {
        String s = new Result(true, "x").toString();

        assertTrue(s.startsWith("Response{"));
        assertTrue(s.contains("ok=true"));
        assertTrue(s.contains("data='x'"));
    }
}
