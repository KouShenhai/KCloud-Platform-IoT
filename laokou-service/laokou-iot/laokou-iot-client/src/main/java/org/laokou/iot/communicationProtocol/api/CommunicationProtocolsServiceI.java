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

package org.laokou.iot.communicationProtocol.api;

import org.laokou.iot.communicationProtocol.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.communicationProtocol.dto.clientobject.CommunicationProtocolCO;

/**
 *
 * 通讯协议接口.
 *
 * @author laokou
 */
public interface CommunicationProtocolsServiceI {

	/**
	 * 保存通讯协议.
	 * @param cmd 保存命令
	 */
	void save(CommunicationProtocolSaveCmd cmd);

	/**
	 * 修改通讯协议.
	 * @param cmd 修改命令
	 */
	void modify(CommunicationProtocolModifyCmd cmd);

	/**
	 * 删除通讯协议.
	 * @param cmd 删除命令
	 */
	void remove(CommunicationProtocolRemoveCmd cmd);

	/**
	 * 导入通讯协议.
	 * @param cmd 导入命令
	 */
	void importI(CommunicationProtocolImportCmd cmd);

	/**
	 * 导出通讯协议.
	 * @param cmd 导出命令
	 */
	void export(CommunicationProtocolExportCmd cmd);

	/**
	 * 分页查询通讯协议.
	 * @param qry 分页查询请求
	 */
	Result<Page<CommunicationProtocolCO>> page(CommunicationProtocolPageQry qry);

	/**
	 * 查看通讯协议.
	 * @param qry 查看请求
	 */
	Result<CommunicationProtocolCO> getById(CommunicationProtocolGetQry qry);

}
