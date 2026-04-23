package com.walmartlabs.concord.config;

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

import com.google.inject.BindingAnnotation;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigAnnotationTest {

    @Config("foo.bar")
    private int annotatedField;

    @Test
    public void annotationIsRuntimeRetained() throws Exception {
        var retention = Config.class.getAnnotation(Retention.class);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    public void annotationAllowsFieldParameterAndMethodTargets() {
        var target = Config.class.getAnnotation(Target.class);
        var targets = java.util.Arrays.asList(target.value());
        assertTrue(targets.contains(FIELD));
        assertTrue(targets.contains(PARAMETER));
        assertTrue(targets.contains(METHOD));
    }

    @Test
    public void annotationCarriesGuiceBindingAnnotation() {
        assertTrue(Config.class.isAnnotationPresent(BindingAnnotation.class));
    }

    @Test
    public void valueIsReadableAtRuntime() throws Exception {
        var field = ConfigAnnotationTest.class.getDeclaredField("annotatedField");
        var cfg = field.getAnnotation(Config.class);
        assertEquals("foo.bar", cfg.value());
    }
}
