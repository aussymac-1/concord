package com.walmartlabs.concord.plugins.misc;

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

import io.takari.bpm.api.BpmnError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MiscTaskTest {

    @Test
    public void testThrowRuntimeExceptionPropagatesMessage() {
        MiscTask task = new MiscTask();

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> task.throwRuntimeException("boom"));
        assertEquals("boom", ex.getMessage());
    }

    @Test
    public void testThrowBpmnErrorCarriesRef() {
        MiscTask task = new MiscTask();

        BpmnError ex = assertThrows(BpmnError.class,
                () -> task.throwBpmnError("my-error"));
        assertEquals("my-error", ex.getErrorRef());
        assertNotNull(ex.getCause());
        assertInstanceOf(RuntimeException.class, ex.getCause());
        assertTrue(ex.getCause().getMessage().contains("my-error"));
    }
}
