package com.walmartlabs.concord.common;

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

import java.lang.annotation.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionUtilsTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface MyAnnotation {
        String value() default "";
    }

    @MyAnnotation("direct")
    static class DirectAnnotated {
    }

    @MyAnnotation("iface")
    interface AnnotatedInterface {
    }

    static class ImplementsAnnotated implements AnnotatedInterface {
    }

    static class SubClass extends DirectAnnotated {
    }

    static class NoAnnotation {
    }

    @Test
    public void testFindDirectAnnotation() {
        var result = ReflectionUtils.findAnnotation(DirectAnnotated.class, MyAnnotation.class);
        assertNotNull(result);
        assertEquals("direct", result.value());
    }

    @Test
    public void testFindAnnotationOnInterface() {
        var result = ReflectionUtils.findAnnotation(ImplementsAnnotated.class, MyAnnotation.class);
        assertNotNull(result);
        assertEquals("iface", result.value());
    }

    @Test
    public void testFindAnnotationOnSuperClass() {
        var result = ReflectionUtils.findAnnotation(SubClass.class, MyAnnotation.class);
        assertNotNull(result);
        assertEquals("direct", result.value());
    }

    @Test
    public void testFindAnnotationNotFound() {
        var result = ReflectionUtils.findAnnotation(NoAnnotation.class, MyAnnotation.class);
        assertNull(result);
    }
}
