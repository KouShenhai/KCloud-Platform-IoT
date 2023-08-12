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
package org.laokou.admin.server.application.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.client.dto.SysUserDTO;
import org.laokou.admin.client.vo.SysUserOnlineVO;
import org.laokou.admin.client.vo.SysUserVO;
import org.laokou.admin.client.vo.UserInfoVO;
import org.laokou.admin.server.application.service.SysUserApplicationService;
import org.laokou.admin.server.domain.sys.entity.SysUserDO;
import org.laokou.admin.server.domain.sys.entity.SysUserRoleDO;
import org.laokou.admin.server.domain.sys.repository.service.SysRoleService;
import org.laokou.admin.server.domain.sys.repository.service.SysUserRoleService;
import org.laokou.admin.server.domain.sys.repository.service.SysUserService;
import org.laokou.admin.server.interfaces.qo.SysUserOnlineQo;
import org.laokou.admin.server.interfaces.qo.SysUserQo;
import org.laokou.auth.domain.user.SuperAdmin;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.common.data.filter.annotation.DataFilter;
import org.laokou.common.i18n.common.CustomException;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.common.jasypt.utils.AesUtil;
import org.laokou.common.mybatisplus.utils.BatchUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.laokou.admin.server.infrastructure.Constant.DEFAULT_SOURCE;
import static org.laokou.common.core.constant.Constant.DEFAULT;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysUserApplicationServiceImpl implements SysUserApplicationService {

	private final SysUserService sysUserService;

	private final SysRoleService sysRoleService;

	private final SysUserRoleService sysUserRoleService;

	private final PasswordEncoder passwordEncoder;

	private final BatchUtil batchUtil;

	private final RedisUtil redisUtil;

	@Override
	@DSTransactional(rollbackFor = Exception.class)
	@DS(Constant.SHARDING_SPHERE)
	public Boolean updateUser(SysUserDTO dto) {
		try {
			ValidatorUtil.validateEntity(dto);
			Long id = dto.getId();
			if (null == id) {
				throw new CustomException("用户编号不为空");
			}
			if (CollectionUtil.isEmpty(dto.getRoleIds())) {
				throw new CustomException("所选角色不少于一个，请重新选择");
			}
			if (dto.getDeptId() == null) {
				throw new CustomException("请选择部门");
			}
			dto.setEditor(UserUtil.getUserId());
			Integer version = sysUserService.getVersion(id);
			dto.setVersion(version);
			sysUserService.updateUser(dto);
			List<Long> roleIds = dto.getRoleIds();
			DynamicDataSourceContextHolder.push(DEFAULT_SOURCE);
			// 删除中间表
			List<SysUserRoleDO> list = sysUserRoleService.list(Wrappers.lambdaQuery(SysUserRoleDO.class)
					.eq(SysUserRoleDO::getUserId, dto.getId()).select(SysUserRoleDO::getId));
			if (CollectionUtil.isNotEmpty(list)) {
				sysUserRoleService.removeBatchByIds(list.stream().map(SysUserRoleDO::getId).toList());
			}
			if (!CollectionUtil.isEmpty(roleIds)) {
				saveOrUpdate(dto.getId(), roleIds);
			}
		}
		catch (Exception ex) {
			log.error("错误信息：{}", ex.getMessage());
			throw ex;
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@DS(Constant.SHARDING_SPHERE)
	public Boolean updatePassword(Long id, String newPassword) {
		Integer version = sysUserService.getVersion(id);
		SysUserDTO dto = new SysUserDTO();
		dto.setEditor(UserUtil.getUserId());
		dto.setId(id);
		dto.setPassword(passwordEncoder.encode(newPassword));
		dto.setVersion(version);
		sysUserService.updateUser(dto);
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@DS(Constant.SHARDING_SPHERE)
	public Boolean updateStatus(Long id, Integer status) {
		Integer version = sysUserService.getVersion(id);
		SysUserDTO dto = new SysUserDTO();
		dto.setEditor(UserUtil.getUserId());
		dto.setId(id);
		dto.setStatus(status);
		dto.setVersion(version);
		sysUserService.updateUser(dto);
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@DS(Constant.SHARDING_SPHERE)
	public Boolean updateInfo(SysUserDTO dto) {
		Long id = dto.getId();
		if (null == id) {
			throw new CustomException("用户编号不为空");
		}
		String mobile = dto.getMobile();
		// 验证手机号唯一
		if (StringUtil.isNotEmpty(mobile)) {
			long mobileCount = sysUserService
					.count(Wrappers.lambdaQuery(SysUserDO.class).eq(SysUserDO::getTenantId, UserUtil.getTenantId())
							.eq(SysUserDO::getMobile, mobile).ne(SysUserDO::getId, id));
			if (mobileCount > 0) {
				throw new CustomException("手机号已被注册，请重新填写");
			}
			dto.setMobile(AesUtil.encrypt(mobile));
		}
		// 验证邮箱唯一
		String mail = dto.getMail();
		if (StringUtil.isNotEmpty(mail)) {
			long mailCount = sysUserService
					.count(Wrappers.lambdaQuery(SysUserDO.class).eq(SysUserDO::getTenantId, UserUtil.getTenantId())
							.eq(SysUserDO::getMail, mail).ne(SysUserDO::getId, id));
			if (mailCount > 0) {
				throw new CustomException("邮箱已被注册，请重新填写");
			}
			dto.setMail(AesUtil.encrypt(mail));
		}
		dto.setEditor(UserUtil.getUserId());
		Integer version = sysUserService.getVersion(id);
		dto.setVersion(version);
		sysUserService.updateUser(dto);
		return true;
	}

	@Override
	@DSTransactional(rollbackFor = Exception.class)
	@DS(Constant.SHARDING_SPHERE)
	public Boolean insertUser(SysUserDTO dto) {
		try {
			ValidatorUtil.validateEntity(dto);
			long count = sysUserService
					.count(Wrappers.lambdaQuery(SysUserDO.class).eq(SysUserDO::getUsername, dto.getUsername()));
			if (count > 0) {
				throw new CustomException("用户名已存在，请重新填写");
			}
			if (CollectionUtil.isEmpty(dto.getRoleIds())) {
				throw new CustomException("所选角色不少于一个，请重新选择");
			}
			if (dto.getDeptId() == null) {
				throw new CustomException("请选择部门");
			}
			if (StringUtil.isEmpty(dto.getPassword())) {
				throw new CustomException("请输入密码");
			}
			SysUserDO sysUserDO = ConvertUtil.sourceToTarget(dto, SysUserDO.class);
			sysUserDO.setTenantId(UserUtil.getTenantId());
			sysUserDO.setPassword(passwordEncoder.encode(dto.getPassword()));
			sysUserService.save(sysUserDO);
			DynamicDataSourceContextHolder.push(DEFAULT_SOURCE);
			List<Long> roleIds = dto.getRoleIds();
			if (!CollectionUtil.isEmpty(roleIds)) {
				saveOrUpdate(sysUserDO.getId(), roleIds);
			}
		}
		catch (Exception ex) {
			log.error("错误信息：{}", ex.getMessage());
			throw ex;
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
		return true;
	}

	@Override
	@DataFilter(tableAlias = "boot_sys_user")
	@DS(Constant.SHARDING_SPHERE)
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public IPage<SysUserVO> queryUserPage(SysUserQo qo) {
		ValidatorUtil.validateEntity(qo);
		qo.setTenantId(UserUtil.getTenantId());
		IPage<SysUserVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
		IPage<SysUserVO> userPage = sysUserService.getUserPage(page, qo);
		List<SysUserVO> records = userPage.getRecords();
		if (!CollectionUtil.isEmpty(records)) {
			records.forEach(item -> item.setUsername(AesUtil.decrypt(item.getUsername())));
		}
		return userPage;
	}

	@Override
	@DS(Constant.SHARDING_SPHERE)
	@DSTransactional(rollbackFor = Exception.class)
	public SysUserVO getUserById(Long id) {
		SysUserVO sysUserVO;
		try {
			SysUserDO sysUserDO = sysUserService.getById(id);
			sysUserVO = ConvertUtil.sourceToTarget(sysUserDO, SysUserVO.class);
			DynamicDataSourceContextHolder.push(DEFAULT_SOURCE);
			sysUserVO.setRoleIds(sysRoleService.getRoleIdsByUserId(sysUserVO.getId()));
		}
		catch (Exception ex) {
			log.error("错误信息：{}", ex.getMessage());
			sysUserVO = new SysUserVO();
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
		return sysUserVO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@DS(Constant.SHARDING_SPHERE)
	public Boolean deleteUser(Long id) {
		SysUserDO sysUser = sysUserService.getById(id);
		User user = UserUtil.user();
		if (SuperAdmin.YES.ordinal() == sysUser.getSuperAdmin() && SuperAdmin.YES.ordinal() != user.getSuperAdmin()) {
			throw new CustomException("只有超级管理员才能删除");
		}
		sysUserService.deleteUser(id);
		return true;
	}

	@Override

	public List<OptionVO> getOptionList() {
		Long tenantId = UserUtil.getTenantId();
		List<OptionVO> optionList = sysUserService.getOptionList(tenantId);
		if (!CollectionUtil.isEmpty(optionList)) {
			optionList.forEach(item -> item.setLabel(AesUtil.decrypt(item.getLabel())));
		}
		return optionList;
	}

	@Override
	public UserInfoVO getUserInfo() {
		return ConvertUtil.sourceToTarget(UserUtil.user(), UserInfoVO.class);
	}

	@Override
	public IPage<SysUserOnlineVO> onlineQueryPage(SysUserOnlineQo qo) {
		String userInfoKey = RedisKeyUtil.getUserInfoKey("*");
		Set<String> keys = redisUtil.keys(userInfoKey);
		List<SysUserOnlineVO> list = new ArrayList<>(keys.size());
		String keyword = qo.getUsername();
		Integer pageNum = qo.getPageNum();
		Integer pageSize = qo.getPageSize();
		String userInfoKeyPrefix = RedisKeyUtil.getUserInfoKey("");
		for (String key : keys) {
			User user = (User) redisUtil.get(key);
			String username = AesUtil.decrypt(user.getUsername());
			if (StringUtil.isEmpty(keyword) || username.contains(keyword)) {
				SysUserOnlineVO vo = new SysUserOnlineVO();
				vo.setUsername(username);
				vo.setToken(key.substring(userInfoKeyPrefix.length()));
				vo.setLoginIp(user.getLoginIp());
				vo.setLoginDate(user.getLoginDate());
				list.add(vo);
			}
		}
		int size = list.size();
		list = list.stream().limit(pageSize).skip((long) (pageNum - 1) * pageSize).toList();
		IPage<SysUserOnlineVO> page = new Page<>(pageNum, pageSize);
		page.setTotal(size);
		page.setRecords(list);
		return page;
	}

	@Override
	public Boolean onlineKill(String token) {
		String userKillKey = RedisKeyUtil.getUserKillKey(token);
		String userInfoKey = RedisKeyUtil.getUserInfoKey(token);
		long expire = redisUtil.getExpire(userInfoKey);
		if (expire > 0) {
			redisUtil.set(userKillKey, DEFAULT, expire);
			redisUtil.delete(userInfoKey);
		}
		return true;
	}

	private void saveOrUpdate(Long userId, List<Long> roleIds) {
		List<SysUserRoleDO> doList = new ArrayList<>(roleIds.size());
		if (!CollectionUtil.isEmpty(roleIds)) {
			for (Long roleId : roleIds) {
				SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
				sysUserRoleDO.setRoleId(roleId);
				sysUserRoleDO.setUserId(userId);
				sysUserRoleDO.setId(IdGenerator.defaultSnowflakeId());
				doList.add(sysUserRoleDO);
			}
		}
	}

}
