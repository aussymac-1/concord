package com.walmartlabs.concord.forms;

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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class FormOptionsTest {

    @Test
    public void testDefaults() {
        FormOptions opts = FormOptions.builder().build();

        assertFalse(opts.isYield());
        assertFalse(opts.saveSubmittedBy());
        assertTrue(opts.runAs().isEmpty());
        assertTrue(opts.extraValues().isEmpty());
    }

    @Test
    public void testOverridesApplied() {
        FormOptions opts = FormOptions.builder()
                .isYield(true)
                .saveSubmittedBy(true)
                .putExtraValues("color", "blue")
                .build();

        assertTrue(opts.isYield());
        assertTrue(opts.saveSubmittedBy());
        assertEquals(Collections.singletonMap("color", "blue"), opts.extraValues());
    }

    @Test
    public void testEqualsAndHashCode() {
        FormOptions a = FormOptions.builder().isYield(true).build();
        FormOptions b = FormOptions.builder().isYield(true).build();
        FormOptions c = FormOptions.builder().isYield(false).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    public void testIsSerializable() throws Exception {
        FormOptions opts = FormOptions.builder()
                .isYield(true)
                .putExtraValues("k", "v")
                .build();

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(buf)) {
            oos.writeObject(opts);
        }

        Object restored;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf.toByteArray()))) {
            restored = ois.readObject();
        }

        assertEquals(opts, restored);
    }
}
