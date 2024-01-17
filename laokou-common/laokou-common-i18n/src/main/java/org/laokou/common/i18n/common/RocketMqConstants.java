/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

/**
 * rocketmq消息常量.
 *
 * @author laokou
 */
public final class RocketMqConstants {

	private RocketMqConstants() {
	}

	/**
	 * 分布式链路Topic.
	 */
	public static final String LAOKOU_MESSAGE_TOPIC = "laokou_message_topic";

	/**
	 * 通知消息Tag.
	 */
	public static final String LAOKOU_NOTICE_MESSAGE_TAG = "notice";

	/**
	 * 提醒消息Tag.
	 */
	public static final String LAOKOU_REMIND_MESSAGE_TAG = "remind";

	/**
	 * 提醒消息消费者组.
	 */
	public static final String LAOKOU_REMIND_MESSAGE_CONSUMER_GROUP = "laokou_remind_message_consumer_group";

	/**
	 * 通知消息消费者组.
	 */
	public static final String LAOKOU_NOTICE_MESSAGE_CONSUMER_GROUP = "laokou_notice_message_consumer_group";

	/**
	 * 分隔符.
	 */
	public static final String TOPIC_TAG = "%s:%s";

}
