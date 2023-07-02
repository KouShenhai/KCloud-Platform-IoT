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
package org.laokou.auth.server.application.service.impl;

import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import jakarta.servlet.http.HttpServletRequest;
import jodd.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.client.constant.AuthConstant;
import org.laokou.auth.client.user.UserDetail;
import org.laokou.auth.client.vo.IdempotentToken;
import org.laokou.auth.client.vo.SecretInfoVO;
import org.laokou.auth.server.application.service.SysAuthApplicationService;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.common.easy.captcha.service.SysCaptchaService;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.i18n.core.StatusCode;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.jasypt.utils.RsaUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.tenant.service.SysTenantService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;
import java.awt.*;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

/**
 * @author laokou
 */
@Service
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SysAuthApplicationServiceImpl implements SysAuthApplicationService {

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	private final RedisUtil redisUtil;

	private final SysCaptchaService sysCaptchaService;

	private final SysTenantService sysTenantService;

	@Override
	public String captcha(HttpServletRequest request) {
		// 判断唯一标识是否为空
		String uuid = request.getParameter(AuthConstant.UUID);
		log.info("唯一标识：{}", uuid);
		if (StringUtil.isEmpty(uuid)) {
			throw new CustomException(StatusCode.IDENTIFIER_NOT_NULL);
		}
		// 三个参数分别为宽、高、位数
		Captcha captcha = new GifCaptcha(130, 48, 4);
		// 设置字体，有默认字体，可以不用设置
		captcha.setFont(new Font("Verdana", Font.PLAIN, 32));
		// 设置类型，纯数字、纯字母、字母数字混合
		captcha.setCharType(Captcha.TYPE_DEFAULT);
		sysCaptchaService.setCode(uuid, captcha.text());
		return captcha.toBase64();
	}

	@Override
	public Boolean logout(HttpServletRequest request) {
		try {
			String token = getToken(request);
			OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token,
					OAuth2TokenType.ACCESS_TOKEN);
			if (oAuth2Authorization == null) {
				return true;
			}
			UserDetail userDetail = (UserDetail) ((UsernamePasswordAuthenticationToken) Objects
					.requireNonNull(oAuth2Authorization.getAttribute(Principal.class.getName()))).getPrincipal();
			Long userId = userDetail.getId();
			// 清空
			oAuth2AuthorizationService.remove(oAuth2Authorization);
			// 用户key
			String userInfoKey = RedisKeyUtil.getUserInfoKey(token);
			redisUtil.delete(userInfoKey);
			// 菜单key
			String resourceTreeKey = RedisKeyUtil.getResourceTreeKey(userId);
			redisUtil.delete(resourceTreeKey);
			// 消息key
			String messageUnReadKey = RedisKeyUtil.getMessageUnReadKey(userId);
			redisUtil.delete(messageUnReadKey);
			// 踢出Key
			String userKillKey = RedisKeyUtil.getUserKillKey(token);
			redisUtil.delete(userKillKey);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<OptionVO> getOptionList() {
		return sysTenantService.getOptionList();
	}

	@Override
	public SecretInfoVO getSecretInfo() {
		return new SecretInfoVO(RsaUtil.getPublicKey());
	}

	@Override
	public IdempotentToken idempotentToken() {
		String token = getToken();
		String idempotentTokenKey = RedisKeyUtil.getIdempotentTokenKey(token);
		redisUtil.set(idempotentTokenKey, Constant.DEFAULT, RedisUtil.HOUR_ONE_EXPIRE);
		return new IdempotentToken(token);
	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader(Constant.AUTHORIZATION_HEAD);
		if (StringUtil.isEmpty(token)) {
			throw new CustomException("令牌不存在");
		}
		return token.substring(7);
	}

	private String getToken() {
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[20];
		random.nextBytes(bytes);
		return Base64.encodeToString(bytes);
	}

}
