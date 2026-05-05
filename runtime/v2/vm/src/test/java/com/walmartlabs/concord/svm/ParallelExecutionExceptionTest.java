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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("deprecation")
public class ParallelExecutionExceptionTest {

    @Test
    public void testFieldsAndMessage() {
        var causes = List.<Exception>of(new RuntimeException("a"), new IllegalStateException("b"));

        var ex = new ParallelExecutionException(causes);

        assertEquals(causes, ex.getExceptions());
        assertTrue(ex.getMessage().startsWith("Parallel execution errors:"));
        assertTrue(ex.getMessage().contains("RuntimeException: a"));
        assertTrue(ex.getMessage().contains("IllegalStateException: b"));
    }

    @Test
    public void testGetExceptionsIsIndependentList() {
        var ex = new ParallelExecutionException(List.of(new RuntimeException("a")));

        var list = ex.getExceptions();
        assertEquals(1, list.size());
    }

    @Test
    public void testPrintStackTraceToStream() {
        var ex = new ParallelExecutionException(List.of(new RuntimeException("a")));

        var bos = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(bos));

        var out = bos.toString();
        assertTrue(out.contains(ex.getMessage()));
    }

    @Test
    public void testPrintStackTraceToWriter() {
        var ex = new ParallelExecutionException(List.of(new RuntimeException("a")));

        var sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));

        assertTrue(sw.toString().contains(ex.getMessage()));
    }

    @Test
    public void testStackTraceTruncationMessage() {
        // Build a synthetic exception with > 3 stack trace elements
        var bigEx = new RuntimeException("big");
        var trace = new StackTraceElement[5];
        for (int i = 0; i < trace.length; i++) {
            trace[i] = new StackTraceElement("Class" + i, "method" + i, "File" + i + ".java", i);
        }
        bigEx.setStackTrace(trace);

        var ex = new ParallelExecutionException(List.of(bigEx));

        // 5 elements total, MAX_STACK_TRACE_ELEMENTS=3 => 2 omitted
        assertTrue(ex.getMessage().contains("...2 more"));
    }
}
