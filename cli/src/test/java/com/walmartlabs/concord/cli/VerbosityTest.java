package com.walmartlabs.concord.cli;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2026 Walmart Inc.
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

import static org.assertj.core.api.Assertions.assertThat;

class VerbosityTest {

    @Test
    void emptyArrayDisablesAll() {
        Verbosity v = new Verbosity(new boolean[]{});
        assertThat(v.logFlowSteps()).isFalse();
        assertThat(v.logTaskParams()).isFalse();
        assertThat(v.verbose()).isFalse();
    }

    @Test
    void singleElementEnablesLogFlowSteps() {
        Verbosity v = new Verbosity(new boolean[]{true});
        assertThat(v.logFlowSteps()).isTrue();
        assertThat(v.logTaskParams()).isFalse();
        assertThat(v.verbose()).isFalse();
    }

    @Test
    void twoElementsEnablesLogTaskParams() {
        Verbosity v = new Verbosity(new boolean[]{true, true});
        assertThat(v.logFlowSteps()).isTrue();
        assertThat(v.logTaskParams()).isTrue();
        assertThat(v.verbose()).isFalse();
    }

    @Test
    void threeElementsEnablesVerbose() {
        Verbosity v = new Verbosity(new boolean[]{true, true, true});
        assertThat(v.logFlowSteps()).isTrue();
        assertThat(v.logTaskParams()).isTrue();
        assertThat(v.verbose()).isTrue();
    }
}
