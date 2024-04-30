/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
import org.laokou.admin.convertor.MenuConvertor;
import org.laokou.admin.domain.gateway.MenuGateway;
import org.laokou.admin.domain.menu.Menu;
import org.laokou.admin.gatewayimpl.database.MenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 菜单管理.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MenuGatewayImpl implements MenuGateway {

	private final MenuMapper menuMapper;

	private final TransactionalUtil transactionalUtil;

	private final MenuConvertor menuConvertor;

	/**
	 * 修改菜单.
	 * @param menu 菜单对象
	 */
	@Override
	public void modify(Menu menu) {
		// 检查ID
		menu.checkNullId();
		// 检查菜单名称
		long count = menuMapper.selectCount(
				Wrappers.lambdaQuery(MenuDO.class).eq(MenuDO::getName, menu.getName()).ne(MenuDO::getId, menu.getId()));
		menu.checkName(count);
		MenuDO menuDO = menuConvertor.toDataObject(menu);
		// 版本号
		menuDO.setVersion(menuMapper.selectVersion(menuDO.getId()));
		modify(menuDO);
	}

	/**
	 * 新增菜单.
	 * @param menu 菜单对象
	 */
	@Override
	public void create(Menu menu) {
		// 检查菜单名称
		long count = menuMapper.selectCount(Wrappers.lambdaQuery(MenuDO.class).eq(MenuDO::getName, menu.getName()));
		menu.checkName(count);
		create(menuConvertor.toDataObject(menu));
	}

	/**
	 * 根据ID删除菜单.
	 * @param ids IDS
	 */
	@Override
	public void remove(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				menuMapper.deleteBatchIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(LogUtil.record(e.getMessage()));
			}
		});
	}

	/**
	 * 修改菜单.
	 * @param menuDO 菜单数据模型
	 */
	private void modify(MenuDO menuDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				menuMapper.updateById(menuDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(LogUtil.record(e.getMessage()));
			}
		});
	}

	/**
	 * 新增菜单.
	 * @param menuDO 菜单数据模型
	 */
	private void create(MenuDO menuDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				menuMapper.insert(menuDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

}
