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

package org.laokou.app.service;

import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.grpc.user.Result;
import org.laokou.grpc.user.User;
import org.laokou.grpc.user.UserGetQry;
import org.laokou.grpc.user.UserServiceGrpc;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

	@Override
	public void getUserById(UserGetQry qry, io.grpc.stub.StreamObserver<Result> responseObserver) {
		Result result;
		if (ObjectUtil.equals(qry.getId(), 1L)) {
			User user = User.newBuilder().setId(1).setUsername("laokou").setPassword("laokou123").build();
			result = Result.newBuilder().setMsg("请求成功").setCode("OK").setData(user).build();
		}
		else {
			User user = User.newBuilder().build();
			result = Result.newBuilder().setMsg("用户不存在").setCode("B_User_NotExist").setData(user).build();
		}
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}

}
