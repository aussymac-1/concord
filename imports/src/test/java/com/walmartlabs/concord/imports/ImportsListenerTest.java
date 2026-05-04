package com.walmartlabs.concord.imports;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ImportsListenerTest {

    @Test
    public void testNopListenerNeverThrows() {
        ImportsListener l = ImportsListener.NOP_LISTENER;
        assertNotNull(l);

        var imp = Import.MvnDefinition.builder().url("mvn://x").build();
        assertDoesNotThrow(() -> {
            l.onStart(List.of(imp));
            l.beforeImport(imp);
            l.afterImport(imp);
            l.onEnd(List.of(imp));
        });
    }
}
