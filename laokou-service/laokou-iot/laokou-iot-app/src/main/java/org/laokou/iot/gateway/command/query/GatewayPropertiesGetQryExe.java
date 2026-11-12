/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.gateway.command.query;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.laokou.common.tenant.constant.DSConstants;
import org.laokou.iot.gateway.convertor.GatewayConvertor;
import org.laokou.iot.gateway.dto.GatewayPropertiesGetQry;
import org.laokou.iot.gateway.dto.clientobject.GatewayPropertiesCO;
import org.laokou.iot.gateway.gatewayimpl.database.GatewayMapper;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayDO;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayPropertiesGetQryExe {

	private final GatewayMapper gatewayMapper;

	private final SystemSettingsProperties systemSettingsProperties;

	private final RedisUtils redisUtils;

	public Result<GatewayPropertiesCO> execute(GatewayPropertiesGetQry qry) {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.IOT);
			GatewayDO gatewayDO = gatewayMapper.selectById(qry.getId());
			String gatewayPropertiesKey = RedisKeyUtils
				.getGatewayPropertiesKey(systemSettingsProperties.getTenantCode(), gatewayDO.getSn());
			return Result.ok(GatewayConvertor.toClientObject(gatewayDO,
					redisUtils.hGet(gatewayPropertiesKey, "timestamp"), redisUtils.hGet(gatewayDO.getSn(), "data")));
		}
		catch (Exception ex) {
			log.error("查看网关属性失败，错误信息：{}", ex.getMessage(), ex);
			throw ex;
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
