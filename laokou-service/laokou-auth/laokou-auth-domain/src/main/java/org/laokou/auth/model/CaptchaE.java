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

package org.laokou.auth.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.common.i18n.dto.Identifier;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author laokou
 */
@Entity
@Getter
public class CaptchaE {

	private final Long id;

	private String uuid;

	private SendCaptchaTypeEnum sendCaptchaTypeEnum;

	private String tenantCode;

	private Long tenantId;

	private final CaptchaParamValidator mailCaptchaParamValidator;

	private final CaptchaParamValidator mobileCaptchaParamValidator;

	public CaptchaE(IdGenerator idGenerator,
			@Qualifier("mailCaptchaParamValidator") CaptchaParamValidator mailCaptchaParamValidator,
			@Qualifier("mobileCaptchaParamValidator") CaptchaParamValidator mobileCaptchaParamValidator) {
		this.id = idGenerator.getId();
		this.mailCaptchaParamValidator = mailCaptchaParamValidator;
		this.mobileCaptchaParamValidator = mobileCaptchaParamValidator;
	}

	public void getTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public void checkTenantId() {
		if (ObjectUtils.isNull(this.tenantId)) {
			throw new BizException(OAuth2Constants.TENANT_NOT_EXIST);
		}
	}

	public void checkCaptchaParam() {
		switch (sendCaptchaTypeEnum) {
			case SEND_MAIL_CAPTCHA -> this.mailCaptchaParamValidator.validateCaptcha(this);
			case SEND_MOBILE_CAPTCHA -> this.mobileCaptchaParamValidator.validateCaptcha(this);
			default -> {
			}
		}
	}

}
