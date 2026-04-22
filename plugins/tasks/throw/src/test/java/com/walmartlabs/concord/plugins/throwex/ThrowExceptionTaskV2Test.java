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

import com.walmartlabs.concord.runtime.v2.sdk.MapBackedVariables;
import com.walmartlabs.concord.runtime.v2.sdk.UserDefinedException;
import com.walmartlabs.concord.runtime.v2.sdk.Variables;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ThrowExceptionTaskV2Test {

    private static Variables vars(Map<String, Object> m) {
        return new MapBackedVariables(m);
    }

    @Test
    public void rethrowsExistingException() {
        ThrowExceptionTaskV2 task = new ThrowExceptionTaskV2();
        IOException original = new IOException("io-broke");

        Map<String, Object> input = new HashMap<>();
        input.put("exception", original);

        IOException thrown = assertThrows(IOException.class, () -> task.execute(vars(input)));
        assertSame(original, thrown);
    }

    @Test
    public void stringBecomesUserDefinedExceptionWithPayload() {
        ThrowExceptionTaskV2 task = new ThrowExceptionTaskV2();

        Map<String, Object> payload = new HashMap<>();
        payload.put("a", 1);

        Map<String, Object> input = new HashMap<>();
        input.put("exception", "bad");
        input.put("payload", payload);

        UserDefinedException thrown = assertThrows(UserDefinedException.class, () -> task.execute(vars(input)));
        assertEquals("bad", thrown.getMessage());
        assertEquals(payload, thrown.getPayload());
    }

    @Test
    public void stringWithoutPayloadDefaultsToEmptyMap() {
        ThrowExceptionTaskV2 task = new ThrowExceptionTaskV2();

        Map<String, Object> input = new HashMap<>();
        input.put("exception", "oops");

        UserDefinedException thrown = assertThrows(UserDefinedException.class, () -> task.execute(vars(input)));
        assertEquals("oops", thrown.getMessage());
        assertEquals(Map.of(), thrown.getPayload());
    }

    @Test
    public void serializablePayloadBecomesConcordException() {
        ThrowExceptionTaskV2 task = new ThrowExceptionTaskV2();

        ArrayList<String> payload = new ArrayList<>(List.of("a", "b"));
        Map<String, Object> input = new HashMap<>();
        input.put("exception", payload);

        ConcordException thrown = assertThrows(ConcordException.class, () -> task.execute(vars(input)));
        assertEquals("Process Error", thrown.getMessage());
        assertSame(payload, thrown.getPayload());
    }

    @Test
    public void nullInputYieldsNaMessage() {
        ThrowExceptionTaskV2 task = new ThrowExceptionTaskV2();

        Map<String, Object> input = new HashMap<>();
        input.put("exception", null);

        UserDefinedException thrown = assertThrows(UserDefinedException.class, () -> task.execute(vars(input)));
        assertEquals("n/a", thrown.getMessage());
    }

    @Test
    public void nonSerializableInputUsesToString() {
        ThrowExceptionTaskV2 task = new ThrowExceptionTaskV2();

        Object o = new Object() {
            @Override
            public String toString() {
                return "custom-repr";
            }
        };

        Map<String, Object> input = new HashMap<>();
        input.put("exception", o);

        UserDefinedException thrown = assertThrows(UserDefinedException.class, () -> task.execute(vars(input)));
        assertEquals("custom-repr", thrown.getMessage());
    }
}
