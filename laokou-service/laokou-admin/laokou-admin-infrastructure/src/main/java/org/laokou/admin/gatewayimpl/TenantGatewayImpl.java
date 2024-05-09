/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.config.DefaultConfigProperties;
import org.laokou.admin.convertor.TenantConvertor;
import org.laokou.admin.domain.gateway.TenantGateway;
import org.laokou.admin.domain.tenant.Tenant;
import org.laokou.admin.gatewayimpl.database.MenuMapper;
import org.laokou.admin.gatewayimpl.database.TenantMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.admin.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.admin.gatewayimpl.database.dataobject.TenantDO;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.core.utils.*;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.mybatisplus.template.TableTemplate;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.laokou.common.i18n.common.DSConstant.*;
import static org.laokou.common.i18n.common.constants.StringConstant.COMMA;
import static org.laokou.common.i18n.common.constants.StringConstant.EMPTY;

/**
 * 租户管理.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TenantGatewayImpl implements TenantGateway {

	@Schema(name = "TENANT_USERNAME", description = "默认租户用户名")
	private static final String TENANT_USERNAME = "tenant";

	@Schema(name = "TENANT_PASSWORD", description = "默认租户密码")
	private static final String TENANT_PASSWORD = "tenant123";

	private final TenantMapper tenantMapper;

	private final TransactionalUtil transactionalUtil;

	private final PasswordEncoder passwordEncoder;

	private final MenuMapper menuMapper;

	private final TenantConvertor tenantConvertor;

	private final DefaultConfigProperties defaultConfigProperties;

	private final Environment env;

	/**
	 * 新增租户.
	 * @param tenant 租户对象
	 */
	@Override
	public void create(Tenant tenant) {
		long count = tenantMapper
			.selectCount(Wrappers.lambdaQuery(TenantDO.class).eq(TenantDO::getName, tenant.getName()));
		tenant.checkName(count);
		TenantDO tenantDO = tenantConvertor.toDataObject(tenant);
		tenantDO.setLabel(defaultConfigProperties.getTenantPrefix() + tenantMapper.selectMaxLabelNum());
		create(tenantDO);
	}

	/**
	 * 修改租户.
	 * @param tenant 租户对象
	 */
	@Override
	public void modify(Tenant tenant) {
		long count = tenantMapper.selectCount(Wrappers.lambdaQuery(TenantDO.class)
			.eq(TenantDO::getName, tenant.getName())
			.ne(TenantDO::getId, tenant.getId()));
		tenant.checkName(count);
		TenantDO tenantDO = tenantConvertor.toDataObject(tenant);
		tenantDO.setVersion(tenantMapper.selectVersion(tenantDO.getId()));
		modify(tenantDO);
	}

	/**
	 * 根据IDS删除租户.
	 * @param ids IDS
	 */
	@Override
	public void remove(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				tenantMapper.deleteBatchIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new SystemException(msg);
			}
		});
	}

	/**
	 * 下载租户数据库压缩包.
	 * @param id ID
	 * @param response 响应对象
	 */
	@Override
	@SneakyThrows
	public void downloadDatasource(Long id, HttpServletResponse response) {
		String fileName = "kcloud_platform_iot_tenant.sql";
		String fileExt = FileUtil.getFileExt(fileName);
		String name = DateUtil.format(DateUtil.now(), DateUtil.YYYYMMDDHHMMSS) + fileExt;
		response.setContentType("application/octet-stream");
		response.setCharacterEncoding(UTF_8);
		response.setHeader("Content-disposition", "attachment;filename=" + UTF_8.encode(fileName + ".zip"));
		TenantDO tenantDO = tenantMapper.selectById(id);
		Assert.isTrue(ObjectUtil.isNotNull(tenantDO), "tenantDO is null");
		try (ServletOutputStream outputStream = response.getOutputStream()) {
			File file = writeTempFile(fileName, name, id, tenantDO.getPackageId());
			FileUtil.zip(file, outputStream);
			deleteTempFile(file);
		}
	}

	/**
	 * 新增租户.
	 * @param tenantDO 租户数据模型
	 */
	private void create(TenantDO tenantDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				tenantMapper.insert(tenantDO);
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new SystemException(msg);
			}
		});
	}

	/**
	 * 修改租户.
	 * @param tenantDO 租户数据模型
	 */
	private void modify(TenantDO tenantDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				tenantMapper.updateById(tenantDO);
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new SystemException(msg);
			}
		});
	}

	/**
	 * SQL写入本地文件.
	 * @param fileName SQL文件名称
	 * @param name 写入文件名称
	 * @param tenantId 租户ID
	 * @param packageId 套餐ID
	 * @return 文件对象
	 */
	@SneakyThrows
	private File writeTempFile(String fileName, String name, long tenantId, long packageId) {
		String tempPath = env.getProperty("file.temp-path");
		File file = FileUtil.createFile(tempPath, name);
		Assert.isTrue(StringUtil.isNotEmpty(tempPath), "tempPath is empty");
		try (InputStream inputStream = ResourceUtil.getResource("scripts/" + fileName).getInputStream();
				FileOutputStream outputStream = new FileOutputStream(file);
				FileChannel outChannel = outputStream.getChannel()) {
			ByteBuffer buffer = ByteBuffer.wrap(inputStream.readAllBytes());
			ByteBuffer buff = ByteBuffer.wrap(CollectionUtil.toStr(getSql(tenantId, packageId), EMPTY).getBytes());
			outChannel.write(buffer);
			outChannel.write(buff);
			buffer.clear();
			buff.clear();
		}
		return file;
	}

	/**
	 * 删除临时文件.
	 * @param file 文件对象
	 */
	private void deleteTempFile(File file) {
		if (file.exists()) {
			log.info("删除结果：{}", file.delete());
		}
	}

	/**
	 * 查询菜单、用户和部门列表转换为SQL语句列表.
	 * @param tenantId 租户ID
	 * @param packageId 套餐ID
	 * @return SQL语句列表
	 */
	private List<String> getSql(long tenantId, long packageId) {
		long userId = IdGenerator.defaultSnowflakeId();
		long deptId = IdGenerator.defaultSnowflakeId();
		String deptPath = 0 + COMMA + deptId;
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
		List<String> list = new ArrayList<>(userSqlList.size() + deptSqlList.size() + menuSqlList.size() + 1);
		list.addAll(userSqlList);
		list.addAll(deptSqlList);
		list.addAll(menuSqlList);
		list.add(getUpdateUsernameSql(userId));
		return list;
	}

	/**
	 * 查询菜单列表转换为Map列表.
	 * @param menuList 菜单数据模型列表
	 * @return Map列表
	 */
	private List<Map<String, String>> getMenuMapList(List<MenuDO> menuList) {
		List<Map<String, String>> menuMapList = new ArrayList<>(menuList.size());
		menuList.forEach(item -> menuMapList.add(JacksonUtil.toMap(item, String.class, String.class)));
		return menuMapList;
	}

	/**
	 * 查询菜单列表.
	 * @param tenantId 租户ID
	 * @param userId 用户ID
	 * @param deptId 部门ID
	 * @param deptPath 部门PATH
	 * @param packageId 套餐ID
	 * @return 菜单列表
	 */
	private List<MenuDO> getMenuList(long tenantId, long userId, long deptId, String deptPath, long packageId) {
		List<MenuDO> menuList = menuMapper.selectTenantMenuListByPackageId(packageId);
		menuList.forEach(item -> {
			item.setTenantId(tenantId);
			item.setCreateDate(DateUtil.now());
			item.setUpdateDate(DateUtil.now());
			item.setCreator(userId);
			item.setEditor(userId);
			item.setVersion(0);
			item.setDelFlag(0);
			item.setDeptId(deptId);
			item.setDeptPath(deptPath);
		});
		return menuList;
	}

	/**
	 * 构建用户数据模型.
	 * @param tenantId 租户ID
	 * @param userId 用户ID
	 * @param deptId 部门ID
	 * @param deptPath 部门PATH
	 * @return 用户数据模型
	 */
	private UserDO getUser(long tenantId, long userId, long deptId, String deptPath) {
		// 初始化超级管理员
		// UserDO userDO = new UserDO();
		// userDO.setId(userId);
		// userDO.setUsername(TENANT_USERNAME);
		// userDO.setTenantId(tenantId);
		// userDO.setPassword(passwordEncoder.encode(TENANT_PASSWORD));
		// userDO.setSuperAdmin(YES.ordinal());
		// userDO.setDeptId(deptId);
		// userDO.setDeptPath(deptPath);
		// userDO.setCreateDate(DateUtil.now());
		// userDO.setUpdateDate(DateUtil.now());
		// userDO.setCreator(userId);
		// userDO.setEditor(userId);
		// userDO.setVersion(NumberConstant.DEFAULT);
		// userDO.setDelFlag(NumberConstant.DEFAULT);
		// userDO.setStatus(NumberConstant.DEFAULT);
		return null;
	}

	/**
	 * 构建部门数据模型.
	 * @param tenantId 租户ID
	 * @param userId 用户ID
	 * @param deptId 部门ID
	 * @param deptPath 部门PATH
	 * @return 部门数据模型
	 */
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
		deptDO.setVersion(0);
		deptDO.setDelFlag(0);
		return deptDO;
	}

	/**
	 * 构建修改用户SQL.
	 * @param userId 用户ID
	 * @return 修改用户SQL
	 */
	private String getUpdateUsernameSql(long userId) {
		return String.format(UPDATE_USERNAME_BY_ID_SQL_TEMPLATE, BOOT_SYS_USER, TENANT_USERNAME, null, userId);
	}

}
