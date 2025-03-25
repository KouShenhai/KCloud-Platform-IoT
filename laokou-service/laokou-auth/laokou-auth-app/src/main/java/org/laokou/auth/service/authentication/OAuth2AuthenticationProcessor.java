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

package org.laokou.auth.service.authentication;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.blueconic.browscap.Capabilities;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.ability.DomainService;
import org.laokou.auth.convertor.UserConvertor;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.InfoV;
import org.laokou.auth.service.extensionpoint.AuthParamValidatorExtPt;
import org.laokou.common.core.util.AddressUtils;
import org.laokou.common.core.util.IdGenerator;
import org.laokou.common.core.util.IpUtils;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.extension.BizScenario;
import org.laokou.common.extension.ExtensionExecutor;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.common.exception.ParamException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.rocketmq.template.SendMessageType;
import org.laokou.common.security.util.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import static org.laokou.auth.common.constant.BizConstants.SCENARIO;
import static org.laokou.auth.model.AuthA.USE_CASE_AUTH;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.ERROR_URL;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.getOAuth2AuthenticationException;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
@Component("authProcessor")
final class OAuth2AuthenticationProcessor {

	private final DomainService domainService;

	private final ExtensionExecutor extensionExecutor;

	private final DomainEventPublisher rocketMQDomainEventPublisher;

	public UsernamePasswordAuthenticationToken authenticationToken(AuthA auth, HttpServletRequest request) {
		long eventId = IdGenerator.defaultSnowflakeId();
		try {
			// 校验参数
			extensionExecutor.executeVoid(AuthParamValidatorExtPt.class,
					BizScenario.valueOf(auth.getGrantTypeEnum().getCode(), USE_CASE_AUTH, SCENARIO),
					extension -> extension.validate(auth));
			// 认证授权
			domainService.auth(auth, getInfo(request));
			// 记录日志
			auth.recordLog(eventId, null);
			// 登录成功，转换成用户对象【业务】
			UserDetails userDetails = UserConvertor.to(auth);
			// 认证成功，转换成认证对象【系统】
			return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getUsername(),
					userDetails.getAuthorities());
		}
		catch (ParamException | BizException e) {
			// 记录日志
			auth.recordLog(eventId, e);
			// 抛出OAuth2认证异常，SpringSecurity全局异常处理并响应前端
			throw getOAuth2AuthenticationException(e.getCode(), e.getMsg(), ERROR_URL);
		}
		catch (Exception e) {
			log.error("未知错误，错误信息：{}", e.getMessage(), e);
			throw new SystemException("S_UnKnow_Error", e.getMessage(), e);
		}
		finally {
			// 清除数据源上下文
			DynamicDataSourceContextHolder.clear();
			// 发布事件
			auth.releaseEvents().forEach(item -> rocketMQDomainEventPublisher.publish(item, SendMessageType.ASYNC));
		}
	}

	private InfoV getInfo(HttpServletRequest request) throws Exception {
		Capabilities capabilities = RequestUtils.getCapabilities(request);
		String ip = IpUtils.getIpAddr(request);
		String address = AddressUtils.getRealAddress(ip);
		String os = capabilities.getPlatform();
		String browser = capabilities.getBrowser();
		return new InfoV(os, ip, address, browser);
	}

}
