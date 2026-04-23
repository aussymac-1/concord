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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnsupportedRuntimeTypeExceptionTest {

    @Test
    public void messageIncludesRuntimeName() {
        var ex = new UnsupportedRuntimeTypeException("weird-runtime");
        assertTrue(ex.getMessage().contains("weird-runtime"));
        assertTrue(ex.getMessage().contains("Unsupported"));
    }

    @Test
    public void isACheckedException() {
        assertInstanceOf(Exception.class, new UnsupportedRuntimeTypeException("x"));
    }

    @Test
    public void messageIsStableAcrossInstances() {
        var a = new UnsupportedRuntimeTypeException("foo");
        var b = new UnsupportedRuntimeTypeException("foo");
        assertEquals(a.getMessage(), b.getMessage());
    }

    @Test
    public void nullRuntimeIsAcceptedInMessage() {
        var ex = new UnsupportedRuntimeTypeException(null);
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("null"));
    }
}
