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

package org.laokou.auth.gatewayimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.domain.auth.Auth;
import org.laokou.auth.domain.gateway.UserGateway;
import org.laokou.auth.domain.user.User;
import org.laokou.auth.gatewayimpl.database.UserMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.mybatisplus.template.TableTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.auth.common.Constant.USER;
import static org.laokou.common.mybatisplus.template.DsConstant.BOOT_SYS_USER;
import static org.laokou.common.mybatisplus.template.TableTemplate.MAX_TIME;
import static org.laokou.common.mybatisplus.template.TableTemplate.MIN_TIME;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserGatewayImpl implements UserGateway {

	private final UserMapper userMapper;

	@Override
	@DS(USER)
	public User getUserByUsername(Auth auth) {
		List<String> dynamicTables = TableTemplate.getDynamicTables(MIN_TIME, MAX_TIME, BOOT_SYS_USER);
		UserDO userDO = userMapper.getUserByUsernameAndTenantId(dynamicTables, auth.getUsername(), auth.getTenantId(),
				auth.getType());
		return ConvertUtil.sourceToTarget(userDO, User.class);
	}

}
