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

package org.laokou.admin.ip.api;

import org.laokou.admin.ip.dto.*;
import org.laokou.admin.ip.dto.clientobject.IpCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * IP接口.
 *
 * @author laokou
 */
public interface IpsServiceI {

	/**
	 * 保存IP.
	 * @param cmd 保存命令
	 */
	void save(IpSaveCmd cmd);

	/**
	 * 修改IP.
	 * @param cmd 修改命令
	 */
	void modify(IpModifyCmd cmd);

	/**
	 * 删除IP.
	 * @param cmd 删除命令
	 */
	void remove(IpRemoveCmd cmd);

	/**
	 * 导入IP.
	 * @param cmd 导入命令
	 */
	void importI(IpImportCmd cmd);

	/**
	 * 导出IP.
	 * @param cmd 导出命令
	 */
	void export(IpExportCmd cmd);

	/**
	 * 分页查询IP.
	 * @param qry 分页查询请求
	 */
	Result<Page<IpCO>> page(IpPageQry qry);

	/**
	 * 查看IP.
	 * @param qry 查看请求
	 */
	Result<IpCO> getById(IpGetQry qry);

}
