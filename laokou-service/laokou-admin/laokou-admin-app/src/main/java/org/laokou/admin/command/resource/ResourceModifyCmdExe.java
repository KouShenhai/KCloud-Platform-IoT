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

package org.laokou.admin.command.resource;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.ResourceConvertor;
import org.laokou.admin.domain.gateway.ResourceGateway;
import org.laokou.admin.dto.resource.ResourceModifyCmd;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DSConstant.TENANT;

/**
 * 修改资源执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ResourceModifyCmdExe {

	private final ResourceGateway resourceGateway;

	private final ResourceConvertor resourceConvertor;

	/**
	 * 执行修改资源.
	 * @param cmd 修改资源参数
	 */
	@DS(TENANT)
	public void executeVoid(ResourceModifyCmd cmd) {
		resourceGateway.modify(resourceConvertor.toEntity(cmd.getResourceCO()));
	}

}
