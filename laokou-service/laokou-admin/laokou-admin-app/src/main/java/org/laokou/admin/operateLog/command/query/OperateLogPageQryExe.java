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
import org.laokou.admin.operateLog.dto.OperateLogPageQry;
import org.laokou.admin.operateLog.dto.clientobject.OperateLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.log.mapper.OperateLogMapper;
import org.laokou.common.log.mapper.OperateLogDO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分页查询操作日志请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OperateLogPageQryExe {

	private final OperateLogMapper operateLogMapper;

	public Result<Page<OperateLogCO>> execute(OperateLogPageQry qry) {
		try {
			DynamicDataSourceContextHolder.push("domain");
			List<OperateLogDO> list = operateLogMapper.selectObjectPage(qry);
			long total = operateLogMapper.selectObjectCount(qry);
			return Result.ok(Page.create(OperateLogConvertor.toClientObjects(list), total));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
