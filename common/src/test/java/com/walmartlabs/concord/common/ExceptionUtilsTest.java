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
    public void testGetExceptionListChain() {
        Exception root = new Exception("root");
        Exception middle = new Exception("middle", root);
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
    public void testFindLastExceptionMatchesLast() {
        RuntimeException root = new RuntimeException("root");
        RuntimeException middle = new RuntimeException("middle", root);
        RuntimeException top = new RuntimeException("top", middle);

        RuntimeException result = ExceptionUtils.findLastException(top, RuntimeException.class);
        assertSame(root, result);
    }

    @Test
    public void testFindLastExceptionNoMatch() {
        IllegalArgumentException e = new IllegalArgumentException("test");

        IllegalArgumentException result = ExceptionUtils.findLastException(e, IllegalArgumentException.class);
        assertSame(e, result);
    }

    @Test
    public void testFindLastExceptionMixedTypes() {
        IllegalStateException root = new IllegalStateException("root");
        RuntimeException top = new RuntimeException("top", root);

        RuntimeException result = ExceptionUtils.findLastException(top, RuntimeException.class);
        assertSame(root, result);
    }
}
