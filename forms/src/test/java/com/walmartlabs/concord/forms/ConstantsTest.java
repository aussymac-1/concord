package com.walmartlabs.concord.forms;

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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConstantsTest {

    @Test
    public void keysAreStableAndDocumented() {
        assertEquals("runAs", Constants.RUN_AS_KEY);
        assertEquals("username", Constants.RUN_AS_USERNAME_KEY);
        assertEquals("ldap", Constants.RUN_AS_LDAP_KEY);
        assertEquals("group", Constants.RUN_AS_GROUP_KEY);
        assertEquals("_form_files", Constants.FORM_FILES);
    }

    @Test
    public void keysAreDistinct() {
        var all = Set.of(
                Constants.RUN_AS_KEY,
                Constants.RUN_AS_USERNAME_KEY,
                Constants.RUN_AS_LDAP_KEY,
                Constants.RUN_AS_GROUP_KEY,
                Constants.FORM_FILES);
        assertEquals(5, all.size());
    }

    @Test
    public void classCanBeInstantiated() {
        assertNotNull(new Constants());
    }
}
