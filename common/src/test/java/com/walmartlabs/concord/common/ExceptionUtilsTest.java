package com.walmartlabs.concord.common;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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
        RuntimeException e = new RuntimeException("test");
        List<Throwable> list = ExceptionUtils.getExceptionList(e);
        assertEquals(1, list.size());
        assertSame(e, list.get(0));
    }

    @Test
    public void testGetExceptionListChainedExceptions() {
        IOException root = new IOException("root");
        RuntimeException middle = new RuntimeException("middle", root);
        Exception top = new Exception("top", middle);

        List<Throwable> list = ExceptionUtils.getExceptionList(top);
        assertEquals(3, list.size());
        assertSame(top, list.get(0));
        assertSame(middle, list.get(1));
        assertSame(root, list.get(2));
    }

    @Test
    public void testGetExceptionListNull() {
        List<Throwable> list = ExceptionUtils.getExceptionList(null);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testGetExceptionListCircularReference() {
        RuntimeException e1 = new RuntimeException("e1");
        RuntimeException e2 = new RuntimeException("e2", e1);
        e1.initCause(e2);

        List<Throwable> list = ExceptionUtils.getExceptionList(e1);
        assertEquals(2, list.size());
        assertSame(e1, list.get(0));
        assertSame(e2, list.get(1));
    }

    @Test
    public void testFindLastExceptionMatchingType() {
        IOException root = new IOException("root cause");
        RuntimeException wrapper = new RuntimeException("wrapper", root);

        RuntimeException result = ExceptionUtils.findLastException(wrapper, RuntimeException.class);
        assertSame(wrapper, result);
    }

    @Test
    public void testFindLastExceptionNoMatch() {
        RuntimeException e = new RuntimeException("test");
        RuntimeException result = ExceptionUtils.findLastException(e, RuntimeException.class);
        assertSame(e, result);
    }

    @Test
    public void testFindLastExceptionDeepChain() {
        IOException io1 = new IOException("first io");
        RuntimeException middle = new RuntimeException("middle", io1);
        IOException io2 = new IOException("second io", middle);

        IOException result = ExceptionUtils.findLastException(io2, IOException.class);
        assertSame(io1, result);
    }
}
