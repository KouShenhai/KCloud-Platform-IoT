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
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.user.convertor.UserConvertor;
import org.laokou.admin.user.gateway.UserGateway;
import org.laokou.admin.user.gatewayimpl.database.UserMapper;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserDO;
import org.laokou.admin.user.model.UserE;
import org.laokou.common.core.util.ThreadUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;

/**
 * 用户网关实现.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserGatewayImpl implements UserGateway {

	private final PasswordEncoder passwordEncoder;

	private final UserMapper userMapper;

	@Override
	public void create(UserE userE) {
		UserDO userDO = UserConvertor.toDataObject(passwordEncoder, userE, true);
		userMapper.insert(userDO);
	}

	@Override
	public void update(UserE userE) {
		UserDO userDO = UserConvertor.toDataObject(passwordEncoder, userE, false);
		userDO.setVersion(userMapper.selectVersion(userE.getId()));
		userMapper.updateById(userDO);
	}

	@Override
	public Mono<Void> delete(Long[] ids) {
		return Mono.fromCallable(() -> userMapper.deleteByIds(Arrays.asList(ids)))
			.subscribeOn(Schedulers.fromExecutor(ThreadUtils.newVirtualTaskExecutor()))
			.retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
				.maxBackoff(Duration.ofSeconds(30))
				.jitter(0.5)
				.doBeforeRetry(retry -> log.info("Retry attempt #{}", retry.totalRetriesInARow()))) // 增强型指数退避策略
			.then();
	}

}
