/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.auth.convertor.UserConvertor;
import org.laokou.auth.gateway.UserGateway;
import org.laokou.auth.gatewayimpl.database.UserMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.UserDO;
import org.laokou.auth.model.UserE;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.MessageUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;
import static org.laokou.auth.model.OAuth2Constants.DATA_TABLE_NOT_EXIST;
import static org.laokou.common.tenant.constant.DSConstants.Master.USER_TABLE;

/**
 * 用户.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserGatewayImpl implements UserGateway {

	private final UserMapper userMapper;

	/**
	 * 查看用户信息.
	 * @param user 用户对象
	 * @return 用户信息
	 */
	@Override
	public UserE getProfileUser(UserE user) {
		try {
			UserDO userDO = userMapper.selectUser(UserConvertor.toDataObject(user));
			return ObjectUtils.isNotNull(userDO) ? UserConvertor.toEntity(userDO) : null;
		}
		catch (BadSqlGrammarException e) {
			log.error("表 {} 不存在，错误信息：{}", USER_TABLE, e.getMessage());
			throw new BizException(DATA_TABLE_NOT_EXIST,
					MessageUtils.getMessage(DATA_TABLE_NOT_EXIST, new String[] { USER_TABLE }));
		}
	}

}
