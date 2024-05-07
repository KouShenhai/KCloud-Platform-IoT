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

package org.laokou.auth.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.domain.gateway.MenuGateway;
import org.laokou.auth.domain.model.auth.MenuE;
import org.laokou.auth.domain.model.auth.UserE;
import org.laokou.auth.gatewayimpl.database.MenuMapper;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.MessageUtil;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

import java.util.HashSet;

import static org.laokou.common.i18n.common.DSConstant.BOOT_SYS_MENU;
import static org.laokou.common.i18n.common.exception.SystemException.TABLE_NOT_EXIST;

/**
 * 菜单.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MenuGatewayImpl implements MenuGateway {

	private final MenuMapper menuMapper;

	/**
	 * 查看菜单权限标识集合.
	 * @param user 用户对象
	 * @return 菜单权限标识集合
	 */
	@Override
	public MenuE getPermissions(UserE user) {
		try {
			if (user.isSuperAdministrator()) {
				return new MenuE(new HashSet<>(menuMapper.selectPermissions()));
			}
			return new MenuE(new HashSet<>(menuMapper.selectPermissionsByUserId(user.getId())));
		}
		catch (BadSqlGrammarException e) {
			log.error("表 {} 不存在，错误信息：{}，详情见日志", BOOT_SYS_MENU, LogUtil.record(e.getMessage()), e);
			throw new SystemException(TABLE_NOT_EXIST,
					MessageUtil.getMessage(TABLE_NOT_EXIST, new String[] { BOOT_SYS_MENU }));
		}
	}

}
