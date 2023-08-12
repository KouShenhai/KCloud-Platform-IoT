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

package org.laokou.auth.event.handler;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.laokou.auth.dto.domainevent.LoginLogEvent;
import org.laokou.auth.gatewayimpl.database.LoginLogMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.utils.ConvertUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class LoginLogHandler implements ApplicationListener<LoginLogEvent> {

	private final LoginLogMapper loginLogMapper;

	@Transactional(rollbackFor = Exception.class)
	@DS(Constant.SHARDING_SPHERE_READWRITE)
	public void execute(LoginLogEvent event) {
		LoginLogDO logDO = ConvertUtil.sourceToTarget(event, LoginLogDO.class);
		loginLogMapper.insert(logDO);
	}

	@Override
	@Async
	public void onApplicationEvent(@NotNull LoginLogEvent event) {
		execute(event);
	}

}
