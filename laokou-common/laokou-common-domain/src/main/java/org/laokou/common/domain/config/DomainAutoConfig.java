/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.domain.config;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.laokou.common.domain.factory.DefaultDomainEventFactory;
import org.laokou.common.domain.handler.DisruptorAbstractDomainEventHandler;
import org.laokou.common.i18n.dto.DefaultExtDomainEvent;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author laokou
 */
@AutoConfiguration
@MapperScan("org.laokou.common.domain.mapper")
public class DomainAutoConfig {

	@Bean(destroyMethod = "shutdown", initMethod = "start")
	public Disruptor<DefaultExtDomainEvent> sharedDisruptor(
			DisruptorAbstractDomainEventHandler disruptorAbstractDomainEventHandler) {
		// 创建一个可以容纳不同事件的Disruptor
		Disruptor<DefaultExtDomainEvent> disruptor = new Disruptor<>(new DefaultDomainEventFactory(), 8096,
				Thread.ofVirtual().factory(), ProducerType.SINGLE, new YieldingWaitStrategy());
		disruptor.handleEventsWith(disruptorAbstractDomainEventHandler);
		return disruptor;
	}

	@Bean
	public RingBuffer<DefaultExtDomainEvent> ringBuffer(Disruptor<DefaultExtDomainEvent> sharedDisruptor) {
		return sharedDisruptor.getRingBuffer();
	}

}
