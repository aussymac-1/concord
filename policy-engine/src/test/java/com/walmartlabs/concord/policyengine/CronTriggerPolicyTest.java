package com.walmartlabs.concord.policyengine;

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

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

public class CronTriggerPolicyTest {

    @Test
    public void testNullRule() {
        CronTriggerPolicy policy = new CronTriggerPolicy(null);
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        CheckResult<CronTriggerRule, Duration> result = policy.check(now, now.plusMinutes(1));
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testIntervalTooSmall() {
        CronTriggerRule rule = CronTriggerRule.of("min 60s", 60);
        CronTriggerPolicy policy = new CronTriggerPolicy(rule);

        OffsetDateTime fireAt = OffsetDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime nextFireAt = OffsetDateTime.of(2024, 1, 1, 10, 0, 30, 0, ZoneOffset.UTC);

        CheckResult<CronTriggerRule, Duration> result = policy.check(fireAt, nextFireAt);
        assertFalse(result.getDeny().isEmpty());
    }

    @Test
    public void testIntervalSufficient() {
        CronTriggerRule rule = CronTriggerRule.of("min 60s", 60);
        CronTriggerPolicy policy = new CronTriggerPolicy(rule);

        OffsetDateTime fireAt = OffsetDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime nextFireAt = OffsetDateTime.of(2024, 1, 1, 10, 2, 0, 0, ZoneOffset.UTC);

        CheckResult<CronTriggerRule, Duration> result = policy.check(fireAt, nextFireAt);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testIntervalExactlyAtMinimum() {
        CronTriggerRule rule = CronTriggerRule.of("min 60s", 60);
        CronTriggerPolicy policy = new CronTriggerPolicy(rule);

        OffsetDateTime fireAt = OffsetDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime nextFireAt = OffsetDateTime.of(2024, 1, 1, 10, 1, 0, 0, ZoneOffset.UTC);

        CheckResult<CronTriggerRule, Duration> result = policy.check(fireAt, nextFireAt);
        assertTrue(result.getDeny().isEmpty());
    }

    @Test
    public void testZeroMinInterval() {
        CronTriggerRule rule = CronTriggerRule.of("no limit", 0);
        CronTriggerPolicy policy = new CronTriggerPolicy(rule);

        OffsetDateTime fireAt = OffsetDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime nextFireAt = OffsetDateTime.of(2024, 1, 1, 10, 0, 1, 0, ZoneOffset.UTC);

        CheckResult<CronTriggerRule, Duration> result = policy.check(fireAt, nextFireAt);
        assertTrue(result.getDeny().isEmpty());
    }
}
