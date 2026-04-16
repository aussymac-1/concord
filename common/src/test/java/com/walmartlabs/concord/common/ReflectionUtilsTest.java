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

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionUtilsTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface MyAnnotation {
        String value() default "";
    }

    @MyAnnotation("direct")
    static class DirectlyAnnotated {}

    @MyAnnotation("iface")
    interface AnnotatedInterface {}

    static class ImplementsAnnotatedInterface implements AnnotatedInterface {}

    @MyAnnotation("parent")
    static class AnnotatedParent {}

    static class ChildOfAnnotatedParent extends AnnotatedParent {}

    static class GrandchildOfAnnotatedParent extends ChildOfAnnotatedParent {}

    static class NotAnnotated {}

    @Test
    void findAnnotationOnDirectClass() {
        MyAnnotation result = ReflectionUtils.findAnnotation(DirectlyAnnotated.class, MyAnnotation.class);
        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo("direct");
    }

    @Test
    void findAnnotationOnInterface() {
        MyAnnotation result = ReflectionUtils.findAnnotation(ImplementsAnnotatedInterface.class, MyAnnotation.class);
        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo("iface");
    }

    @Test
    void findAnnotationOnSuperclass() {
        MyAnnotation result = ReflectionUtils.findAnnotation(ChildOfAnnotatedParent.class, MyAnnotation.class);
        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo("parent");
    }

    @Test
    void findAnnotationOnGrandparentClass() {
        MyAnnotation result = ReflectionUtils.findAnnotation(GrandchildOfAnnotatedParent.class, MyAnnotation.class);
        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo("parent");
    }

    @Test
    void findAnnotationReturnsNullWhenNotPresent() {
        MyAnnotation result = ReflectionUtils.findAnnotation(NotAnnotated.class, MyAnnotation.class);
        assertThat(result).isNull();
    }
}
