/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.SpringContextUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEvent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;


/**
 * @author laokou
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor
@ContextConfiguration(classes = { SpringContextUtils.class, TestEventListener.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SpringContextUtilsTest {

	@Test
	void test_publishEvent() {
		SpringContextUtils.publishEvent(new LoginEvent(this, "laokou"));
	}

	@Test
	void test_getBean() {
		Assertions.assertThat(SpringContextUtils.getBean(TestEventListener.class)).isNotNull();
		DefaultListableBeanFactory beanFactory = SpringContextUtils.getFactory();
		Assertions.assertThat(beanFactory).isNotNull();
		Assertions.assertThat(beanFactory.getBeanNamesForType(TestEventListener.class)[0]).isEqualTo("testEventListener");
		Assertions.assertThat(beanFactory.containsBean("testEventListener")).isTrue();
		Assertions.assertThat(beanFactory.containsBeanDefinition("testEventListener")).isTrue();
		Assertions.assertThat(beanFactory.containsBeanDefinition("testEventListener1")).isFalse();
		Assertions.assertThat(beanFactory.containsBean("testEventListener1")).isFalse();
		Assertions.assertThatNoException().isThrownBy(() -> beanFactory.removeBeanDefinition("testEventListener"));
		Assertions.assertThat(beanFactory.containsBeanDefinition("testEventListener")).isFalse();
		Assertions.assertThatNoException()
			.isThrownBy(() -> SpringContextUtils.registerBean("testEventListener", TestEventListener.class));
		Assertions.assertThat(beanFactory.containsBeanDefinition("testEventListener")).isTrue();
		Assertions.assertThatNoException().isThrownBy(() -> SpringContextUtils.removeBean("testEventListener"));
		Assertions.assertThat(beanFactory.containsBeanDefinition("testEventListener")).isFalse();
		Assertions.assertThat(SpringContextUtils.getServiceId()).isEqualTo("laokou-common-core");
		Assertions.assertThatNoException().isThrownBy(() -> beanFactory.registerBeanDefinition("testEventListener",
				BeanDefinitionBuilder.genericBeanDefinition(TestEventListener.class).getBeanDefinition()));
		Assertions.assertThat(SpringContextUtils.getBean(TestEventListener.class))
			.isEqualTo(SpringContextUtils.getBean("testEventListener"));
		Assertions.assertThat(SpringContextUtils.containsBean("testEventListener")).isTrue();
		Assertions.assertThat(SpringContextUtils.isSingleton("testEventListener")).isTrue();
		Assertions.assertThat(SpringContextUtils.getType("testEventListener")).isEqualTo(TestEventListener.class);
		Assertions.assertThat(SpringContextUtils.getBean(TestEventListener.class))
			.isEqualTo(SpringContextUtils.getBean("testEventListener", TestEventListener.class));
		Assertions.assertThat(SpringContextUtils.getBean("testEventListener"))
			.isEqualTo(SpringContextUtils.getBeansOfType(TestEventListener.class).get("testEventListener"));
	}

	@Getter
	@Setter
	static class LoginEvent extends ApplicationEvent {

		private String username;

		public LoginEvent(Object source, String username) {
			super(source);
			this.username = username;
		}

	}

}
