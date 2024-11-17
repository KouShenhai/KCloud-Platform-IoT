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

package org.laokou.adapter.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.dto.Result;
import org.laokou.grpc.user.UserGetQry;
import org.laokou.grpc.user.UserServiceGrpc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laokou
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

	@GetMapping(produces = { "application/json;charset=utf-8" })
	public Result<?> getById() {
		UserGetQry userGetQry = UserGetQry.newBuilder().setId(1).build();
		org.laokou.grpc.user.Result result = userServiceBlockingStub.getUserById(userGetQry);
		log.info("{}", result.getMsg().split("\\\\")[0]);
		return Result.ok(1);
	}

}
