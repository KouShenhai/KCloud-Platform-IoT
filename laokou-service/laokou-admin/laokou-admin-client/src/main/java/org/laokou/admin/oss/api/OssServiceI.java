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

package org.laokou.admin.oss.api;

import org.laokou.admin.oss.dto.*;
import org.laokou.admin.oss.dto.clientobject.OssCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * OSS接口.
 *
 * @author laokou
 */
public interface OssServiceI {

	/**
	 * 保存OSS.
	 *
	 * @param cmd 保存命令
	 */
	void save(OssSaveCmd cmd);

	/**
	 * 修改OSS.
	 *
	 * @param cmd 修改命令
	 */
	void modify(OssModifyCmd cmd);

	/**
	 * 删除OSS.
	 *
	 * @param cmd 删除命令
	 */
	void remove(OssRemoveCmd cmd);

	/**
	 * 导入OSS.
	 *
	 * @param cmd 导入命令
	 */
	void importI(OssImportCmd cmd);

	/**
	 * 导出OSS.
	 *
	 * @param cmd 导出命令
	 */
	void export(OssExportCmd cmd);

	/**
	 * 分页查询OSS.
	 *
	 * @param qry 分页查询请求
	 */
	Result<Page<OssCO>> page(OssPageQry qry);

	/**
	 * 查看OSS.
	 *
	 * @param qry 查看请求
	 */
	Result<OssCO> getById(OssGetQry qry);

}
