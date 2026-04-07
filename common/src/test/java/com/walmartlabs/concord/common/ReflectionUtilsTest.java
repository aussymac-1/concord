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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionUtilsTest {

    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnnotation {
        String value() default "";
    }

    @MyAnnotation("direct")
    static class DirectlyAnnotated {
    }

    @MyAnnotation("interface")
    interface AnnotatedInterface {
    }

    static class ImplementsAnnotatedInterface implements AnnotatedInterface {
    }

    @MyAnnotation("parent")
    static class AnnotatedParent {
    }

    static class ChildOfAnnotatedParent extends AnnotatedParent {
    }

    static class UnannotatedClass {
    }

    @Test
    public void testFindDirectAnnotation() {
        MyAnnotation annotation = ReflectionUtils.findAnnotation(DirectlyAnnotated.class, MyAnnotation.class);
        assertNotNull(annotation);
        assertEquals("direct", annotation.value());
    }

    @Test
    public void testFindAnnotationOnInterface() {
        MyAnnotation annotation = ReflectionUtils.findAnnotation(ImplementsAnnotatedInterface.class, MyAnnotation.class);
        assertNotNull(annotation);
        assertEquals("interface", annotation.value());
    }

    @Test
    public void testFindAnnotationOnSuperclass() {
        MyAnnotation annotation = ReflectionUtils.findAnnotation(ChildOfAnnotatedParent.class, MyAnnotation.class);
        assertNotNull(annotation);
        assertEquals("parent", annotation.value());
    }

    @Test
    public void testAnnotationNotFound() {
        MyAnnotation annotation = ReflectionUtils.findAnnotation(UnannotatedClass.class, MyAnnotation.class);
        assertNull(annotation);
    }
}
