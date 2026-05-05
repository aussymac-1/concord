package com.walmartlabs.concord.runtime.model;

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

import com.walmartlabs.concord.imports.Imports;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EffectiveConfigurationTest {

    @Test
    public void testNullActiveProfilesReturnsBaseCopy() {
        var base = new LinkedHashMap<String, Object>();
        base.put("a", 1);

        var def = definition(cfg(base), Collections.emptyMap());

        var result = EffectiveConfiguration.getEffectiveConfiguration(def, null);

        assertEquals(1, result.get("a"));
        // returned map is a copy, not the same reference
        assertEquals(base, result);
    }

    @Test
    public void testNullProfilesReturnsBaseCopy() {
        var base = new LinkedHashMap<String, Object>();
        base.put("a", 1);

        var def = new TestDef("v2", cfg(base), null, Imports.builder().build());

        var result = EffectiveConfiguration.getEffectiveConfiguration(def, List.of("dev"));

        assertEquals(1, result.get("a"));
    }

    @Test
    public void testNullBaseConfigurationProducesEmptyMap() {
        var def = new TestDef("v2", null, Collections.emptyMap(), Imports.builder().build());

        var result = EffectiveConfiguration.getEffectiveConfiguration(def, Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNullBaseConfigurationMapProducesEmptyMap() {
        // configuration() returns non-null but asMap() returns null
        var nullMapCfg = new Configuration() {
            @Override
            public Map<String, Object> asMap() {
                return null;
            }

            @Override
            public List<String> dependencies() {
                return Collections.emptyList();
            }

            @Override
            public List<String> extraDependencies() {
                return Collections.emptyList();
            }
        };

        var def = new TestDef("v2", nullMapCfg, Collections.emptyMap(), Imports.builder().build());

        var result = EffectiveConfiguration.getEffectiveConfiguration(def, Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testActiveProfileMissingIsIgnored() {
        var base = new LinkedHashMap<String, Object>();
        base.put("a", 1);

        var def = definition(cfg(base), Collections.emptyMap());

        var result = EffectiveConfiguration.getEffectiveConfiguration(def, List.of("notThere"));

        assertEquals(1, result.get("a"));
    }

    @Test
    public void testActiveProfileNullConfigurationIsIgnored() {
        var base = new LinkedHashMap<String, Object>();
        base.put("a", 1);

        Map<String, Profile> profiles = new HashMap<>();
        profiles.put("dev", profile(null));

        var def = definition(cfg(base), profiles);

        var result = EffectiveConfiguration.getEffectiveConfiguration(def, List.of("dev"));

        assertEquals(1, result.get("a"));
    }

    @Test
    public void testProfileOverridesBase() {
        var base = new LinkedHashMap<String, Object>();
        base.put("a", 1);
        base.put("b", 2);

        var dev = new LinkedHashMap<String, Object>();
        dev.put("b", 99);
        dev.put("c", 3);

        Map<String, Profile> profiles = new HashMap<>();
        profiles.put("dev", profile(cfg(dev)));

        var def = definition(cfg(base), profiles);

        var result = EffectiveConfiguration.getEffectiveConfiguration(def, List.of("dev"));

        assertEquals(1, result.get("a"));
        assertEquals(99, result.get("b"));
        assertEquals(3, result.get("c"));
    }

    @Test
    public void testProfilesAppliedInOrder() {
        var base = new LinkedHashMap<String, Object>();
        base.put("v", 0);

        var p1 = new LinkedHashMap<String, Object>();
        p1.put("v", 1);

        var p2 = new LinkedHashMap<String, Object>();
        p2.put("v", 2);

        Map<String, Profile> profiles = new HashMap<>();
        profiles.put("a", profile(cfg(p1)));
        profiles.put("b", profile(cfg(p2)));

        var def = definition(cfg(base), profiles);

        // the last applied profile wins
        assertEquals(2, EffectiveConfiguration.getEffectiveConfiguration(def, List.of("a", "b")).get("v"));
        assertEquals(1, EffectiveConfiguration.getEffectiveConfiguration(def, List.of("b", "a")).get("v"));
    }

    @Test
    public void testNestedDeepMerge() {
        var base = new LinkedHashMap<String, Object>();
        var baseInner = new LinkedHashMap<String, Object>();
        baseInner.put("x", 1);
        baseInner.put("y", 2);
        base.put("nested", baseInner);

        var dev = new LinkedHashMap<String, Object>();
        var devInner = new LinkedHashMap<String, Object>();
        devInner.put("y", 99);
        devInner.put("z", 3);
        dev.put("nested", devInner);

        Map<String, Profile> profiles = new HashMap<>();
        profiles.put("dev", profile(cfg(dev)));

        var def = definition(cfg(base), profiles);

        var result = EffectiveConfiguration.getEffectiveConfiguration(def, List.of("dev"));

        @SuppressWarnings("unchecked")
        var nested = (Map<String, Object>) result.get("nested");
        assertEquals(1, nested.get("x"));
        assertEquals(99, nested.get("y"));
        assertEquals(3, nested.get("z"));
    }

    private static Configuration cfg(Map<String, Object> map) {
        return new Configuration() {
            @Override
            public Map<String, Object> asMap() {
                return map;
            }

            @Override
            public List<String> dependencies() {
                return Collections.emptyList();
            }

            @Override
            public List<String> extraDependencies() {
                return Collections.emptyList();
            }
        };
    }

    private static Profile profile(Configuration cfg) {
        return new Profile() {
            @Override
            public Configuration configuration() {
                return cfg;
            }

            @Override
            public Set<String> publicFlows() {
                return Collections.emptySet();
            }

            @Override
            public Map<String, ? extends FlowDefinition> flows() {
                return Collections.emptyMap();
            }
        };
    }

    private static ProcessDefinition definition(Configuration cfg, Map<String, ? extends Profile> profiles) {
        return new TestDef("v2", cfg, profiles, Imports.builder().build());
    }

    private static final class TestDef implements ProcessDefinition {

        private final String runtime;
        private final Configuration configuration;
        private final Map<String, ? extends Profile> profiles;
        private final Imports imports;

        TestDef(String runtime, Configuration configuration, Map<String, ? extends Profile> profiles, Imports imports) {
            this.runtime = runtime;
            this.configuration = configuration;
            this.profiles = profiles;
            this.imports = imports;
        }

        @Override
        public String runtime() {
            return runtime;
        }

        @Override
        public Configuration configuration() {
            return configuration;
        }

        @Override
        public Map<String, ? extends FlowDefinition> flows() {
            return Collections.emptyMap();
        }

        @Override
        public Set<String> publicFlows() {
            return Collections.emptySet();
        }

        @Override
        public Map<String, ? extends Profile> profiles() {
            return profiles;
        }

        @Override
        public List<Trigger> triggers() {
            return Collections.emptyList();
        }

        @Override
        public Imports imports() {
            return imports;
        }

        @Override
        public List<Form> forms() {
            return Collections.emptyList();
        }

        @Override
        public void serialize(Options options, OutputStream out) {
            // no-op for tests
        }
    }
}
