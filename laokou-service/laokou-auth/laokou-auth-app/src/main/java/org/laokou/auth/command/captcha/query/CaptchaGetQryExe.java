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

package org.laokou.auth.command.captcha.query;

import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.domain.gateway.CaptchaGateway;
import org.laokou.auth.dto.captcha.CaptchaGetQry;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;
import java.awt.*;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class CaptchaGetQryExe {

	private final CaptchaGateway captchaGateway;

	public Result<String> execute(CaptchaGetQry qry) {
		String uuid = qry.getUuid();
		Captcha captcha = generate();
		String code = captcha.text();
		String base64 = captcha.toBase64();
		captchaGateway.set(uuid, code);
		return Result.of(base64);
	}

	private Captcha generate() {
		// 三个参数分别为宽、高、位数
		Captcha captcha = new GifCaptcha(130, 48, 4);
		// 设置字体，有默认字体，可以不用设置
		captcha.setFont(new Font("Verdana", Font.PLAIN, 32));
		// 设置类型，纯数字、纯字母、字母数字混合
		captcha.setCharType(Captcha.TYPE_DEFAULT);
		return captcha;
	}

}
