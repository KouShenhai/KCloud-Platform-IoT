/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.rabbitmq.constant;

/**
 * <a href="https://spring.io/guides/gs/messaging-rabbitmq/">...</a> 车牌号区分成36个队列
 *
 * @author laokou
 */
public interface MqConstant {

	String LAOKOU_GPS_INFO_TOPIC_EXCHANGE = "laokou.gps.info.topic.exchange";

	String LAOKOU_GPS_INFO_1_QUEUE = "laokou.gps.info.1.queue";

	String LAOKOU_ROUTER_1_KEY = "1.#";

	String LAOKOU_GPS_INFO_2_QUEUE = "laokou.gps.info.2.queue";

	String LAOKOU_ROUTER_2_KEY = "2.#";

	String LAOKOU_GPS_INFO_3_QUEUE = "laokou.gps.info.3.queue";

	String LAOKOU_ROUTER_3_KEY = "3.#";

	String LAOKOU_GPS_INFO_4_QUEUE = "laokou.gps.info.4.queue";

	String LAOKOU_ROUTER_4_KEY = "4.#";

	String LAOKOU_GPS_INFO_5_QUEUE = "laokou.gps.info.5.queue";

	String LAOKOU_ROUTER_5_KEY = "5.#";

	String LAOKOU_GPS_INFO_6_QUEUE = "laokou.gps.info.6.queue";

	String LAOKOU_ROUTER_6_KEY = "6.#";

	String LAOKOU_GPS_INFO_7_QUEUE = "laokou.gps.info.7.queue";

	String LAOKOU_ROUTER_7_KEY = "7.#";

	String LAOKOU_GPS_INFO_8_QUEUE = "laokou.gps.info.8.queue";

	String LAOKOU_ROUTER_8_KEY = "8.#";

	String LAOKOU_GPS_INFO_9_QUEUE = "laokou.gps.info.9.queue";

	String LAOKOU_ROUTER_9_KEY = "9.#";

	String LAOKOU_GPS_INFO_0_QUEUE = "laokou.gps.info.0.queue";

	String LAOKOU_ROUTER_0_KEY = "0.#";

}
