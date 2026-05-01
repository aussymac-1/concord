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
    static class AnnotatedClass {}

    static class UnannotatedClass {}

    @MyAnnotation("iface")
    interface AnnotatedInterface {}

    static class ImplOfAnnotatedInterface implements AnnotatedInterface {}

    @MyAnnotation("parent")
    static class AnnotatedParent {}

    static class ChildOfAnnotatedParent extends AnnotatedParent {}

    static class GrandChild extends ChildOfAnnotatedParent {}

    @Test
    public void testDirectAnnotation() {
        MyAnnotation a = ReflectionUtils.findAnnotation(AnnotatedClass.class, MyAnnotation.class);
        assertNotNull(a);
        assertEquals("direct", a.value());
    }

    @Test
    public void testNoAnnotation() {
        MyAnnotation a = ReflectionUtils.findAnnotation(UnannotatedClass.class, MyAnnotation.class);
        assertNull(a);
    }

    @Test
    public void testAnnotationOnInterface() {
        MyAnnotation a = ReflectionUtils.findAnnotation(ImplOfAnnotatedInterface.class, MyAnnotation.class);
        assertNotNull(a);
        assertEquals("iface", a.value());
    }

    @Test
    public void testAnnotationOnSuperclass() {
        MyAnnotation a = ReflectionUtils.findAnnotation(ChildOfAnnotatedParent.class, MyAnnotation.class);
        assertNotNull(a);
        assertEquals("parent", a.value());
    }

    @Test
    public void testAnnotationOnGrandparent() {
        MyAnnotation a = ReflectionUtils.findAnnotation(GrandChild.class, MyAnnotation.class);
        assertNotNull(a);
        assertEquals("parent", a.value());
    }
}
