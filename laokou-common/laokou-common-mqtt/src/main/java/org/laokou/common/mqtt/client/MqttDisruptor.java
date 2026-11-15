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

package org.laokou.common.mqtt.client;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Getter;
import org.laokou.common.core.util.VirtualThreadFactory;

/**
 * @author laokou
 */
@Getter
public final class MqttDisruptor implements AutoCloseable {

	private final Disruptor<GatewayCommandEvent> disruptor;

	public MqttDisruptor(GatewayCommandEventHandler gatewayCommandEventHandler) {
		this.disruptor = new Disruptor<>(GatewayCommandEvent::new, 1024, VirtualThreadFactory.INSTANCE,
				ProducerType.MULTI, new BlockingWaitStrategy());
		this.disruptor.handleEventsWith(gatewayCommandEventHandler);
	}

	public void start() {
		disruptor.start();
	}

	@Override
	public void close() throws Exception {
		disruptor.shutdown();
	}

}
