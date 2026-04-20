package com.walmartlabs.concord.plugins.log;

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
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogUtilsTest {

    @Test
    public void testLoggerIsConfigured() {
        Logger logger = LogUtils.log;

        // SLF4J may return a NOP logger when no provider is on the classpath,
        // so we only assert that a logger instance is wired up.
        assertNotNull(logger);
    }

    @Test
    public void testAllStringOverloadsAcceptNullOrValue() {
        // These methods just delegate to the underlying SLF4J logger; we verify
        // they do not throw for either a regular string or a null argument.
        assertDoesNotThrow(() -> LogUtils.error("hello"));
        assertDoesNotThrow(() -> LogUtils.warn("hello"));
        assertDoesNotThrow(() -> LogUtils.info("hello"));
        assertDoesNotThrow(() -> LogUtils.debug("hello"));

        assertDoesNotThrow(() -> LogUtils.error((String) null));
        assertDoesNotThrow(() -> LogUtils.warn((String) null));
        assertDoesNotThrow(() -> LogUtils.info((String) null));
        assertDoesNotThrow(() -> LogUtils.debug((String) null));
    }

    @Test
    public void testAllObjectOverloadsAcceptNullOrValue() {
        Object payload = new Object() {
            @Override
            public String toString() {
                return "payload";
            }
        };

        assertDoesNotThrow(() -> LogUtils.error(payload));
        assertDoesNotThrow(() -> LogUtils.warn(payload));
        assertDoesNotThrow(() -> LogUtils.info(payload));
        assertDoesNotThrow(() -> LogUtils.debug(payload));

        assertDoesNotThrow(() -> LogUtils.error((Object) null));
        assertDoesNotThrow(() -> LogUtils.warn((Object) null));
        assertDoesNotThrow(() -> LogUtils.info((Object) null));
        assertDoesNotThrow(() -> LogUtils.debug((Object) null));
    }

    @Test
    public void testClassIsNotInstantiable() throws Exception {
        Constructor<LogUtils> ctor = LogUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(ctor.getModifiers()));

        // Invoke through reflection to cover the private constructor.
        ctor.setAccessible(true);
        assertNotNull(ctor.newInstance());
    }
}
