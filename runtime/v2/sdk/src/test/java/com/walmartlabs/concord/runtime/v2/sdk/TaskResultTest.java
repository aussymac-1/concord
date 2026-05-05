package com.walmartlabs.concord.runtime.v2.sdk;

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

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskResultTest {

    @Test
    public void testSuccess() {
        var r = TaskResult.success();

        assertTrue(r.ok());
        assertNull(r.error());
        assertNotNull(r.values());
        assertTrue(r.values().isEmpty());
    }

    @Test
    public void testFailWithStringError() {
        var r = TaskResult.fail("boom");

        assertTrue(r instanceof TaskResult.SimpleFailResult);
        var fail = (TaskResult.SimpleFailResult) r;
        assertFalse(fail.ok());
        assertEquals("boom", fail.error());
        assertNull(fail.cause());
    }

    @Test
    public void testFailWithExceptionPreservesCause() {
        var cause = new IOException("io error");

        var r = TaskResult.fail(cause);

        assertTrue(r instanceof TaskResult.SimpleFailResult);
        var fail = (TaskResult.SimpleFailResult) r;
        assertFalse(fail.ok());
        assertEquals("io error", fail.error());
        assertSame(cause, fail.cause());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testDeprecatedFactoryMethods() {
        var r1 = TaskResult.of(true);
        assertTrue(r1.ok());

        var r2 = TaskResult.of(false, "boom");
        assertFalse(r2.ok());
        assertEquals("boom", r2.error());

        var r3 = TaskResult.of(true, null, Map.of("a", 1));
        assertTrue(r3.ok());
        assertEquals(1, r3.values().get("a"));

        var r4 = TaskResult.error("nope");
        assertFalse(r4.ok());
        assertEquals("nope", r4.error());
    }

    @Test
    public void testSimpleResultValueAndValues() {
        var r = TaskResult.success()
                .value("a", 1)
                .values(new LinkedHashMap<>(Map.of("b", "two", "c", true)));

        assertEquals(1, r.values().get("a"));
        assertEquals("two", r.values().get("b"));
        assertEquals(true, r.values().get("c"));
    }

    @Test
    public void testSimpleResultValuesNullIsNoOp() {
        var r = TaskResult.success();

        // calling with null should not throw
        assertSame(r, r.values(null));
        assertTrue(r.values().isEmpty());
    }

    @Test
    public void testSimpleResultRejectsNonSerializableValue() {
        var r = TaskResult.success();

        assertThrows(IllegalArgumentException.class, () -> r.value("k", new Object()));
    }

    @Test
    public void testSimpleResultAcceptsNullValue() {
        var r = TaskResult.success();

        r.value("k", null);

        assertTrue(r.values().containsKey("k"));
        assertNull(r.values().get("k"));
    }

    @Test
    public void testToMapIncludesOkAndError() {
        var r = TaskResult.success().value("a", 1);

        var map = r.toMap();
        assertEquals(true, map.get("ok"));
        assertEquals(1, map.get("a"));
        assertFalse(map.containsKey("error"));

        @SuppressWarnings("deprecation")
        var failed = TaskResult.error("nope");
        var failedMap = failed.toMap();
        assertEquals(false, failedMap.get("ok"));
        assertEquals("nope", failedMap.get("error"));
    }

    @Test
    public void testSuspendResult() {
        var r = TaskResult.suspend("event1");

        assertTrue(r instanceof TaskResult.SuspendResult);
        assertEquals("event1", ((TaskResult.SuspendResult) r).eventName());
    }

    @Test
    public void testReentrantSuspendResult() {
        Map<String, Serializable> payload = new HashMap<>();
        payload.put("k", "v");

        var r = TaskResult.reentrantSuspend("ev", payload);

        assertTrue(r instanceof TaskResult.ReentrantSuspendResult);
        var rr = (TaskResult.ReentrantSuspendResult) r;
        assertEquals("ev", rr.eventName());
        assertSame(payload, rr.payload());
    }

    @Test
    public void testAssertValueAcceptsNull() {
        // Should not throw
        TaskResult.assertValue("k", null);
    }

    @Test
    public void testAssertValueAcceptsSerializable() {
        TaskResult.assertValue("k", "hello");
        TaskResult.assertValue("k", 1);
    }

    @Test
    public void testAssertValueRejectsNonSerializable() {
        var ex = assertThrows(IllegalArgumentException.class, () -> TaskResult.assertValue("k", new Object()));

        assertTrue(ex.getMessage().contains("Can't set the 'k' key"));
    }
}
