package com.walmartlabs.concord.plugins.asserts;

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

import com.walmartlabs.concord.runtime.v2.sdk.UserDefinedException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class AssertsTaskTest {

    @Test
    void testAssertTrueSuccess() {
        assertDoesNotThrow(() -> AssertsTask.assertTrue(true));
    }

    @Test
    void testAssertTrueFails() {
        var ex = assertThrows(UserDefinedException.class, () -> AssertsTask.assertTrue(false));
        assertEquals("Expected value to be true but is false", ex.getMessage());
    }

    @Test
    void testAssertTrueWithMessageFails() {
        var ex = assertThrows(UserDefinedException.class, () -> AssertsTask.assertTrue("custom msg", false));
        assertEquals("custom msg", ex.getMessage());
    }

    @Test
    void testAssertTrueWithMessageSuccess() {
        assertDoesNotThrow(() -> AssertsTask.assertTrue("custom msg", true));
    }

    @Test
    void testAssertEqualsBothNull() {
        var task = new AssertsTask(null);
        assertDoesNotThrow(() -> task.assertEquals(null, null));
    }

    @Test
    void testAssertEqualsExpectedNullActualNotNull() {
        var task = new AssertsTask(null);
        var ex = assertThrows(UserDefinedException.class, () -> task.assertEquals(null, "value"));
        assertTrue(ex.getMessage().contains("'null'"));
    }

    @Test
    void testAssertEqualsExpectedNotNullActualNull() {
        var task = new AssertsTask(null);
        var ex = assertThrows(UserDefinedException.class, () -> task.assertEquals("value", null));
        assertTrue(ex.getMessage().contains("'null'"));
    }

    @Test
    void testAssertEqualsStrings() {
        var task = new AssertsTask(null);
        assertDoesNotThrow(() -> task.assertEquals("hello", "hello"));
    }

    @Test
    void testAssertEqualsStringsMismatch() {
        var task = new AssertsTask(null);
        var ex = assertThrows(UserDefinedException.class, () -> task.assertEquals("hello", "world"));
        assertTrue(ex.getMessage().contains("hello"));
        assertTrue(ex.getMessage().contains("world"));
    }

    @Test
    void testAssertEqualsIntAndLong() {
        var task = new AssertsTask(null);
        assertDoesNotThrow(() -> task.assertEquals(42, 42L));
    }

    @Test
    void testAssertEqualsIntAndDouble() {
        var task = new AssertsTask(null);
        assertDoesNotThrow(() -> task.assertEquals(5, 5.0));
    }

    @Test
    void testAssertEqualsNumbersMismatch() {
        var task = new AssertsTask(null);
        assertThrows(UserDefinedException.class, () -> task.assertEquals(1, 2));
    }

    @Test
    void testAssertEqualsBigDecimal() {
        var task = new AssertsTask(null);
        assertDoesNotThrow(() -> task.assertEquals(new BigDecimal("1.0"), new BigDecimal("1.00")));
    }

    @Test
    void testAssertEqualsBigInteger() {
        var task = new AssertsTask(null);
        assertDoesNotThrow(() -> task.assertEquals(BigInteger.valueOf(100), 100L));
    }

    @Test
    void testAssertEqualsSpecialDoubleNaN() {
        var task = new AssertsTask(null);
        assertDoesNotThrow(() -> task.assertEquals(Double.NaN, Double.NaN));
    }

    @Test
    void testAssertEqualsSpecialDoubleInfinity() {
        var task = new AssertsTask(null);
        assertDoesNotThrow(() -> task.assertEquals(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    @Test
    void testAssertEqualsSpecialFloatNaN() {
        var task = new AssertsTask(null);
        assertDoesNotThrow(() -> task.assertEquals(Float.NaN, Float.NaN));
    }

    @Test
    void testAssertEqualsNaNAndRegularNumber() {
        var task = new AssertsTask(null);
        assertThrows(UserDefinedException.class, () -> task.assertEquals(Double.NaN, 1.0));
    }
}
