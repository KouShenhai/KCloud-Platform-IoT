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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.PackageConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.PackageGateway;
import org.laokou.admin.domain.packages.Package;
import org.laokou.admin.gatewayimpl.database.PackageMapper;
import org.laokou.admin.gatewayimpl.database.PackageMenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.PackageDO;
import org.laokou.admin.gatewayimpl.database.dataobject.PackageMenuDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.utils.BatchUtil;
import org.laokou.common.mybatisplus.utils.IdUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.laokou.admin.common.DsConstant.BOOT_SYS_PACKAGE;

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
	private final BatchUtil batchUtil;

	@Override
	public Boolean insert(Package pack) {
		PackageDO packageDO = PackageConvertor.toDataObject(pack);
		return insertPackage(packageDO,pack);
	}

	@Override
	public Boolean update(Package pack) {
		Long id = pack.getId();
		PackageDO packageDO = PackageConvertor.toDataObject(pack);
		packageDO.setVersion(packageMapper.getVersion(id, PackageDO.class));
		List<Long> ids = packageMenuMapper.getIdsByPackageId(id);
		return updatePackage(packageDO,pack,ids);
	}

	@Override
	@DataFilter(alias = BOOT_SYS_PACKAGE)
	public Datas<Package> list(Package pack, PageQuery pageQuery) {
		IPage<PackageDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<PackageDO> newPage = packageMapper.getPackageListByLikeName(page, pack.getName(),
				pageQuery.getSqlFilter());
		Datas<Package> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), Package.class));
		return datas;
	}

	@Override
	public Package getById(Long id) {
		PackageDO packageDO = packageMapper.selectById(id);
		return ConvertUtil.sourceToTarget(packageDO,Package.class);
	}

	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.execute(r -> {
			try {
				return packageMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean insertPackage(PackageDO packageDO,Package pack) {
		boolean flag = packageMapper.insert(packageDO) > 0;
		return flag && insertPackageMenu(packageDO.getId(), pack.getMenuIds());
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean updatePackage(PackageDO packageDO,Package pack,List<Long> ids) {
		boolean flag = packageMapper.updateById(packageDO) > 0;
		return flag && updatePackageMenu(packageDO.getId(),pack.getMenuIds(), ids);
	}

	private Boolean updatePackageMenu(Long packageId, List<Long> menuIds, List<Long> ids) {
		boolean flag = true;
		if (CollectionUtil.isNotEmpty(ids)) {
			flag = packageMenuMapper.deletePackageMenuByIds(ids) > 0;
		}
		return flag && insertPackageMenu(packageId, menuIds);
	}

	private Boolean insertPackageMenu(Long packageId, List<Long> menuIds) {
		if (CollectionUtil.isEmpty(menuIds)) {
			return false;
		}
		List<PackageMenuDO> list = new ArrayList<>(menuIds.size());
		for (Long menuId : menuIds) {
			PackageMenuDO packageMenuDO = new PackageMenuDO();
			packageMenuDO.setMenuId(menuId);
			packageMenuDO.setPackageId(packageId);
			packageMenuDO.setId(IdUtil.defaultId());
			packageMenuDO.setCreator(UserUtil.getUserId());
			packageMenuDO.setDeptId(UserUtil.getDeptId());
			packageMenuDO.setTenantId(UserUtil.getTenantId());
			list.add(packageMenuDO);
		}
		batchUtil.insertBatch(list,packageMenuMapper::insertBatch);
		return true;
	}

}
