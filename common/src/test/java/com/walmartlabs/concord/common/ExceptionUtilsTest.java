package com.walmartlabs.concord.common;

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

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionUtilsTest {

    @Test
    public void testGetExceptionListSingleException() {
        RuntimeException e = new RuntimeException("single");
        List<Throwable> result = ExceptionUtils.getExceptionList(e);
        assertEquals(1, result.size());
        assertSame(e, result.get(0));
    }

    @Test
    public void testGetExceptionListChainedExceptions() {
        IOException root = new IOException("root");
        RuntimeException middle = new RuntimeException("middle", root);
        Exception top = new Exception("top", middle);

        List<Throwable> result = ExceptionUtils.getExceptionList(top);
        assertEquals(3, result.size());
        assertSame(top, result.get(0));
        assertSame(middle, result.get(1));
        assertSame(root, result.get(2));
    }

    @Test
    public void testGetExceptionListNull() {
        List<Throwable> result = ExceptionUtils.getExceptionList(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindLastExceptionFindsDeepestMatch() {
        IOException root = new IOException("root cause");
        RuntimeException wrapper = new RuntimeException("wrapper", root);

        RuntimeException result = ExceptionUtils.findLastException(wrapper, RuntimeException.class);
        assertSame(wrapper, result);
    }

    @Test
    public void testFindLastExceptionReturnsOriginalWhenNoMatch() {
        RuntimeException e = new RuntimeException("only one");
        IOException result = ExceptionUtils.findLastException(new IOException("top", e), IOException.class);
        assertNotNull(result);
        assertEquals("top", result.getMessage());
    }
}
