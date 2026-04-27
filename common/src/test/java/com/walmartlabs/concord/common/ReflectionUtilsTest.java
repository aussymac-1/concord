package com.walmartlabs.concord.common;

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

import java.lang.annotation.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionUtilsTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface MyAnnotation {
        String value();
    }

    @MyAnnotation("direct")
    static class AnnotatedClass {
    }

    static class PlainClass {
    }

    @MyAnnotation("on-interface")
    interface AnnotatedInterface {
    }

    static class ImplementsAnnotated implements AnnotatedInterface {
    }

    @MyAnnotation("on-parent")
    static class AnnotatedParent {
    }

    static class ChildOfAnnotated extends AnnotatedParent {
    }

    @Test
    public void testFindAnnotationDirect() {
        var annotation = ReflectionUtils.findAnnotation(AnnotatedClass.class, MyAnnotation.class);
        assertNotNull(annotation);
        assertEquals("direct", annotation.value());
    }

    @Test
    public void testFindAnnotationNotPresent() {
        var annotation = ReflectionUtils.findAnnotation(PlainClass.class, MyAnnotation.class);
        assertNull(annotation);
    }

    @Test
    public void testFindAnnotationOnInterface() {
        var annotation = ReflectionUtils.findAnnotation(ImplementsAnnotated.class, MyAnnotation.class);
        assertNotNull(annotation);
        assertEquals("on-interface", annotation.value());
    }

    @Test
    public void testFindAnnotationOnSuperclass() {
        var annotation = ReflectionUtils.findAnnotation(ChildOfAnnotated.class, MyAnnotation.class);
        assertNotNull(annotation);
        assertEquals("on-parent", annotation.value());
    }
}
