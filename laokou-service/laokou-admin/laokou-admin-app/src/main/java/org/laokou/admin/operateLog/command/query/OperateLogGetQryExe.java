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

package org.laokou.admin.operateLog.command.query;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.operateLog.convertor.OperateLogConvertor;
import org.laokou.admin.operateLog.dto.OperateLogGetQry;
import org.laokou.admin.operateLog.dto.clientobject.OperateLogCO;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.log.mapper.OperateLogMapper;
import org.laokou.common.tenant.constant.DSConstants;
import org.springframework.stereotype.Component;

/**
 * 查看操作日志请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OperateLogGetQryExe {

	private final OperateLogMapper operateLogMapper;

	public Result<OperateLogCO> execute(OperateLogGetQry qry) {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.DOMAIN);
			return Result.ok(OperateLogConvertor.toClientObject(operateLogMapper.selectById(qry.getId())));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
