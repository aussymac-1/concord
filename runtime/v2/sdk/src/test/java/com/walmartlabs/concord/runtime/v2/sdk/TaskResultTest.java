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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TaskResultTest {

    @Test
    public void testSuccess() {
        TaskResult.SimpleResult result = TaskResult.success();
        assertTrue(result.ok());
        assertNull(result.error());
    }

    @Test
    public void testFailWithMessage() {
        TaskResult.SimpleFailResult result = TaskResult.fail("something went wrong");
        assertFalse(result.ok());
        assertEquals("something went wrong", result.error());
        assertNull(result.cause());
    }

    @Test
    public void testFailWithException() {
        RuntimeException ex = new RuntimeException("test error");
        TaskResult result = TaskResult.fail(ex);
        assertInstanceOf(TaskResult.SimpleFailResult.class, result);
        TaskResult.SimpleFailResult failResult = (TaskResult.SimpleFailResult) result;
        assertFalse(failResult.ok());
        assertEquals("test error", failResult.error());
        assertSame(ex, failResult.cause());
    }

    @Test
    public void testSuccessWithValues() {
        TaskResult.SimpleResult result = TaskResult.success()
                .value("key1", "value1")
                .value("key2", 42);

        assertTrue(result.ok());
        assertEquals("value1", result.values().get("key1"));
        assertEquals(42, result.values().get("key2"));
    }

    @Test
    public void testSuccessWithValuesMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("a", 1);
        values.put("b", "two");

        TaskResult.SimpleResult result = TaskResult.success().values(values);

        assertTrue(result.ok());
        assertEquals(1, result.values().get("a"));
        assertEquals("two", result.values().get("b"));
    }

    @Test
    public void testToMap() {
        TaskResult.SimpleResult result = TaskResult.success()
                .value("data", "hello");

        Map<String, Object> map = result.toMap();
        assertEquals(true, map.get("ok"));
        assertNull(map.get("error"));
        assertEquals("hello", map.get("data"));
    }

    @Test
    public void testToMapWithError() {
        TaskResult.SimpleResult result = TaskResult.of(false, "bad thing");

        Map<String, Object> map = result.toMap();
        assertEquals(false, map.get("ok"));
        assertEquals("bad thing", map.get("error"));
    }

    @Test
    public void testSuspendResult() {
        TaskResult result = TaskResult.suspend("myEvent");
        assertInstanceOf(TaskResult.SuspendResult.class, result);
        assertEquals("myEvent", ((TaskResult.SuspendResult) result).eventName());
    }

    @Test
    public void testReentrantSuspendResult() {
        Map<String, Serializable> payload = new HashMap<>();
        payload.put("key", "value");

        TaskResult result = TaskResult.reentrantSuspend("myEvent", payload);
        assertInstanceOf(TaskResult.ReentrantSuspendResult.class, result);
        TaskResult.ReentrantSuspendResult reentrantResult = (TaskResult.ReentrantSuspendResult) result;
        assertEquals("myEvent", reentrantResult.eventName());
        assertEquals("value", reentrantResult.payload().get("key"));
    }

    @Test
    public void testAssertValueSerializable() {
        assertDoesNotThrow(() -> TaskResult.assertValue("key", "string"));
        assertDoesNotThrow(() -> TaskResult.assertValue("key", 42));
        assertDoesNotThrow(() -> TaskResult.assertValue("key", null));
    }

    @Test
    public void testAssertValueNotSerializable() {
        Object nonSerializable = new Object();
        assertThrows(IllegalArgumentException.class, () -> TaskResult.assertValue("key", nonSerializable));
    }

    @Test
    public void testValuesWithNull() {
        TaskResult.SimpleResult result = TaskResult.success().values(null);
        assertTrue(result.ok());
    }

    @Test
    public void testDeprecatedOf() {
        TaskResult.SimpleResult result = TaskResult.of(true);
        assertTrue(result.ok());

        result = TaskResult.of(false, "error");
        assertFalse(result.ok());
        assertEquals("error", result.error());
    }

    @Test
    public void testDeprecatedError() {
        TaskResult.SimpleResult result = TaskResult.error("something broke");
        assertFalse(result.ok());
        assertEquals("something broke", result.error());
    }
}
