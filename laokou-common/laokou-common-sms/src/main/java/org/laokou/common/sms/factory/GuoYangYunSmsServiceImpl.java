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

package org.laokou.common.sms.factory;

import com.fasterxml.jackson.databind.JsonNode;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.laokou.common.core.utils.HttpUtil;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.core.utils.TemplateUtil;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.laokou.common.core.constant.BizConstant.AUTHORIZATION;
import static org.laokou.common.sms.exception.ErrorCode.*;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GuoYangYunSmsServiceImpl implements SmsService {

	private final GuoYangYunProperties guoYangYunProperties;

	private static final Map<String, String> TEMPLATE_MAP = new HashMap<>(15);

	private static final Map<String, String> ERROR_MAP = new HashMap<>(7);

	private static final Map<String, Integer> SMS_STATUS_CODE_MAP = Map.of("1204", SMS_SIGNATURE_NOT_REPORTED, "1205",
			SMS_SIGNATURE_NOT_AVAILABLE, "1302", SMS_CONTENT_CONTAINS_SENSITIVE, "1304", SMS_CONTENT_TOO_LONG, "1320",
			SMS_TEMPLATE_ID_NOT_EXIST, "1403", MOBILE_ERROR, "1905", SMS_VERIFICATION_FAILED);

	private final RedisUtil redisUtil;

	private static final String PARAMS_TEMPLATE = "**code**:${captcha},**minute**:${minute}";

	private static final String URL = "https://gyytz.market.alicloudapi.com/sms/smsSend";

	private static final int SUCCESS_CODE = 0;

	static {
		TEMPLATE_MAP.put("908e94ccf08b4476ba6c876d13f084ad", "验证码：**code**，**minute**分钟内有效，请勿泄漏于他人！");
		TEMPLATE_MAP.put("63698e3463bd490dbc3edc46a20c55f5", "验证码：**code**，如非本人操作，请忽略本短信！");
		TEMPLATE_MAP.put("a09602b817fd47e59e7c6e603d3f088d", "验证码：**code**，**minute**分钟内有效，您正在进行注册，若非本人操作，请勿泄露。");
		TEMPLATE_MAP.put("305b8db49cdb4b18964ffe255188cb20", "尊敬的用户，您的注册验证码为：**code**，请勿泄漏于他人！");
		TEMPLATE_MAP.put("47990cc6d3ca42e2bbaad2dd06371238", "验证码：**code**，您正在进行注册操作，感谢您的支持！");
		TEMPLATE_MAP.put("96d32c69f15a4fbf89410bdba185cbdc", "验证码：**code**，您正在进行密码重置操作，如非本人操作，请忽略本短信！");
		TEMPLATE_MAP.put("29833afb9ae94f21a3f66af908d54627", "验证码：**code**，**minute**分钟内有效，您正在进行密码重置操作，请妥善保管账户信息。");
		TEMPLATE_MAP.put("8166a0ae27b7499fa8bdda1ed12a07bd", "验证码：**code**，您正在尝试修改登录密码，请妥善保管账户信息。");
		TEMPLATE_MAP.put("d6d95d8fb03c4246b944abcc1ea7efd8", "验证码：**code**，您正在进行身份验证，打死不要告诉别人哦！");
		TEMPLATE_MAP.put("f7e31e0d8c264a9c8d6e9756de806767", "验证码：**code**，您正在登录，若非本人操作，请勿泄露。");
		TEMPLATE_MAP.put("02551a4313154fe4805794ca069d70bf", "验证码：**code**，**minute**分钟内容有效，您正在登录，若非本人操作，请勿泄露。");
		TEMPLATE_MAP.put("dd7423a5749840f4ae6836ab31b7839e", "验证码：**code**，您正尝试异地登录，若非本人操作，请勿泄露。");
		TEMPLATE_MAP.put("81e8a442ea904694a37d2cec6ea6f2bc", "验证码：**code**，**minute**分钟内容有效，您正尝试异地登录，若非本人操作，请勿泄露。");
		TEMPLATE_MAP.put("9c16efaf248d41c59334e926634b4dc0", "验证码：**code**，您正在尝试变更重要信息，请妥善保管账户信息。");
		TEMPLATE_MAP.put("ea66d14c664649a69a19a6b47ba028db", "验证码：**code**，**minute**分钟内容有效，您正在尝试变更重要信息，请妥善保管账户信息。");

		ERROR_MAP.put("1204", "签名未报备");
		ERROR_MAP.put("1205", "签名不可用");
		ERROR_MAP.put("1302", "短信内容包含敏感词");
		ERROR_MAP.put("1304", "短信内容过长");
		ERROR_MAP.put("1320", "模板ID不存在");
		ERROR_MAP.put("1403", "手机号码不正确");
		ERROR_MAP.put("1905", "验证未通过");
	}

	@Override
	public Boolean sendSms(String mobile) throws TemplateException, IOException {
		boolean mobileRegex = RegexUtil.mobileRegex(mobile);
		if (!mobileRegex) {
			throw new GlobalException(MOBILE_ERROR, MessageUtil.getMessage(MOBILE_ERROR));
		}
		String templateId = guoYangYunProperties.getTemplateId();
		String appcode = guoYangYunProperties.getAppcode();
		String signId = guoYangYunProperties.getSignId();
		// 验证模块id
		boolean exist = TEMPLATE_MAP.containsKey(templateId);
		if (!exist) {
			throw new GlobalException(SMS_TEMPLATE_ID_NOT_EXIST, MessageUtil.getMessage(SMS_TEMPLATE_ID_NOT_EXIST));
		}
		int minute = 5;
		String captcha = RandomStringUtils.randomNumeric(6);
		Map<String, Object> param = Map.of("captcha", captcha, "minute", minute);
		String paramValue = TemplateUtil.getContent(PARAMS_TEMPLATE, param);
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		Map<String, String> headers = Map.of(AUTHORIZATION, "APPCODE " + appcode);
		// smsSignId（短信前缀）和templateId（短信模板），可登录国阳云控制台自助申请。参考文档：http://help.guoyangyun.com/Problem/Qm.html
		Map<String, String> params = Map.of("mobile", mobile, "param", paramValue, "smsSignId", signId, "templateId",
				templateId);
		try {
			/**
			 * 重要提示如下: HttpUtils请从
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
			 * 下载
			 * <p>
			 * 相应的依赖请参照
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
			 */
			String json = HttpUtil.doPost(URL, params, headers);
			JsonNode node = JacksonUtil.readTree(json);
			node = node.get("code");
			String code = node.textValue();
			int statusCode = node.asInt();
			if (SUCCESS_CODE == statusCode) {
				String mobileCodeKey = RedisKeyUtil.getMobileCodeKey(mobile);
				redisUtil.set(mobileCodeKey, captcha, minute * 60);
			}
			else {
				log.error("错误信息：{}", ERROR_MAP.get(code));
				statusCode = SMS_STATUS_CODE_MAP.get(code);
				throw new GlobalException(statusCode, MessageUtil.getMessage(statusCode));
			}
		}
		catch (GlobalException ex) {
			throw ex;
		}
		catch (Exception e) {
			log.error("错误信息：{}", e.getMessage());
			throw e;
		}
		return true;
	}

}
