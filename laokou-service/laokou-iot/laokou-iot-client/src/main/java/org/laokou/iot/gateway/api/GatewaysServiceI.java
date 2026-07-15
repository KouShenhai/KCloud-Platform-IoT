/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.gateway.api;

import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.gateway.dto.GatewayExportCmd;
import org.laokou.iot.gateway.dto.GatewayGetQry;
import org.laokou.iot.gateway.dto.GatewayImportCmd;
import org.laokou.iot.gateway.dto.GatewayModifyCmd;
import org.laokou.iot.gateway.dto.GatewayPageQry;
import org.laokou.iot.gateway.dto.GatewayRemoveCmd;
import org.laokou.iot.gateway.dto.GatewaySaveCmd;
import org.laokou.iot.gateway.dto.clientobject.GatewayCO;

/**
 *
 * 网关接口.
 *
 * @author laokou
 */
public interface GatewaysServiceI {

	/**
	 * 保存网关.
	 * @param cmd 保存命令
	 */
	void saveGateway(GatewaySaveCmd cmd);

	/**
	 * 修改网关.
	 * @param cmd 修改命令
	 */
	void modifyGateway(GatewayModifyCmd cmd);

	/**
	 * 删除网关.
	 * @param cmd 删除命令
	 */
	void removeGateway(GatewayRemoveCmd cmd);

	/**
	 * 导入网关.
	 * @param cmd 导入命令
	 */
	void importGateway(GatewayImportCmd cmd);

	/**
	 * 导出网关.
	 * @param cmd 导出命令
	 */
	void exportGateway(GatewayExportCmd cmd);

	/**
	 * 分页查询网关.
	 * @param qry 分页查询请求
	 */
	Result<Page<GatewayCO>> pageGateway(GatewayPageQry qry);

	/**
	 * 查看网关.
	 * @param qry 查看请求
	 */
	Result<GatewayCO> getGatewayById(GatewayGetQry qry);

}
