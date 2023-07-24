/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.common.rocketmq.constant;

/**
 * 队列常量值
 *
 * @author laokou
 */
public interface MqConstant {

	String LAOKOU_TRACE_TOPIC = "laokou_trace_topic";

	String LAOKOU_MESSAGE_TOPIC = "laokou_message_topic";

	String LAOKOU_NOTICE_MESSAGE_TAG = "notice";

	String LAOKOU_REMIND_MESSAGE_TAG = "remind";

	String LAOKOU_LOGSTASH_CONSUMER_GROUP = "laokou_logstash_consumer_group";

	String LAOKOU_REMIND_MESSAGE_CONSUMER_GROUP = "laokou_remind_message_consumer_group";

	String LAOKOU_NOTICE_MESSAGE_CONSUMER_GROUP = "laokou_notice_message_consumer_group";

	String TOPIC_TAG = "%s:%s";

}
