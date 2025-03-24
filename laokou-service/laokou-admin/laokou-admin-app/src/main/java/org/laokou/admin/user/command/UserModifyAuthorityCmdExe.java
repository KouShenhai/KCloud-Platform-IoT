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

package org.laokou.admin.user.command;

import org.laokou.admin.user.ability.UserDomainService;
import org.laokou.admin.user.convertor.UserConvertor;
import org.laokou.admin.user.dto.UserModifyAuthorityCmd;
import org.laokou.admin.user.dto.clientobject.UserCO;
import org.laokou.admin.user.model.UserE;
import org.laokou.admin.user.service.extensionpoint.UserParamValidatorExtPt;
import org.laokou.common.domain.annotation.CommandLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author laokou
 */
@Component
public class UserModifyAuthorityCmdExe {

	private final UserDomainService userDomainService;

	@Autowired
	@Qualifier("modifyAuthorityUserParamValidator")
	private UserParamValidatorExtPt modifyAuthorityUserParamValidator;

	public UserModifyAuthorityCmdExe(UserDomainService userDomainService) {
		this.userDomainService = userDomainService;
	}

	@CommandLog
	public Flux<Void> executeVoid(UserModifyAuthorityCmd cmd) throws Exception {
		// 校验参数
		UserCO co = cmd.getCo();
		UserE userE = UserConvertor.toEntity(co, co.getId());
		modifyAuthorityUserParamValidator.validate(userE);
		return userDomainService.updateAuthority(userE);
	}

}
