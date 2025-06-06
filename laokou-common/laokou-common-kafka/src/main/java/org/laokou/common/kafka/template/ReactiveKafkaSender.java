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

package org.laokou.common.kafka.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.internals.DefaultKafkaSender;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public class ReactiveKafkaSender implements KafkaSender {

	private final DefaultKafkaSender<String, Object> defaultKafkaSender;

	@Override
	public Flux<Boolean> send(String topic, String payload) {
		return defaultKafkaSender.send(Mono.just(SenderRecord.create(topic, null, null, null, payload, null)))
			.map(result -> {
				Exception exception = result.exception();
				if (ObjectUtils.isNotNull(exception)) {
					log.error("【Kafka】 => 发送消息失败，错误信息：{}", exception.getMessage(), exception);
					return false;
				}
				else {
					return true;
				}
			});
	}

}
