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
import org.laokou.admin.convertor.ResourceConvertor;
import org.laokou.admin.domain.gateway.ResourceGateway;
import org.laokou.admin.domain.resource.Resource;
import org.laokou.admin.dto.resource.ResourceListQry;
import org.laokou.admin.dto.resource.clientobject.ResourceCO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ResourceListQryExe {

	private final ResourceGateway resourceGateway;

	private final ResourceConvertor resourceConvertor;

	/**
	 *
	 * @param qry
	 * @return
	 */
	@DS(TENANT)
	public Result<Datas<ResourceCO>> execute(ResourceListQry qry) {
		Resource resource = ConvertUtil.sourceToTarget(qry, Resource.class);
		Datas<Resource> newPage = resourceGateway.list(resource, qry);
		Datas<ResourceCO> datas = new Datas<>();
		datas.setRecords(resourceConvertor.convertClientObjectList(newPage.getRecords()));
		datas.setTotal(newPage.getTotal());
		return Result.of(datas);
	}

}
