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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationUtilsTest {

    @Test
    public void testSerializeAndDeserializeString() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(baos, "hello world");

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        String result = SerializationUtils.deserialize(bais, String.class);
        assertEquals("hello world", result);
    }

    @Test
    public void testSerializeAndDeserializeMap() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(baos, (Serializable) map);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Map<?, ?> result = SerializationUtils.deserialize(bais, Map.class);
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    public void testSerializeAndDeserializeInteger() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(baos, 42);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Integer result = SerializationUtils.deserialize(bais, Integer.class);
        assertEquals(42, result);
    }

    @Test
    public void testDeserializeInvalidData() {
        byte[] invalidData = "not a serialized object".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(invalidData);
        assertThrows(IOException.class, () -> SerializationUtils.deserialize(bais, String.class));
    }
}
