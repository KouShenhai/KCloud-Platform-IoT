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
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.UserConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.UserRoleMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.admin.gatewayimpl.database.dataobject.UserRoleDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.context.DynamicTableContextHolder;
import org.laokou.common.mybatisplus.utils.BatchUtil;
import org.laokou.common.mybatisplus.utils.IdUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.laokou.admin.common.Constant.DEFAULT_SOURCE;
import static org.laokou.admin.common.Constant.SHARDING_SPHERE;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserGatewayImpl implements UserGateway {

	private final UserMapper userMapper;

	private final PasswordEncoder passwordEncoder;

	private final BatchUtil batchUtil;

	private final UserRoleMapper userRoleMapper;

	private final TransactionalUtil transactionalUtil;

	@Override
	@DS(SHARDING_SPHERE)
	public Boolean insert(User user) {
		UserDO userDO = getInsertUserDO(user);
		return insertUser(userDO,user);
	}

	@Override
	@DS(SHARDING_SPHERE)
	public Boolean update(User user) {
		UserDO userDO = getUpdateUserDO(user);
		List<Long> ids = userRoleMapper.getIdsByUserId(userDO.getId());
		return updateUser(userDO,user,ids);
	}

	@Override
	@DS(SHARDING_SPHERE)
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteById(Long id) {
		return userMapper.deleteById(id) > 0;
	}

	@Override
	@DS(SHARDING_SPHERE)
	public Boolean resetPassword(User user) {
		return updateUser(getResetPasswordDO(user));
	}

	@Override
	@DS(SHARDING_SPHERE)
	public Boolean updateStatus(User user) {
		return updateUser(getUpdateUserDO(user));
	}

	@Override
	@DS(SHARDING_SPHERE)
	public User getById(Long id) {
		return null;
	}

	@Override
	@DS(SHARDING_SPHERE)
	@DataFilter(alias = "boot_sys_user")
	public Datas<User> list(User user, PageQuery pageQuery) {
		UserDO userDO = UserConvertor.toDataObject(user);
		Page<UserDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<UserDO> newPage = userMapper.getUserListFilter(page, userDO,pageQuery.getSqlFilter());
		Datas<User> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(),User.class));
		return datas;
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean insertUser(UserDO userDO,User user) {
		boolean flag = userMapper.insert(userDO) > 0;
		return flag && insertBatch(userDO.getId(),user.getRoleIds());
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean updateUser(UserDO userDO,User user,List<Long> ids) {
		boolean flag = userMapper.updateUser(userDO) > 0;
		flag = flag && deleteUserRole(ids);
		return flag && insertBatch(userDO.getId(),user.getRoleIds());
	}

	private Boolean deleteUserRole(List<Long> ids) {
		if (CollectionUtil.isNotEmpty(ids)) {
			return false;
		}
		return userRoleMapper.deleteBatchIds(ids) > 0;
	}

	private UserDO getInsertUserDO(User user) {
		UserDO userDO = UserConvertor.toDataObject(user);
		userDO.setPassword(passwordEncoder.encode(userDO.getPassword()));
		return userDO;
	}

	private UserDO getUpdateUserDO(User user) {
		UserDO userDO = UserConvertor.toDataObject(user);
		userDO.setEditor(UserUtil.getUserId());
		userDO.setVersion(userMapper.getVersion(userDO.getId(), UserDO.class));
		return userDO;
	}

	private UserDO getResetPasswordDO(User user) {
		UserDO updateUserDO = getUpdateUserDO(user);
		updateUserDO.setPassword(passwordEncoder.encode(updateUserDO.getPassword()));
		return updateUserDO;
	}

	private Boolean insertBatch(Long userId, List<Long> roleIds) {
		try {
			DynamicTableContextHolder.set(DEFAULT_SOURCE);
			if (CollectionUtil.isEmpty(roleIds)) {
				return false;
			}
			List<UserRoleDO> list = new ArrayList<>(roleIds.size());
			for (Long roleId : roleIds) {
				UserRoleDO userRoleDO = new UserRoleDO();
				userRoleDO.setId(IdUtil.defaultId());
				userRoleDO.setDeptId(UserUtil.getDeptId());
				userRoleDO.setTenantId(UserUtil.getTenantId());
				userRoleDO.setCreator(UserUtil.getUserId());
				userRoleDO.setUserId(userId);
				userRoleDO.setRoleId(roleId);
				list.add(userRoleDO);
			}
			batchUtil.insertBatch(list, userRoleMapper::insertBatch);
		} finally {
			DynamicTableContextHolder.clear();
		}
		return true;
	}

	private Boolean updateUser(UserDO userDO) {
		return transactionalUtil.execute(r -> {
			try {
				return userMapper.updateUser(userDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

}
