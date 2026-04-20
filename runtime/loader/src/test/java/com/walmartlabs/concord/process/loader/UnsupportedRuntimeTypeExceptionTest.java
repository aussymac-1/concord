package com.walmartlabs.concord.process.loader;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnsupportedRuntimeTypeExceptionTest {

    @Test
    public void testMessageIncludesRuntimeName() {
        UnsupportedRuntimeTypeException ex = new UnsupportedRuntimeTypeException("concord-v99");

        assertEquals("Unsupported runtime type: concord-v99", ex.getMessage());
    }

    @Test
    public void testIsCheckedException() {
        // Callers rely on this being a checked exception so the compiler forces them to
        // decide whether to handle or propagate unsupported runtime types.
        assertTrue(Exception.class.isAssignableFrom(UnsupportedRuntimeTypeException.class));
        assertTrue(!RuntimeException.class.isAssignableFrom(UnsupportedRuntimeTypeException.class),
                "UnsupportedRuntimeTypeException must be a checked exception");
    }

    @Test
    public void testMessageWithNullRuntime() {
        UnsupportedRuntimeTypeException ex = new UnsupportedRuntimeTypeException(null);

        assertEquals("Unsupported runtime type: null", ex.getMessage());
    }
}
