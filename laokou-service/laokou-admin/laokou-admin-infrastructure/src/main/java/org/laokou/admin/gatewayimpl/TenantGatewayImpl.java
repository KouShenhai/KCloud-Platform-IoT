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

package org.laokou.admin.gatewayimpl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.config.DefaultConfigProperties;
import org.laokou.admin.convertor.TenantConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.TenantGateway;
import org.laokou.admin.domain.tenant.Tenant;
import org.laokou.admin.domain.user.SuperAdmin;
import org.laokou.admin.gatewayimpl.database.DeptMapper;
import org.laokou.admin.gatewayimpl.database.TenantMapper;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.admin.gatewayimpl.database.dataobject.TenantDO;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.Constant.COMMA;
import static org.laokou.common.i18n.common.Constant.DEFAULT;
import static org.laokou.common.mybatisplus.constant.DsConstant.BOOT_SYS_TENANT;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TenantGatewayImpl implements TenantGateway {

	private final TenantMapper tenantMapper;

	private final TransactionalUtil transactionalUtil;

	private final PasswordEncoder passwordEncoder;

	private final UserMapper userMapper;

	private final DeptMapper deptMapper;

	private final TenantConvertor tenantConvertor;

	private final DefaultConfigProperties defaultConfigProperties;

	private static final String TENANT_USERNAME = "tenant";

	private static final String TENANT_PASSWORD = "tenant123";

	@Override
	@DSTransactional(rollbackFor = Exception.class)
	public Boolean insert(Tenant tenant) {
		TenantDO tenantDO = tenantConvertor.toDataObject(tenant);
		tenantDO.setLabel(defaultConfigProperties.getTenantPrefix() + tenantMapper.maxLabelNum());
		return insertTenant(tenantDO);
	}

	@Override
	@DataFilter(alias = BOOT_SYS_TENANT)
	public Datas<Tenant> list(Tenant tenant, PageQuery pageQuery) {
		IPage<TenantDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<TenantDO> newPage = tenantMapper.getTenantListFilter(page, tenant.getName(), pageQuery);
		Datas<Tenant> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(tenantConvertor.convertEntityList(newPage.getRecords()));
		return datas;
	}

	@Override
	public Tenant getById(Long id) {
		return tenantConvertor.convertEntity(tenantMapper.selectById(id));
	}

	@Override
	public Boolean update(Tenant tenant) {
		TenantDO tenantDO = tenantConvertor.toDataObject(tenant);
		tenantDO.setVersion(tenantMapper.getVersion(tenant.getId(), TenantDO.class));
		return updateTenant(tenantDO);
	}

	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return tenantMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.error(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private Boolean insertTenant(TenantDO tenantDO) {
		tenantMapper.insertTable(tenantDO);
		insertUser(tenantDO.getId());
		return true;
	}

	private Boolean updateTenant(TenantDO tenantDO) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return tenantMapper.updateById(tenantDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.error(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private void insertUser(Long tenantId) {
		DeptDO deptDO = new DeptDO();
		deptDO.setTenantId(tenantId);
		insertDept(deptDO);
		insertUser(deptDO, tenantId);
	}

	private void insertUser(DeptDO deptDO, Long tenantId) {
		try {
			// 初始化超级管理员
			UserDO userDO = new UserDO();
			userDO.setUsername(TENANT_USERNAME);
			userDO.setTenantId(tenantId);
			userDO.setPassword(passwordEncoder.encode(TENANT_PASSWORD));
			userDO.setSuperAdmin(SuperAdmin.YES.ordinal());
			userDO.setDeptId(deptDO.getId());
			userDO.setDeptPath(deptDO.getPath());
			userMapper.insertTable(userDO);
		}
		catch (Exception e) {
			log.error("错误信息", e);
			throw new SystemException(e.getMessage());
		}
	}

	private void insertDept(DeptDO deptDO) {
		Long id = IdGenerator.defaultSnowflakeId();
		deptDO.setId(id);
		deptDO.setName("多租户集团");
		deptDO.setPath(DEFAULT + COMMA + id);
		deptDO.setSort(1000);
		deptDO.setDeptPath(deptDO.getPath());
		deptDO.setDeptId(deptDO.getId());
		deptDO.setPid(0L);
		deptMapper.insert(deptDO);
	}

}
