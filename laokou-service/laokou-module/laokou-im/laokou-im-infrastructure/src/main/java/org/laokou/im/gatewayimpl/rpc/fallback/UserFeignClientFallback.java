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

package org.laokou.im.gatewayimpl.rpc.fallback;

import org.laokou.common.i18n.dto.Result;
import org.laokou.im.dto.clientobject.UserProfileCO;
import org.laokou.im.gatewayimpl.rpc.UserFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
public class UserFeignClientFallback implements UserFeignClient {

	@Override
	public Result<UserProfileCO> getProfileV3(String Authorization) {
		return Result.fail("S_Feign_CallGetUserProfileFail", "获取个人信息失败，请联系管理员");
	}

}
