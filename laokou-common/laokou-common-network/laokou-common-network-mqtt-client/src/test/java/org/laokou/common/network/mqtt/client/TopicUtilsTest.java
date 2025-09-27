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

package org.laokou.common.network.mqtt.client;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.network.mqtt.client.util.VertxMqttUtils;

/**
 * @author laokou
 */
class TopicUtilsTest {

	@Test
	void test_match() {
		Assertions.assertThat(VertxMqttUtils.matchTopic("test/topic", "test/topic")).isTrue();
		Assertions.assertThat(VertxMqttUtils.matchTopic("test/topic", "test/topic/")).isFalse();
		Assertions.assertThat(VertxMqttUtils.matchTopic("test/#", "test/topic/test")).isTrue();
		Assertions.assertThat(VertxMqttUtils.matchTopic("test/+", "test/topic")).isTrue();
		Assertions.assertThat(VertxMqttUtils.matchTopic("test/+/test", "test/topic/test")).isTrue();
		Assertions.assertThat(VertxMqttUtils.matchTopic("test/+/+", "test/topic/test")).isTrue();
	}

}
