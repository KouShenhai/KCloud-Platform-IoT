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

package org.laokou.generator.info.api;

import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.generator.info.dto.InfoExportCmd;
import org.laokou.generator.info.dto.InfoGetQry;
import org.laokou.generator.info.dto.InfoImportCmd;
import org.laokou.generator.info.dto.InfoModifyCmd;
import org.laokou.generator.info.dto.InfoPageQry;
import org.laokou.generator.info.dto.InfoRemoveCmd;
import org.laokou.generator.info.dto.InfoSaveCmd;
import org.laokou.generator.info.dto.clientobject.InfoCO;

/**
 *
 * 代码生成器信息接口.
 *
 * @author laokou
 */
public interface InfosServiceI {

	/**
	 * 保存代码生成器信息.
	 * @param cmd 保存命令
	 */
	void saveInfo(InfoSaveCmd cmd);

	/**
	 * 修改代码生成器信息.
	 * @param cmd 修改命令
	 */
	void modifyInfo(InfoModifyCmd cmd);

	/**
	 * 删除代码生成器信息.
	 * @param cmd 删除命令
	 */
	void removeInfo(InfoRemoveCmd cmd);

	/**
	 * 导入代码生成器信息.
	 * @param cmd 导入命令
	 */
	void importInfo(InfoImportCmd cmd);

	/**
	 * 导出代码生成器信息.
	 * @param cmd 导出命令
	 */
	void exportInfo(InfoExportCmd cmd);

	/**
	 * 分页查询代码生成器信息.
	 * @param qry 分页查询请求
	 */
	Result<Page<InfoCO>> pageInfo(InfoPageQry qry);

	/**
	 * 查看代码生成器信息.
	 * @param qry 查看请求
	 */
	Result<InfoCO> getInfoById(InfoGetQry qry);

}
