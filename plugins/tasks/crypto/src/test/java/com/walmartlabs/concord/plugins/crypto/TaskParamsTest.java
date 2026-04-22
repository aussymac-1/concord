package com.walmartlabs.concord.plugins.crypto;

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

import com.walmartlabs.concord.runtime.v2.sdk.MapBackedVariables;
import com.walmartlabs.concord.runtime.v2.sdk.Variables;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TaskParamsTest {

    private static Variables vars(Map<String, Object> m) {
        return new MapBackedVariables(m);
    }

    @Test
    public void actionParsesCaseInsensitively() {
        Map<String, Object> m = new HashMap<>();
        m.put("action", "create");

        TaskParams p = new TaskParams(vars(m));
        assertEquals(TaskParams.Action.CREATE, p.action());
    }

    @Test
    public void actionRejectsUnknown() {
        Map<String, Object> m = new HashMap<>();
        m.put("action", "doStuff");

        TaskParams p = new TaskParams(vars(m));

        RuntimeException ex = assertThrows(RuntimeException.class, p::action);
        assertTrue(ex.getMessage().contains("doStuff"));
        assertTrue(ex.getMessage().contains("CREATE"));
    }

    @Test
    public void secretNameIsRequired() {
        TaskParams p = new TaskParams(vars(new HashMap<>()));
        assertThrows(RuntimeException.class, p::secretName);
    }

    @Test
    public void generatePasswordDefaultsToFalse() {
        TaskParams p = new TaskParams(vars(new HashMap<>()));
        assertFalse(p.generatePassword());

        Map<String, Object> m = new HashMap<>();
        m.put("generatePassword", true);
        assertTrue(new TaskParams(vars(m)).generatePassword());
    }

    @Test
    public void nullableGettersReturnNull() {
        TaskParams p = new TaskParams(vars(new HashMap<>()));
        assertNull(p.storePassword());
        assertNull(p.visibility());
        assertNull(p.project());
        assertNull(p.data());
        assertNull(p.keyPair());
        assertNull(p.usernamePassword());
    }

    @Test
    public void orgOrDefaultUsesProvidedDefault() {
        TaskParams p = new TaskParams(vars(new HashMap<>()));
        assertEquals("fallback", p.orgOrDefault("fallback"));
    }

    @Test
    public void orgOrDefaultPrefersExplicitValue() {
        Map<String, Object> m = new HashMap<>();
        m.put("org", "acme");

        TaskParams p = new TaskParams(vars(m));
        assertEquals("acme", p.orgOrDefault("fallback"));
    }

    @Test
    public void orgOrDefaultRejectsNullAll() {
        TaskParams p = new TaskParams(vars(new HashMap<>()));
        assertThrows(IllegalArgumentException.class, () -> p.orgOrDefault(null));
    }

    @Test
    public void keyPairReadsNestedFields() {
        Map<String, Object> kp = new HashMap<>();
        kp.put("public", "pub");
        kp.put("private", "priv");

        Map<String, Object> m = new HashMap<>();
        m.put("keyPair", kp);

        TaskParams.KeyPair pair = new TaskParams(vars(m)).keyPair();
        assertNotNull(pair);
        assertEquals("pub", pair.publicKey());
        assertEquals("priv", pair.privateKey());
    }

    @Test
    public void usernamePasswordReadsNestedFields() {
        Map<String, Object> up = new HashMap<>();
        up.put("username", "user");
        up.put("password", "p4ss");

        Map<String, Object> m = new HashMap<>();
        m.put("usernamePassword", up);

        TaskParams.UsernamePassword userPw = new TaskParams(vars(m)).usernamePassword();
        assertNotNull(userPw);
        assertEquals("user", userPw.username());
        assertEquals("p4ss", userPw.password());
    }

    @Test
    public void dataAndVisibilityAndProjectAreExposed() {
        Map<String, Object> m = new HashMap<>();
        m.put("data", "payload");
        m.put("visibility", "PUBLIC");
        m.put("project", "proj");
        m.put("storePassword", "sp");

        TaskParams p = new TaskParams(vars(m));
        assertEquals("payload", p.data());
        assertEquals("PUBLIC", p.visibility());
        assertEquals("proj", p.project());
        assertEquals("sp", p.storePassword());
    }
}
