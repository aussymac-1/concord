package com.walmartlabs.concord.plugins.docker;

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

public class DockerConstantsTest {

    @Test
    public void wellKnownValues() {
        assertEquals(0, DockerConstants.SUCCESS_EXIT_CODE);
        assertEquals("/workspace", DockerConstants.VOLUME_CONTAINER_DEST);
        assertEquals("cmd", DockerConstants.CMD_KEY);
        assertEquals("image", DockerConstants.IMAGE_KEY);
        assertEquals("env", DockerConstants.ENV_KEY);
        assertEquals("envFile", DockerConstants.ENV_FILE_KEY);
        assertEquals("hosts", DockerConstants.HOSTS_KEY);
        assertEquals("forcePull", DockerConstants.FORCE_PULL_KEY);
        assertEquals("debug", DockerConstants.DEBUG_KEY);
        assertEquals("pullRetryCount", DockerConstants.PULL_RETRY_COUNT_KEY);
        assertEquals("pullRetryInterval", DockerConstants.PULL_RETRY_INTERVAL_KEY);
    }

    @Test
    public void privateConstructor() throws Exception {
        Constructor<DockerConstants> ctor = DockerConstants.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(ctor.getModifiers()));
        ctor.setAccessible(true);
        assertNotNull(ctor.newInstance());
    }
}
