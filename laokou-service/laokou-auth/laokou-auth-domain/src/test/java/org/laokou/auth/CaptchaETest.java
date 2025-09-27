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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gateway.DeptGateway;
import org.laokou.auth.gateway.LoginLogGateway;
import org.laokou.auth.gateway.MenuGateway;
import org.laokou.auth.gateway.NoticeLogGateway;
import org.laokou.auth.gateway.OssLogGateway;
import org.laokou.auth.gateway.TenantGateway;
import org.laokou.auth.gateway.UserGateway;
import org.laokou.auth.model.CaptchaE;
import org.laokou.auth.model.CaptchaParamValidator;
import org.laokou.auth.model.SendCaptchaTypeEnum;
import org.laokou.common.i18n.dto.IdGenerator;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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
	private OssLogGateway ossLogGateway;

	@MockitoBean
	private NoticeLogGateway noticeLogGateway;

	@MockitoBean
	private IdGenerator idGenerator;

	@MockitoBean("mailCaptchaParamValidator")
	private CaptchaParamValidator mailCaptchaParamValidator;

	@MockitoBean("mobileCaptchaParamValidator")
	private CaptchaParamValidator mobileCaptchaParamValidator;

	@Test
	void test_checkTenantId() {
		// 构造租户
		Mockito.when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 校验租户ID
		CaptchaE captchaE = getCaptcha(SendCaptchaTypeEnum.SEND_MAIL_CAPTCHA.getCode());
		// 获取租户ID
		Assertions.assertThatNoException()
			.isThrownBy(() -> captchaE.getTenantId(tenantGateway.getTenantId(captchaE.getTenantCode())));
		Assertions.assertThatNoException().isThrownBy(captchaE::checkTenantId);
		// 校验调用次数
		Mockito.verify(tenantGateway, Mockito.times(1)).getTenantId("laokou");
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
