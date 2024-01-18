/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n.common;

import io.swagger.v3.oas.annotations.media.Schema;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author laokou
 */
@Schema(name = "MqttConstants", description = "MQTT消息常量")
public final class MqttConstants {

	private MqttConstants() {
	}

	@Schema(name = "WILL_TOPIC", description = "服务停止前的消息主题")
	public static final String WILL_TOPIC = "will_topic";

	@Schema(name = "WILL_DATA", description = "服务下线")
	public static final byte[] WILL_DATA = "offline".getBytes(UTF_8);

}
