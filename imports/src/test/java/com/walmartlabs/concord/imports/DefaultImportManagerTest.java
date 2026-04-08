package com.walmartlabs.concord.imports;

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

import com.walmartlabs.concord.repository.Snapshot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultImportManagerTest {

    @Mock
    private ImportsListener listener;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testProcessEmptyImports() throws Exception {
        DefaultImportManager manager = new DefaultImportManager(Collections.emptyList(), Collections.emptySet());
        Imports imports = Imports.builder().build();

        List<Snapshot> result = manager.process(imports, Path.of("/tmp"), listener);
        assertTrue(result.isEmpty());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testProcessWithNullListener() throws Exception {
        DefaultImportManager manager = new DefaultImportManager(Collections.emptyList(), Collections.emptySet());
        Imports imports = Imports.builder().build();

        List<Snapshot> result = manager.process(imports, Path.of("/tmp"), null);
        assertTrue(result.isEmpty());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testProcessWithProcessor() throws Exception {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/test/repo")
                .build();

        Snapshot mockSnapshot = mock(Snapshot.class);

        ImportProcessor gitProcessor = mock(ImportProcessor.class);
        when(gitProcessor.type()).thenReturn("git");
        when(gitProcessor.process(any(), any())).thenReturn(mockSnapshot);

        DefaultImportManager manager = new DefaultImportManager(
                Collections.singletonList(gitProcessor),
                Collections.emptySet());

        Imports imports = Imports.of(Collections.singletonList(git));
        List<Snapshot> result = manager.process(imports, Path.of("/tmp"), listener);

        assertEquals(1, result.size());
        assertSame(mockSnapshot, result.get(0));

        verify(listener).onStart(anyList());
        verify(listener).beforeImport(git);
        verify(listener).afterImport(git);
        verify(listener).onEnd(anyList());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testProcessUnknownType() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/test/repo")
                .build();

        DefaultImportManager manager = new DefaultImportManager(
                Collections.emptyList(),
                Collections.emptySet());

        Imports imports = Imports.of(Collections.singletonList(git));
        assertThrows(ImportProcessingException.class,
                () -> manager.process(imports, Path.of("/tmp"), listener));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testProcessDisabledType() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/test/repo")
                .build();

        ImportProcessor gitProcessor = mock(ImportProcessor.class);
        when(gitProcessor.type()).thenReturn("git");

        DefaultImportManager manager = new DefaultImportManager(
                Collections.singletonList(gitProcessor),
                Collections.singleton("git"));

        Imports imports = Imports.of(Collections.singletonList(git));
        assertThrows(ImportProcessingException.class,
                () -> manager.process(imports, Path.of("/tmp"), listener));
    }

    @Test
    public void testImportProcessingException() {
        Import.GitDefinition git = Import.GitDefinition.builder()
                .url("https://github.com/test/repo")
                .build();

        Exception cause = new RuntimeException("test error");
        ImportProcessingException ex = new ImportProcessingException(git, cause);

        assertSame(git, ex.getImport());
        assertEquals("test error", ex.getMessage());
        assertSame(cause, ex.getCause());
    }
}
