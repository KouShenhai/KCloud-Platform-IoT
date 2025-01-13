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

package org.laokou.infrastructure.gatewayimpl.rpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.grpc.user.Result;
import org.laokou.grpc.user.UserGetQry;
import org.laokou.grpc.user.UserServiceGrpc;
import org.laokou.infrastructure.convertor.UserConvertor;
import org.laokou.infrastructure.gatewayimpl.rpc.dataobject.UserDO;
import org.springframework.stereotype.Repository;

import static org.laokou.common.i18n.common.exception.StatusCode.OK;

/**
 * @author laokou
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserMapper {

	private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

	public UserDO getById(Long id) {
		UserGetQry userGetQry = UserGetQry.newBuilder().setId(id).build();
		log.info("开始调用GRPC");
		Result result = userServiceBlockingStub.getUserById(userGetQry);
		log.info("结束调用GRPC");
		if (ObjectUtil.equals(result.getCode(), OK)) {
			return UserConvertor.toDataObject(result.getData());
		}
		else {
			throw new SystemException("S_Request_RPCCallFailed",
					String.format("RPC调用失败，%s", result.getMsg().split("\\\\")[0]));
		}
	}

}
