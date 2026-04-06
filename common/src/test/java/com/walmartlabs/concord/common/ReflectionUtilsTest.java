package com.walmartlabs.concord.common;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionUtilsTest {

    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnnotation {
        String value() default "";
    }

    @MyAnnotation("on-interface")
    interface AnnotatedInterface {
    }

    interface UnannotatedInterface {
    }

    static class ImplementsAnnotated implements AnnotatedInterface {
    }

    @MyAnnotation("on-class")
    static class DirectlyAnnotated {
    }

    static class ExtendsAnnotated extends DirectlyAnnotated {
    }

    static class DeepChild extends ExtendsAnnotated {
    }

    static class NoAnnotation {
    }

    @Test
    void testFindAnnotationOnClass() {
        MyAnnotation a = ReflectionUtils.findAnnotation(DirectlyAnnotated.class, MyAnnotation.class);
        assertNotNull(a);
        assertEquals("on-class", a.value());
    }

    @Test
    void testFindAnnotationOnInterface() {
        MyAnnotation a = ReflectionUtils.findAnnotation(ImplementsAnnotated.class, MyAnnotation.class);
        assertNotNull(a);
        assertEquals("on-interface", a.value());
    }

    @Test
    void testFindAnnotationOnSuperclass() {
        MyAnnotation a = ReflectionUtils.findAnnotation(ExtendsAnnotated.class, MyAnnotation.class);
        assertNotNull(a);
        assertEquals("on-class", a.value());
    }

    @Test
    void testFindAnnotationDeepInheritance() {
        MyAnnotation a = ReflectionUtils.findAnnotation(DeepChild.class, MyAnnotation.class);
        assertNotNull(a);
        assertEquals("on-class", a.value());
    }

    @Test
    void testFindAnnotationReturnsNullWhenAbsent() {
        MyAnnotation a = ReflectionUtils.findAnnotation(NoAnnotation.class, MyAnnotation.class);
        assertNull(a);
    }
}
