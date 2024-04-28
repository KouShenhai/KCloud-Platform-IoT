/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.api;

import org.laokou.common.i18n.dto.Option;
import org.laokou.admin.dto.dict.clientobject.DictCO;
import org.laokou.admin.dto.dict.*;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;

import java.util.List;

/**
 * 字典管理.
 *
 * @author laokou
 */
public interface DictsServiceI {

	/**
	 * 新增字典.
	 * @param cmd 新增字典参数
	 */
	void create(DictCreateCmd cmd);

	/**
	 * 修改字典.
	 * @param cmd 修改字典参数
	 */
	void modify(DictModifyCmd cmd);

	/**
	 * 根据IDS删除字典.
	 * @param cmd 根据IDS删除字典参数
	 */
	void remove(DictRemoveCmd cmd);

	/**
	 * 根据ID查看字典.
	 * @param qry 根据ID查看字典参数
	 * @return 字典
	 */
	Result<DictCO> findById(DictGetQry qry);

	/**
	 * 查询字典下拉框选择项列表.
	 * @param qry 查询字典下拉框选择项列表参数
	 * @return 字典下拉框选择项列表
	 */
	Result<List<Option>> optionList(DictOptionListQry qry);

	/**
	 * 查询字典列表.
	 * @param qry 查询字典列表参数
	 * @return 字典列表
	 */
	Result<Datas<DictCO>> findList(DictListQry qry);

}
