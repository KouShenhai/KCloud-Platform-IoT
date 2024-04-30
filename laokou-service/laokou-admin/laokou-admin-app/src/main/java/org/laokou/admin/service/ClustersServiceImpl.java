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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.ClustersServiceI;
import org.laokou.admin.command.cluster.query.ClusterInstanceListQryExe;
import org.laokou.admin.command.cluster.query.ClusterServiceListQryExe;
import org.laokou.admin.dto.cluster.ClusterInstanceListQry;
import org.laokou.admin.dto.cluster.ClusterServiceListQry;
import org.laokou.admin.dto.cluster.clientobject.ClusterInstanceCO;
import org.laokou.admin.dto.cluster.clientobject.ClusterServiceCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 集群管理.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class ClustersServiceImpl implements ClustersServiceI {

	private final ClusterServiceListQryExe clusterServiceListQryExe;

	private final ClusterInstanceListQryExe clusterInstanceListQryExe;

	/**
	 * 查询服务列表.
	 * @param qry 服务列表查询参数
	 * @return 服务列表
	 */
	@Override
	public Result<Datas<ClusterServiceCO>> findServiceList(ClusterServiceListQry qry) {
		return clusterServiceListQryExe.execute(qry);
	}

	/**
	 * 查询服务实例列表.
	 * @param qry 服务实例查询参数
	 * @return 服务实例列表
	 */
	@Override
	public Result<Datas<ClusterInstanceCO>> findInstanceList(ClusterInstanceListQry qry) {
		return clusterInstanceListQryExe.execute(qry);
	}

}
