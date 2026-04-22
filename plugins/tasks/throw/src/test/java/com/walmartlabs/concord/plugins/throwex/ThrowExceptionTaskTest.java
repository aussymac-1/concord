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

import com.walmartlabs.concord.sdk.UserDefinedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ThrowExceptionTaskTest {

    @Test
    public void rethrowsExistingException() {
        ThrowExceptionTask task = new ThrowExceptionTask();
        IOException original = new IOException("io-broke");

        IOException thrown = assertThrows(IOException.class, () -> task.call(original));
        assertSame(original, thrown);
    }

    @Test
    public void stringBecomesUserDefinedException() {
        ThrowExceptionTask task = new ThrowExceptionTask();

        UserDefinedException thrown = assertThrows(UserDefinedException.class, () -> task.call("bad"));
        assertEquals("bad", thrown.getMessage());
    }

    @Test
    public void serializablePayloadBecomesConcordException() {
        ThrowExceptionTask task = new ThrowExceptionTask();
        ArrayList<String> payload = new ArrayList<>(List.of("a", "b"));

        ConcordException thrown = assertThrows(ConcordException.class, () -> task.call(payload));
        assertEquals("Process error", thrown.getMessage());
        assertSame(payload, thrown.getPayload());
    }

    @Test
    public void nullInputProducesUserDefinedException() {
        ThrowExceptionTask task = new ThrowExceptionTask();

        UserDefinedException thrown = assertThrows(UserDefinedException.class, () -> task.call(null));
        assertEquals("n/a", thrown.getMessage());
    }

    @Test
    public void nonSerializableInputProducesUserDefinedException() {
        ThrowExceptionTask task = new ThrowExceptionTask();

        // Object is not Serializable/Exception/String
        Object o = new Object() {
            @Override
            public String toString() {
                return "custom-repr";
            }
        };

        UserDefinedException thrown = assertThrows(UserDefinedException.class, () -> task.call(o));
        assertEquals("custom-repr", thrown.getMessage());
    }
}
