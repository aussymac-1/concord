package com.walmartlabs.concord.plugins.lock;

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

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class ConstantsTest {

    @Test
    public void wellKnownValues() {
        assertEquals(3, Constants.RETRY_COUNT);
        assertEquals(5000L, Constants.RETRY_INTERVAL);
        assertEquals("name", Constants.LOCK_NAME_KEY);
        assertEquals("scope", Constants.SCOPE_KEY);
        assertEquals("PROJECT", Constants.PROJECT_SCOPE);
    }

    @Test
    public void classIsFinalWithPrivateConstructor() throws Exception {
        assertTrue(Modifier.isFinal(Constants.class.getModifiers()));

        Constructor<Constants> ctor = Constants.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(ctor.getModifiers()));
        ctor.setAccessible(true);
        assertNotNull(ctor.newInstance());
    }
}
