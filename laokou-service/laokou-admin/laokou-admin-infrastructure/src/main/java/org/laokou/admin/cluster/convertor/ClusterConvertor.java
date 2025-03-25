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

package org.laokou.admin.cluster.convertor;

import org.laokou.admin.cluster.dto.clientobject.ClusterCO;
import org.laokou.admin.cluster.gatewayimpl.database.dataobject.ClusterDO;
import org.laokou.admin.cluster.model.ClusterE;
import org.laokou.common.core.util.IdGenerator;

/**
 *
 * 集群转换器.
 *
 * @author laokou
 */
public class ClusterConvertor {

	public static ClusterDO toDataObject(ClusterE clusterE, boolean isInsert) {
		ClusterDO clusterDO = new ClusterDO();
		if (isInsert) {
			clusterDO.setId(IdGenerator.defaultSnowflakeId());
		}
		else {
			clusterDO.setId(clusterE.getId());
		}
		clusterDO.setName(clusterE.getName());
		clusterDO.setCode(clusterE.getCode());
		clusterDO.setRemark(clusterE.getRemark());
		return clusterDO;
	}

	public static ClusterCO toClientObject(ClusterDO clusterDO) {
		ClusterCO clusterCO = new ClusterCO();
		clusterCO.setName(clusterDO.getName());
		clusterCO.setCode(clusterDO.getCode());
		clusterCO.setRemark(clusterDO.getRemark());
		return clusterCO;
	}

	public static ClusterE toEntity(ClusterCO clusterCO) {
		ClusterE clusterE = new ClusterE();
		clusterE.setName(clusterCO.getName());
		clusterE.setCode(clusterCO.getCode());
		clusterE.setRemark(clusterCO.getRemark());
		return clusterE;
	}

}
