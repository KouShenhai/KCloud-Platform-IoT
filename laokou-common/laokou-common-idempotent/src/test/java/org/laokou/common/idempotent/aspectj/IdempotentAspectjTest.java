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

package org.laokou.common.idempotent.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.idempotent.util.IdempotentUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * IdempotentAspectj unit test class.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
class IdempotentAspectjTest {

	@Mock
	private RedisUtils redisUtils;

	@Mock
	private ProceedingJoinPoint proceedingJoinPoint;

	private IdempotentAspectj idempotentAspectj;

	private MockHttpServletRequest mockRequest;

	@BeforeEach
	void setUp() {
		idempotentAspectj = new IdempotentAspectj(redisUtils);
		IdempotentUtils.cleanIdempotent();

		// Setup MockHttpServletRequest and bind to RequestContextHolder
		mockRequest = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));
	}

	@AfterEach
	void tearDown() {
		IdempotentUtils.cleanIdempotent();
		RequestContextHolder.resetRequestAttributes();
	}

	@Test
	@DisplayName("Test REQUEST_ID constant value")
	void test_request_id_constant() {
		// Then
		Assertions.assertThat(IdempotentAspectj.REQUEST_ID).isEqualTo("request-id");
	}

	@Test
	@DisplayName("Test doAround throws exception when request id is empty")
	void test_doAround_throws_exception_when_request_id_is_empty() {
		// Given - no request-id set in mockRequest

		// When & Then
		Assertions.assertThatThrownBy(() -> idempotentAspectj.doAround(proceedingJoinPoint))
			.isInstanceOf(SystemException.class)
			.hasMessageContaining("请求ID不能为空");
	}

	@Test
	@DisplayName("Test doAround throws exception when request id is blank")
	void test_doAround_throws_exception_when_request_id_is_blank() {
		// Given
		mockRequest.addHeader(IdempotentAspectj.REQUEST_ID, "");

		// When & Then
		Assertions.assertThatThrownBy(() -> idempotentAspectj.doAround(proceedingJoinPoint))
			.isInstanceOf(SystemException.class)
			.hasMessageContaining("请求ID不能为空");
	}

	@Test
	@DisplayName("Test doAround gets request id from header")
	void test_doAround_gets_request_id_from_header() throws Throwable {
		// Given
		String requestId = "test-request-id-123";
		mockRequest.addHeader(IdempotentAspectj.REQUEST_ID, requestId);
		Mockito.when(redisUtils.setIfAbsent(Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong())).thenReturn(true);
		Mockito.when(proceedingJoinPoint.proceed()).thenReturn("success");

		// When
		Object result = idempotentAspectj.doAround(proceedingJoinPoint);

		// Then
		Assertions.assertThat(result).isEqualTo("success");
		Mockito.verify(redisUtils)
			.setIfAbsent(Mockito.contains("api:idempotent:" + requestId), Mockito.eq(0),
					Mockito.eq(RedisUtils.FIVE_MINUTE_EXPIRE));
	}

	@Test
	@DisplayName("Test doAround gets request id from parameter when header is empty")
	void test_doAround_gets_request_id_from_parameter_when_header_is_empty() throws Throwable {
		// Given
		String requestId = "test-request-id-456";
		mockRequest.addHeader(IdempotentAspectj.REQUEST_ID, "");
		mockRequest.addParameter(IdempotentAspectj.REQUEST_ID, requestId);
		Mockito.when(redisUtils.setIfAbsent(Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong())).thenReturn(true);
		Mockito.when(proceedingJoinPoint.proceed()).thenReturn("success");

		// When
		Object result = idempotentAspectj.doAround(proceedingJoinPoint);

		// Then
		Assertions.assertThat(result).isEqualTo("success");
	}

	@Test
	@DisplayName("Test doAround throws exception when repeated submit")
	void test_doAround_throws_exception_when_repeated_submit() {
		// Given
		String requestId = "test-request-id-789";
		mockRequest.addHeader(IdempotentAspectj.REQUEST_ID, requestId);
		Mockito.when(redisUtils.setIfAbsent(Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong()))
			.thenReturn(false);

		// When & Then
		Assertions.assertThatThrownBy(() -> idempotentAspectj.doAround(proceedingJoinPoint))
			.isInstanceOf(SystemException.class)
			.hasMessageContaining("不可重复提交请求");
	}

	@Test
	@DisplayName("Test doAround executes target method successfully")
	void test_doAround_executes_target_method_successfully() throws Throwable {
		// Given
		String requestId = "test-request-id-execute";
		String expectedResult = "target method result";
		mockRequest.addHeader(IdempotentAspectj.REQUEST_ID, requestId);
		Mockito.when(redisUtils.setIfAbsent(Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong())).thenReturn(true);
		Mockito.when(proceedingJoinPoint.proceed()).thenReturn(expectedResult);

		// When
		Object result = idempotentAspectj.doAround(proceedingJoinPoint);

		// Then
		Assertions.assertThat(result).isEqualTo(expectedResult);
		Mockito.verify(proceedingJoinPoint).proceed();
	}

	@Test
	@DisplayName("Test doBefore opens idempotent context")
	void test_doBefore_opens_idempotent_context() {
		// Given
		Assertions.assertThat(IdempotentUtils.isIdempotent()).isFalse();

		// When
		idempotentAspectj.doBefore();

		// Then
		Assertions.assertThat(IdempotentUtils.isIdempotent()).isTrue();
	}

	@Test
	@DisplayName("Test doAfter cleans idempotent context")
	void test_doAfter_cleans_idempotent_context() {
		// Given
		IdempotentUtils.openIdempotent();
		Assertions.assertThat(IdempotentUtils.isIdempotent()).isTrue();

		// When
		idempotentAspectj.doAfter();

		// Then
		Assertions.assertThat(IdempotentUtils.isIdempotent()).isFalse();
	}

	@Test
	@DisplayName("Test doAround sets idempotent context during execution")
	void test_doAround_sets_idempotent_context_during_execution() throws Throwable {
		// Given
		String requestId = "test-request-id-context";
		mockRequest.addHeader(IdempotentAspectj.REQUEST_ID, requestId);
		Mockito.when(redisUtils.setIfAbsent(Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong())).thenReturn(true);
		Mockito.when(proceedingJoinPoint.proceed()).thenAnswer(_ -> {
			// Verify idempotent is open during execution
			Assertions.assertThat(IdempotentUtils.isIdempotent()).isTrue();
			return "success";
		});

		// When
		idempotentAspectj.doAround(proceedingJoinPoint);

		// Then - idempotent should be cleaned after execution
		Assertions.assertThat(IdempotentUtils.isIdempotent()).isFalse();
	}

	@Test
	@DisplayName("Test doAround cleans idempotent context even when exception occurs")
	void test_doAround_cleans_idempotent_context_on_exception() throws Throwable {
		// Given
		String requestId = "test-request-id-exception";
		mockRequest.addHeader(IdempotentAspectj.REQUEST_ID, requestId);
		Mockito.when(redisUtils.setIfAbsent(Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong())).thenReturn(true);
		Mockito.when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("test exception"));

		// When & Then
		Assertions.assertThatThrownBy(() -> idempotentAspectj.doAround(proceedingJoinPoint))
			.isInstanceOf(RuntimeException.class);

		// Verify idempotent is cleaned even after exception
		Assertions.assertThat(IdempotentUtils.isIdempotent()).isFalse();
	}

}
