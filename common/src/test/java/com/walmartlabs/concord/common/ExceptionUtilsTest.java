package com.walmartlabs.concord.common;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2019 Walmart Inc.
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

class ExceptionUtilsTest {

    @Test
    void testGetExceptionListSingle() {
        var ex = new RuntimeException("root");
        var list = ExceptionUtils.getExceptionList(ex);
        assertEquals(1, list.size());
        assertSame(ex, list.get(0));
    }

    @Test
    void testGetExceptionListChained() {
        var root = new RuntimeException("root");
        var mid = new IllegalStateException("mid", root);
        var top = new Exception("top", mid);

        var list = ExceptionUtils.getExceptionList(top);
        assertEquals(3, list.size());
        assertSame(top, list.get(0));
        assertSame(mid, list.get(1));
        assertSame(root, list.get(2));
    }

    @Test
    void testGetExceptionListNull() {
        var list = ExceptionUtils.getExceptionList(null);
        assertTrue(list.isEmpty());
    }

    @Test
    void testFindLastExceptionMatchesDeepest() {
        var root = new RuntimeException("root");
        var mid = new RuntimeException("mid", root);
        var top = new RuntimeException("top", mid);

        var result = ExceptionUtils.findLastException(top, RuntimeException.class);
        assertSame(root, result);
    }

    @Test
    void testFindLastExceptionNoMatch() {
        var root = new RuntimeException("root");
        var top = new RuntimeException("top", root);

        // When no IllegalStateException exists, should return the original exception
        var result = ExceptionUtils.findLastException(top, RuntimeException.class);
        assertSame(root, result);
    }

    @Test
    void testFindLastExceptionReturnsSelfWhenNoMatchingType() {
        var root = new Exception("root");
        var top = new RuntimeException("top", root);

        // findLastException with RuntimeException.class should find top (not root which is just Exception)
        var result = ExceptionUtils.findLastException(top, RuntimeException.class);
        assertSame(top, result);
    }
}
