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

package org.laokou.iot.gateway.gatewayimpl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.pulsar.util.TopicUtils;
import org.laokou.iot.gateway.convertor.GatewayCommandConvertor;
import org.laokou.iot.gateway.gateway.GatewayCommandGateway;
import org.laokou.iot.gateway.gatewayimpl.database.GatewayCommandLogMapper;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayCommandLogDO;
import org.laokou.iot.gateway.model.GatewayCommandE;
import org.springframework.stereotype.Component;

/**
 *
 * 网关指令网关实现.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayCommandGatewayImpl implements GatewayCommandGateway {

	/**
	 * 租户编码.
	 */
	private static final String TENANT_CODE = "laokouyun";

	/**
	 * 命名空间.
	 */
	private static final String NAMESPACE = "mqtt";

	private final GatewayCommandLogMapper gatewayCommandLogMapper;

	@Override
	public void saveLog(GatewayCommandE gatewayCommandE) {
		gatewayCommandLogMapper.insert(GatewayCommandConvertor.toDataObject(1L, gatewayCommandE));
	}

	@Override
	public void publish(GatewayCommandE gatewayCommandE) {
		// 下发主题：persistent://laokouyun/mqtt/down-<gatewayKey>-command
		String topic = TopicUtils.getTopic(TENANT_CODE, NAMESPACE,
				"down-" + gatewayCommandE.getGatewayKey() + "-command");
		// TODO: 待MQTT/Pulsar生产者接入后，通过PulsarTemplate下发指令；当前记录下发内容
		log.info("下发网关指令，主题：{}，指令内容：{}", topic, gatewayCommandE.getPayload());
	}

	@Override
	public void updateLogStatus(Long commandId, Integer status, String result) {
		GatewayCommandLogDO gatewayCommandLogDO = gatewayCommandLogMapper
			.selectOne(Wrappers.lambdaQuery(GatewayCommandLogDO.class)
				.eq(GatewayCommandLogDO::getCommandId, commandId)
				.last("limit 1"));
		if (ObjectUtils.isNull(gatewayCommandLogDO)) {
			log.warn("网关指令回执无对应记录，指令ID：{}", commandId);
			return;
		}
		gatewayCommandLogDO.setStatus(status);
		gatewayCommandLogDO.setResult(result);
		gatewayCommandLogDO.setVersion(gatewayCommandLogMapper.selectVersion(gatewayCommandLogDO.getId()));
		gatewayCommandLogMapper.updateById(gatewayCommandLogDO);
	}

}
