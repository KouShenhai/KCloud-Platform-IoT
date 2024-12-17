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

package org.laokou.auth.consumer.handler;

import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.sms.service.SmsService;

/**
 * @author laokou
 */
// @Slf4j
// @Component
// @NonNullApi
// @RocketMQMessageListener(consumerGroup = LAOKOU_MOBILE_CAPTCHA_CONSUMER_GROUP, topic =
// LAOKOU_CAPTCHA_TOPIC,
// selectorExpression = MOBILE_TAG, messageModel = CLUSTERING, consumeMode = CONCURRENTLY)
public class SendMobileCaptchaEventHandler {

	private final SmsService smsService;

	private final RedisUtil redisUtil;

	public SendMobileCaptchaEventHandler(DomainEventPublisher domainEventPublisher, SmsService smsService,
			RedisUtil redisUtil) {
		// super(domainEventPublisher);
		this.smsService = smsService;
		this.redisUtil = redisUtil;
	}

	// @Override
	// protected NoticeLog getNoticeLog(SendCaptchaEvent event) {
	// return smsService.send(event.getUuid(), 5);
	// }

}
