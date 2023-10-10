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

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import org.laokou.common.mybatisplus.template.TableTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.laokou.admin.common.Constant.LOGIN_LOG;
import static org.laokou.common.mybatisplus.template.DsConstant.BOOT_SYS_LOGIN_LOG;
import static org.laokou.common.mybatisplus.template.DsConstant.BOOT_SYS_OPERATE_LOG;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class LogGatewayImpl implements LogGateway {

	private final OperateLogMapper operateLogMapper;

	private final LoginLogMapper loginLogMapper;

	private final ThreadPoolTaskExecutor taskExecutor;

	@Override
	@DataFilter(alias = BOOT_SYS_LOGIN_LOG)
	@SneakyThrows
	public Datas<LoginLog> loginList(LoginLog loginLog, User user, PageQuery pageQuery) {
		final PageQuery page = pageQuery.time().page().ignore(true);
		LoginLogDO loginLogDO = ConvertUtil.sourceToTarget(loginLog, LoginLogDO.class);
		loginLogDO.setTenantId(user.getTenantId());
		List<String> dynamicTables = TableTemplate.getDynamicTables(pageQuery.getStartTime(), pageQuery.getEndTime(),
				BOOT_SYS_LOGIN_LOG);
		CompletableFuture<List<LoginLogDO>> c1 = CompletableFuture.supplyAsync(() -> {
			try {
				DynamicDataSourceContextHolder.push(LOGIN_LOG);
				return loginLogMapper.getLoginLogListFilter(dynamicTables, loginLogDO, page);
			}
			finally {
				DynamicDataSourceContextHolder.clear();
			}
		}, taskExecutor);
		CompletableFuture<Integer> c2 = CompletableFuture.supplyAsync(() -> {
			try {
				DynamicDataSourceContextHolder.push(LOGIN_LOG);
				return loginLogMapper.getLoginLogCountFilter(dynamicTables, loginLogDO, page);
			}
			finally {
				DynamicDataSourceContextHolder.clear();
			}
		}, taskExecutor);
		CompletableFuture.allOf(c1, c2).join();
		Datas<LoginLog> datas = new Datas<>();
		datas.setTotal(c2.get());
		datas.setRecords(ConvertUtil.sourceToTarget(c1.get(), LoginLog.class));
		return datas;
	}

	@Override
	@DataFilter(alias = BOOT_SYS_OPERATE_LOG)
	public Datas<OperateLog> operateList(OperateLog operateLog, User user, PageQuery pageQuery) {
		IPage<OperateLogDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<OperateLogDO> newPage = operateLogMapper.getOperateListByTenantIdAndLikeModuleNameFilter(page,
				user.getTenantId(), operateLog.getModuleName(), operateLog.getStatus(), pageQuery);
		Datas<OperateLog> datas = new Datas<>();
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), OperateLog.class));
		datas.setTotal(newPage.getTotal());
		return datas;
	}

}
