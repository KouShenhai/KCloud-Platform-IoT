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

package org.laokou.admin.command.log;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.common.utils.ExcelUtil;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.dto.log.LoginLogExportCmd;
import org.laokou.admin.dto.log.clientobject.LoginLogExcel;
import org.laokou.admin.gatewayimpl.database.LoginLogMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.template.TableTemplate;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.i18n.common.DatasourceConstant.BOOT_SYS_LOGIN_LOG;
import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 导出登录日志执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class LoginLogExportCmdExe {

	private final LoginLogMapper loginLogMapper;

	/**
	 * 执行导出登录日志.
	 * @param cmd 导出登录日志参数
	 */
	@DS(TENANT)
	@DataFilter(tableAlias = BOOT_SYS_LOGIN_LOG)
	public void executeVoid(LoginLogExportCmd cmd) {
		PageQuery pageQuery = cmd.time().ignore();
		List<String> dynamicTables = TableTemplate.getDynamicTables(pageQuery.getStartTime(), pageQuery.getEndTime(),
				BOOT_SYS_LOGIN_LOG);
		ExcelUtil.doExport(dynamicTables, cmd.getResponse(), convert(cmd), pageQuery, loginLogMapper,
				LoginLogExcel.class);
	}

	/**
	 * 构建登录日志数据对象.
	 * @param cmd 导出登录日志参数
	 * @return 登录日志数据模型
	 */
	private LoginLogDO convert(LoginLogExportCmd cmd) {
		LoginLogDO loginLogDO = new LoginLogDO();
		loginLogDO.setTenantId(UserUtil.getTenantId());
		loginLogDO.setUsername(cmd.getUsername());
		loginLogDO.setStatus(cmd.getStatus());
		return loginLogDO;
	}

}
