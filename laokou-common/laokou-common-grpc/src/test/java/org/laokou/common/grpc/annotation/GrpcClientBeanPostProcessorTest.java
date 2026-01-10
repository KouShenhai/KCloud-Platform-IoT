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

package org.laokou.common.grpc.annotation;

import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.grpc.client.GrpcClientFactory;

/**
 * @author laokou
 */
class GrpcClientBeanPostProcessorTest {

	private GrpcClientBeanPostProcessor postProcessor;

	private final String mockClient = "mockClient";

	@BeforeEach
	void setUp() {
		GrpcClientFactory grpcClientFactory = Mockito.mock(GrpcClientFactory.class);
		postProcessor = new GrpcClientBeanPostProcessor(grpcClientFactory);

		Mockito
			.when(grpcClientFactory.getClient(ArgumentMatchers.eq("discovery://laokou-auth"),
					ArgumentMatchers.eq(String.class), ArgumentMatchers.any()))
			.thenReturn(mockClient);
	}

	@Test
	void testFieldInjection() {
		FieldBean bean = new FieldBean();
		postProcessor.postProcessBeforeInitialization(bean, "fieldBean");
		Assertions.assertThat(bean.getClient()).isEqualTo(mockClient);
	}

	@Test
	void testMethodInjection() {
		MethodBean bean = new MethodBean();
		postProcessor.postProcessBeforeInitialization(bean, "methodBean");
		Assertions.assertThat(bean.getClient()).isEqualTo(mockClient);
	}

	@Test
	void testSuperclassInjection() {
		SubBean bean = new SubBean();
		postProcessor.postProcessBeforeInitialization(bean, "subBean");
		Assertions.assertThat(bean.getClient()).isEqualTo(mockClient);
	}

	@Getter
	static class FieldBean {

		@GrpcClient(serviceId = "laokou-auth")
		private String client;

	}

	@Getter
	static class MethodBean {

		private String client;

		@GrpcClient(serviceId = "laokou-auth")
		public void setClient(String client) {
			this.client = client;
		}

	}

	@Getter
	static class BaseBean {

		@GrpcClient(serviceId = "laokou-auth")
		protected String client;

	}

	static class SubBean extends BaseBean {

	}

}
