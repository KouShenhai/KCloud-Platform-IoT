/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.logstash.gatewayimpl.database.dataobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.laokou.common.i18n.dto.Index;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static org.laokou.common.i18n.utils.DateUtil.Constant.*;

/**
 * @author laokou
 */
@Data
public class TraceIndex extends Index {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	private String appName;

	private String profile;

	@DateTimeFormat(pattern = YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS)
	@JsonFormat(pattern = YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS, timezone = DEFAULT_TIMEZONE)
	private LocalDateTime timestamp;

	private String userId;

	private String username;

	private String tenantId;

	private String traceId;

	private String ip;

	private String level;

	private String thread;

	private String logger;

	private String msg;

}
