package com.walmartlabs.concord.plugins.crypto;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2020 Walmart Inc.
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
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TaskParamsTest {

    @Test
    void testActionCreate() {
        var vars = new MapBackedVariables(Map.of("action", "CREATE"));
        var params = new TaskParams(vars);
        assertEquals(TaskParams.Action.CREATE, params.action());
    }

    @Test
    void testActionCaseInsensitive() {
        var vars = new MapBackedVariables(Map.of("action", "create"));
        var params = new TaskParams(vars);
        assertEquals(TaskParams.Action.CREATE, params.action());
    }

    @Test
    void testActionInvalid() {
        var vars = new MapBackedVariables(Map.of("action", "INVALID"));
        var params = new TaskParams(vars);
        assertThrows(RuntimeException.class, params::action);
    }

    @Test
    void testSecretName() {
        var vars = new MapBackedVariables(Map.of("secretName", "mySecret"));
        var params = new TaskParams(vars);
        assertEquals("mySecret", params.secretName());
    }

    @Test
    void testGeneratePasswordDefault() {
        var vars = new MapBackedVariables(Map.of("action", "CREATE"));
        var params = new TaskParams(vars);
        assertFalse(params.generatePassword());
    }

    @Test
    void testGeneratePasswordTrue() {
        var vars = new MapBackedVariables(Map.of("generatePassword", true));
        var params = new TaskParams(vars);
        assertTrue(params.generatePassword());
    }

    @Test
    void testStorePassword() {
        var vars = new MapBackedVariables(Map.of("storePassword", "pass123"));
        var params = new TaskParams(vars);
        assertEquals("pass123", params.storePassword());
    }

    @Test
    void testVisibility() {
        var vars = new MapBackedVariables(Map.of("visibility", "PUBLIC"));
        var params = new TaskParams(vars);
        assertEquals("PUBLIC", params.visibility());
    }

    @Test
    void testOrgOrDefaultWithValue() {
        var vars = new MapBackedVariables(Map.of("org", "myOrg"));
        var params = new TaskParams(vars);
        assertEquals("myOrg", params.orgOrDefault("defaultOrg"));
    }

    @Test
    void testOrgOrDefaultUsesDefault() {
        var vars = new MapBackedVariables(Map.of("action", "CREATE"));
        var params = new TaskParams(vars);
        assertEquals("defaultOrg", params.orgOrDefault("defaultOrg"));
    }

    @Test
    void testOrgOrDefaultBothNull() {
        var vars = new MapBackedVariables(Map.of("action", "CREATE"));
        var params = new TaskParams(vars);
        assertThrows(IllegalArgumentException.class, () -> params.orgOrDefault(null));
    }

    @Test
    void testProject() {
        var vars = new MapBackedVariables(Map.of("project", "myProject"));
        var params = new TaskParams(vars);
        assertEquals("myProject", params.project());
    }

    @Test
    void testKeyPairPresent() {
        var m = new HashMap<String, Object>();
        m.put("keyPair", Map.of("public", "pubKey", "private", "privKey"));
        var vars = new MapBackedVariables(m);
        var params = new TaskParams(vars);
        var keyPair = params.keyPair();
        assertNotNull(keyPair);
        assertEquals("pubKey", keyPair.publicKey());
        assertEquals("privKey", keyPair.privateKey());
    }

    @Test
    void testKeyPairAbsent() {
        var vars = new MapBackedVariables(Map.of("action", "CREATE"));
        var params = new TaskParams(vars);
        assertNull(params.keyPair());
    }

    @Test
    void testUsernamePasswordPresent() {
        var m = new HashMap<String, Object>();
        m.put("usernamePassword", Map.of("username", "user1", "password", "pass1"));
        var vars = new MapBackedVariables(m);
        var params = new TaskParams(vars);
        var up = params.usernamePassword();
        assertNotNull(up);
        assertEquals("user1", up.username());
        assertEquals("pass1", up.password());
    }

    @Test
    void testUsernamePasswordAbsent() {
        var vars = new MapBackedVariables(Map.of("action", "CREATE"));
        var params = new TaskParams(vars);
        assertNull(params.usernamePassword());
    }

    @Test
    void testData() {
        var vars = new MapBackedVariables(Map.of("data", "someData"));
        var params = new TaskParams(vars);
        assertEquals("someData", params.data());
    }
}
