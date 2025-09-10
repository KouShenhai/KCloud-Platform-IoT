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

package org.laokou.admin.loginLog.command;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.loginLog.convertor.LoginLogConvertor;
import org.laokou.admin.loginLog.dto.LoginLogExportCmd;
import org.laokou.admin.loginLog.dto.excel.LoginLogExcel;
import org.laokou.admin.loginLog.gatewayimpl.database.LoginLogMapper;
import org.laokou.common.core.util.ResponseUtils;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.excel.util.ExcelUtils;
import org.laokou.common.tenant.constant.DSConstants;
import org.springframework.stereotype.Component;

/**
 * 导出登录日志命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class LoginLogExportCmdExe {

	private final LoginLogMapper loginLogMapper;

	@CommandLog
	public void executeVoid(LoginLogExportCmd cmd) {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.DOMAIN);
			ExcelUtils.doExport("登录日志", "登录日志", ResponseUtils.getHttpServletResponse(), cmd, loginLogMapper,
					LoginLogExcel.class, LoginLogConvertor.INSTANCE);
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
