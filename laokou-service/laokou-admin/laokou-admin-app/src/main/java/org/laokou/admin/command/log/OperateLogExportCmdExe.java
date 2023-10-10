/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.admin.command.log;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.common.utils.ExcelUtil;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.dto.log.OperateLogExportCmd;
import org.laokou.admin.dto.log.clientobject.OperateLogExcel;
import org.laokou.admin.gatewayimpl.database.OperateLogMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.OperateLogDO;
import org.laokou.common.core.utils.SpringContextUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static org.laokou.common.mybatisplus.template.DsConstant.BOOT_SYS_OPERATE_LOG;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OperateLogExportCmdExe {

	@DataFilter(alias = BOOT_SYS_OPERATE_LOG)
	public void executeVoid(OperateLogExportCmd cmd) {
		OperateLogMapper operateLogMapper = SpringContextUtil.getBean(OperateLogMapper.class);
		ExcelUtil.export(Collections.emptyList(), cmd.getResponse(), buildOperateLog(cmd), cmd, operateLogMapper,
				OperateLogExcel.class);
	}

	private OperateLogDO buildOperateLog(OperateLogExportCmd cmd) {
		OperateLogDO operateLogDO = new OperateLogDO();
		operateLogDO.setTenantId(UserUtil.getTenantId());
		operateLogDO.setModuleName(cmd.getModuleName());
		operateLogDO.setStatus(cmd.getStatus());
		return operateLogDO;
	}

}
