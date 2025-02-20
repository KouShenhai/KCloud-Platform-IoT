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

import lombok.RequiredArgsConstructor;
import org.laokou.admin.user.ability.UserDomainService;
import org.laokou.admin.user.convertor.UserConvertor;
import org.laokou.admin.user.dto.UserSaveCmd;
import org.laokou.admin.user.gatewayimpl.database.UserMapper;
import org.laokou.admin.user.model.UserE;
import org.laokou.admin.user.service.extensionpoint.UserParamValidatorExtPt;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.extension.BizScenario;
import org.laokou.common.extension.ExtensionExecutor;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import static org.laokou.admin.common.constant.Constant.*;
import static org.laokou.common.i18n.common.constant.Constant.SCENARIO;

/**
 * 保存用户命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserSaveCmdExe {

	private final UserDomainService userDomainService;

	private final TransactionalUtil transactionalUtil;

	private final ExtensionExecutor extensionExecutor;

	private final UserMapper userMapper;

	public void executeVoid(UserSaveCmd cmd) {
		// 校验参数
		UserE userE = UserConvertor.toEntity(cmd.getCo());
		extensionExecutor.executeVoid(UserParamValidatorExtPt.class, BizScenario.valueOf(SAVE, USER, SCENARIO),
				extension -> {
					try {
						extension.validate(userE, userMapper);
					}
					catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		userE.setId(IdGenerator.defaultSnowflakeId());
		transactionalUtil.executeInTransaction(() -> {
			try {
				userDomainService.create(userE);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

}
