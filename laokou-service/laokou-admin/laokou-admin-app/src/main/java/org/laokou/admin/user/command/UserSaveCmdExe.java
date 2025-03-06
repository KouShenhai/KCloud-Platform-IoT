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

import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.user.ability.UserDomainService;
import org.laokou.admin.user.convertor.UserConvertor;
import org.laokou.admin.user.dto.UserSaveCmd;
import org.laokou.admin.user.model.UserE;
import org.laokou.admin.user.service.extensionpoint.UserParamValidatorExtPt;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;

/**
 * 保存用户命令执行器.
 *
 * @author laokou
 */
@Slf4j
@Component
public class UserSaveCmdExe {

	@Autowired
	@Qualifier("saveUserParamValidator")
	private UserParamValidatorExtPt saveUserParamValidator;

	private final UserDomainService userDomainService;

	private final TransactionalUtil transactionalUtil;

	private final ExecutorService virtualThreadExecutor;

	public UserSaveCmdExe(UserDomainService userDomainService, TransactionalUtil transactionalUtil,
			ExecutorService virtualThreadExecutor) {
		this.userDomainService = userDomainService;
		this.transactionalUtil = transactionalUtil;
		this.virtualThreadExecutor = virtualThreadExecutor;
	}

	public Flux<Void> executeVoid(UserSaveCmd cmd) throws Exception {
		// 校验参数
		UserE userE = UserConvertor.toEntity(cmd.getCo());
		saveUserParamValidator.validate(userE);
		userE.setId(IdGenerator.defaultSnowflakeId());
		return transactionalUtil.executeResultInTransaction(() -> userDomainService.create(userE)
			.subscribeOn(Schedulers.fromExecutorService(virtualThreadExecutor))
			.onErrorResume(e -> {
				log.error("新增用户信息失败，错误信息：{}", e.getMessage(), e);
				return Mono.error(new BizException("B_User_CreateError", e.getMessage(), e));
			})).onErrorResume(Mono::error).subscribeOn(Schedulers.fromExecutorService(virtualThreadExecutor));
	}

}
