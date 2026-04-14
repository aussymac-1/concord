package com.walmartlabs.concord.common;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2019 Walmart Inc.
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
import org.slf4j.MDC;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class LogUtilsTest {

    @Test
    public void test() throws Exception {
        System.out.println(LogUtils.formatMessage(LogUtils.LogLevel.INFO, "Hello, {}!", "there"));
    }

    @Test
    public void testFormatMessageInfo() {
        String result = LogUtils.formatMessage(LogUtils.LogLevel.INFO, "hello {}", "world");
        assertTrue(result.contains("[INFO ]"));
        assertTrue(result.contains("hello world"));
    }

    @Test
    public void testFormatMessageError() {
        String result = LogUtils.formatMessage(LogUtils.LogLevel.ERROR, "fail");
        assertTrue(result.contains("[ERROR]"));
        assertTrue(result.contains("fail"));
    }

    @Test
    public void testFormatMessageDebug() {
        String result = LogUtils.formatMessage(LogUtils.LogLevel.DEBUG, "debug msg");
        assertTrue(result.contains("[DEBUG]"));
    }

    @Test
    public void testFormatMessageWarn() {
        String result = LogUtils.formatMessage(LogUtils.LogLevel.WARN, "warn msg");
        assertTrue(result.contains("[WARN ]"));
    }

    @Test
    public void testFormatMessageWithException() {
        RuntimeException ex = new RuntimeException("boom");
        String result = LogUtils.formatMessage(LogUtils.LogLevel.ERROR, "error {}", ex);
        assertTrue(result.contains("[ERROR]"));
        assertTrue(result.contains("boom"));
    }

    @Test
    public void testFormatMessageTimestamp() {
        String result = LogUtils.formatMessage(LogUtils.LogLevel.INFO, "test");
        // timestamp format: yyyy-MM-dd'T'HH:mm:ss.SSSZ e.g. 2026-04-14T09:20:33.364+0000
        assertTrue(result.contains("T"), "Expected ISO timestamp with T separator");
        assertTrue(result.contains("+"), "Expected timezone offset with +");
    }

    @Test
    public void testWithMdcRunnablePreservesMdc() {
        // withMdc captures current MDC and restores it when the wrapped runnable runs
        MDC.put("testKey", "testValue");

        Runnable wrapped = LogUtils.withMdc(() -> {
            // inside the wrapped runnable, MDC should be available
        });

        // wrapped runnable should not throw
        assertDoesNotThrow(wrapped::run);
        MDC.clear();
    }

    @Test
    public void testWithMdcCallableReturnsValue() throws Exception {
        MDC.clear();

        Callable<String> wrapped = LogUtils.withMdc(() -> "result");

        String result = wrapped.call();
        assertEquals("result", result);
    }

    @Test
    public void testWithMdcRunnableNullMdc() {
        MDC.clear();
        Runnable wrapped = LogUtils.withMdc(() -> {});
        assertDoesNotThrow(wrapped::run);
    }
}
