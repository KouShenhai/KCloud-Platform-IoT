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

package org.laokou.auth.command;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.dto.CaptchaSendCmd;
import org.laokou.auth.dto.domainevent.SendCaptchaEvent;
import org.laokou.auth.extensionpoint.CaptchaValidatorExtPt;
import org.laokou.auth.gateway.SourceGateway;
import org.laokou.auth.model.SourceV;
import org.laokou.auth.model.UserE;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.SpringContextUtil;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.extension.BizScenario;
import org.laokou.common.extension.ExtensionExecutor;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.stereotype.Component;

import static org.laokou.auth.common.constant.Constant.SCENARIO;
import static org.laokou.auth.common.constant.Constant.USE_CASE_CAPTCHA;
import static org.laokou.auth.common.constant.MqConstant.LAOKOU_CAPTCHA_TOPIC;
import static org.laokou.common.i18n.common.constant.EventType.CAPTCHA;
import static org.laokou.common.i18n.common.exception.AuthException.OAUTH2_SOURCE_NOT_EXIST;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class CaptchaSendCmdExe {

	private final DomainEventPublisher domainEventPublisher;

	private final ExtensionExecutor extensionExecutor;

	private final SourceGateway sourceGateway;

	private final SpringContextUtil springContextUtil;

	public void executeVoid(CaptchaSendCmd cmd) {
		// 校验
		extensionExecutor.executeVoid(CaptchaValidatorExtPt.class,
			BizScenario.valueOf(cmd.getTag(), USE_CASE_CAPTCHA, SCENARIO),
			extension -> extension.validate(cmd.getUuid()));
		// 发布发送验证码事件
		SendCaptchaEvent sendCaptchaEvent = new SendCaptchaEvent(cmd.getTag(), cmd.getUuid());
		sendCaptchaEvent.create(LAOKOU_CAPTCHA_TOPIC, cmd.getTag(), CAPTCHA, springContextUtil.getAppName(),
			getSourceName(cmd.getTenantId()), DateUtil.nowInstant(), IdGenerator.defaultSnowflakeId());
		domainEventPublisher.publishToCreate(sendCaptchaEvent);
	}

	private String getSourceName(Long tenantId) {
		SourceV sourceV = sourceGateway.getName(new UserE(tenantId));
		if (ObjectUtil.isNull(sourceV)) {
			throw new AuthException(OAUTH2_SOURCE_NOT_EXIST);
		}
		return sourceV.name();
	}

}
