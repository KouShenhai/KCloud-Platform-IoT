/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	@PostMapping("list")
	@Operation(summary = "集群管理", description = "查询节点列表")
	@PreAuthorize("hasAuthority('clusters:list')")
	public Result<Datas<String>> list() {
		return null;
	}

	@TraceLog
	@PostMapping("{serviceId}/instance-list")
	@Operation(summary = "集群管理", description = "查询节点列表")
	@PreAuthorize("hasAuthority('clusters:instance-list')")
	public Result<Datas<String>> instanceList(@PathVariable("serviceId") String serviceId) {
		return null;
	}

}
