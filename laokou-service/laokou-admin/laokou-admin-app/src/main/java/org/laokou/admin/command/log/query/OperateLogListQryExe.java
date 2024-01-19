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

package org.laokou.admin.command.log.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.OperateLogConvertor;
import org.laokou.admin.domain.gateway.LogGateway;
import org.laokou.admin.domain.log.OperateLog;
import org.laokou.admin.dto.log.OperateLogListQry;
import org.laokou.admin.dto.log.clientobject.OperateLogCO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * 查询操作日志列表执行器.
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OperateLogListQryExe {

	private final LogGateway logGateway;

	private final OperateLogConvertor operateLogConvertor;

	/**
	 * 执行查询操作日志列表.
	 * @param qry 查询操作日志列表参数
	 * @return 操作日志列表
	 */
	@DS(TENANT)
	public Result<Datas<OperateLogCO>> execute(OperateLogListQry qry) {
		OperateLog operateLog = ConvertUtil.sourceToTarget(qry, OperateLog.class);
		Datas<OperateLog> newPage = logGateway.operateList(operateLog, qry);
		Datas<OperateLogCO> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(operateLogConvertor.convertClientObjectList(newPage.getRecords()));
		return Result.of(datas);
	}

}
