package com.walmartlabs.concord.config.subpkg.primitives;

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

import com.walmartlabs.concord.config.Config;

import javax.inject.Inject;
import java.time.Duration;

public final class PrimitivesHolder {

    public final int intVal;
    public final String stringVal;
    public final boolean boolVal;
    public final Duration duration;

    @Inject
    public PrimitivesHolder(@Config("foo.intVal") int intVal,
                            @Config("foo.stringVal") String stringVal,
                            @Config("foo.boolVal") boolean boolVal,
                            @Config("foo.duration") Duration duration) {
        this.intVal = intVal;
        this.stringVal = stringVal;
        this.boolVal = boolVal;
        this.duration = duration;
    }
}
