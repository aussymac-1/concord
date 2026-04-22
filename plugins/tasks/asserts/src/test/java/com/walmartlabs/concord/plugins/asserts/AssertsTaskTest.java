package com.walmartlabs.concord.plugins.asserts;

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

import com.walmartlabs.concord.runtime.v2.sdk.UserDefinedException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class AssertsTaskTest {

    @Test
    public void assertTrueBooleanDoesNotThrowForTrue() {
        assertDoesNotThrow(() -> AssertsTask.assertTrue(true));
    }

    @Test
    public void assertTrueBooleanThrowsDefaultMessageForFalse() {
        UserDefinedException ex = assertThrows(UserDefinedException.class, () -> AssertsTask.assertTrue(false));
        assertEquals("Expected value to be true but is false", ex.getMessage());
    }

    @Test
    public void assertTrueWithMessageDoesNotThrowForTrue() {
        assertDoesNotThrow(() -> AssertsTask.assertTrue("custom", true));
    }

    @Test
    public void assertTrueWithMessageThrowsForFalse() {
        UserDefinedException ex = assertThrows(UserDefinedException.class, () -> AssertsTask.assertTrue("custom", false));
        assertEquals("custom", ex.getMessage());
    }

    @Test
    public void assertEqualsBothNullIsOk() {
        AssertsTask task = new AssertsTask(null);
        assertDoesNotThrow(() -> task.assertEquals(null, null));
    }

    @Test
    public void assertEqualsExpectedNullActualNonNullThrows() {
        AssertsTask task = new AssertsTask(null);
        UserDefinedException ex = assertThrows(UserDefinedException.class,
                () -> task.assertEquals(null, "x"));
        assertTrue(ex.getMessage().contains("Expected value to be 'null'"));
    }

    @Test
    public void assertEqualsActualNullExpectedNonNullThrows() {
        AssertsTask task = new AssertsTask(null);
        UserDefinedException ex = assertThrows(UserDefinedException.class,
                () -> task.assertEquals("x", null));
        assertTrue(ex.getMessage().contains("is 'null'"));
    }

    @Test
    public void assertEqualsStringsEqualDoesNotThrow() {
        AssertsTask task = new AssertsTask(null);
        assertDoesNotThrow(() -> task.assertEquals("abc", "abc"));
    }

    @Test
    public void assertEqualsStringsDifferentThrowsWithClassInfo() {
        AssertsTask task = new AssertsTask(null);
        UserDefinedException ex = assertThrows(UserDefinedException.class,
                () -> task.assertEquals("abc", "xyz"));
        assertTrue(ex.getMessage().contains("abc"));
        assertTrue(ex.getMessage().contains("xyz"));
        assertTrue(ex.getMessage().contains("java.lang.String"));
    }

    @Test
    public void assertEqualsNumericallyEqualDifferentTypes() {
        AssertsTask task = new AssertsTask(null);

        // 1 == 1.0 == 1L numerically
        assertDoesNotThrow(() -> task.assertEquals(1, 1L));
        assertDoesNotThrow(() -> task.assertEquals(1, 1.0));
        assertDoesNotThrow(() -> task.assertEquals(1L, BigInteger.ONE));
        assertDoesNotThrow(() -> task.assertEquals(BigDecimal.valueOf(1.0), BigInteger.ONE));
        assertDoesNotThrow(() -> task.assertEquals((byte) 1, (short) 1));
        assertDoesNotThrow(() -> task.assertEquals((float) 1.5, 1.5));
    }

    @Test
    public void assertEqualsNumericallyDifferentThrows() {
        AssertsTask task = new AssertsTask(null);

        UserDefinedException ex = assertThrows(UserDefinedException.class,
                () -> task.assertEquals(1, 2));
        assertTrue(ex.getMessage().contains("'1'"));
        assertTrue(ex.getMessage().contains("'2'"));
    }

    @Test
    public void assertEqualsSpecialFloatingPointValues() {
        AssertsTask task = new AssertsTask(null);

        assertDoesNotThrow(() -> task.assertEquals(Double.NaN, Double.NaN));
        assertDoesNotThrow(() -> task.assertEquals(Float.NaN, Float.NaN));
        assertDoesNotThrow(() -> task.assertEquals(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));

        assertThrows(UserDefinedException.class,
                () -> task.assertEquals(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));
    }
}
