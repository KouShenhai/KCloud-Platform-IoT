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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.PackageConvertor;
import org.laokou.admin.domain.gateway.PackageGateway;
import org.laokou.admin.domain.pack.Package;
import org.laokou.admin.gatewayimpl.database.PackageMapper;
import org.laokou.admin.gatewayimpl.database.PackageMenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.PackageDO;
import org.laokou.admin.gatewayimpl.database.dataobject.PackageMenuDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 套餐管理.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PackageGatewayImpl implements PackageGateway {

	private final PackageMapper packageMapper;

	private final PackageMenuMapper packageMenuMapper;

	private final TransactionalUtil transactionalUtil;

	private final PackageConvertor packageConvertor;

	private final MybatisUtil mybatisUtil;

	/**
	 * 新增套餐.
	 * @param pack 套餐对象
	 */
	@Override
	public void create(Package pack) {
		long count = packageMapper
			.selectCount(Wrappers.lambdaQuery(PackageDO.class).eq(PackageDO::getName, pack.getName()));
		pack.checkName(count);
		PackageDO packageDO = packageConvertor.toDataObject(pack);
		create(packageDO, pack);
	}

	/**
	 * 修改套餐.
	 * @param pack 套餐对象
	 */
	@Override
	public void modify(Package pack) {
		// pack.checkNullId();
		long count = packageMapper.selectCount(Wrappers.lambdaQuery(PackageDO.class)
			.eq(PackageDO::getName, pack.getName())
			.ne(PackageDO::getId, pack.getId()));
		pack.checkName(count);
		PackageDO packageDO = packageConvertor.toDataObject(pack);
		// 版本号
		packageDO.setVersion(packageMapper.selectVersion(packageDO.getId()));
		modify(packageDO, pack);
	}

	/**
	 * 根据IDS删除套餐.
	 * @param ids IDS
	 */
	@Override
	public void remove(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				packageMapper.deleteByIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 新增套餐.
	 * @param packageDO 套餐数据模型
	 * @param pack 套餐对象
	 */
	private void create(PackageDO packageDO, Package pack) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				packageMapper.insert(packageDO);
				createPackageMenu(packageDO, pack.getMenuIds());
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 修改套餐.
	 * @param packageDO 套餐数据模型
	 * @param pack 套餐对象
	 */
	private void modify(PackageDO packageDO, Package pack) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				packageMapper.updateById(packageDO);
				modifyPackageMenu(packageDO, pack.getMenuIds());
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
	 * 修改套餐菜单.
	 * @param packageDO 套餐对象
	 * @param menuIds 菜单ID
	 */
	private void modifyPackageMenu(PackageDO packageDO, List<Long> menuIds) {
		if (CollectionUtil.isNotEmpty(menuIds)) {
			removePackageMenu(packageDO);
			createPackageMenu(packageDO, menuIds);
		}
	}

	private void removePackageMenu(PackageDO packageDO) {
		packageMenuMapper.deleteByPackageId(packageDO.getId());
	}

	private void createPackageMenu(PackageDO packageDO, List<Long> menuIds) {
		List<PackageMenuDO> list = menuIds.parallelStream().map(menuId -> convert(packageDO.getId(), menuId)).toList();
		mybatisUtil.batch(list, PackageMenuMapper.class, PackageMenuMapper::insertOne);
	}

	/**
	 * 转换套餐菜单数据模型.
	 * @param packageId 套餐ID
	 * @param menuId 菜单ID
	 * @return 套餐菜单数据模型
	 */
	private PackageMenuDO convert(Long packageId, Long menuId) {
		PackageMenuDO packageMenuDO = new PackageMenuDO();
		packageMenuDO.setMenuId(menuId);
		packageMenuDO.setPackageId(packageId);
		packageMenuDO.setId(IdGenerator.defaultSnowflakeId());
		packageMenuDO.setCreator(UserUtil.getUserId());
		packageMenuDO.setDeptId(UserUtil.getDeptId());
		packageMenuDO.setTenantId(UserUtil.getTenantId());
		packageMenuDO.setDeptPath(UserUtil.getDeptPath());
		return packageMenuDO;
	}

}
