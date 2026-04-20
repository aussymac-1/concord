package com.walmartlabs.concord.plugins.throwex;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConcordExceptionTest {

    @Test
    public void testMessageOnlyConstructor() {
        ConcordException ex = new ConcordException("boom");

        assertEquals("boom", ex.getMessage());
        assertNull(ex.getPayload());
    }

    @Test
    public void testMessageAndPayload() {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("code", "E_FAIL");

        ConcordException ex = new ConcordException("boom", payload);

        assertEquals("boom", ex.getMessage());
        assertEquals(payload, ex.getPayload());
    }

    @Test
    public void testIsCheckedException() {
        // Keeping ConcordException as a checked exception is part of the task contract --
        // task authors rely on the compiler to force handling of failures.
        assertTrue(Exception.class.isAssignableFrom(ConcordException.class));
        assertTrue(!RuntimeException.class.isAssignableFrom(ConcordException.class),
                "ConcordException should remain a checked exception");
    }

    @Test
    public void testExceptionIsSerializable() throws Exception {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("k", "v");
        ConcordException original = new ConcordException("serialize me", payload);

        ConcordException round = roundTrip(original);

        assertEquals("serialize me", round.getMessage());
        Serializable recovered = round.getPayload();
        assertEquals(payload, recovered);
    }

    private static ConcordException roundTrip(ConcordException ex) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(ex);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            return (ConcordException) ois.readObject();
        }
    }

    @Test
    public void testNullPayloadIsAllowed() {
        ConcordException ex = new ConcordException("x", null);

        assertEquals("x", ex.getMessage());
        assertNull(ex.getPayload());
    }

    @Test
    public void testPayloadRoundTripWithSerializableMap() {
        Map<String, Integer> payload = new HashMap<>();
        payload.put("n", 1);

        ConcordException ex = new ConcordException("msg", (Serializable) payload);

        @SuppressWarnings("unchecked")
        Map<String, Integer> recovered = (Map<String, Integer>) ex.getPayload();
        assertEquals(Integer.valueOf(1), recovered.get("n"));
    }
}
