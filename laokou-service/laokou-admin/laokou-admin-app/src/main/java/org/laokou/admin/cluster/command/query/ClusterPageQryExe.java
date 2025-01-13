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

package org.laokou.admin.cluster.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.cluster.convertor.ClusterConvertor;
import org.laokou.admin.cluster.dto.ClusterPageQry;
import org.laokou.admin.cluster.dto.clientobject.ClusterCO;
import org.laokou.admin.cluster.gatewayimpl.database.ClusterMapper;
import org.laokou.admin.cluster.gatewayimpl.database.dataobject.ClusterDO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分页查询集群请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ClusterPageQryExe {

	private final ClusterMapper clusterMapper;

	public Result<Page<ClusterCO>> execute(ClusterPageQry qry) {
		List<ClusterDO> list = clusterMapper.selectObjectPage(qry);
		long total = clusterMapper.selectObjectCount(qry);
		return Result.ok(Page.create(list.stream().map(ClusterConvertor::toClientObject).toList(), total));
	}

}
