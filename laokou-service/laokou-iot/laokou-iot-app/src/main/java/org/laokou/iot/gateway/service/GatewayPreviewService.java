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

package org.laokou.iot.gateway.service;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.RequiredArgsConstructor;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.mqtt.client.MqttClientManager;
import org.laokou.common.mqtt.client.message.DownCommandGatewayMessage;
import org.laokou.common.redis.config.JacksonCodec;
import org.laokou.common.tenant.constant.DSConstants;
import org.laokou.iot.gateway.dto.GatewayCommandCmd;
import org.laokou.iot.gateway.dto.clientobject.GatewayPropertiesCO;
import org.laokou.iot.gateway.gatewayimpl.database.GatewayMapper;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayDO;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 查询网关属性及下发网关指令.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class GatewayPreviewService {

	private final GatewayMapper gatewayMapper;

	private final SystemSettingsProperties systemSettingsProperties;

	private final RedisTemplate<String, Object> redisTemplate;

	private final MqttClientManager mqttClientManager;

	public GatewayPropertiesCO getProperties(Long id) {
		GatewayDO gateway = getGateway(id);
		String key = RedisKeyUtils.getGatewayPropertiesKey(systemSettingsProperties.getTenantCode(), gateway.getSn());
		byte[] rawKey = JacksonCodec.STRING_REDIS_SERIALIZER.serialize(key);
		// Lua 写入的字段名是原始字符串，哈希键和字段值使用 RedisTemplate 序列化。
		RedisSerializer<String> fields = RedisSerializer.string();
		List<byte[]> values = redisTemplate
			.execute((RedisCallback<List<byte[]>>) connection -> connection.hashCommands()
				.hMGet(rawKey, fields.serialize("timestamp"), fields.serialize("data")));
		if (values == null || values.get(1) == null) {
			return new GatewayPropertiesCO(null, Map.of());
		}
		Object timestamp = JacksonCodec.OBJECT_REDIS_SERIALIZER.deserialize(values.get(0));
		String data = (String) JacksonCodec.OBJECT_REDIS_SERIALIZER.deserialize(values.get(1));
		Map<String, Object> properties = JacksonUtils.toMap(data, String.class, Object.class);
		return new GatewayPropertiesCO(timestamp == null ? null : Long.valueOf(timestamp.toString()),
				properties == null ? Map.of() : properties);
	}

	public void sendCommand(Long id, GatewayCommandCmd cmd) {
		GatewayDO gateway = getGateway(id);
		if (gateway.getSessionId() == null) {
			throw new BizException("B_Gateway_SessionMissing", "网关未绑定 MQTT 会话");
		}
		String sn = gateway.getSn();
		if (sn == null || sn.isBlank() || sn.contains("/") || sn.contains("+") || sn.contains("#")
				|| sn.indexOf(0) >= 0) {
			throw new BizException("B_Gateway_InvalidSn", "网关序列号不能用作 MQTT 主题");
		}
		String topic = String.format("down/%s/%s/command", systemSettingsProperties.getTenantCode(), sn);
		DownCommandGatewayMessage message = new DownCommandGatewayMessage();
		message.setCommand(cmd.command());
		mqttClientManager.publish(gateway.getSessionId(), topic, JacksonUtils.toBytes(message), MqttQoS.AT_LEAST_ONCE);
	}

	private GatewayDO getGateway(Long id) {
		DynamicDataSourceContextHolder.push(DSConstants.IOT);
		try {
			GatewayDO gateway = gatewayMapper.selectById(id);
			if (gateway == null) {
				throw new BizException("B_Gateway_NotFound", "网关不存在");
			}
			return gateway;
		}
		finally {
			DynamicDataSourceContextHolder.poll();
		}
	}

}
