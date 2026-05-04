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

import com.walmartlabs.concord.repository.Snapshot;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultImportManagerTest {

    @Test
    public void testEmptyImportsReturnsEmpty() throws Exception {
        var m = new DefaultImportManager(List.of(), Set.of());

        var result = m.process(Imports.builder().build(), Path.of("/tmp"), ImportsListener.NOP_LISTENER);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testListenerInvokedAndProcessorCalled() throws Exception {
        var processor = new RecordingProcessor("git");
        var m = new DefaultImportManager(List.of(processor), Set.of());

        var listener = new RecordingListener();
        var imports = Imports.of(List.of(
                Import.GitDefinition.builder().name("a").url("u").build(),
                Import.GitDefinition.builder().name("b").url("u2").build()
        ));

        var result = m.process(imports, Path.of("/tmp"), listener);

        assertEquals(2, result.size());
        assertEquals(2, processor.calls);
        assertEquals(1, listener.startCount);
        assertEquals(1, listener.endCount);
        assertEquals(2, listener.beforeCount);
        assertEquals(2, listener.afterCount);
    }

    @Test
    public void testNullListenerSwapsForNoop() throws Exception {
        var processor = new RecordingProcessor("git");
        var m = new DefaultImportManager(List.of(processor), Set.of());

        var imports = Imports.of(List.of(
                Import.GitDefinition.builder().name("a").url("u").build()
        ));

        var result = m.process(imports, Path.of("/tmp"), null);
        assertEquals(1, result.size());
    }

    @Test
    public void testDisabledProcessorThrows() {
        var processor = new RecordingProcessor("git");
        var m = new DefaultImportManager(List.of(processor), Set.of("git"));

        var imports = Imports.of(List.of(
                Import.GitDefinition.builder().name("a").url("u").build()
        ));

        var ex = assertThrows(ImportProcessingException.class,
                () -> m.process(imports, Path.of("/tmp"), ImportsListener.NOP_LISTENER));
        assertTrue(ex.getCause().getMessage().contains("Disabled"));
    }

    @Test
    public void testUnknownProcessorThrows() {
        var m = new DefaultImportManager(List.of(), Set.of());

        var imports = Imports.of(List.of(
                Import.GitDefinition.builder().name("a").url("u").build()
        ));

        var ex = assertThrows(ImportProcessingException.class,
                () -> m.process(imports, Path.of("/tmp"), ImportsListener.NOP_LISTENER));
        assertTrue(ex.getCause().getMessage().contains("Unknown"));
    }

    @Test
    public void testProcessorExceptionWrappedInImportProcessingException() {
        var processor = new ImportProcessor<Import>() {
            @Override
            public String type() {
                return "git";
            }

            @Override
            public Snapshot process(Import entry, Path workDir) throws Exception {
                throw new IllegalStateException("kaboom");
            }
        };

        var m = new DefaultImportManager(List.of(processor), Set.of());
        var imports = Imports.of(List.of(
                Import.GitDefinition.builder().name("a").url("u").build()
        ));

        var ex = assertThrows(ImportProcessingException.class,
                () -> m.process(imports, Path.of("/tmp"), ImportsListener.NOP_LISTENER));
        assertNotNull(ex.getImport());
        assertEquals("kaboom", ex.getCause().getMessage());
    }

    private static final class RecordingProcessor implements ImportProcessor<Import> {

        private final String type;
        int calls;

        RecordingProcessor(String type) {
            this.type = type;
        }

        @Override
        public String type() {
            return type;
        }

        @Override
        public Snapshot process(Import entry, Path workDir) {
            calls++;
            return Snapshot.includeAll();
        }
    }

    private static final class RecordingListener implements ImportsListener {

        int startCount;
        int endCount;
        int beforeCount;
        int afterCount;

        @Override
        public void onStart(List<Import> items) {
            startCount++;
        }

        @Override
        public void onEnd(List<Import> items) {
            endCount++;
        }

        @Override
        public void beforeImport(Import i) {
            beforeCount++;
        }

        @Override
        public void afterImport(Import i) {
            afterCount++;
        }
    }
}
