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
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
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
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.core.utils.DateUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.utils.BatchUtil;
import org.laokou.common.mybatisplus.utils.IdUtil;
import org.laokou.common.shardingsphere.utils.TableUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.baomidou.dynamic.datasource.enums.DdConstants.MASTER;
import static org.laokou.admin.common.Constant.USER;
import static org.laokou.admin.common.DsConstant.BOOT_SYS_USER;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserGatewayImpl implements UserGateway {

	private final UserMapper userMapper;

	private final RoleMapper roleMapper;

	private final PasswordEncoder passwordEncoder;

	private final BatchUtil batchUtil;

	private final UserRoleMapper userRoleMapper;

	@Override
	@DS(USER)
	public Boolean insert(User user) {
		UserDO userDO = getInsertUserDO(user);
		return insertUser(userDO, user);
	}

	@Override
	@DS(USER)
	public Boolean update(User user) {
		UserDO userDO = getUpdateUserDO(user);
		List<Long> ids = userRoleMapper.getIdsByUserId(userDO.getId());
		return updateUser(userDO, user, ids);
	}

	@Override
	@DS(USER)
	public Boolean deleteById(Long id) {
		return deleteUserById(id);
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteUserById(Long id) {
		return userMapper.deleteById(id) > 0;
	}

	@Override
	@DS(USER)
	public Boolean resetPassword(User user) {
		return updateUser(getResetPasswordDO(user));
	}

	@Override
	@DS(USER)
	public Boolean updateInfo(User user) {
		return updateUser(getUpdateUserDO(user));
	}

	@Override
	@DS(USER)
	public User getById(Long id, Long tenantId) {
		UserDO userDO = userMapper.selectOne(Wrappers.query(UserDO.class).eq("id", id).select("id", "username",
				"status", "dept_id", "dept_path", "super_admin"));
		User user = ConvertUtil.sourceToTarget(userDO, User.class);
		try {
			DynamicDataSourceContextHolder.push(MASTER);
			if (user.getSuperAdmin() == SuperAdmin.YES.ordinal()) {
				user.setRoleIds(roleMapper.getRoleIdsByTenantId(tenantId));
			}
			else {
				user.setRoleIds(userRoleMapper.getRoleIdsByUserId(id));
			}
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
		return user;
	}

	@Override
	@DataFilter(alias = BOOT_SYS_USER)
	@DS(USER)
	public Datas<User> list(User user, PageQuery pageQuery) {
		Page<UserDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<UserDO> newPage = userMapper.getUserListByTenantIdAndUsernameFilter(page, user.getTenantId(),
				user.getUsername(), pageQuery.getSqlFilter());
		Datas<User> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), User.class));
		return datas;
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean insertUser(UserDO userDO, User user) {
		boolean flag = userMapper.insertTable(userDO, TableUtil.getUserSqlScript(DateUtil.now()));
		return flag && insertUserRole(userDO.getId(), user.getRoleIds(), user);
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean updateUser(UserDO userDO, User user, List<Long> ids) {
		boolean flag = userMapper.updateUser(userDO) > 0;
		flag = flag && deleteUserRole(ids);
		return flag && insertUserRole(userDO.getId(), user.getRoleIds(), user);
	}

	private Boolean deleteUserRole(List<Long> ids) {
		if (CollectionUtil.isEmpty(ids)) {
			return true;
		}
		return userRoleMapper.deleteUserRoleByIds(ids) > 0;
	}

	private UserDO getInsertUserDO(User user) {
		UserDO userDO = UserConvertor.toDataObject(user);
		userDO.setPassword(passwordEncoder.encode(userDO.getPassword()));
		return userDO;
	}

	private UserDO getUpdateUserDO(User user) {
		UserDO userDO = UserConvertor.toDataObject(user);
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

	private Boolean insertUserRole(Long userId, List<Long> roleIds, User user) {
		if (CollectionUtil.isEmpty(roleIds)) {
			return false;
		}
		List<UserRoleDO> list = new ArrayList<>(roleIds.size());
		for (Long roleId : roleIds) {
			UserRoleDO userRoleDO = new UserRoleDO();
			userRoleDO.setId(IdUtil.defaultId());
			userRoleDO.setDeptId(user.getDeptId());
			userRoleDO.setTenantId(user.getTenantId());
			userRoleDO.setCreator(user.getCreator());
			userRoleDO.setUserId(userId);
			userRoleDO.setRoleId(roleId);
			userRoleDO.setDeptPath(user.getDeptPath());
			list.add(userRoleDO);
		}
		batchUtil.insertBatch(list, userRoleMapper::insertBatch);
		return true;
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean updateUser(UserDO userDO) {
		return userMapper.updateUser(userDO) > 0;
	}

}
