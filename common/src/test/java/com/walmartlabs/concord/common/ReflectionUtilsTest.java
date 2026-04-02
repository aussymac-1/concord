package com.walmartlabs.concord.common;

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
    static class DirectlyAnnotated {}

    @MyAnnotation("ifc")
    interface AnnotatedInterface {}

    static class ImplementsAnnotated implements AnnotatedInterface {}

    @MyAnnotation("parent")
    static class AnnotatedParent {}

    static class ChildOfAnnotated extends AnnotatedParent {}

    static class Unannotated {}

    @Test
    public void testFindDirectAnnotation() {
        MyAnnotation a = ReflectionUtils.findAnnotation(DirectlyAnnotated.class, MyAnnotation.class);
        assertNotNull(a);
        assertEquals("direct", a.value());
    }

    @Test
    public void testFindAnnotationFromInterface() {
        MyAnnotation a = ReflectionUtils.findAnnotation(ImplementsAnnotated.class, MyAnnotation.class);
        assertNotNull(a);
        assertEquals("ifc", a.value());
    }

    @Test
    public void testFindAnnotationFromSuperclass() {
        MyAnnotation a = ReflectionUtils.findAnnotation(ChildOfAnnotated.class, MyAnnotation.class);
        assertNotNull(a);
        assertEquals("parent", a.value());
    }

    @Test
    public void testReturnsNullWhenNotFound() {
        assertNull(ReflectionUtils.findAnnotation(Unannotated.class, MyAnnotation.class));
    }
}
