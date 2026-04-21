package com.walmartlabs.concord.plugins.misc;

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

import static org.junit.jupiter.api.Assertions.*;

public class MiscTaskV2Test {

    @Test
    public void testThrowRuntimeExceptionPropagatesMessage() {
        MiscTaskV2 task = new MiscTaskV2();

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> task.throwRuntimeException("kaboom"));
        assertEquals("kaboom", ex.getMessage());
    }

    @Test
    public void testTrimReturnsNullForNullInput() {
        MiscTaskV2 task = new MiscTaskV2();
        assertNull(task.trim(null, 5));
    }

    @Test
    public void testTrimReturnsInputWhenShorterThanLimit() {
        MiscTaskV2 task = new MiscTaskV2();
        assertEquals("abc", task.trim("abc", 5));
    }

    @Test
    public void testTrimReturnsInputWhenExactlyAtLimit() {
        MiscTaskV2 task = new MiscTaskV2();
        assertEquals("abcde", task.trim("abcde", 5));
    }

    @Test
    public void testTrimAppendsEllipsisWhenLongerThanLimit() {
        MiscTaskV2 task = new MiscTaskV2();
        // length=10, limit=8 -> keep first (8-3)=5 chars, then "..."
        assertEquals("abcde...", task.trim("abcdefghij", 8));
    }

    @Test
    public void testTrimEmptyString() {
        MiscTaskV2 task = new MiscTaskV2();
        assertEquals("", task.trim("", 5));
    }
}
