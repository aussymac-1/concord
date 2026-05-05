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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConstantsTest {

    @Test
    public void testRetryAttemptNumber() {
        assertEquals("__retry_attemptNo", Constants.Runtime.RETRY_ATTEMPT_NUMBER);
    }

    @Test
    public void testClassesAreInstantiableViaReflectionForCoverage() throws Exception {
        // Both classes are intentionally final with private constructors, but we
        // exercise the constructor via reflection to register coverage.
        var outer = Constants.class.getDeclaredConstructor();
        outer.setAccessible(true);
        assertNotNull(outer.newInstance());

        var inner = Constants.Runtime.class.getDeclaredConstructor();
        inner.setAccessible(true);
        assertNotNull(inner.newInstance());
    }
}
