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

package org.laokou.admin.command.pack;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.PackageGateway;
import org.laokou.admin.dto.packages.PackageRemoveCmd;
import org.springframework.stereotype.Component;

/**
 * 删除套餐执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class PackageRemoveCmdExe {

	private final PackageGateway packageGateway;

	/**
	 * 执行删除套餐.
	 * @param cmd 删除套餐参数
	 */
	public void executeVoid(PackageRemoveCmd cmd) {
		packageGateway.remove(cmd.getIds());
	}

}
