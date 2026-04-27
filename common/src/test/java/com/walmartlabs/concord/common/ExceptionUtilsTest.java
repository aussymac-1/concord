package com.walmartlabs.concord.common;

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

public class ExceptionUtilsTest {

    @Test
    public void testGetExceptionListSingle() {
        var ex = new RuntimeException("boom");
        List<Throwable> result = ExceptionUtils.getExceptionList(ex);
        assertEquals(1, result.size());
        assertSame(ex, result.get(0));
    }

    @Test
    public void testGetExceptionListChain() {
        var root = new IllegalArgumentException("root");
        var mid = new RuntimeException("mid", root);
        var top = new Exception("top", mid);

        List<Throwable> result = ExceptionUtils.getExceptionList(top);
        assertEquals(3, result.size());
        assertSame(top, result.get(0));
        assertSame(mid, result.get(1));
        assertSame(root, result.get(2));
    }

    @Test
    public void testGetExceptionListNull() {
        List<Throwable> result = ExceptionUtils.getExceptionList(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindLastExceptionMatchesDeepest() {
        var root = new RuntimeException("root");
        var mid = new RuntimeException("mid", root);
        var top = new RuntimeException("top", mid);

        var found = ExceptionUtils.findLastException(top, RuntimeException.class);
        assertSame(root, found);
    }

    @Test
    public void testFindLastExceptionSubclassMatchesDeepest() {
        var root = new IllegalArgumentException("root");
        var top = new RuntimeException("top", root);

        var found = ExceptionUtils.findLastException(top, RuntimeException.class);
        assertSame(root, found);
    }

    @Test
    public void testFindLastExceptionReturnsSelfWhenOnlyTopMatches() {
        var root = new Exception("root cause");
        var top = new RuntimeException("top", root);

        var found = ExceptionUtils.findLastException(top, RuntimeException.class);
        assertSame(top, found);
    }

    @Test
    public void testFindLastExceptionSingleElement() {
        var ex = new RuntimeException("only");
        var found = ExceptionUtils.findLastException(ex, RuntimeException.class);
        assertSame(ex, found);
    }
}
