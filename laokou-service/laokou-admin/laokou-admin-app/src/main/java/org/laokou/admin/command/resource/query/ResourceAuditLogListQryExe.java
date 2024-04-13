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

package org.laokou.admin.command.resource.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.AuditLogConvertor;
import org.laokou.admin.dto.resource.ResourceAuditLogListQry;
import org.laokou.admin.dto.resource.clientobject.AuditLogCO;
import org.laokou.admin.gatewayimpl.database.AuditLogMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.AuditLogDO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查询资源审批日志列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ResourceAuditLogListQryExe {

	private final AuditLogMapper auditLogMapper;

	private final AuditLogConvertor auditLogConvertor;

	/**
	 * 执行查询资源审批日志列表.
	 * @param qry 查询资源审批日志列表参数
	 * @return 资源审批日志
	 */
	@DS(TENANT)
	public Result<List<AuditLogCO>> execute(ResourceAuditLogListQry qry) {
		List<AuditLogDO> list = auditLogMapper.selectList(Wrappers.lambdaQuery(AuditLogDO.class)
			.eq(AuditLogDO::getBusinessId, qry.getId())
			.select(AuditLogDO::getId, AuditLogDO::getStatus, AuditLogDO::getComment, AuditLogDO::getCreateDate,
					AuditLogDO::getApprover)
			.orderByDesc(AuditLogDO::getId));
		return null;
		// return Result.of(auditLogConvertor.convertClientObjList(list));
	}

}
