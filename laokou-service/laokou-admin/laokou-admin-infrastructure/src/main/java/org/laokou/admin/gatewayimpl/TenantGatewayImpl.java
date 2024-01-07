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
import org.laokou.admin.gatewayimpl.database.MenuMapper;
import org.laokou.admin.gatewayimpl.database.TenantMapper;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.admin.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.admin.gatewayimpl.database.dataobject.TenantDO;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.template.TableTemplate;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.laokou.common.i18n.common.Constant.*;
import static org.laokou.common.mybatisplus.constant.DsConstant.*;

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

	private final MenuMapper menuMapper;

	private final TenantConvertor tenantConvertor;

	private final DefaultConfigProperties defaultConfigProperties;

	@Override
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
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private Boolean insertTenant(TenantDO tenantDO) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return tenantMapper.insertTable(tenantDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private Boolean updateTenant(TenantDO tenantDO) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return tenantMapper.updateById(tenantDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private List<String> getSql(long tenantId, long packageId) {
		long userId = IdGenerator.defaultSnowflakeId();
		long deptId = IdGenerator.defaultSnowflakeId();
		String deptPath = DEFAULT + COMMA + deptId;
		UserDO user = getUser(tenantId, userId, deptId, deptPath);
		DeptDO dept = getDept(tenantId, userId, deptId, deptPath);
		List<MenuDO> menuList = getMenuList(tenantId, userId, deptId, deptPath, packageId);
		List<Map<String, String>> menuMapList = getMenuMapList(menuList);
		Map<String, String> userMap = JacksonUtil.toMap(user, String.class, String.class);
		Map<String, String> deptMap = JacksonUtil.toMap(dept, String.class, String.class);
		List<String> userSqlList = TableTemplate.getInsertSqlScriptList(Collections.singletonList(userMap),
				BOOT_SYS_USER);
		List<String> deptSqlList = TableTemplate.getInsertSqlScriptList(Collections.singletonList(deptMap),
				BOOT_SYS_DEPT);
		List<String> menuSqlList = TableTemplate.getInsertSqlScriptList(menuMapList, BOOT_SYS_MENU);
		List<String> list = new ArrayList<>(userSqlList.size() + deptSqlList.size() + menuSqlList.size());
		list.addAll(userSqlList);
		list.addAll(deptSqlList);
		list.addAll(menuSqlList);
		return list;
	}

	private List<Map<String, String>> getMenuMapList(List<MenuDO> menuList) {
		List<Map<String, String>> menuMapList = new ArrayList<>(menuList.size());
		menuList.forEach(item -> menuMapList.add(JacksonUtil.toMap(item, String.class, String.class)));
		return menuMapList;
	}

	private List<MenuDO> getMenuList(long tenantId, long userId, long deptId, String deptPath, long packageId) {
		List<MenuDO> menuList = menuMapper.getTenantMenuListByPackageId(packageId);
		menuList.forEach(item -> {
			item.setTenantId(tenantId);
			item.setCreateDate(DateUtil.now());
			item.setUpdateDate(DateUtil.now());
			item.setCreator(userId);
			item.setEditor(userId);
			item.setVersion(DEFAULT);
			item.setDelFlag(DEFAULT);
			item.setDeptId(deptId);
			item.setDeptPath(deptPath);
		});
		return menuList;
	}

	private UserDO getUser(long tenantId, long userId, long deptId, String deptPath) {
		// 初始化超级管理员
		UserDO userDO = new UserDO();
		userDO.setId(userId);
		userDO.setUsername(TENANT_USERNAME);
		userDO.setTenantId(tenantId);
		userDO.setPassword(passwordEncoder.encode(TENANT_PASSWORD));
		userDO.setSuperAdmin(SuperAdmin.YES.ordinal());
		userDO.setDeptId(deptId);
		userDO.setDeptPath(deptPath);
		userDO.setCreateDate(DateUtil.now());
		userDO.setUpdateDate(DateUtil.now());
		userDO.setCreator(userId);
		userDO.setEditor(userId);
		userDO.setVersion(DEFAULT);
		userDO.setDelFlag(DEFAULT);
		userDO.setStatus(DEFAULT);
		return userDO;
	}

	private DeptDO getDept(long tenantId, long userId, long deptId, String deptPath) {
		DeptDO deptDO = new DeptDO();
		deptDO.setId(deptId);
		deptDO.setName("多租户集团");
		deptDO.setPath(deptPath);
		deptDO.setSort(1000);
		deptDO.setDeptPath(deptDO.getPath());
		deptDO.setDeptId(deptDO.getId());
		deptDO.setPid(0L);
		deptDO.setTenantId(tenantId);
		deptDO.setCreateDate(DateUtil.now());
		deptDO.setUpdateDate(DateUtil.now());
		deptDO.setCreator(userId);
		deptDO.setEditor(userId);
		deptDO.setVersion(DEFAULT);
		deptDO.setDelFlag(DEFAULT);
		return deptDO;
	}

}
