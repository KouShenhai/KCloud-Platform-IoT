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

package org.laokou.iot.gateway.command;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.laokou.common.tenant.constant.DSConstants;
import org.laokou.iot.device.gatewayimpl.database.DeviceMapper;
import org.laokou.iot.device.gatewayimpl.database.dataobject.DeviceDO;
import org.laokou.iot.gateway.ability.GatewayDomainService;
import org.laokou.iot.gateway.convertor.GatewayConvertor;
import org.laokou.iot.gateway.dto.GatewayDownCommandCmd;
import org.laokou.iot.gateway.factory.GatewayDomainFactory;
import org.laokou.iot.gateway.gatewayimpl.database.GatewayMapper;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayDO;
import org.laokou.iot.gateway.model.GatewayCommandA;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayDownCommandCmdExe {

	private final GatewayMapper gatewayMapper;

	private final DeviceMapper deviceMapper;

	private final TransactionalUtils transactionalUtils;

	private final GatewayDomainService gatewayDomainService;

	@CommandLog
	public void execute(GatewayDownCommandCmd cmd) {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.IOT);
			GatewayDO gatewayDO = gatewayMapper.selectById(cmd.getGatewayId());
			DeviceDO deviceDO = ObjectUtils.isNotNull(cmd.getDeviceId()) ? deviceMapper.selectById(cmd.getDeviceId())
					: null;
			GatewayCommandA gatewayCommandA = GatewayDomainFactory.createGatewayCommandA()
				.create(GatewayConvertor.toEntity(gatewayDO, deviceDO, cmd.getCommand(), cmd.getParam()));
			transactionalUtils.executeInTransaction(() -> gatewayDomainService.createGatewayCommand(gatewayCommandA));
		}
		catch (Exception ex) {
			log.error("下发重启网关指令失败，错误信息：{}", ex.getMessage(), ex);
			throw ex;
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
