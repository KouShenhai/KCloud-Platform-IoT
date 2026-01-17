/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.common.security.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.function.Supplier;

/**
 * TransmittableThreadLocalSecurityContextHolderStrategy测试类.
 *
 * @author laokou
 */
class TransmittableThreadLocalSecurityContextHolderStrategyTest {

	private TransmittableThreadLocalSecurityContextHolderStrategy strategy;

	@BeforeEach
	void setUp() {
		strategy = new TransmittableThreadLocalSecurityContextHolderStrategy();
		strategy.clearContext();
	}

	@Test
	void test_setContext_and_getContext() {
		// Given
		SecurityContext context = new SecurityContextImpl();

		// When
		strategy.setContext(context);
		SecurityContext result = strategy.getContext();

		// Then
		Assertions.assertThat(result).isNotNull().isSameAs(context);
	}

	@Test
	void test_clearContext() {
		// Given
		SecurityContext context = new SecurityContextImpl();
		strategy.setContext(context);

		// When
		strategy.clearContext();
		SecurityContext result = strategy.getContext();

		// Then
		Assertions.assertThat(result).isNotNull().isNotSameAs(context);
	}

	@Test
	void test_createEmptyContext() {
		// When
		SecurityContext result = strategy.createEmptyContext();

		// Then
		Assertions.assertThat(result).isNotNull().isInstanceOf(SecurityContextImpl.class);
		Assertions.assertThat(result.getAuthentication()).isNull();
	}

	@Test
	void test_getDeferredContext_returns_empty_context_when_not_set() {
		// When
		Supplier<SecurityContext> result = strategy.getDeferredContext();

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.get()).isNotNull().isInstanceOf(SecurityContextImpl.class);
	}

	@Test
	void test_setDeferredContext_and_getDeferredContext() {
		// Given
		SecurityContext context = new SecurityContextImpl();
		Supplier<SecurityContext> deferredContext = () -> context;

		// When
		strategy.setDeferredContext(deferredContext);
		Supplier<SecurityContext> result = strategy.getDeferredContext();

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.get()).isSameAs(context);
	}

	@Test
	void test_setContext_with_null_throws_exception() {
		// When & Then
		Assertions.assertThatThrownBy(() -> strategy.setContext(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Only non-null SecurityContext instances are permitted");
	}

	@Test
	void test_setDeferredContext_with_null_throws_exception() {
		// When & Then
		Assertions.assertThatThrownBy(() -> strategy.setDeferredContext(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Only non-null Supplier instances are permitted");
	}

}
