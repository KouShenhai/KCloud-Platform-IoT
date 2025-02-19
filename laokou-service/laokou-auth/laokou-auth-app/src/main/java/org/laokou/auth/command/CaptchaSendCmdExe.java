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
import org.laokou.auth.convertor.CaptchaConvertor;
import org.laokou.auth.dto.CaptchaSendCmd;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.CaptchaE;
import org.laokou.auth.service.extensionpoint.CaptchaParamValidatorExtPt;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.extension.BizScenario;
import org.laokou.common.extension.ExtensionExecutor;
import org.laokou.common.rocketmq.template.SendMessageType;
import org.springframework.stereotype.Component;

import static org.laokou.auth.dto.clientobject.CaptchaCO.USE_CASE_CAPTCHA;
import static org.laokou.common.i18n.common.constant.Constant.SCENARIO;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class CaptchaSendCmdExe {

	private final DomainEventPublisher rocketMQDomainEventPublisher;

	private final ExtensionExecutor extensionExecutor;

	private final DomainService domainService;

	public void executeVoid(CaptchaSendCmd cmd) {
		// 校验参数
		CaptchaE entity = CaptchaConvertor.toEntity(cmd.getCo());
		extensionExecutor.executeVoid(CaptchaParamValidatorExtPt.class,
				BizScenario.valueOf(entity.getTag(), USE_CASE_CAPTCHA, SCENARIO),
				extension -> extension.validate(entity));
		AuthA auth = DomainFactory.getAuth(IdGenerator.defaultSnowflakeId(), entity.getTenantCode());
		// 创建验证码
		domainService.createCaptcha(IdGenerator.defaultSnowflakeId(), auth, entity);
		// 发布事件
		auth.releaseEvents().forEach(item -> rocketMQDomainEventPublisher.publish(item, SendMessageType.ASYNC));
	}

}
