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

package org.laokou.common.mybatisplus.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.laokou.common.crypto.utils.AESUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;

/**
 * @author laokou
 */
@Slf4j
@Component
public class CryptoTypeHandler implements TypeHandler<String> {

	@Override
	public void setParameter(PreparedStatement preparedStatement, int parameterIndex, String content,
			JdbcType jdbcType) {
		try {
			if (StringUtils.isNotEmpty(content)) {
				content = AESUtils.encrypt(content);
			}
			preparedStatement.setString(parameterIndex, content);
		}
		catch (Exception e) {
			log.error("加密失败，错误信息：{}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getResult(ResultSet resultSet, String columnName) {
		try {
			String data = resultSet.getString(columnName);
			if (StringUtils.isEmpty(data)) {
				return EMPTY;
			}
			return AESUtils.decrypt(data.trim());
		}
		catch (Exception e) {
			log.error("解密失败，错误信息：{}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getResult(ResultSet resultSet, int columnIndex) {
		try {
			String data = resultSet.getString(columnIndex);
			if (StringUtils.isEmpty(data)) {
				return EMPTY;
			}
			return AESUtils.decrypt(data.trim());
		}
		catch (Exception e) {
			log.error("解密失败，错误信息：{}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
		try {
			String data = callableStatement.getString(columnIndex);
			if (StringUtils.isEmpty(data)) {
				return EMPTY;
			}
			return AESUtils.decrypt(data.trim());
		}
		catch (Exception e) {
			log.error("解密失败，错误信息：{}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

}
