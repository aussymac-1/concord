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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MiscTaskV2Test {

    private final MiscTaskV2 task = new MiscTaskV2();

    @Test
    public void throwRuntimeExceptionThrowsWithMessage() {
        var ex = assertThrows(RuntimeException.class, () -> task.throwRuntimeException("fail"));
        assertEquals("fail", ex.getMessage());
    }

    @Test
    public void trimReturnsNullForNull() {
        assertNull(task.trim(null, 10));
    }

    @Test
    public void trimReturnsOriginalWhenUnderLimit() {
        assertEquals("abc", task.trim("abc", 10));
        assertEquals("abc", task.trim("abc", 3));
    }

    @Test
    public void trimTruncatesLongStringWithEllipsis() {
        assertEquals("abcdefg...", task.trim("abcdefghijk", 10));
    }

    @Test
    public void trimExactLengthReturnsSame() {
        assertEquals("12345", task.trim("12345", 5));
    }

    @Test
    public void trimVeryShortLimitStillTruncates() {
        assertEquals("a...", task.trim("abcdef", 4));
    }
}
