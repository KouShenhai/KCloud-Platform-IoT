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

package org.laokou.adapter;

import lombok.RequiredArgsConstructor;
import org.laokou.client.dto.clientobject.ReceiveCO;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.laokou.common.trace.utils.TraceUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.infrastructure.common.constant.MqConstant.LAOKOU_MESSAGE_TOPIC;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
public class ApiController {

	private final TraceUtil traceUtil;

	private final RocketMqTemplate rocketMqTemplate;

	@PostMapping("send")
	public void send(@RequestBody ReceiveCO co) {
		rocketMqTemplate.sendAsyncMessage(LAOKOU_MESSAGE_TOPIC, EMPTY, co, traceUtil.getTraceId(),
				traceUtil.getSpanId());
	}

}
