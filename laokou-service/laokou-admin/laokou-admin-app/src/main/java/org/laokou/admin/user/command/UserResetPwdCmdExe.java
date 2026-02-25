/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.user.ability.UserDomainService;
import org.laokou.admin.user.convertor.UserConvertor;
import org.laokou.admin.user.dto.UserResetPwdCmd;
import org.laokou.admin.user.factory.UserDomainFactory;
import org.laokou.admin.user.model.UserA;
import org.laokou.admin.user.model.enums.OperateType;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserResetPwdCmdExe {

	private final UserDomainService userDomainService;

	private final TransactionalUtils transactionalUtils;

	@CommandLog
	public void executeVoid(UserResetPwdCmd cmd) throws Exception {
		UserA userA = UserDomainFactory.createUserA()
			.create(UserConvertor.toEntity(cmd.getId(), cmd.getPassword()), OperateType.RESET_PWD);
		// 校验用户参数
		userA.checkUserParam();
		transactionalUtils.executeInTransaction(() -> userDomainService.updateUser(userA));
	}

}
