/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.dto.log.domainevent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.laokou.common.i18n.common.EventStatusEnums;
import org.laokou.common.i18n.common.EventTypeEnums;
import org.laokou.common.i18n.dto.DomainEvent;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Getter
@Setter
@Schema(name = "OssLogEvent", description = "OSS日志事件")
public class OssLogEvent extends DomainEvent<Long> {

	@Serial
	private static final long serialVersionUID = 3776732013732856552L;

	@Schema(name = "md5", description = "文件的MD5标识")
	private String md5;

	@Schema(name = "url", description = "文件的URL")
	private String url;

	@Schema(name = "name", description = "文件名称")
	private String name;

	@Schema(name = "size", description = "文件大小")
	private Long size;

	protected OssLogEvent(Long aLong, Long aggregateId, EventTypeEnums eventType, EventStatusEnums eventStatus, String topic, String sourceName, String appName, Long creator, Long editor, Long deptId, String deptPath, Long tenantId, LocalDateTime createDate, LocalDateTime updateDate) {
		super(aLong, aggregateId, eventType, eventStatus, topic, sourceName, appName, creator, editor, deptId, deptPath, tenantId, createDate, updateDate);
	}
}
