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
import io.seata.spring.annotation.GlobalTransactional;
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
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.mybatisplus.template.TableTemplate;
import org.laokou.common.mybatisplus.utils.BatchUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.baomidou.dynamic.datasource.enums.DdConstants.MASTER;
import static org.laokou.common.i18n.common.Constant.UNDER;
import static org.laokou.common.mybatisplus.constant.DsConstant.BOOT_SYS_USER;
import static org.laokou.common.mybatisplus.constant.DsConstant.USER;

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

	private final BatchUtil batchUtil;

	private final UserRoleMapper userRoleMapper;

	private final TransactionalUtil transactionalUtil;

	private final ThreadPoolTaskExecutor taskExecutor;

	@Override
	@GlobalTransactional
	public Boolean insert(User user) {
		return insertUser(getInsertUserDO(user), user);
	}

	@Override
	@GlobalTransactional
	public Boolean update(User user) {
		return updateUser(getUpdateUserDO(user),user);
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
		try {
			LocalDateTime localDateTime = IdGenerator.getLocalDateTime(id);
			DynamicDataSourceContextHolder.push(USER);
			UserDO userDO = userMapper.getDynamicTableById(UserDO.class, id,
					UNDER.concat(DateUtil.format(localDateTime, DateUtil.YYYYMM)), "id", "username", "status", "dept_id",
					"dept_path", "super_admin");
			User user = userConvertor.convertEntity(userDO);
			DynamicDataSourceContextHolder.push(MASTER);
			if (user.getSuperAdmin() == SuperAdmin.YES.ordinal()) {
				user.setRoleIds(roleMapper.getRoleIds());
			}
			else {
				user.setRoleIds(userRoleMapper.getRoleIdsByUserId(id));
			}
			return user;
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	@Override
	@DataFilter(alias = BOOT_SYS_USER)
	@SneakyThrows
	public Datas<User> list(User user, PageQuery pageQuery) {
		UserDO userDO = userConvertor.toDataObject(user);
		final PageQuery page = pageQuery.time().page().ignore();
		List<String> dynamicTables = TableTemplate.getDynamicTables(pageQuery.getStartTime(), pageQuery.getEndTime(),
				BOOT_SYS_USER);
		CompletableFuture<List<UserDO>> c1 = CompletableFuture.supplyAsync(() -> {
			try {
				DynamicDataSourceContextHolder.push(USER);
				return userMapper.getUserListFilter(dynamicTables, userDO, page);
			}
			finally {
				DynamicDataSourceContextHolder.clear();
			}
		}, taskExecutor);
		CompletableFuture<Integer> c2 = CompletableFuture.supplyAsync(() -> {
			try {
				DynamicDataSourceContextHolder.push(USER);
				return userMapper.getUserListTotalFilter(dynamicTables, userDO, page);
			}
			finally {
				DynamicDataSourceContextHolder.clear();
			}
		}, taskExecutor);
		CompletableFuture.allOf(c1, c2).join();
		Datas<User> datas = new Datas<>();
		datas.setTotal(c2.get());
		datas.setRecords(ConvertUtil.sourceToTarget(c1.get(), User.class));
		return datas;
	}

	private Boolean updateUser(UserDO userDO,User user) {
		userMapper.updateUser(userDO, TableTemplate.getDynamicTable(userDO.getId(), BOOT_SYS_USER));
		deleteUserRole(user);
		insertUserRole(user.getRoleIds(), userDO);
		return true;
	}

	private void deleteUserRole(User user) {
		try {
			DynamicDataSourceContextHolder.push(MASTER);
			transactionalUtil.executeWithoutResult(rollback -> {
				try {
					userRoleMapper.deleteUserRoleByUserId(user.getId());
				} catch (Exception e) {
					log.error("错误信息：{}", e.getMessage());
					rollback.setRollbackOnly();
					throw new SystemException(e.getMessage());
				}
			});
		} finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	private Boolean insertUser(UserDO userDO, User user) {
		return transactionalUtil.execute(rollback -> {
			try {
				userMapper.insertDynamicTable(userDO, TableTemplate.getUserSqlScript(DateUtil.now()), UNDER.concat(DateUtil.format(DateUtil.now(), DateUtil.YYYYMM)));
				insertUserRole(user.getRoleIds(), userDO);
				return true;
			} catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				rollback.setRollbackOnly();
				throw new SystemException(e.getMessage());
			} finally {
				DynamicDataSourceContextHolder.clear();
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
		userDO.setVersion(userMapper.getDynamicVersion(userDO.getId(), UserDO.class, getUserTableSuffix(user.getId())));
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
			batchUtil.insertBatch(list, UserRoleMapper.class);
		}
	}

	private Boolean updateUser(UserDO userDO) {
		try {
			DynamicDataSourceContextHolder.push(USER);
			return transactionalUtil.execute(rollback -> {
				try {
					return userMapper.updateUser(userDO, TableTemplate.getDynamicTable(userDO.getId(), BOOT_SYS_USER)) > 0;
				} catch (Exception e) {
					log.error("错误信息：{}", e.getMessage());
					rollback.setRollbackOnly();
					throw new SystemException(e.getMessage());
				}
			});
		} finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	private Boolean deleteUserById(Long id) {
		try {
			DynamicDataSourceContextHolder.push(USER);
			return transactionalUtil.execute(rollback -> {
				try {
					return userMapper.deleteDynamicTableById(id, getUserTableSuffix(id)) > 0;
				} catch (Exception e) {
					log.error("错误信息：{}", e.getMessage());
					rollback.setRollbackOnly();
					throw new SystemException(e.getMessage());
				}
			});
		} finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	private String getUserTableSuffix(Long id) {
		LocalDateTime localDateTime = IdGenerator.getLocalDateTime(id);
		return UNDER.concat(DateUtil.format(localDateTime, DateUtil.YYYYMM));
	}

	private UserRoleDO toUserRoleDO(UserDO userDO,Long roleId) {
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
