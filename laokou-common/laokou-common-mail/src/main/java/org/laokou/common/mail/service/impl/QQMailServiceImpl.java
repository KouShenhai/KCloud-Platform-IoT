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

package org.laokou.common.mail.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.RandomStringUtil;
import org.laokou.common.core.utils.TemplateUtil;
import org.laokou.common.i18n.dto.ApiLog;
import org.laokou.common.i18n.dto.Cache;
import org.springframework.boot.autoconfigure.mail.MailProperties;

import java.util.Map;

import static org.laokou.common.i18n.common.constant.Constant.FAIL;
import static org.laokou.common.i18n.common.constant.Constant.OK;
import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;

/**
 * @author laokou
 */
@Slf4j
public class QQMailServiceImpl extends AbstractMailServiceImpl {

	private static final String CAPTCHA_TEMPLATE = "验证码：${captcha}，${minute}分钟内容有效，您正在登录，若非本人操作，请勿泄露。";

	public QQMailServiceImpl(MailProperties mailProperties) {
		super(mailProperties);
	}

	@Override
	@SneakyThrows
	public void send(ApiLog apiLog, String mail, int minute, Cache cache) {
		String desc = "QQ邮箱";
		String subject = "验证码";
		String captcha = RandomStringUtil.randomNumeric(6);
		Map<String, Object> param = Map.of("captcha", captcha, "minute", minute);
		String content = TemplateUtil.getContent(CAPTCHA_TEMPLATE, param);
		String params = JacksonUtil.toJsonStr(Map.of("mail", mail, "content", content));
		try {
			// 发送邮件
			sendMail(subject, content, mail);
			// 写入缓存
			cache.set(captcha, (long) minute * 60 * 1000);
			apiLog.update(params, OK, EMPTY, desc);
		}
		catch (Exception e) {
			log.error("错误信息：{}", e.getMessage(), e);
			apiLog.update(params, FAIL, e.getMessage(), desc);
		}
	}

}
