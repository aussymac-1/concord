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
        RuntimeException e = new RuntimeException("test");
        List<Throwable> list = ExceptionUtils.getExceptionList(e);
        assertEquals(1, list.size());
        assertSame(e, list.get(0));
    }

    @Test
    public void testGetExceptionListChained() {
        RuntimeException root = new RuntimeException("root");
        RuntimeException mid = new RuntimeException("mid", root);
        RuntimeException top = new RuntimeException("top", mid);

        List<Throwable> list = ExceptionUtils.getExceptionList(top);
        assertEquals(3, list.size());
        assertSame(top, list.get(0));
        assertSame(mid, list.get(1));
        assertSame(root, list.get(2));
    }

    @Test
    public void testGetExceptionListNull() {
        List<Throwable> list = ExceptionUtils.getExceptionList(null);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testFindLastExceptionSingle() {
        RuntimeException e = new RuntimeException("test");
        RuntimeException found = ExceptionUtils.findLastException(e, RuntimeException.class);
        assertSame(e, found);
    }

    @Test
    public void testFindLastExceptionChained() {
        IllegalArgumentException root = new IllegalArgumentException("root");
        RuntimeException mid = new RuntimeException("mid", root);
        RuntimeException top = new RuntimeException("top", mid);

        RuntimeException found = ExceptionUtils.findLastException(top, RuntimeException.class);
        assertSame(root, found);
    }

    @Test
    public void testFindLastExceptionNoMatch() {
        RuntimeException e = new RuntimeException("test");
        RuntimeException found = ExceptionUtils.findLastException(e, RuntimeException.class);
        assertSame(e, found);
    }
}
