// @formatter:off
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

package ${packageName}.${instanceName}.api;

import ${packageName}.${instanceName}.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import ${packageName}.${instanceName}.dto.clientobject.${className}CO;

/**
 *
 * ${comment}接口.
 *
 * @author ${author}
 */
public interface ${className}sServiceI {

    /**
     * 保存${comment}.
     * @param cmd 保存命令
     */
	void save(${className}SaveCmd cmd);

	/**
	 * 修改${comment}.
     * @param cmd 修改命令
	 */
	void modify(${className}ModifyCmd cmd);

	/**
	 * 删除${comment}.
     * @param cmd 删除命令
	 */
	void remove(${className}RemoveCmd cmd);

	/**
	 * 导入${comment}.
     * @param cmd 导入命令
	 */
	void importI(${className}ImportCmd cmd);

	/**
	 * 导出${comment}.
     * @param cmd 导出命令
	 */
	void export(${className}ExportCmd cmd);

	/**
	 * 分页查询${comment}.
     * @param qry 分页查询请求
	 */
	Result<Page<${className}CO>> page(${className}PageQry qry);

	/**
	 * 查看${comment}.
	 * @param qry 查看请求
	 */
	Result<${className}CO> getById(${className}GetQry qry);

}
// @formatter:on
