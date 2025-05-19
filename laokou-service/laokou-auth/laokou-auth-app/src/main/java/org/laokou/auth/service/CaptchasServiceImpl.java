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

package org.laokou.auth.service;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.api.CaptchasServiceI;
import org.laokou.auth.command.CaptchaSendCmdExe;
import org.laokou.auth.command.query.CaptchaGetQryExe;
import org.laokou.auth.dto.CaptchaGetQry;
import org.laokou.auth.dto.CaptchaSendCmd;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 验证码.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class CaptchasServiceImpl implements CaptchasServiceI {

	private final CaptchaGetQryExe captchaGetQryExe;

	private final CaptchaSendCmdExe captchaSendCmdExe;

	/**
	 * 获取验证码.
	 * @param qry 获取验证码参数
	 * @return 验证码
	 */
	@Override
	public Result<String> getByUuid(CaptchaGetQry qry) {
		return captchaGetQryExe.execute(qry);
	}

	@Override
	public void sendByUuid(CaptchaSendCmd cmd) {
		captchaSendCmdExe.executeVoid(cmd);
	}

}
