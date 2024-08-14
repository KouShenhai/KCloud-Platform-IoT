/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.ClustersServiceI;
import org.laokou.admin.dto.cluster.ClusterInstanceListQry;
import org.laokou.admin.dto.cluster.ClusterServiceListQry;
import org.laokou.admin.dto.cluster.clientobject.ClusterInstanceCO;
import org.laokou.admin.dto.cluster.clientobject.ClusterServiceCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author laokou
 */
@RestController
@Tag(name = "ClustersController", description = "集群管理")
@RequiredArgsConstructor
@RequestMapping("v1/clusters")
public class ClustersController {

	private final ClustersServiceI clustersServiceI;

	@TraceLog
	@PostMapping("service-list")
	@Operation(summary = "集群管理", description = "查询服务列表")
	@PreAuthorize("hasAuthority('clusters:service-list')")
	public Result<Page<ClusterServiceCO>> findServiceList(@RequestBody ClusterServiceListQry qry) {
		return clustersServiceI.findServiceList(qry);
	}

	@TraceLog
	@PostMapping("instance-list")
	@Operation(summary = "集群管理", description = "查询实例列表")
	@PreAuthorize("hasAuthority('clusters:instance-list')")
	public Result<Page<ClusterInstanceCO>> findInstanceList(@RequestBody ClusterInstanceListQry qry) {
		return clustersServiceI.findInstanceList(qry);
	}

}
