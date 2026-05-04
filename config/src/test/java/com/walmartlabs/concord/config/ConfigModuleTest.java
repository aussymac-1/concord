package com.walmartlabs.concord.config;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2018 Takari
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

import com.google.inject.Guice;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.walmartlabs.concord.config.subpkg.field.FieldHolder;
import com.walmartlabs.concord.config.subpkg.list.ListHolder;
import com.walmartlabs.concord.config.subpkg.map.MapHolder;
import com.walmartlabs.concord.config.subpkg.nullable.NullableHolder;
import com.walmartlabs.concord.config.subpkg.primitives.PrimitivesHolder;
import com.walmartlabs.concord.config.subpkg.required.RequiredHolder;
import com.walmartlabs.concord.config.subpkg.weird.WeirdHolder;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigModuleTest {

    @Test
    public void testInjectPrimitivesAndStrings() {
        var cfg = ConfigFactory.parseString("foo.intVal=42\nfoo.stringVal=\"hello\"\nfoo.boolVal=true\nfoo.duration=10s");

        var module = new ConfigModule(PrimitivesHolder.class.getPackage().getName(), cfg);
        var injector = Guice.createInjector(module);

        var holder = injector.getInstance(PrimitivesHolder.class);

        assertEquals(42, holder.intVal);
        assertEquals("hello", holder.stringVal);
        assertTrue(holder.boolVal);
        assertEquals(Duration.ofSeconds(10), holder.duration);
    }

    @Test
    public void testNullableMissingPathBindsNull() {
        var cfg = ConfigFactory.empty();

        var module = new ConfigModule(NullableHolder.class.getPackage().getName(), cfg);
        var injector = Guice.createInjector(module);

        var holder = injector.getInstance(NullableHolder.class);
        assertNull(holder.maybeMissing);
    }

    @Test
    public void testRequiredMissingPathThrows() {
        var cfg = ConfigFactory.empty();

        var module = new ConfigModule(RequiredHolder.class.getPackage().getName(), cfg);

        var ex = assertThrows(RuntimeException.class, () -> Guice.createInjector(module));
        // The underlying cause should be a typesafe-config Missing exception.
        Throwable cause = ex;
        while (cause != null && !(cause instanceof ConfigException.Missing)) {
            cause = cause.getCause();
        }
        assertTrue(cause instanceof ConfigException.Missing,
                "expected ConfigException.Missing in the cause chain, got: " + ex);
    }

    @Test
    public void testInjectsList() {
        var cfg = ConfigFactory.parseString("foo.items=[\"a\",\"b\",\"c\"]");

        var module = new ConfigModule(ListHolder.class.getPackage().getName(), cfg);
        var injector = Guice.createInjector(module);

        var holder = injector.getInstance(ListHolder.class);
        assertEquals(List.of("a", "b", "c"), holder.items);
    }

    @Test
    public void testInjectsMap() {
        var cfg = ConfigFactory.parseString("foo.map { a=1, b=2 }");

        var module = new ConfigModule(MapHolder.class.getPackage().getName(), cfg);
        var injector = Guice.createInjector(module);

        var holder = injector.getInstance(MapHolder.class);
        assertEquals(2, holder.map.size());
        assertEquals(1, holder.map.get("a"));
    }

    @Test
    public void testUnsupportedTypeThrowsRuntimeException() {
        var cfg = ConfigFactory.parseString("foo.weird=42");

        var module = new ConfigModule(WeirdHolder.class.getPackage().getName(), cfg);
        var ex = assertThrows(RuntimeException.class, () -> Guice.createInjector(module));
        assertNotNull(ex);
    }

    @Test
    public void testLoadFromClasspathReadsResource() {
        var cfg = ConfigModule.load("concordtest");
        assertEquals("hello", cfg.getString("greeting"));
    }

    @Test
    public void testLoadFromExternalFileOverridesClasspath() throws Exception {
        var tmp = Files.createTempFile("concord-config-", ".conf");
        Files.writeString(tmp, "concordtest { greeting = \"override\" }");
        var prevValue = System.getProperty("concord.conf");
        try {
            System.setProperty("concord.conf", tmp.toString());
            var cfg = ConfigModule.load("concordtest");
            assertEquals("override", cfg.getString("greeting"));
        } finally {
            if (prevValue == null) {
                System.clearProperty("concord.conf");
            } else {
                System.setProperty("concord.conf", prevValue);
            }
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    public void testLoadFromLegacyOllieFileOverridesClasspath() throws Exception {
        var tmp = Files.createTempFile("ollie-", ".conf");
        Files.writeString(tmp, "concordtest { greeting = \"legacy\" }");
        var prev = System.getProperty("ollie.conf");
        try {
            System.setProperty("ollie.conf", tmp.toString());
            var cfg = ConfigModule.load("concordtest");
            assertEquals("legacy", cfg.getString("greeting"));
        } finally {
            if (prev == null) {
                System.clearProperty("ollie.conf");
            } else {
                System.setProperty("ollie.conf", prev);
            }
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    public void testFieldInjection() {
        var cfg = ConfigFactory.parseString("foo.intVal=99");

        var module = new ConfigModule(FieldHolder.class.getPackage().getName(), cfg);
        var injector = Guice.createInjector(module);

        var holder = injector.getInstance(FieldHolder.class);
        assertEquals(99, holder.intVal);
    }
}
