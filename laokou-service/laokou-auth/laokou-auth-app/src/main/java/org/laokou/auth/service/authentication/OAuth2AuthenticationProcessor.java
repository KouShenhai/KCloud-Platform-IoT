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

package org.laokou.auth.service.authentication;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.ability.DomainService;
import org.laokou.auth.convertor.UserConvertor;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.service.extensionpoint.AuthParamValidatorExtPt;
import org.laokou.common.extension.BizScenario;
import org.laokou.common.extension.ExtensionExecutor;
import org.laokou.common.i18n.common.exception.ParamException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.security.utils.UserDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import static org.laokou.auth.model.AuthA.USE_CASE_AUTH;
import static org.laokou.common.i18n.common.constant.Constant.SCENARIO;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.*;

/**
 * @author laokou
 */
@RequiredArgsConstructor
@Component("authProcessor")
public class OAuth2AuthenticationProcessor {

	private final DomainService domainService;

	private final ExtensionExecutor extensionExecutor;

	public UsernamePasswordAuthenticationToken authenticationToken(AuthA auth, HttpServletRequest request) {
		try {
			// 校验参数
			extensionExecutor.executeVoid(AuthParamValidatorExtPt.class,
					BizScenario.valueOf(auth.getGrantType().getCode(), USE_CASE_AUTH, SCENARIO),
					extension -> extension.validate(auth));
			// 认证授权
			domainService.auth(auth);
			// 记录日志
			domainService.recordLog(DomainFactory.getLog(auth, request, null));
			// 登录成功，转换成用户对象【业务】
			UserDetail userDetail = UserConvertor.to(auth);
			// 认证成功，转换成认证对象【系统】
			return new UsernamePasswordAuthenticationToken(userDetail, userDetail.getUsername(),
					userDetail.getAuthorities());
		}
		catch (ParamException | SystemException e) {
			// 记录日志
			domainService.recordLog(DomainFactory.getLog(auth, request, e));
			// 抛出OAuth2认证异常，SpringSecurity全局异常处理并响应前端
			throw getOAuth2AuthenticationException(e.getCode(), e.getMsg(), ERROR_URL);
		}
		finally {
			// 清除数据源上下文
			DynamicDataSourceContextHolder.clear();
		}
	}

}
