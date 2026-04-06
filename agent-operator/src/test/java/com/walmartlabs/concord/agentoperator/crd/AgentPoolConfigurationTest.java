package com.walmartlabs.concord.agentoperator.crd;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2019 Walmart Inc.
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AgentPoolConfigurationTest {

    @Test
    void testDefaults() {
        var config = new AgentPoolConfiguration();
        assertTrue(config.isAutoScale());
        assertEquals(10, config.getMaxSize());
        assertEquals(1, config.getMinSize());
        assertEquals(1, config.getSize());
        assertEquals(1, config.getSizeIncrement());
        assertEquals(300, config.getQueueQueryLimit());
    }

    @Test
    void testSetAutoScale() {
        var config = new AgentPoolConfiguration();
        config.setAutoScale(false);
        assertFalse(config.isAutoScale());
    }

    @Test
    void testSetMaxSize() {
        var config = new AgentPoolConfiguration();
        config.setMaxSize(50);
        assertEquals(50, config.getMaxSize());
    }

    @Test
    void testSetMinSize() {
        var config = new AgentPoolConfiguration();
        config.setMinSize(5);
        assertEquals(5, config.getMinSize());
    }

    @Test
    void testSetSize() {
        var config = new AgentPoolConfiguration();
        config.setSize(3);
        assertEquals(3, config.getSize());
    }

    @Test
    void testSetSizeIncrement() {
        var config = new AgentPoolConfiguration();
        config.setSizeIncrement(2);
        assertEquals(2, config.getSizeIncrement());
    }

    @Test
    void testSetAutoScaleStrategy() {
        var config = new AgentPoolConfiguration();
        config.setAutoScaleStrategy("linear");
        assertEquals("linear", config.getAutoScaleStrategy());
    }

    @Test
    void testSetScaleDelays() {
        var config = new AgentPoolConfiguration();
        config.setScaleUpDelayMs(60000);
        config.setScaleDownDelayMs(300000);
        assertEquals(60000, config.getScaleUpDelayMs());
        assertEquals(300000, config.getScaleDownDelayMs());
    }

    @Test
    void testSetPercentages() {
        var config = new AgentPoolConfiguration();
        config.setPercentIncrement(75.0);
        config.setPercentDecrement(25.0);
        assertEquals(75.0, config.getPercentIncrement());
        assertEquals(25.0, config.getPercentDecrement());
    }

    @Test
    void testSetThresholdFactors() {
        var config = new AgentPoolConfiguration();
        config.setIncrementThresholdFactor(2.0);
        config.setDecrementThresholdFactor(0.5);
        assertEquals(2.0, config.getIncrementThresholdFactor());
        assertEquals(0.5, config.getDecrementThresholdFactor());
    }

    @Test
    void testSetQueueSelector() {
        var config = new AgentPoolConfiguration();
        var selector = Map.<String, Object>of("agent", Map.of("flavor", "default"));
        config.setQueueSelector(selector);
        assertEquals(selector, config.getQueueSelector());
    }

    @Test
    void testSetQueueQueryLimit() {
        var config = new AgentPoolConfiguration();
        config.setQueueQueryLimit(500);
        assertEquals(500, config.getQueueQueryLimit());
    }
}
