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
import org.laokou.auth.convertor.UserConvertor;
import org.laokou.auth.gateway.UserGateway;
import org.laokou.auth.gatewayimpl.database.UserMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.UserDO;
import org.laokou.auth.model.UserE;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

import static org.laokou.auth.common.constant.Constant.TABLE_USER;
import static org.laokou.common.i18n.common.exception.SystemException.TABLE_NOT_EXIST;

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
	public UserE getProfile(UserE user, String tenantCode) {
		try {
			UserDO userDO = userMapper.selectOneByCondition(UserConvertor.toDataObject(user), tenantCode);
			return ObjectUtil.isNotNull(userDO) ? UserConvertor.toEntity(userDO) : null;
		}
		catch (BadSqlGrammarException e) {
			log.error("表 {} 不存在，错误信息：{}，详情见日志", TABLE_USER, LogUtil.record(e.getMessage()), e);
			throw new SystemException(TABLE_NOT_EXIST,
					MessageUtil.getMessage(TABLE_NOT_EXIST, new String[] { TABLE_USER }));
		}
	}

}
