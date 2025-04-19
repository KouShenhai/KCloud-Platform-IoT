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

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.SpringContextUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@ContextConfiguration(classes = { SpringContextUtils.class, TestEventListener.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SpringContextUtilsTest {

	@Test
	void testPublishEvent() {
		SpringContextUtils.publishEvent(new TestEvent(this, "456"));
	}

	@Test
	void testGetBean() {
		Assertions.assertNotNull(SpringContextUtils.getBean(TestEventListener.class));
		DefaultListableBeanFactory beanFactory = SpringContextUtils.getFactory();
		Assertions.assertNotNull(beanFactory);
		Assertions.assertEquals("testEventListener", beanFactory.getBeanNamesForType(TestEventListener.class)[0]);
		Assertions.assertTrue(beanFactory.containsBean("testEventListener"));
		Assertions.assertTrue(beanFactory.containsBeanDefinition("testEventListener"));
		Assertions.assertFalse(beanFactory.containsBeanDefinition("testEventListener1"));
		Assertions.assertFalse(beanFactory.containsBean("testEventListener1"));
		Assertions.assertDoesNotThrow(() -> beanFactory.removeBeanDefinition("testEventListener"));
		Assertions.assertFalse(beanFactory.containsBeanDefinition("testEventListener"));
		Assertions
			.assertDoesNotThrow(() -> SpringContextUtils.registerBean("testEventListener", TestEventListener.class));
		Assertions.assertTrue(beanFactory.containsBeanDefinition("testEventListener"));
		Assertions.assertDoesNotThrow(() -> SpringContextUtils.removeBean("testEventListener"));
		Assertions.assertFalse(beanFactory.containsBeanDefinition("testEventListener"));
		Assertions.assertEquals("laokou-common-core", SpringContextUtils.getServiceId());
		Assertions.assertDoesNotThrow(() -> beanFactory.registerBeanDefinition("testEventListener",
				BeanDefinitionBuilder.genericBeanDefinition(TestEventListener.class).getBeanDefinition()));
		Assertions.assertEquals(SpringContextUtils.getBean("testEventListener"),
				SpringContextUtils.getBean(TestEventListener.class));
		Assertions.assertTrue(SpringContextUtils.containsBean("testEventListener"));
		Assertions.assertTrue(SpringContextUtils.isSingleton("testEventListener"));
		Assertions.assertEquals(TestEventListener.class, SpringContextUtils.getType("testEventListener"));
		Assertions.assertEquals(SpringContextUtils.getBean("testEventListener", TestEventListener.class),
				SpringContextUtils.getBean(TestEventListener.class));
		Assertions.assertEquals(SpringContextUtils.getType(TestEventListener.class).get("testEventListener"),
				SpringContextUtils.getBean("testEventListener"));
	}

}
