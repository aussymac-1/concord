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
        RuntimeException e = new RuntimeException("root");
        List<Throwable> result = ExceptionUtils.getExceptionList(e);
        assertEquals(1, result.size());
        assertSame(e, result.get(0));
    }

    @Test
    public void testGetExceptionListChained() {
        Exception root = new Exception("root");
        RuntimeException mid = new RuntimeException("mid", root);
        RuntimeException top = new RuntimeException("top", mid);

        List<Throwable> result = ExceptionUtils.getExceptionList(top);
        assertEquals(3, result.size());
        assertSame(top, result.get(0));
        assertSame(mid, result.get(1));
        assertSame(root, result.get(2));
    }

    @Test
    public void testGetExceptionListNoCycle() {
        RuntimeException e = new RuntimeException("self");
        List<Throwable> result = ExceptionUtils.getExceptionList(e);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindLastExceptionFound() {
        IllegalArgumentException root = new IllegalArgumentException("root");
        RuntimeException mid = new RuntimeException("mid", root);
        RuntimeException top = new RuntimeException("top", mid);

        RuntimeException found = ExceptionUtils.findLastException(top, RuntimeException.class);
        assertSame(root, found);
    }

    @Test
    public void testFindLastExceptionNotFound() {
        RuntimeException top = new RuntimeException("top");

        RuntimeException found = ExceptionUtils.findLastException(top, RuntimeException.class);
        assertSame(top, found);
    }

    @Test
    public void testFindLastExceptionReturnsInputWhenNoMatch() {
        RuntimeException e = new RuntimeException("only");
        IllegalStateException result = ExceptionUtils.findLastException(
                new IllegalStateException("wrapper", e), IllegalStateException.class);
        assertNotNull(result);
    }
}
