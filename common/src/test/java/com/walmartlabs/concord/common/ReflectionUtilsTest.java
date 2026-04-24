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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionUtilsTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface MyAnnotation {
        String value() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface OtherAnnotation {
    }

    @MyAnnotation("direct")
    static class AnnotatedClass {
    }

    static class UnannotatedClass {
    }

    @MyAnnotation("onInterface")
    interface AnnotatedInterface {
    }

    static class ImplementsAnnotated implements AnnotatedInterface {
    }

    @MyAnnotation("onParent")
    static class AnnotatedParent {
    }

    static class ChildOfAnnotated extends AnnotatedParent {
    }

    @Test
    public void testFindAnnotationDirect() {
        MyAnnotation result = ReflectionUtils.findAnnotation(AnnotatedClass.class, MyAnnotation.class);
        assertNotNull(result);
        assertEquals("direct", result.value());
    }

    @Test
    public void testFindAnnotationNotPresent() {
        OtherAnnotation result = ReflectionUtils.findAnnotation(AnnotatedClass.class, OtherAnnotation.class);
        assertNull(result);
    }

    @Test
    public void testFindAnnotationOnInterface() {
        MyAnnotation result = ReflectionUtils.findAnnotation(ImplementsAnnotated.class, MyAnnotation.class);
        assertNotNull(result);
        assertEquals("onInterface", result.value());
    }

    @Test
    public void testFindAnnotationOnSuperclass() {
        MyAnnotation result = ReflectionUtils.findAnnotation(ChildOfAnnotated.class, MyAnnotation.class);
        assertNotNull(result);
        assertEquals("onParent", result.value());
    }

    @Test
    public void testFindAnnotationNotFound() {
        MyAnnotation result = ReflectionUtils.findAnnotation(UnannotatedClass.class, MyAnnotation.class);
        assertNull(result);
    }
}
