package com.walmartlabs.concord.common.secret;

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

public class UsernamePasswordTest {

    @Test
    public void testSerializeDeserialize() {
        UsernamePassword original = new UsernamePassword("admin", "secret123".toCharArray());

        byte[] serialized = UsernamePassword.serialize(original);
        assertNotNull(serialized);

        UsernamePassword deserialized = UsernamePassword.deserialize(serialized);
        assertEquals("admin", deserialized.getUsername());
        assertArrayEquals("secret123".toCharArray(), deserialized.getPassword());
    }

    @Test
    public void testSerializeDeserializeUnicode() {
        UsernamePassword original = new UsernamePassword("user\u00E9", "p\u00E4ss".toCharArray());

        byte[] serialized = UsernamePassword.serialize(original);
        UsernamePassword deserialized = UsernamePassword.deserialize(serialized);

        assertEquals("user\u00E9", deserialized.getUsername());
        assertArrayEquals("p\u00E4ss".toCharArray(), deserialized.getPassword());
    }

    @Test
    public void testSerializeDeserializeEmpty() {
        UsernamePassword original = new UsernamePassword("", "".toCharArray());

        byte[] serialized = UsernamePassword.serialize(original);
        UsernamePassword deserialized = UsernamePassword.deserialize(serialized);

        assertEquals("", deserialized.getUsername());
        assertArrayEquals("".toCharArray(), deserialized.getPassword());
    }

    @Test
    public void testGetters() {
        char[] password = "mypass".toCharArray();
        UsernamePassword up = new UsernamePassword("user", password);

        assertEquals("user", up.getUsername());
        assertSame(password, up.getPassword());
    }
}
