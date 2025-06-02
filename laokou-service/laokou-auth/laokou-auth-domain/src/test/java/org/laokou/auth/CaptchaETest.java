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

package org.laokou.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gateway.*;
import org.laokou.auth.model.CaptchaE;
import org.laokou.auth.model.CaptchaParamValidator;
import org.laokou.auth.model.SendCaptchaTypeEnum;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.laokou.auth.model.SendCaptchaTypeEnum.SEND_MAIL_CAPTCHA;
import static org.mockito.Mockito.*;

/**
 * @author laokou
 */
@SpringBootTest
class CaptchaETest {

	@MockitoBean
	private UserGateway userGateway;

	@MockitoBean
	private MenuGateway menuGateway;

	@MockitoBean
	private DeptGateway deptGateway;

	@MockitoBean
	private TenantGateway tenantGateway;

	@MockitoBean
	private LoginLogGateway loginLogGateway;

	@MockitoBean
	private NoticeLogGateway noticeLogGateway;

	@MockitoBean("mailCaptchaParamValidator")
	private CaptchaParamValidator mailCaptchaParamValidator;

	@MockitoBean("mobileCaptchaParamValidator")
	private CaptchaParamValidator mobileCaptchaParamValidator;

	@Test
	void testCheckTenantId() {
		// 构造租户
		when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 校验租户ID
		CaptchaE captchaE = getCaptcha(SEND_MAIL_CAPTCHA.getCode());
		// 获取租户ID
		Assertions.assertDoesNotThrow(() -> captchaE.getTenantId(tenantGateway.getTenantId(captchaE.getTenantCode())));
		Assertions.assertDoesNotThrow(captchaE::checkTenantId);
		// 校验调用次数
		verify(tenantGateway, times(1)).getTenantId("laokou");
	}

	private CaptchaE getCaptcha(String tag) {
		CaptchaE captchaE = DomainFactory.getCaptcha();
		captchaE.setId(1L);
		captchaE.setUuid("2413176044@qq.com");
		captchaE.setSendCaptchaTypeEnum(SendCaptchaTypeEnum.getByCode(tag));
		captchaE.setTenantCode("laokou");
		return captchaE;
	}

}
