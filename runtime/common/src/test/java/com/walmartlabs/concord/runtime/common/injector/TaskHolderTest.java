package com.walmartlabs.concord.runtime.common.injector;

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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TaskHolderTest {

    @Test
    void testAddAndGet() {
        TaskHolder<Runnable> holder = new TaskHolder<>();
        holder.add("myTask", cast(Runnable.class));
        assertEquals(Runnable.class, holder.get("myTask"));
    }

    @Test
    void testGetMissingReturnsNull() {
        TaskHolder<Runnable> holder = new TaskHolder<>();
        assertNull(holder.get("nonExistent"));
    }

    @Test
    void testDuplicateKeyThrows() {
        TaskHolder<Runnable> holder = new TaskHolder<>();
        holder.add("myTask", cast(Runnable.class));
        assertThrows(IllegalStateException.class,
                () -> holder.add("myTask", cast(Runnable.class)));
    }

    @Test
    void testKeys() {
        TaskHolder<Runnable> holder = new TaskHolder<>();
        holder.add("task1", cast(Runnable.class));
        holder.add("task2", cast(Runnable.class));
        Set<String> keys = holder.keys();
        assertEquals(2, keys.size());
        assertTrue(keys.contains("task1"));
        assertTrue(keys.contains("task2"));
    }

    @Test
    void testKeysUnmodifiable() {
        TaskHolder<Runnable> holder = new TaskHolder<>();
        holder.add("task1", cast(Runnable.class));
        Set<String> keys = holder.keys();
        assertThrows(UnsupportedOperationException.class,
                () -> keys.add("task2"));
    }

    @Test
    void testEmptyKeys() {
        TaskHolder<Runnable> holder = new TaskHolder<>();
        assertTrue(holder.keys().isEmpty());
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> cast(Class<?> clazz) {
        return (Class<T>) clazz;
    }
}
