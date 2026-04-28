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
    static class DirectAnnotated {
    }

    @MyAnnotation("iface")
    interface AnnotatedInterface {
    }

    static class ImplementsAnnotatedInterface implements AnnotatedInterface {
    }

    static class ChildOfDirect extends DirectAnnotated {
    }

    static class GrandChild extends ChildOfDirect {
    }

    static class NotAnnotated {
    }

    @Test
    public void testFindAnnotationDirectlyOnClass() {
        MyAnnotation ann = ReflectionUtils.findAnnotation(DirectAnnotated.class, MyAnnotation.class);
        assertNotNull(ann);
        assertEquals("direct", ann.value());
    }

    @Test
    public void testFindAnnotationOnInterface() {
        MyAnnotation ann = ReflectionUtils.findAnnotation(ImplementsAnnotatedInterface.class, MyAnnotation.class);
        assertNotNull(ann);
        assertEquals("iface", ann.value());
    }

    @Test
    public void testFindAnnotationInherited() {
        MyAnnotation ann = ReflectionUtils.findAnnotation(ChildOfDirect.class, MyAnnotation.class);
        assertNotNull(ann);
        assertEquals("direct", ann.value());
    }

    @Test
    public void testFindAnnotationGrandchild() {
        MyAnnotation ann = ReflectionUtils.findAnnotation(GrandChild.class, MyAnnotation.class);
        assertNotNull(ann);
        assertEquals("direct", ann.value());
    }

    @Test
    public void testFindAnnotationNotPresent() {
        MyAnnotation ann = ReflectionUtils.findAnnotation(NotAnnotated.class, MyAnnotation.class);
        assertNull(ann);
    }
}
