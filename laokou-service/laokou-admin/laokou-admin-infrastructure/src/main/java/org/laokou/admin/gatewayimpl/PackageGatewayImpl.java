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
import org.laokou.admin.convertor.PackageConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.PackageGateway;
import org.laokou.admin.domain.packages.Package;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.gatewayimpl.database.PackageMapper;
import org.laokou.admin.gatewayimpl.database.PackageMenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.PackageDO;
import org.laokou.admin.gatewayimpl.database.dataobject.PackageMenuDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.mybatisplus.constant.DsConstant.BOOT_SYS_PACKAGE;

/**
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

	@Override
	public Boolean insert(Package pack, User user) {
		return insertPackage(packageConvertor.toDataObject(pack), pack, user);
	}

	@Override
	public Boolean update(Package pack, User user) {
		PackageDO packageDO = packageConvertor.toDataObject(pack);
		packageDO.setVersion(packageMapper.getVersion(pack.getId(), PackageDO.class));
		return updatePackage(packageDO, pack, user);
	}

	@Override
	@DataFilter(alias = BOOT_SYS_PACKAGE)
	public Datas<Package> list(Package pack, PageQuery pageQuery) {
		IPage<PackageDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<PackageDO> newPage = packageMapper.getPackageListFilter(page, pack.getName(), pageQuery);
		Datas<Package> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(packageConvertor.convertEntityList(newPage.getRecords()));
		return datas;
	}

	@Override
	public Package getById(Long id) {
		Package pack = packageConvertor.convertEntity(packageMapper.selectById(id));
		pack.setMenuIds(packageMenuMapper.getMenuIdsByPackageId(id));
		return pack;
	}

	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return packageMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息", e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private Boolean insertPackage(PackageDO packageDO, Package pack, User user) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				packageMapper.insertTable(packageDO);
				insertPackageMenu(packageDO.getId(), pack.getMenuIds(), user);
				return true;
			}
			catch (Exception e) {
				log.error("错误信息", e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private Boolean updatePackage(PackageDO packageDO, Package pack, User user) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				packageMapper.updateById(packageDO);
				updatePackageMenu(packageDO.getId(), pack.getMenuIds(), user);
				return true;
			}
			catch (Exception e) {
				log.error("错误信息", e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private void updatePackageMenu(Long packageId, List<Long> menuIds, User user) {
		packageMenuMapper.deletePackageMenuByPackageId(packageId);
		insertPackageMenu(packageId, menuIds, user);
	}

	private void insertPackageMenu(Long packageId, List<Long> menuIds, User user) {
		if (CollectionUtil.isNotEmpty(menuIds)) {
			List<PackageMenuDO> list = menuIds.parallelStream()
				.map(menuId -> toPackageMenuDO(packageId, menuId, user))
				.toList();
			mybatisUtil.batch(list, PackageMenuMapper.class, PackageMenuMapper::save);
		}
	}

	private PackageMenuDO toPackageMenuDO(Long packageId, Long menuId, User user) {
		PackageMenuDO packageMenuDO = new PackageMenuDO();
		packageMenuDO.setMenuId(menuId);
		packageMenuDO.setPackageId(packageId);
		packageMenuDO.setId(IdGenerator.defaultSnowflakeId());
		packageMenuDO.setCreator(user.getId());
		packageMenuDO.setDeptId(user.getDeptId());
		packageMenuDO.setTenantId(user.getTenantId());
		return packageMenuDO;
	}

}
