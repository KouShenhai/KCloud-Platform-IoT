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

package org.laokou.admin.operateLog.command;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.operateLog.convertor.OperateLogConvertor;
import org.laokou.admin.operateLog.dto.OperateLogExportCmd;
import org.laokou.admin.operateLog.dto.excel.OperateLogExcel;
import org.laokou.common.core.util.ResponseUtils;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.excel.util.ExcelUtils;
import org.laokou.common.log.mapper.OperateLogMapper;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

import static org.laokou.common.tenant.constant.DSConstants.DOMAIN;

/**
 * 导出操作日志命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OperateLogExportCmdExe {

	private final ExecutorService virtualThreadExecutor;

	private final OperateLogMapper operateLogMapper;

	@CommandLog
	public void executeVoid(OperateLogExportCmd cmd) {
		try {
			DynamicDataSourceContextHolder.push(DOMAIN);
			ExcelUtils.doExport("操作日志", "操作日志", ResponseUtils.getHttpServletResponse(), cmd, operateLogMapper,
					OperateLogExcel.class, OperateLogConvertor.INSTANCE, virtualThreadExecutor);
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
