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

package org.laokou.admin.cluster.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.cluster.api.ClustersServiceI;
import org.laokou.admin.cluster.command.*;
import org.laokou.admin.cluster.command.query.ClusterGetQryExe;
import org.laokou.admin.cluster.command.query.ClusterPageQryExe;
import org.laokou.admin.cluster.dto.*;
import org.laokou.admin.cluster.dto.clientobject.ClusterCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 *
 * 集群接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class ClustersServiceImpl implements ClustersServiceI {

	private final ClusterSaveCmdExe clusterSaveCmdExe;

	private final ClusterModifyCmdExe clusterModifyCmdExe;

	private final ClusterRemoveCmdExe clusterRemoveCmdExe;

	private final ClusterImportCmdExe clusterImportCmdExe;

	private final ClusterExportCmdExe clusterExportCmdExe;

	private final ClusterPageQryExe clusterPageQryExe;

	private final ClusterGetQryExe clusterGetQryExe;

	@Override
	public void saveCluster(ClusterSaveCmd cmd) {
		clusterSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyCluster(ClusterModifyCmd cmd) {
		clusterModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeCluster(ClusterRemoveCmd cmd) {
		clusterRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importCluster(ClusterImportCmd cmd) {
		clusterImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void exportCluster(ClusterExportCmd cmd) {
		clusterExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<ClusterCO>> pageCluster(ClusterPageQry qry) {
		return clusterPageQryExe.execute(qry);
	}

	@Override
	public Result<ClusterCO> getByIdCluster(ClusterGetQry qry) {
		return clusterGetQryExe.execute(qry);
	}

}
