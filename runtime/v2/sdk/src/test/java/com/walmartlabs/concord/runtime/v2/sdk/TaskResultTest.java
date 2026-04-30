package com.walmartlabs.concord.runtime.v2.sdk;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2024 Walmart Inc.
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

import static org.junit.jupiter.api.Assertions.*;

public class TaskResultTest {

    @Test
    public void testSuccess() {
        var result = TaskResult.success();
        assertTrue(result.ok());
        assertNull(result.error());
    }

    @Test
    public void testFailWithMessage() {
        var result = TaskResult.fail("something broke");
        assertFalse(result.ok());
        assertEquals("something broke", result.error());
    }

    @Test
    public void testFailWithException() {
        var ex = new RuntimeException("boom");
        var result = TaskResult.fail(ex);
        assertTrue(result instanceof TaskResult.SimpleFailResult);
        var failResult = (TaskResult.SimpleFailResult) result;
        assertFalse(failResult.ok());
        assertEquals("boom", failResult.error());
        assertEquals(ex, failResult.cause());
    }

    @Test
    public void testFailWithStringNoCause() {
        var result = (TaskResult.SimpleFailResult) TaskResult.fail("oops");
        assertNull(result.cause());
    }

    @Test
    public void testOf() {
        var result = TaskResult.of(true);
        assertTrue(result.ok());
        assertNull(result.error());
    }

    @Test
    public void testOfWithError() {
        var result = TaskResult.of(false, "err");
        assertFalse(result.ok());
        assertEquals("err", result.error());
    }

    @Test
    public void testOfWithValues() {
        var values = new HashMap<String, Object>();
        values.put("count", 42);
        var result = TaskResult.of(true, null, values);
        assertTrue(result.ok());
        assertEquals(42, result.values().get("count"));
    }

    @Test
    public void testError() {
        var result = TaskResult.error("problem");
        assertFalse(result.ok());
        assertEquals("problem", result.error());
    }

    @Test
    public void testSimpleResultValue() {
        var result = TaskResult.success().value("key", "val");
        assertEquals("val", result.values().get("key"));
    }

    @Test
    public void testSimpleResultValues() {
        var extra = new HashMap<String, Object>();
        extra.put("a", 1);
        extra.put("b", 2);
        var result = TaskResult.success().values(extra);
        assertEquals(1, result.values().get("a"));
        assertEquals(2, result.values().get("b"));
    }

    @Test
    public void testSimpleResultValuesNull() {
        var result = TaskResult.success().values(null);
        assertNotNull(result.values());
    }

    @Test
    public void testToMap() {
        var result = TaskResult.success().value("data", "hello");
        var map = result.toMap();
        assertEquals(true, map.get("ok"));
        assertEquals("hello", map.get("data"));
        assertFalse(map.containsKey("error"));
    }

    @Test
    public void testToMapWithError() {
        var result = TaskResult.of(false, "fail");
        var map = result.toMap();
        assertEquals(false, map.get("ok"));
        assertEquals("fail", map.get("error"));
    }

    @Test
    public void testSuspend() {
        var result = TaskResult.suspend("myEvent");
        assertTrue(result instanceof TaskResult.SuspendResult);
        assertEquals("myEvent", ((TaskResult.SuspendResult) result).eventName());
    }

    @Test
    public void testReentrantSuspend() {
        var payload = new HashMap<String, Serializable>();
        payload.put("key", "value");
        var result = TaskResult.reentrantSuspend("myEvent", payload);
        assertTrue(result instanceof TaskResult.ReentrantSuspendResult);
        var rs = (TaskResult.ReentrantSuspendResult) result;
        assertEquals("myEvent", rs.eventName());
        assertEquals("value", rs.payload().get("key"));
    }

    @Test
    public void testAssertValueNull() {
        TaskResult.assertValue("key", null);
    }

    @Test
    public void testAssertValueSerializable() {
        TaskResult.assertValue("key", "hello");
    }

    @Test
    public void testAssertValueNonSerializable() {
        var nonSerializable = new Object();
        assertThrows(IllegalArgumentException.class,
                () -> TaskResult.assertValue("key", nonSerializable));
    }
}
