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

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.UserConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.admin.domain.user.SuperAdmin;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.gatewayimpl.database.RoleMapper;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.UserRoleMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.admin.gatewayimpl.database.dataobject.UserRoleDO;
import org.laokou.common.core.holder.UserContextHolder;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.laokou.common.mybatisplus.constant.DsConstant.BOOT_SYS_USER;

/**
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

	private final ThreadPoolTaskExecutor taskExecutor;

	@Override
	public Boolean insert(User user) {
		return insertUser(getInsertUserDO(user), user);
	}

	@Override
	public Boolean update(User user) {
		return updateUser(getUpdateUserDO(user), user);
	}

	@Override
	public Boolean deleteById(Long id) {
		return deleteUserById(id);
	}

	@Override
	public Boolean resetPassword(User user) {
		return updateUser(getResetPasswordDO(user));
	}

	@Override
	public Boolean updateInfo(User user) {
		return updateUser(getUpdateUserDO(user));
	}

	@Override
	public User getById(Long id, Long tenantId) {
		UserDO userDO = userMapper.selectOne(Wrappers.query(UserDO.class).eq("id", id).select("id", "username", "status", "dept_id", "dept_path", "super_admin"));
		User user = userConvertor.convertEntity(userDO);
		if (user.getSuperAdmin() == SuperAdmin.YES.ordinal()) {
			user.setRoleIds(roleMapper.getRoleIds());
		}
		else {
			user.setRoleIds(userRoleMapper.getRoleIdsByUserId(id));
		}
		return user;
	}

	@Override
	@DataFilter(alias = BOOT_SYS_USER)
	@SneakyThrows
	public Datas<User> list(User user, PageQuery pageQuery) {
		UserDO userDO = userConvertor.toDataObject(user);
		final PageQuery page = pageQuery.page();
		String sourceName = UserContextHolder.get().getSourceName();
		CompletableFuture<List<UserDO>> c1 = CompletableFuture.supplyAsync(() -> {
			try {
				DynamicDataSourceContextHolder.push(sourceName);
				return userMapper.getUserListFilter(userDO, page);
			}
			finally {
				DynamicDataSourceContextHolder.clear();
			}
		}, taskExecutor);
		CompletableFuture<Integer> c2 = CompletableFuture.supplyAsync(() -> {
			try {
				DynamicDataSourceContextHolder.push(sourceName);
				return userMapper.getUserListTotalFilter(userDO, page);
			}
			finally {
				DynamicDataSourceContextHolder.clear();
			}
		}, taskExecutor);
		CompletableFuture.allOf(c1, c2).join();
		Datas<User> datas = new Datas<>();
		datas.setTotal(c2.get());
		datas.setRecords(userConvertor.convertEntityList(c1.get()));
		return datas;
	}

	private Boolean updateUser(UserDO userDO, User user) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				userMapper.updateUser(userDO);
				deleteUserRole(user);
				insertUserRole(user.getRoleIds(), userDO);
				return true;
			}
			catch (Exception e) {
				log.error("错误信息", e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private void deleteUserRole(User user) {
		userRoleMapper.deleteUserRoleByUserId(user.getId());
	}

	private Boolean insertUser(UserDO userDO, User user) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				userMapper.insertTable(userDO);
				insertUserRole(user.getRoleIds(), userDO);
				return true;
			}
			catch (Exception e) {
				log.error("错误信息", e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private UserDO getInsertUserDO(User user) {
		UserDO userDO = userConvertor.toDataObject(user);
		userDO.setPassword(passwordEncoder.encode(userDO.getPassword()));
		return userDO;
	}

	private UserDO getUpdateUserDO(User user) {
		UserDO userDO = userConvertor.toDataObject(user);
		userDO.setEditor(user.getEditor());
		userDO.setUpdateDate(DateUtil.now());
		userDO.setVersion(userMapper.getVersion(userDO.getId(), UserDO.class));
		return userDO;
	}

	private UserDO getResetPasswordDO(User user) {
		UserDO updateUserDO = getUpdateUserDO(user);
		updateUserDO.setPassword(passwordEncoder.encode(updateUserDO.getPassword()));
		return updateUserDO;
	}

	private void insertUserRole(List<Long> roleIds, UserDO userDO) {
		if (CollectionUtil.isNotEmpty(roleIds)) {
			List<UserRoleDO> list = roleIds.parallelStream().map(roleId -> toUserRoleDO(userDO, roleId)).toList();
			mybatisUtil.batch(list, UserRoleMapper.class, DynamicDataSourceContextHolder.peek(), UserRoleMapper::save);
		}
	}

	private Boolean updateUser(UserDO userDO) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return userMapper.updateUser(userDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息", e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private Boolean deleteUserById(Long id) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return userMapper.deleteById(id) > 0;
			} catch (Exception e) {
				log.error("错误信息", e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

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
