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

package org.laokou.auth.command.query;

import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.dto.CaptchaGetQry;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.springframework.stereotype.Component;

import java.awt.Font;

/**
 * 获取验证码执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class CaptchaGetQryExe {

	private final RedisUtils redisUtils;

	/**
	 * 执行获取验证码.
	 * @param qry 获取验证码参数
	 * @return 验证码
	 */
	public Result<String> execute(CaptchaGetQry qry) {
		Captcha ca = generate();
		String captcha = ca.text();
		String base64 = ca.toBase64();
		String usernamePasswordAuthCaptchaKey = RedisKeyUtils.getUsernamePasswordAuthCaptchaKey(qry.getUuid());
		redisUtils.del(usernamePasswordAuthCaptchaKey);
		redisUtils.set(usernamePasswordAuthCaptchaKey, captcha, RedisUtils.FIVE_MINUTE_EXPIRE);
		return Result.ok(base64);
	}

	/**
	 * 生成验证码.
	 * @return 验证码
	 */
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
