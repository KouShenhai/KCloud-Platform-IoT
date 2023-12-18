/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.AuditLogConvertor;
import org.laokou.admin.dto.resource.ResourceAuditLogListQry;
import org.laokou.admin.dto.resource.clientobject.AuditLogCO;
import org.laokou.admin.gatewayimpl.database.AuditLogMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.AuditLogDO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ResourceAuditLogListQryExe {

	private final AuditLogMapper auditLogMapper;

	private final AuditLogConvertor auditLogConvertor;

	public Result<Datas<AuditLogCO>> execute(ResourceAuditLogListQry qry) {
		Page<AuditLogDO> page = new Page<>(qry.getPageNum(), qry.getPageSize());
		page = auditLogMapper.selectPage(page, Wrappers.lambdaQuery(AuditLogDO.class)
			.select(AuditLogDO::getStatus, AuditLogDO::getComment, AuditLogDO::getApprover));
		return null;
	}

}
