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

package org.laokou.admin.command.tenant;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.TenantGateway;
import org.laokou.admin.dto.tenant.TenantDownloadDatasourceCmd;
import org.springframework.stereotype.Component;

/**
 * 下载租户数据库压缩包执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TenantDownloadDatasourceCmdExe {

	private final TenantGateway tenantGateway;

	/**
	 * 执行下载租户数据库压缩包.
	 * @param cmd 下载租户数据库压缩包参数
	 */
	public void executeVoid(TenantDownloadDatasourceCmd cmd) {
		// tenantGateway.downloadDatasource(cmd.getId(), cmd.getResponse());
	}

}
