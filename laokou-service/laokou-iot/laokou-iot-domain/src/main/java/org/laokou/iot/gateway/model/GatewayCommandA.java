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

package org.laokou.iot.gateway.model;

import lombok.Getter;
import org.laokou.common.core.util.ForyJsonUtils;
import org.laokou.common.core.util.MapUtils;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.IdGenerator;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.iot.gateway.model.entity.GatewayCommandE;
import org.laokou.iot.gateway.model.enums.Command;
import org.laokou.iot.gateway.model.enums.Status;
import org.laokou.iot.gateway.model.exception.CommandNotFoundException;

import java.io.Serial;
import java.util.Arrays;
import java.util.Map;

/**
 * 网关指令聚合根.
 *
 * @author laokou
 */
@Entity
@Getter
public class GatewayCommandA extends AggregateRoot {

	@Serial
	private static final long serialVersionUID = 1L;

	private GatewayCommandE gatewayCommandE;

	private final transient IdGenerator idGenerator;

	public GatewayCommandA(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public GatewayCommandA create(GatewayCommandE gatewayCommandE) {
		this.gatewayCommandE = gatewayCommandE;
		super.createTime = InstantUtils.now();
		super.id = idGenerator.getId();
		return this;
	}

	public void checkCommand() {
		if (!Arrays.stream(Command.values()).map(Command::getCode).toList().contains(gatewayCommandE.getCommand())) {
			throw new CommandNotFoundException("B_Gateway_CommandNotFound", "指令不存在");
		}
	}

	public void getReplyInfo(Map<String, Object> replyPayload, String replyMessage, Long timestamp, Throwable err) {
		this.gatewayCommandE = gatewayCommandE.toBuilder()
			.replyTime(timestamp != null ? InstantUtils.getInstantOfTimestamp(timestamp) : null)
			.replyPayload(MapUtils.isNotEmpty(replyPayload) ? ForyJsonUtils.toJsonStr(replyPayload) : null)
			.replyMessage(replyMessage)
			.replyStatus(ObjectUtils.equals(StatusCode.OK, replyMessage) ? Status.SUCCESS.getCode()
					: Status.FAILURE.getCode())
			.message(err != null ? err.getCause().getMessage() : null)
			.status(err != null ? Status.FAILURE.getCode() : Status.SUCCESS.getCode())
			.build();
	}

}
