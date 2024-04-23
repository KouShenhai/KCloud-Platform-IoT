/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.resource.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.resource.ResourceGetQry;
import org.laokou.admin.dto.resource.clientobject.ResourceCO;
import org.laokou.admin.gatewayimpl.database.ResourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.ResourceDO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查看资源执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ResourceGetQryExe {

	private final ResourceMapper resourceMapper;

	/**
	 * 执行查看资源.
	 * @param qry 查看资源参数
	 * @return 资源
	 */
	@DS(TENANT)
	public Result<ResourceCO> execute(ResourceGetQry qry) {
		return Result.ok(convert(resourceMapper.selectById(qry.getId())));
	}

	private ResourceCO convert(ResourceDO resourceDO) {
		return ResourceCO.builder()
			.id(resourceDO.getId())
			.title(resourceDO.getTitle())
			.remark(resourceDO.getRemark())
			.code(resourceDO.getCode())
			.status(resourceDO.getStatus())
			.url(resourceDO.getUrl())
			.build();
	}

}
