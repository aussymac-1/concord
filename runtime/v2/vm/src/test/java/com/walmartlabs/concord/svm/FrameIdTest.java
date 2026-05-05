package com.walmartlabs.concord.svm;

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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FrameIdTest {

    @Test
    public void testEqualsAndHashCode() {
        var u1 = UUID.randomUUID();
        var u2 = UUID.randomUUID();

        var a = new FrameId(u1);
        var b = new FrameId(u1);
        var c = new FrameId(u2);

        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    public void testEqualsHandlesNullAndOtherType() {
        var id = new FrameId(UUID.randomUUID());

        assertNotEquals(id, null);
        assertNotEquals(id, "not-a-frame-id");
    }

    @Test
    public void testCompareTo() {
        var u1 = new UUID(0, 1);
        var u2 = new UUID(0, 2);

        var a = new FrameId(u1);
        var b = new FrameId(u2);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(new FrameId(u1)));
    }
}
