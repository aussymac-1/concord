package com.walmartlabs.concord.github.appinstallation.exception;

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

import static org.assertj.core.api.Assertions.assertThat;

class GitHubAppExceptionTest {

    @Test
    void constructorWithMessage() {
        GitHubAppException ex = new GitHubAppException("app error");
        assertThat(ex.getMessage()).isEqualTo("app error");
        assertThat(ex.getCause()).isNull();
    }

    @Test
    void constructorWithMessageAndCause() {
        RuntimeException cause = new RuntimeException("root");
        GitHubAppException ex = new GitHubAppException("wrapped", cause);
        assertThat(ex.getMessage()).isEqualTo("wrapped");
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    void isRuntimeException() {
        assertThat(new GitHubAppException("test")).isInstanceOf(RuntimeException.class);
    }

    @Test
    void notFoundExceptionWithMessage() {
        GitHubAppException.NotFoundException ex = new GitHubAppException.NotFoundException("not found");
        assertThat(ex.getMessage()).isEqualTo("not found");
        assertThat(ex).isInstanceOf(GitHubAppException.class);
    }

    @Test
    void notFoundExceptionWithMessageAndCause() {
        RuntimeException cause = new RuntimeException("root");
        GitHubAppException.NotFoundException ex = new GitHubAppException.NotFoundException("not found", cause);
        assertThat(ex.getMessage()).isEqualTo("not found");
        assertThat(ex.getCause()).isEqualTo(cause);
    }
}
