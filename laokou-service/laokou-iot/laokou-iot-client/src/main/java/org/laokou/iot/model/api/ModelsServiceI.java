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

package org.laokou.iot.model.api;

import org.laokou.iot.model.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.model.dto.clientobject.ModelCO;

/**
 *
 * 模型接口.
 *
 * @author laokou
 */
public interface ModelsServiceI {

	/**
	 * 保存模型.
	 * @param cmd 保存命令
	 */
	void save(ModelSaveCmd cmd);

	/**
	 * 修改模型.
	 * @param cmd 修改命令
	 */
	void modify(ModelModifyCmd cmd);

	/**
	 * 删除模型.
	 * @param cmd 删除命令
	 */
	void remove(ModelRemoveCmd cmd);

	/**
	 * 导入模型.
	 * @param cmd 导入命令
	 */
	void importI(ModelImportCmd cmd);

	/**
	 * 导出模型.
	 * @param cmd 导出命令
	 */
	void export(ModelExportCmd cmd);

	/**
	 * 分页查询模型.
	 * @param qry 分页查询请求
	 */
	Result<Page<ModelCO>> page(ModelPageQry qry);

	/**
	 * 查看模型.
	 * @param qry 查看请求
	 */
	Result<ModelCO> getById(ModelGetQry qry);

}
