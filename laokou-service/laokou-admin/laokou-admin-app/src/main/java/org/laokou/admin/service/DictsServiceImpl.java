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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.DictsServiceI;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.dict.*;
import org.laokou.admin.dto.dict.clientobject.DictCO;
import org.laokou.admin.command.dict.DictRemoveCmdExe;
import org.laokou.admin.command.dict.DictCreateCmdExe;
import org.laokou.admin.command.dict.DictModifyCmdExe;
import org.laokou.admin.command.dict.query.DictGetQryExe;
import org.laokou.admin.command.dict.query.DictListQryExe;
import org.laokou.admin.command.dict.query.DictOptionListQryExe;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典管理.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DictsServiceImpl implements DictsServiceI {

	private final DictCreateCmdExe dictCreateCmdExe;

	private final DictModifyCmdExe dictModifyCmdExe;

	private final DictRemoveCmdExe dictRemoveCmdExe;

	private final DictOptionListQryExe dictOptionListQryExe;

	private final DictGetQryExe dictGetQryExe;

	private final DictListQryExe dictListQryExe;

	/**
	 * 新增字典.
	 * @param cmd 新增字典参数
	 * @return 新增结果
	 */
	@Override
	public Result<Boolean> insert(DictCreateCmd cmd) {
		return dictCreateCmdExe.execute(cmd);
	}

	/**
	 * 修改字典.
	 * @param cmd 修改字典参数
	 * @return 修改结果
	 */
	@Override
	public Result<Boolean> update(DictModifyCmd cmd) {
		return dictModifyCmdExe.execute(cmd);
	}

	/**
	 * 根据ID删除字典.
	 * @param cmd 根据ID删除字典参数
	 * @return 删除字典
	 */
	@Override
	public Result<Boolean> deleteById(DictRemoveCmd cmd) {
		return dictRemoveCmdExe.execute(cmd);
	}

	/**
	 * 根据ID查看字典.
	 * @param qry 根据ID查看字典参数
	 * @return 字典
	 */
	@Override
	public Result<DictCO> getById(DictGetQry qry) {
		return dictGetQryExe.execute(qry);
	}

	/**
	 * 查询字典下拉框选择项列表.
	 * @param qry 查询字典下拉框选择项列表参数
	 * @return 字典下拉框选择项列表
	 */
	@Override
	public Result<List<OptionCO>> optionList(DictOptionListQry qry) {
		return dictOptionListQryExe.execute(qry);
	}

	/**
	 * 查询字典列表.
	 * @param qry 查询字典列表参数
	 * @return 字典列表
	 */
	@Override
	public Result<Datas<DictCO>> list(DictListQry qry) {
		return dictListQryExe.execute(qry);
	}

}
