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

import java.nio.file.attribute.PosixFilePermission;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PosixTest {

    @Test
    public void testUnixModeNullReturnsDefault() {
        assertEquals(Posix.DEFAULT_UNIX_MODE, Posix.unixMode(null));
    }

    @Test
    public void testUnixModeEmptyReturnsDefault() {
        assertEquals(Posix.DEFAULT_UNIX_MODE, Posix.unixMode(Collections.emptySet()));
    }

    @Test
    public void testUnixMode0755() {
        Set<PosixFilePermission> perms = EnumSet.of(
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.OWNER_EXECUTE,
                PosixFilePermission.GROUP_READ,
                PosixFilePermission.GROUP_EXECUTE,
                PosixFilePermission.OTHERS_READ,
                PosixFilePermission.OTHERS_EXECUTE
        );
        assertEquals(0755, Posix.unixMode(perms));
    }

    @Test
    public void testUnixMode0644() {
        Set<PosixFilePermission> perms = EnumSet.of(
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.GROUP_READ,
                PosixFilePermission.OTHERS_READ
        );
        assertEquals(0644, Posix.unixMode(perms));
    }

    @Test
    public void testPosix0755() {
        Set<PosixFilePermission> result = Posix.posix(0755);
        assertTrue(result.contains(PosixFilePermission.OWNER_READ));
        assertTrue(result.contains(PosixFilePermission.OWNER_WRITE));
        assertTrue(result.contains(PosixFilePermission.OWNER_EXECUTE));
        assertTrue(result.contains(PosixFilePermission.GROUP_READ));
        assertTrue(result.contains(PosixFilePermission.GROUP_EXECUTE));
        assertTrue(result.contains(PosixFilePermission.OTHERS_READ));
        assertTrue(result.contains(PosixFilePermission.OTHERS_EXECUTE));
        assertFalse(result.contains(PosixFilePermission.GROUP_WRITE));
        assertFalse(result.contains(PosixFilePermission.OTHERS_WRITE));
    }

    @Test
    public void testPosixZeroReturnsEmpty() {
        assertTrue(Posix.posix(0).isEmpty());
    }

    @Test
    public void testPosixNegativeReturnsEmpty() {
        assertTrue(Posix.posix(-1).isEmpty());
    }

    @Test
    public void testRoundTrip() {
        Set<PosixFilePermission> original = EnumSet.of(
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.GROUP_READ,
                PosixFilePermission.OTHERS_READ
        );
        int mode = Posix.unixMode(original);
        Set<PosixFilePermission> result = Posix.posix(mode);
        assertEquals(original, result);
    }

    @Test
    public void testAllPermissions() {
        Set<PosixFilePermission> all = EnumSet.allOf(PosixFilePermission.class);
        assertEquals(0777, Posix.unixMode(all));
    }
}
