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

package org.laokou.common.sms.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.HttpUtil;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.RandomStringUtil;
import org.laokou.common.core.utils.TemplateUtil;
import org.laokou.common.i18n.dto.Cache;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.sms.config.SmsProperties;

import java.util.HashMap;
import java.util.Map;

import static org.laokou.common.i18n.common.constant.Constant.AUTHORIZATION;

/**
 * @author laokou
 */
@Slf4j
public class GYYSmsServiceImpl extends AbstractSmsServiceImpl {

	private static final Map<String, String> TEMPLATES = new HashMap<>(15);

	private static final Map<String, String> ERRORS = new HashMap<>(7);

	private static final String PARAMS_TEMPLATE = "**code**:${captcha},**minute**:${minute}";

	private static final String URL = "https://gyytz.market.alicloudapi.com/sms/smsSend";

	private static final String CODE = "code";

	private static final int OK = 0;

	static {
		TEMPLATES.put("908e94ccf08b4476ba6c876d13f084ad", "验证码：**code**，**minute**分钟内有效，请勿泄漏于他人！");
		TEMPLATES.put("63698e3463bd490dbc3edc46a20c55f5", "验证码：**code**，如非本人操作，请忽略本短信！");
		TEMPLATES.put("a09602b817fd47e59e7c6e603d3f088d", "验证码：**code**，**minute**分钟内有效，您正在进行注册，若非本人操作，请勿泄露。");
		TEMPLATES.put("305b8db49cdb4b18964ffe255188cb20", "尊敬的用户，您的注册验证码为：**code**，请勿泄漏于他人！");
		TEMPLATES.put("47990cc6d3ca42e2bbaad2dd06371238", "验证码：**code**，您正在进行注册操作，感谢您的支持！");
		TEMPLATES.put("96d32c69f15a4fbf89410bdba185cbdc", "验证码：**code**，您正在进行密码重置操作，如非本人操作，请忽略本短信！");
		TEMPLATES.put("29833afb9ae94f21a3f66af908d54627", "验证码：**code**，**minute**分钟内有效，您正在进行密码重置操作，请妥善保管账户信息。");
		TEMPLATES.put("8166a0ae27b7499fa8bdda1ed12a07bd", "验证码：**code**，您正在尝试修改登录密码，请妥善保管账户信息。");
		TEMPLATES.put("d6d95d8fb03c4246b944abcc1ea7efd8", "验证码：**code**，您正在进行身份验证，打死不要告诉别人哦！");
		TEMPLATES.put("f7e31e0d8c264a9c8d6e9756de806767", "验证码：**code**，您正在登录，若非本人操作，请勿泄露。");
		TEMPLATES.put("02551a4313154fe4805794ca069d70bf", "验证码：**code**，**minute**分钟内容有效，您正在登录，若非本人操作，请勿泄露。");
		TEMPLATES.put("dd7423a5749840f4ae6836ab31b7839e", "验证码：**code**，您正尝试异地登录，若非本人操作，请勿泄露。");
		TEMPLATES.put("81e8a442ea904694a37d2cec6ea6f2bc", "验证码：**code**，**minute**分钟内容有效，您正尝试异地登录，若非本人操作，请勿泄露。");
		TEMPLATES.put("9c16efaf248d41c59334e926634b4dc0", "验证码：**code**，您正在尝试变更重要信息，请妥善保管账户信息。");
		TEMPLATES.put("ea66d14c664649a69a19a6b47ba028db", "验证码：**code**，**minute**分钟内容有效，您正在尝试变更重要信息，请妥善保管账户信息。");

		ERRORS.put("1204", "签名未报备");
		ERRORS.put("1205", "签名不可用");
		ERRORS.put("1302", "短信内容包含敏感词");
		ERRORS.put("1304", "短信内容过长");
		ERRORS.put("1320", "模板ID不存在");
		ERRORS.put("1403", "手机号码不正确");
		ERRORS.put("1905", "验证未通过");

	}

	public GYYSmsServiceImpl(SmsProperties smsProperties) {
		super(smsProperties);
	}

	@Override
	@SneakyThrows
	public Result<String> send(String mobile, int minute, Cache cache) {
		String signId = smsProperties.getGyy().getSignId();
		String appcode = smsProperties.getGyy().getAppcode();
		String templateId = smsProperties.getGyy().getTemplateId();
		boolean isExit = TEMPLATES.containsKey(templateId);
		if (!isExit) {
			return Result.fail("S_Sms_TemplateIdNotExist", "模板不存在");
		}
		String captcha = RandomStringUtil.randomNumeric(6);
		Map<String, Object> param = Map.of("captcha", captcha, "minute", minute);
		String paramValue = TemplateUtil.getContent(PARAMS_TEMPLATE, param);
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		Map<String, String> headers = Map.of(AUTHORIZATION, "APPCODE " + appcode);
		// smsSignId（短信前缀）和templateId（短信模板），可登录国阳云控制台自助申请。参考文档：http://help.guoyangyun.com/Problem/Qm.html
		Map<String, String> params = Map.of("mobile", mobile, "param", paramValue, "smsSignId", signId, "templateId",
				templateId);
		log.info("模板内容：{}", TEMPLATES.get(templateId));
		/*
		 * 重要提示如下: HttpUtils请从
		 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/
		 * com/aliyun/api/gateway/demo/util/HttpUtils.java 下载 <p> 相应的依赖请参照
		 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
		 */
		String json = HttpUtil.doFormDataPost(URL, params, headers, true);
		JsonNode node = JacksonUtil.readTree(json).get(CODE);
		if (node.asInt() == OK) {
			// 写入缓存
			cache.set(captcha, (long) minute * 60 * 1000);
			return Result.ok(captcha);
		}
		log.info("错误信息：{}", json);
		return Result.fail(node.asText(), ERRORS.get(node.asText()));
	}

}
