package com.walmartlabs.concord.server.sdk;

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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PartialProcessKeyTest {

    @Test
    void fromReturnsKeyWithGivenId() {
        UUID id = UUID.randomUUID();
        PartialProcessKey k = PartialProcessKey.from(id);

        assertThat(k.getInstanceId()).isEqualTo(id);
    }

    @Test
    void createReturnsFreshKeyEachTime() {
        PartialProcessKey a = PartialProcessKey.create();
        PartialProcessKey b = PartialProcessKey.create();

        assertThat(a.getInstanceId()).isNotNull();
        assertThat(b.getInstanceId()).isNotNull();
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void equalsAndHashCodeMatchForSameId() {
        UUID id = UUID.randomUUID();
        PartialProcessKey a = PartialProcessKey.from(id);
        PartialProcessKey b = PartialProcessKey.from(id);

        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isEqualTo(a);
    }

    @Test
    void equalsReturnsFalseForNullAndOtherTypes() {
        PartialProcessKey k = PartialProcessKey.create();

        assertThat(k).isNotEqualTo(null);
        assertThat(k.equals("not a key")).isFalse();
    }

    @Test
    void partOfMatchesWhenIdsAreEqual() {
        UUID id = UUID.randomUUID();
        PartialProcessKey a = PartialProcessKey.from(id);
        PartialProcessKey b = PartialProcessKey.from(id);

        assertThat(a.partOf(b)).isTrue();
    }

    @Test
    void partOfFailsForDifferentIds() {
        PartialProcessKey a = PartialProcessKey.create();
        PartialProcessKey b = PartialProcessKey.create();

        assertThat(a.partOf(b)).isFalse();
    }

    @Test
    void toStringReturnsUuidString() {
        UUID id = UUID.randomUUID();
        PartialProcessKey k = PartialProcessKey.from(id);

        assertThat(k).hasToString(id.toString());
    }
}
