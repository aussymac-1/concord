package com.walmartlabs.concord.client2.impl;

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

public class ByteArrayBufferTest {

    @Test
    void testInitialState() {
        ByteArrayBuffer buf = new ByteArrayBuffer(16);
        assertEquals(0, buf.length());
        assertArrayEquals(new byte[0], buf.toByteArray());
    }

    @Test
    void testAppendBytes() {
        ByteArrayBuffer buf = new ByteArrayBuffer(16);
        buf.append(new byte[]{1, 2, 3});
        assertEquals(3, buf.length());
        assertArrayEquals(new byte[]{1, 2, 3}, buf.toByteArray());
    }

    @Test
    void testAppendWithOffsetAndLength() {
        ByteArrayBuffer buf = new ByteArrayBuffer(16);
        buf.append(new byte[]{10, 20, 30, 40, 50}, 1, 3);
        assertEquals(3, buf.length());
        assertArrayEquals(new byte[]{20, 30, 40}, buf.toByteArray());
    }

    @Test
    void testAppendNull() {
        ByteArrayBuffer buf = new ByteArrayBuffer(16);
        buf.append(null, 0, 0);
        assertEquals(0, buf.length());
    }

    @Test
    void testAppendZeroLength() {
        ByteArrayBuffer buf = new ByteArrayBuffer(16);
        buf.append(new byte[]{1, 2, 3}, 0, 0);
        assertEquals(0, buf.length());
    }

    @Test
    void testAppendInvalidOffsetThrows() {
        ByteArrayBuffer buf = new ByteArrayBuffer(16);
        assertThrows(IndexOutOfBoundsException.class,
                () -> buf.append(new byte[]{1, 2, 3}, -1, 2));
    }

    @Test
    void testAppendOverflowOffsetThrows() {
        ByteArrayBuffer buf = new ByteArrayBuffer(16);
        assertThrows(IndexOutOfBoundsException.class,
                () -> buf.append(new byte[]{1, 2, 3}, 2, 5));
    }

    @Test
    void testAutoExpand() {
        ByteArrayBuffer buf = new ByteArrayBuffer(4);
        buf.append(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        assertEquals(8, buf.length());
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, buf.toByteArray());
    }

    @Test
    void testClear() {
        ByteArrayBuffer buf = new ByteArrayBuffer(16);
        buf.append(new byte[]{1, 2, 3});
        buf.clear();
        assertEquals(0, buf.length());
        assertArrayEquals(new byte[0], buf.toByteArray());
    }

    @Test
    void testToByteArrayIsCopy() {
        ByteArrayBuffer buf = new ByteArrayBuffer(16);
        buf.append(new byte[]{1, 2, 3});
        byte[] arr = buf.toByteArray();
        arr[0] = 99;
        assertArrayEquals(new byte[]{1, 2, 3}, buf.toByteArray());
    }

    @Test
    void testMultipleAppends() {
        ByteArrayBuffer buf = new ByteArrayBuffer(4);
        buf.append(new byte[]{1, 2});
        buf.append(new byte[]{3, 4});
        buf.append(new byte[]{5});
        assertEquals(5, buf.length());
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buf.toByteArray());
    }
}
