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

package org.laokou.admin.user.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.user.convertor.UserConvertor;
import org.laokou.admin.user.gateway.UserGateway;
import org.laokou.admin.user.gatewayimpl.database.UserMapper;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserDO;
import org.laokou.admin.user.model.UserE;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;

/**
 * 用户网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserGatewayImpl implements UserGateway {

	private final PasswordEncoder passwordEncoder;

	private final UserMapper userMapper;

	private final ExecutorService virtualThreadExecutor;

	@Override
	public void create(UserE userE) {
		UserDO userDO = UserConvertor.toDataObject(passwordEncoder, userE, true);
		userMapper.insert(userDO);
	}

	@Override
	public void update(UserE userE) {
		UserDO userDO = UserConvertor.toDataObject(passwordEncoder, userE, false);
		userDO.setVersion(userMapper.selectVersion(userE.getId()));
	}

	@Override
	public Mono<Void> delete(Long[] ids) {
		return Mono.fromCallable(() -> userMapper.deleteByIds(Arrays.asList(ids)))
			.subscribeOn(Schedulers.fromExecutorService(virtualThreadExecutor))
			.then();
	}

}
