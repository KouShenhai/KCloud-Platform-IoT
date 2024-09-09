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

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * @author laokou
 */
@AutoConfiguration
@RequiredArgsConstructor
@MapperScan("org.laokou.common.domain.mapper")
public class DomainAutoConfig {

	// @Bean
	public Disruptor<Object> sharedDisruptor() {
		// 创建一个可以容纳不同事件的Disruptor
		Disruptor<Object> disruptor = new Disruptor<>(Object::new, 8096, Thread.ofVirtual().factory(), ProducerType.SINGLE, new BlockingWaitStrategy());

		// 多事件处理器配置
//		EventHandler<Object>[] handlers = new EventHandler[]{
//			(event, sequence, endOfBatch) -> {
//				if (event instanceof TradeEvent) {
//					// 处理TradeEvent
//					TradeEvent tradeEvent = (TradeEvent) event;
//					System.out.println("Processing trade event: " + tradeEvent.getTradeId());
//				} else if (event instanceof OrderEvent) {
//					// 处理OrderEvent
//					OrderEvent orderEvent = (OrderEvent) event;
//					System.out.println("Processing order event: " + orderEvent.getOrderId());
//				}
//			}
//		};
//
//		disruptor.handleEventsWith(handlers);
//
//
//		return disruptor;
		disruptor.start();
		return disruptor;
	}

}
