package com.walmartlabs.concord.client2.impl;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ByteArrayBufferTest {

    @Test
    void initialBufferIsEmpty() {
        ByteArrayBuffer buf = new ByteArrayBuffer(10);
        assertThat(buf.length()).isZero();
        assertThat(buf.toByteArray()).isEmpty();
    }

    @Test
    void appendByteArray() {
        ByteArrayBuffer buf = new ByteArrayBuffer(10);
        buf.append(new byte[]{1, 2, 3});
        assertThat(buf.length()).isEqualTo(3);
        assertThat(buf.toByteArray()).containsExactly(1, 2, 3);
    }

    @Test
    void appendWithOffsetAndLength() {
        ByteArrayBuffer buf = new ByteArrayBuffer(10);
        buf.append(new byte[]{10, 20, 30, 40, 50}, 1, 3);
        assertThat(buf.length()).isEqualTo(3);
        assertThat(buf.toByteArray()).containsExactly(20, 30, 40);
    }

    @Test
    void appendNullIsIgnored() {
        ByteArrayBuffer buf = new ByteArrayBuffer(10);
        buf.append(null, 0, 0);
        assertThat(buf.length()).isZero();
    }

    @Test
    void appendZeroLengthIsIgnored() {
        ByteArrayBuffer buf = new ByteArrayBuffer(10);
        buf.append(new byte[]{1, 2}, 0, 0);
        assertThat(buf.length()).isZero();
    }

    @Test
    void appendInvalidOffsetThrows() {
        ByteArrayBuffer buf = new ByteArrayBuffer(10);
        assertThatThrownBy(() -> buf.append(new byte[]{1, 2}, -1, 1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void appendExceedingLengthThrows() {
        ByteArrayBuffer buf = new ByteArrayBuffer(10);
        assertThatThrownBy(() -> buf.append(new byte[]{1, 2}, 0, 5))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void bufferExpandsWhenCapacityExceeded() {
        ByteArrayBuffer buf = new ByteArrayBuffer(2);
        buf.append(new byte[]{1, 2, 3, 4, 5});
        assertThat(buf.length()).isEqualTo(5);
        assertThat(buf.toByteArray()).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void multipleAppends() {
        ByteArrayBuffer buf = new ByteArrayBuffer(4);
        buf.append(new byte[]{1, 2});
        buf.append(new byte[]{3, 4});
        assertThat(buf.length()).isEqualTo(4);
        assertThat(buf.toByteArray()).containsExactly(1, 2, 3, 4);
    }

    @Test
    void clearResetsLength() {
        ByteArrayBuffer buf = new ByteArrayBuffer(10);
        buf.append(new byte[]{1, 2, 3});
        buf.clear();
        assertThat(buf.length()).isZero();
        assertThat(buf.toByteArray()).isEmpty();
    }

    @Test
    void arrayReturnsInternalBuffer() {
        ByteArrayBuffer buf = new ByteArrayBuffer(10);
        buf.append(new byte[]{1, 2, 3});
        byte[] internal = buf.array();
        assertThat(internal.length).isEqualTo(10);
        assertThat(internal[0]).isEqualTo((byte) 1);
        assertThat(internal[1]).isEqualTo((byte) 2);
        assertThat(internal[2]).isEqualTo((byte) 3);
    }

    @Test
    void toByteArrayReturnsCopy() {
        ByteArrayBuffer buf = new ByteArrayBuffer(10);
        buf.append(new byte[]{1, 2});
        byte[] copy = buf.toByteArray();
        copy[0] = 99;
        assertThat(buf.toByteArray()[0]).isEqualTo((byte) 1);
    }
}
