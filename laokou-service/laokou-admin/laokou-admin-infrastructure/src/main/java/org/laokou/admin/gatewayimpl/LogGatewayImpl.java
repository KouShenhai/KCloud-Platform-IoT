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

package org.laokou.admin.gatewayimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.LogGateway;
import org.laokou.admin.domain.log.LoginLog;
import org.laokou.admin.domain.log.OperateLog;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.gatewayimpl.database.LoginLogMapper;
import org.laokou.admin.gatewayimpl.database.OperateLogMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.admin.gatewayimpl.database.dataobject.OperateLogDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.springframework.stereotype.Component;

import static org.laokou.admin.common.Constant.SHARDING_SPHERE_READWRITE;
import static org.laokou.admin.common.DsConstant.BOOT_SYS_LOGIN_LOG;
import static org.laokou.admin.common.DsConstant.BOOT_SYS_OPERATE_LOG;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class LogGatewayImpl implements LogGateway {

	private final OperateLogMapper operateLogMapper;

	private final LoginLogMapper loginLogMapper;

	@Override
	@DataFilter(alias = BOOT_SYS_LOGIN_LOG)
	@DS(SHARDING_SPHERE_READWRITE)
	public Datas<LoginLog> loginList(LoginLog loginLog, User user, PageQuery pageQuery) {
		IPage<LoginLogDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<LoginLogDO> newPage = loginLogMapper.getLoginLogByTenantIdAndLikeUsernameFilter(page, user.getTenantId(),
				loginLog.getUsername(), loginLog.getStatus(), pageQuery.getSqlFilter());
		Datas<LoginLog> datas = new Datas<>();
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), LoginLog.class));
		datas.setTotal(newPage.getTotal());
		return datas;
	}

	@Override
	@DataFilter(alias = BOOT_SYS_OPERATE_LOG)
	public Datas<OperateLog> operateList(OperateLog operateLog, User user, PageQuery pageQuery) {
		IPage<OperateLogDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<OperateLogDO> newPage = operateLogMapper.getOperateListByTenantIdAndLikeModuleNameFilter(page,
				user.getTenantId(), operateLog.getModuleName(), operateLog.getStatus(), pageQuery.getSqlFilter());
		Datas<OperateLog> datas = new Datas<>();
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), OperateLog.class));
		datas.setTotal(newPage.getTotal());
		return datas;
	}

}
