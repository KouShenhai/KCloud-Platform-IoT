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

package org.laokou.common.vertx.constant;

/**
 * @author laokou
 */
public final class MqConstants {

	private MqConstants() {
	}

	public static final String LAOKOU_MQTT_PROPERTY_UP = "laokou_mqtt_property_up";

	public static final String LAOKOU_MQTT_PROPERTY_DOWN = "laokou_mqtt_property_down";

	public static final String MQTT_TOPIC_RULE_UP = "/+/+/property/up";

	public static final String MQTT_TOPIC_RULE_DOWN = "/+/+/property/down";

	public static final String HTTP_ROUTER_RULE_UP = "/:productId/:deviceId/property/up";

}
