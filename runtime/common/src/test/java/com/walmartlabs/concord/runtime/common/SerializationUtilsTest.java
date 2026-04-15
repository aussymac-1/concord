package com.walmartlabs.concord.runtime.common;

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

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationUtilsTest {

    @Test
    void testSerializeAndDeserializeString() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(baos, "hello world");

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        String result = SerializationUtils.deserialize(bais, String.class);
        assertEquals("hello world", result);
    }

    @Test
    void testSerializeAndDeserializeInteger() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(baos, 42);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Integer result = SerializationUtils.deserialize(bais, Integer.class);
        assertEquals(42, result);
    }

    @Test
    void testDeserializeInvalidStreamThrows() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{1, 2, 3});
        assertThrows(IOException.class,
                () -> SerializationUtils.deserialize(bais, String.class));
    }

    @Test
    void testSerializeAndDeserializeArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String[] data = {"a", "b", "c"};
        SerializationUtils.serialize(baos, data);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        String[] result = SerializationUtils.deserialize(bais, String[].class);
        assertArrayEquals(data, result);
    }
}
