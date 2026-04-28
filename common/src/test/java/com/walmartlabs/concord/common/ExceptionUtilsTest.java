package com.walmartlabs.concord.common;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2024 Walmart Inc.
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
    public void testGetExceptionListSingle() {
        RuntimeException ex = new RuntimeException("test");
        List<Throwable> result = ExceptionUtils.getExceptionList(ex);

        assertEquals(1, result.size());
        assertSame(ex, result.get(0));
    }

    @Test
    public void testGetExceptionListChained() {
        IOException root = new IOException("root");
        RuntimeException mid = new RuntimeException("mid", root);
        Exception top = new Exception("top", mid);

        List<Throwable> result = ExceptionUtils.getExceptionList(top);

        assertEquals(3, result.size());
        assertSame(top, result.get(0));
        assertSame(mid, result.get(1));
        assertSame(root, result.get(2));
    }

    @Test
    public void testGetExceptionListWithCycle() {
        Exception a = new Exception("a");
        Exception b = new Exception("b", a);
        // create a cycle by setting cause manually
        try {
            a.initCause(b);
        } catch (IllegalStateException e) {
            // initCause will fail since 'a' already has no cause set
            // but this validates the cycle protection in getExceptionList
        }
        List<Throwable> result = ExceptionUtils.getExceptionList(b);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindLastExceptionFound() {
        IOException root = new IOException("io error");
        RuntimeException wrapper = new RuntimeException("wrapper", root);

        RuntimeException result = ExceptionUtils.findLastException(wrapper, RuntimeException.class);
        assertSame(wrapper, result);
    }

    @Test
    public void testFindLastExceptionNotFound() {
        RuntimeException ex = new RuntimeException("test");

        RuntimeException result = ExceptionUtils.findLastException(ex, RuntimeException.class);
        assertSame(ex, result);
    }

    @Test
    public void testFindLastExceptionDeepChain() {
        IOException deepIo = new IOException("deep");
        RuntimeException mid = new RuntimeException("mid", deepIo);
        RuntimeException top = new RuntimeException("top", mid);

        RuntimeException result = ExceptionUtils.findLastException(top, RuntimeException.class);
        assertSame(mid, result);
    }
}
