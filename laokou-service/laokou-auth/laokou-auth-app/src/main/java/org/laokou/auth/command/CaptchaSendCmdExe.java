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

package org.laokou.auth.command;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.ability.DomainService;
import org.laokou.auth.convertor.AuthConvertor;
import org.laokou.auth.dto.CaptchaSendCmd;
import org.laokou.auth.dto.clientobject.CaptchaCO;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.model.AuthA;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class CaptchaSendCmdExe {

	private final DomainService domainService;

	private final DomainEventPublisher kafkaDomainEventPublisher;

	@Async
	@CommandLog
	public void executeVoid(CaptchaSendCmd cmd) {
		CaptchaCO co = cmd.getCo();
		AuthA authA = DomainFactory.getAuth().createCaptchaV(co.getUuid(), co.getTag(), co.getTenantCode());
		domainService.sendCaptcha(authA);
		// 发布领域事件
		kafkaDomainEventPublisher.publish(authA.getSendCaptchaTypeEnum().getMqTopic(),
				AuthConvertor.toDomainEvent(authA));
	}

}
