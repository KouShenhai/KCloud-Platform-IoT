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

package org.laokou.admin.gatewayimpl;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.UserConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.gatewayimpl.database.RoleMapper;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.UserRoleMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.admin.gatewayimpl.database.dataobject.UserRoleDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.DatasourceConstants.BOOT_SYS_USER;

/**
 * 用户管理.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserGatewayImpl implements UserGateway {

	private final UserMapper userMapper;

	private final RoleMapper roleMapper;

	private final UserConvertor userConvertor;

	private final PasswordEncoder passwordEncoder;

	private final MybatisUtil mybatisUtil;

	private final UserRoleMapper userRoleMapper;

	private final TransactionalUtil transactionalUtil;

	private final Executor executor;

	/**
	 * 新增用户.
	 * @param user 用户对象
	 * @return 新增结果
	 */
	@Override
	public Boolean insert(User user) {
		return insertUser(getInsertUserDO(user), user);
	}

	/**
	 * 修改用户.
	 * @param user 用户对象
	 * @return 修改结果
	 */
	@Override
	public Boolean update(User user) {
		return updateUser(getUpdateUserDO(user), user);
	}

	/**
	 * 根据ID删除用户.
	 * @param id ID
	 * @return 删除结果
	 */
	@Override
	public Boolean deleteById(Long id) {
		return deleteUserById(id);
	}

	/**
	 * 重置密码.
	 * @param user 用户对象
	 * @return 重置结果
	 */
	@Override
	public Boolean resetPassword(User user) {
		return updateUser(getResetPasswordDO(user));
	}

	/**
	 * 修改用户信息.
	 * @param user 用户对象
	 * @return 修改结果
	 */
	@Override
	public Boolean updateInfo(User user) {
		return updateUser(getUpdateUserDO(user));
	}

	/**
	 * 根据ID查看用户.
	 * @param id ID
	 * @return 用户
	 */
	@Override
	public User getById(Long id) {
		User user = userConvertor.convertEntity(userMapper.selectById(id));
		if (user.isSuperAdministrator()) {
			user.setRoleIds(roleMapper.getRoleIds());
		}
		else {
			user.setRoleIds(userRoleMapper.getRoleIdsByUserId(id));
		}
		return user;
	}

	/**
	 * 查询用户列表.
	 * @param user 用户对象
	 * @param pageQuery 分页参数
	 * @return 用户列表
	 */
	@Override
	@DataFilter(tableAlias = BOOT_SYS_USER)
	@SneakyThrows
	public Datas<User> list(User user, PageQuery pageQuery) {
		UserDO userDO = userConvertor.toDataObject(user);
		final PageQuery page = pageQuery.page();
		CompletableFuture<List<UserDO>> c1 = CompletableFuture
			.supplyAsync(() -> userMapper.getUserListFilter(userDO, page, AesUtil.getKey()), executor);
		CompletableFuture<Integer> c2 = CompletableFuture
			.supplyAsync(() -> userMapper.getUserListTotalFilter(userDO, page, AesUtil.getKey()), executor);
		CompletableFuture.allOf(c1, c2).join();
		Datas<User> datas = new Datas<>();
		datas.setTotal(c2.get());
		datas.setRecords(userConvertor.convertEntityList(c1.get()));
		return datas;
	}

	/**
	 * 修改用户.
	 * @param userDO 用户数据模型
	 * @param user 用户对象
	 * @return 修改结果
	 */
	private Boolean updateUser(UserDO userDO, User user) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				userMapper.updateUser(userDO);
				deleteUserRole(user);
				insertUserRole(user.getRoleIds(), userDO);
				return true;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 删除用户角色.
	 * @param user 用户对象
	 */
	private void deleteUserRole(User user) {
		userRoleMapper.deleteUserRoleByUserId(user.getId());
	}

	/**
	 * 新增用户.
	 * @param userDO 用户数据模型
	 * @param user 用户对象
	 * @return 新增结果
	 */
	private Boolean insertUser(UserDO userDO, User user) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				userMapper.insertUser(userDO, AesUtil.getKey());
				insertUserRole(user.getRoleIds(), userDO);
				return true;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 查看用户数据模型（新增用户）.
	 * @param user 用户对象
	 * @return 用户数据模型（新增用户）
	 */
	private UserDO getInsertUserDO(User user) {
		UserDO userDO = userConvertor.toDataObject(user);
		userDO.setId(IdGenerator.defaultSnowflakeId());
		userDO.setPassword(passwordEncoder.encode(userDO.getPassword()));
		return userDO;
	}

	/**
	 * 查看用户模型（修改用户）.
	 * @param user 用户对象
	 * @return 用户模型（修改用户）
	 */
	private UserDO getUpdateUserDO(User user) {
		UserDO userDO = userConvertor.toDataObject(user);
		userDO.setEditor(user.getEditor());
		userDO.setUpdateDate(DateUtil.now());
		userDO.setVersion(userMapper.getVersion(userDO.getId(), UserDO.class));
		return userDO;
	}

	/**
	 * 查看用户数据模型（重置密码）.
	 * @param user 用户对象
	 * @return 用户数据模型（重置密码）
	 */
	private UserDO getResetPasswordDO(User user) {
		UserDO updateUserDO = getUpdateUserDO(user);
		updateUserDO.setPassword(passwordEncoder.encode(updateUserDO.getPassword()));
		return updateUserDO;
	}

	/**
	 * 新增用户角色（批量）.
	 * @param roleIds 角色IDS
	 * @param userDO 用户数据模型
	 */
	private void insertUserRole(List<Long> roleIds, UserDO userDO) {
		if (CollectionUtil.isNotEmpty(roleIds)) {
			List<UserRoleDO> list = roleIds.parallelStream().map(roleId -> toUserRoleDO(userDO, roleId)).toList();
			mybatisUtil.batch(list, UserRoleMapper.class, DynamicDataSourceContextHolder.peek(), UserRoleMapper::save);
		}
	}

	/**
	 * 修改用户.
	 * @param userDO 用户数据模型
	 * @return 修改结果
	 */
	private Boolean updateUser(UserDO userDO) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return userMapper.updateUser(userDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 根据ID删除用户.
	 * @param id ID
	 * @return 删除结果
	 */
	private Boolean deleteUserById(Long id) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return userMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 转换为用户角色数据模型.
	 * @param userDO 用户数据模型
	 * @param roleId 角色ID
	 * @return 用户角色数据模型
	 */
	private UserRoleDO toUserRoleDO(UserDO userDO, Long roleId) {
		UserRoleDO userRoleDO = new UserRoleDO();
		userRoleDO.setId(IdGenerator.defaultSnowflakeId());
		userRoleDO.setDeptId(userDO.getDeptId());
		userRoleDO.setTenantId(userDO.getTenantId());
		userRoleDO.setCreator(userDO.getCreator());
		userRoleDO.setUserId(userDO.getId());
		userRoleDO.setRoleId(roleId);
		userRoleDO.setDeptPath(userDO.getDeptPath());
		return userRoleDO;
	}

}
