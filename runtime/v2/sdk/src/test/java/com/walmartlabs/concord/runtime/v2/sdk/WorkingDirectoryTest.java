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

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class WorkingDirectoryTest {

    @Test
    public void testGetValue() {
        var path = Paths.get("/tmp/workDir");
        var wd = new WorkingDirectory(path);
        assertEquals(path, wd.getValue());
    }

    @Test
    public void testPreservesPath() {
        var path = Paths.get("/home/user/project");
        var wd = new WorkingDirectory(path);
        assertEquals("/home/user/project", wd.getValue().toString());
    }
}
