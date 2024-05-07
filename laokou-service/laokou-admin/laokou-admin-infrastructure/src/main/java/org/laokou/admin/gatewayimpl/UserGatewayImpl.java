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

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.UserConvertor;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.UserRoleMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.admin.gatewayimpl.database.dataobject.UserRoleDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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

	private final UserConvertor userConvertor;

	private final PasswordEncoder passwordEncoder;

	private final MybatisUtil mybatisUtil;

	private final UserRoleMapper userRoleMapper;

	private final TransactionalUtil transactionalUtil;

	@Override
	public void modify(User user) {
		// 检查ID
		user.checkNullId();
		// 密码加密
		user.encryptPassword(passwordEncoder, user.getPassword());
		// 检查手机号
		checkMobile(user);
		// 检查邮箱
		checkMail(user);
		UserDO userDO = userConvertor.toDataObject(user);
		// 版本号
		userDO.setVersion(userMapper.selectVersion(userDO.getId()));
		// 修改
		modify(userDO, user.getRoleIds());
	}

	@Override
	public void create(User user) {
		// 密码加密
		user.encryptPassword(passwordEncoder, user.getPassword());
		// 检查用户名重复
		long count = userMapper.selectUsernameCount(user.getUsername(), AesUtil.getSecretKeyStr());
		user.checkUserName(count);
		UserDO userDO = userConvertor.toDataObject(user);
		// 新增
		create(userDO, user.getRoleIds());
	}

	@Override
	public void remove(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				userMapper.deleteBatchIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(LogUtil.record(e.getMessage()));
			}
		});
	}

	private void checkMail(User user) {
		String mail = user.getMail();
		if (StringUtil.isNotEmpty(mail)) {
			long count = userMapper.selectCount(Wrappers.lambdaQuery(UserDO.class)
				.eq(UserDO::getMail, AesUtil.encrypt(mail))
				.ne(UserDO::getId, user.getId()));
			user.checkMail(count);
		}
	}

	private void checkMobile(User user) {
		String mobile = user.getMobile();
		if (StringUtil.isNotEmpty(mobile)) {
			long count = userMapper.selectCount(Wrappers.lambdaQuery(UserDO.class)
				.eq(UserDO::getMobile, AesUtil.encrypt(mobile))
				.ne(UserDO::getId, user.getId()));
			user.checkMobile(count);
		}
	}

	private void create(UserDO userDO, List<Long> roleIds) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				// 新增用户
				userMapper.insertObj(userDO, AesUtil.getSecretKeyStr());
				// 新增用户角色
				createUserRole(userDO, roleIds);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(LogUtil.record(e.getMessage()));
			}
		});
	}

	private void modify(UserDO userDO, List<Long> roleIds) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				// 修改用户
				userMapper.updateById(userDO);
				// 修改用户角色
				modifyUserRole(userDO, roleIds);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(LogUtil.record(e.getMessage()));
			}
		});
	}

	private void modifyUserRole(UserDO userDO, List<Long> roleIds) {
		if (CollectionUtil.isNotEmpty(roleIds)) {
			// 删除用户角色
			removeUserRole(userDO);
			// 新增用户角色
			createUserRole(userDO, roleIds);
		}
	}

	private void createUserRole(UserDO userDO, List<Long> roleIds) {
		List<UserRoleDO> list = roleIds.parallelStream().map(roleId -> convert(userDO, roleId)).toList();
		mybatisUtil.batch(list, UserRoleMapper.class, DynamicDataSourceContextHolder.peek(), UserRoleMapper::insertOne);
	}

	private void removeUserRole(UserDO userDO) {
		userRoleMapper.deleteByUserId(userDO.getId());
	}

	/**
	 * 转换为用户角色数据模型.
	 * @param userDO 用户数据模型
	 * @param roleId 角色ID
	 * @return 用户角色数据模型
	 */
	private UserRoleDO convert(UserDO userDO, Long roleId) {
		UserRoleDO userRoleDO = new UserRoleDO();
		userRoleDO.setId(IdGenerator.defaultSnowflakeId());
		userRoleDO.setDeptId(userDO.getDeptId());
		userRoleDO.setDeptPath(userDO.getDeptPath());
		userRoleDO.setRoleId(roleId);
		userRoleDO.setUserId(userDO.getId());
		userRoleDO.setCreateDate(DateUtil.now());
		userRoleDO.setUpdateDate(DateUtil.now());
		userRoleDO.setCreator(userDO.getCreator());
		userRoleDO.setEditor(userDO.getEditor());
		return userRoleDO;
	}

}
