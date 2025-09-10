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

package org.laokou.admin.cluster.api;

import org.laokou.admin.cluster.dto.ClusterExportCmd;
import org.laokou.admin.cluster.dto.ClusterGetQry;
import org.laokou.admin.cluster.dto.ClusterImportCmd;
import org.laokou.admin.cluster.dto.ClusterModifyCmd;
import org.laokou.admin.cluster.dto.ClusterPageQry;
import org.laokou.admin.cluster.dto.ClusterRemoveCmd;
import org.laokou.admin.cluster.dto.ClusterSaveCmd;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.admin.cluster.dto.clientobject.ClusterCO;

/**
 *
 * 集群接口.
 *
 * @author laokou
 */
public interface ClustersServiceI {

	/**
	 * 保存集群.
	 * @param cmd 保存命令
	 */
	void saveCluster(ClusterSaveCmd cmd);

	/**
	 * 修改集群.
	 * @param cmd 修改命令
	 */
	void modifyCluster(ClusterModifyCmd cmd);

	/**
	 * 删除集群.
	 * @param cmd 删除命令
	 */
	void removeCluster(ClusterRemoveCmd cmd);

	/**
	 * 导入集群.
	 * @param cmd 导入命令
	 */
	void importCluster(ClusterImportCmd cmd);

	/**
	 * 导出集群.
	 * @param cmd 导出命令
	 */
	void exportCluster(ClusterExportCmd cmd);

	/**
	 * 分页查询集群.
	 * @param qry 分页查询请求
	 */
	Result<Page<ClusterCO>> pageCluster(ClusterPageQry qry);

	/**
	 * 查看集群.
	 * @param qry 查看请求
	 */
	Result<ClusterCO> getClusterById(ClusterGetQry qry);

}
