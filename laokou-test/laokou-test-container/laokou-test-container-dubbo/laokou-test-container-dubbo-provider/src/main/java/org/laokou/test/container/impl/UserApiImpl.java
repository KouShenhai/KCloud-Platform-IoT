/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.test.container.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.laokou.test.container.UserApi;

import static org.laokou.common.i18n.common.SysConstant.VERSION;

/**
 * @author laokou
 */
@DubboService(version = VERSION)
public class UserApiImpl implements UserApi {

	@Override
	public String getName() {
		return "k↑";
	}

}
