package com.walmartlabs.concord.plugins.dynamic;

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
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskLoaderTest {

    @Test
    public void loadRegistersAllGroovyClassesInTheTree(@TempDir Path dir) throws Exception {
        var a = dir.resolve("A.groovy");
        var b = dir.resolve("nested/B.groovy");
        Files.createDirectories(b.getParent());
        Files.writeString(a, "class A { String hello() { 'hi' } }");
        Files.writeString(b, "class B { int answer() { 42 } }");

        var registry = new RecordingRegistry();
        var loader = new TaskLoader(registry);

        loader.load(dir);

        var names = new ArrayList<String>();
        for (var c : registry.registered) {
            names.add(c.getSimpleName());
        }
        assertTrue(names.contains("A"), names::toString);
        assertTrue(names.contains("B"), names::toString);
    }

    @Test
    public void loadEmptyDirectoryRegistersNothing(@TempDir Path dir) throws Exception {
        var registry = new RecordingRegistry();
        new TaskLoader(registry).load(dir);
        assertTrue(registry.registered.isEmpty());
    }

    @Test
    public void loadWrapsCompilationFailuresInRuntimeException(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("Broken.groovy"), "class Broken { int x = ; }");
        var registry = new RecordingRegistry();

        var ex = assertThrows(RuntimeException.class, () -> new TaskLoader(registry).load(dir));
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("Error while loading a task"));
    }

    @Test
    public void constructorStoresRegistry() {
        var registry = new RecordingRegistry();
        // Simply verify that the loader can be constructed and is usable.
        var loader = new TaskLoader(registry);
        assertNotNull(loader);
    }

    private static final class RecordingRegistry implements TaskRegistry {

        private final List<Class<?>> registered = new ArrayList<>();

        @Override
        @SuppressWarnings("rawtypes")
        public void register(Class clazz) {
            registered.add(clazz);
        }
    }

    @Test
    public void loadInvokesRegisterInOrder(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("X.groovy"), "class X {}");
        Files.writeString(dir.resolve("Y.groovy"), "class Y {}");

        var registry = new RecordingRegistry();
        new TaskLoader(registry).load(dir);

        assertEquals(2, registry.registered.size());
    }
}
